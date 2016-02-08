// Redis and go implementation for github.com/eleme/hackathon.

package main

import (
	"bytes"
	"crypto/sha1"
	"database/sql"
	"encoding/hex"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"math/rand"
	"net/http"
	"os"
	"runtime"
	"strconv"

	"github.com/garyburd/redigo/redis"
	_ "github.com/go-sql-driver/mysql"
	"github.com/kavu/go_reuseport"
)

// ----------------------- Settings -----------------------------
const (
	REDIS_MAX_CLIENTS      = 10 * 1024
	REDIS_MAX_IDLE_CLIENTS = 4000
)

// ----------------------- Abstracts -----------------------------
type H map[string]interface{}
type User struct {
	Id       int
	UserName string
	PassWord string
	Token    string
}

type Food struct {
	Id    int `json:"id"`
	Price int `json:"price"`
	Stock int `json:"stock"`
}

type CartFoodItem struct {
	FoodId int `json:"food_id"`
	Count  int `json:"count"`
	Price  int `json:"price"`
}

type Cart struct {
	Id    string         `json:"id"`
	Items []CartFoodItem `json:"items"`
}

type OrderFoodItem struct {
	FoodId int `json:"food_id"`
	Count  int `json:"count"`
	Price  int `json:"-"`
}
type Order struct {
	Id    string          `json:"id"`
	Items []OrderFoodItem `json:"items"`
	Total int             `json:"total"`
}

// ----------------------- Requests ----------------------------
type RequestLogin struct {
	UserName string `json:"username"`
	PassWord string `json:"password"`
}

type RequestCartAddFood struct {
	FoodId int `json:"food_id"`
	Count  int `json:"count"`
}

type RequestMakeOrder struct {
	CartId string `json:"cart_id"`
}

// ----------------------- Responses -----------------------------
type ResponseAdminOrder struct {
	Id     string          `json:"id"`
	UserId int             `json:"user_id"`
	Items  []OrderFoodItem `json:"items"`
	Total  int             `json:"total"`
}

// ----------------------- Response Errors -----------------------

func ResponseErrUserAuth(w http.ResponseWriter) {
	w.WriteHeader(http.StatusForbidden)
	ResponseJsonString(w, `{"message":"用户名或密码错误","code":"USER_AUTH_FAIL"}`)
}

func ResponseErrEmptyRequest(w http.ResponseWriter) {
	w.WriteHeader(http.StatusBadRequest)
	ResponseJsonString(w, `{"message":"请求体为空", "code":"EMPTY_REQUEST"}`)
}

func ResponseErrMalformedJson(w http.ResponseWriter) {
	w.WriteHeader(http.StatusBadRequest)
	ResponseJsonString(w, `{"message":"格式错误","code":"MALFORMED_JSON"}`)
}

func ResponseErrAccessToken(w http.ResponseWriter) {
	w.WriteHeader(http.StatusUnauthorized)
	ResponseJsonString(w, `{"message":"无效的令牌","code":"INVALID_ACCESS_TOKEN"}`)
}

func ResponseErrCartNotFound(w http.ResponseWriter) {
	w.WriteHeader(http.StatusNotFound)
	ResponseJsonString(w, `{"message":"篮子不存在","code":"CART_NOT_FOUND"}`)
}

func ResponseErrFoodNotFound(w http.ResponseWriter) {
	w.WriteHeader(http.StatusNotFound)
	ResponseJsonString(w, `{"message":"食物不存在","code":"FOOD_NOT_FOUND"}`)
}

func ResponseErrFoodOutOfLimit(w http.ResponseWriter) {
	w.WriteHeader(http.StatusForbidden)
	ResponseJsonString(w, `{"message":"篮子中食物数量超过了三个","code":"FOOD_OUT_OF_LIMIT"}`)
}

func ResponseErrOrderOutOfLimit(w http.ResponseWriter) {
	w.WriteHeader(http.StatusForbidden)
	ResponseJsonString(w, `{"message":"每个用户只能下一单","code":"ORDER_OUT_OF_LIMIT"}`)
}

func ResponseErrFoodOutOfStock(w http.ResponseWriter) {
	w.WriteHeader(http.StatusForbidden)
	ResponseJsonString(w, `{"message":"食物库存不足","code":"FOOD_OUT_OF_STOCK"}`)
}

func ResponseErrNotAuthorizedToAcessCart(w http.ResponseWriter) {
	w.WriteHeader(http.StatusUnauthorized)
	ResponseJsonString(w, `{"message":"无权限访问指定的篮子","code":"NOT_AUTHORIZED_TO_ACCESS_CART"}`)
}

// ----------------------- Globals -----------------------------
var (
	pool              *redis.Pool             // redis pool
	token2Users       = make(map[string]User) // get user by token
	userName2Users    = make(map[string]User) // get user by name
	userId2OrderId    = make(map[int]string)  // get order id string by user id
	food2Price        = make(map[int]int)     // get food price by id
	int2CartIds       = make(map[int]string)  // get cart id by integer
	foodsJsonBytes    []byte                  // foods json bytes
	foodsJsonBytesLen string                  // foods json bytes len
	procId            int                     // host id from redis
)

// ----------------------- Errors -----------------------------
var (
	ErrInvalidAccessToken         = errors.New("Invalid access token")
	ErrInvalidCartFoodItemString  = errors.New("Invalid cart food item string")
	ErrInvalidOrderFoodItemString = errors.New("Invalid order food item string")
	ErrInvalidOrderKeyString      = errors.New("Invalid order key string")
	ErrOrderNil                   = errors.New("No order was found")
)

// ----------------------- Dispatcher -----------------------------
var routeMap = map[string]func(http.ResponseWriter, *http.Request){
	"/login":        Login,
	"/foods":        GetFoods,
	"/admin/orders": AdminGetOrders,
}

type Handler struct{}

func (*Handler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	fn, ok := routeMap[r.URL.Path]
	if ok {
		fn(w, r)
	} else {
		if r.URL.Path == "/orders" {
			if r.Method == "GET" {
				GetOrders(w, r)
			} else {
				MakeOrder(w, r)
			}
		} else {
			if len(r.URL.Path) > 7 {
				CartAddFood(w, r)
			} else {
				CreateCart(w, r)
			}
		}
	}
}

// ----------------------- Dispatcher -----------------------------

// Main
func main() {
	runtime.GOMAXPROCS(1)
	// init redis pool
	pool = CreateRedisPool()
	defer pool.Close()
	// warm data
	Warm()
	// start server
	listener, err := reuseport.NewReusablePortListener("tcp4", ":8080")
	if err != nil {
		panic(err)
	}
	defer listener.Close()
	server := &http.Server{Handler: &Handler{}}
	fmt.Printf("[%d] Listen and serve..OK\n", procId)
	panic(server.Serve(listener))
}

// Create redis pool.
func CreateRedisPool() *redis.Pool {
	host := os.Getenv("REDIS_HOST")
	port := os.Getenv("REDIS_PORT")
	if host == "" {
		host = "localhost"
	}
	if port == "" {
		port = "6379"
	}
	addr := fmt.Sprintf("%s:%s", host, port)
	return &redis.Pool{
		MaxIdle:   REDIS_MAX_IDLE_CLIENTS,
		MaxActive: REDIS_MAX_CLIENTS,
		Dial: func() (redis.Conn, error) {
			c, err := redis.Dial("tcp", addr)
			if err != nil {
				panic(err)
			}
			return c, err
		},
	}
}

// ----------------------- Warm ------------------------
// Warm all.
func Warm() {
	dbHost := os.Getenv("DB_HOST")
	dbPort := os.Getenv("DB_PORT")
	dbName := os.Getenv("DB_NAME")
	dbUser := os.Getenv("DB_USER")
	dbPass := os.Getenv("DB_PASS")

	if dbHost == "" {
		dbHost = "localhost"
	}
	if dbPort == "" {
		dbPort = "3306"
	}
	if dbName == "" {
		dbName = "eleme"
	}
	if dbUser == "" {
		dbUser = "root"
	}
	if dbPass == "" {
		dbPass = "toor"
	}

	dbDsn := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s", dbUser, dbPass, dbHost, dbPort, dbName)
	db, err := sql.Open("mysql", dbDsn)
	defer db.Close()
	if err != nil {
		panic(err)
	}
	err = db.Ping()
	if err != nil {
		panic(err)
	}
	c := pool.Get()
	defer c.Close()
	_, err = c.Do("flushdb")
	if err != nil {
		panic(err)
	}
	WarmUsers(db)
	WarmFoods(db)
	WarmMisc()
}

// Warm users from mysql. (to memory)
func WarmUsers(db *sql.DB) {
	var (
		id       int
		name     string
		password string
	)
	rows, err := db.Query("SELECT `id`, `name`, `password` from user")
	if err != nil {
		panic(err)
	}
	defer rows.Close()
	for rows.Next() {
		err = rows.Scan(&id, &name, &password)
		if err != nil {
			panic(err)
		}
		user := NewUser(id, name, password)
		userName2Users[name] = user
		token2Users[user.Token] = user
		userId2OrderId[id] = strconv.Itoa(id)
	}
}

// Warm users from mysql. (to redis)
func WarmFoods(db *sql.DB) {
	c := pool.Get()
	defer c.Close()
	var (
		id    int
		price int
		stock int
	)
	rows, err := db.Query("SELECT `id`, `stock`, `price` from food")
	if err != nil {
		panic(err)
	}
	defer rows.Close()
	foodsJson := make([]Food, 0)
	for rows.Next() {
		err = rows.Scan(&id, &stock, &price)
		if err != nil {
			panic(err)
		}
		_, err = c.Do("HSET", "f", id, stock)
		if err != nil {
			panic(err)
		}
		food2Price[id] = price
		foodsJson = append(foodsJson, Food{id, price, stock})
	}
	b, err := json.Marshal(foodsJson)
	if err != nil {
		panic(err)
	}
	foodsJsonBytes = b
	foodsJsonBytesLen = strconv.Itoa(len(b))
}

func WarmMisc() {
	c := pool.Get()
	defer c.Close()
	c.Do("CONFIG", "SET", "maxclients", 1024*10)
	c.Do("SELECT", 15)
	procId, _ = redis.Int(c.Do("INCR", "pid"))
	if procId >= 3*3 {
		c.Do("DEL", "pid")
	}
	c.Do("SELECT", 0)
}

// ----------------------- HTTP Utils ---------------------
func RequestBind(r *http.Request, bind interface{}) error {
	return json.NewDecoder(r.Body).Decode(bind)
}

func ResponseJsonString(w http.ResponseWriter, s string) {
	w.Header().Set("Content-Type", "application/json")
	w.Header().Set("Content-Length", strconv.Itoa(len(s)))
	io.WriteString(w, s)
}

func ResponseJson(w http.ResponseWriter, data interface{}) error {
	b, err := json.Marshal(data)
	if err != nil {
		return err
	}
	ResponseJsonString(w, string(b))
	return nil
}

func ResponseJsonBytes(w http.ResponseWriter, b []byte) {
	w.Header().Set("Content-Type", "application/json")
	w.Header().Set("Content-Length", strconv.Itoa(len(b)))
	w.Write(b)
}

func ResponseJsonBytesWithLen(w http.ResponseWriter, b []byte, l string) {
	w.Header().Set("Content-Type", "application/json")
	w.Header().Set("Content-Length", l)
	w.Write(b)
}

func FastConcatStringsToBytes(n int, args ...string) []byte {
	var buffer bytes.Buffer
	buffer.Grow(n * 100)
	for i := 0; i < n; i++ {
		buffer.WriteString(args[i])
	}
	return buffer.Bytes()
}

// ----------------------- User -----------------------

// Generate access token by username.
func GenerateAccessTokenByUserName(userName string) string {
	b := sha1.Sum([]byte(userName))
	return hex.EncodeToString(b[:])
}

// Get user by access token
func GetUserByAccessToken(r *http.Request) (User, error) {
	var user User
	token := r.URL.Query().Get("access_token")
	if token == "" {
		token = r.Header.Get("Access-Token")
	}
	if len(token) != 40 {
		return user, ErrInvalidAccessToken
	}
	user, ok := token2Users[token]
	if !ok {
		return user, ErrInvalidAccessToken
	}
	return user, nil
}

// Validate access token.
func ValidateAccessToken(r *http.Request) bool {
	_, err := GetUserByAccessToken(r)
	if err != nil {
		return false
	}
	return true
}

// Create new user.
// "roboot" => "9a1f8d7080f9155566c14842cd3abc85d906cc74"
func NewUser(id int, userName string, passWord string) User {
	return User{
		Id:       id,
		UserName: userName,
		PassWord: passWord,
		Token:    GenerateAccessTokenByUserName(userName),
	}
}

// Login handler.
func Login(w http.ResponseWriter, r *http.Request) {
	req := RequestLogin{}
	err := RequestBind(r, &req)
	if err == nil {
		user, ok := userName2Users[req.UserName]
		if !ok || user.UserName != req.UserName || user.PassWord != req.PassWord {
			ResponseErrUserAuth(w)
			return
		}
		w.WriteHeader(http.StatusOK)
		b := FastConcatStringsToBytes(7, `{"user_id":`, strconv.Itoa(user.Id), `,"username":"`, user.UserName, `","access_token":"`, user.Token, `"}`)
		ResponseJsonBytes(w, b)
	} else {
		if err == io.EOF {
			ResponseErrEmptyRequest(w)
		} else {
			ResponseErrMalformedJson(w)
		}
	}
}

// ----------------------- Food -----------------------

func NewFood(id int, price int, stock int) Food {
	return Food{
		Id:    id,
		Price: price,
		Stock: stock,
	}
}

// Create food from redis by id
func NewFoodFromRedis(id int) (food Food, err error) {
	c := pool.Get()
	defer c.Close()
	food.Id = id
	food.Price = food2Price[id]
	food.Stock, err = redis.Int(c.Do("HGET", "f", id))
	if err != nil {
		return food, err
	}
	return food, nil
}

// Get foods.
func GetFoods(w http.ResponseWriter, r *http.Request) {
	if !ValidateAccessToken(r) {
		ResponseErrAccessToken(w)
		return
	}
	w.WriteHeader(http.StatusOK)
	ResponseJsonBytesWithLen(w, foodsJsonBytes, foodsJsonBytesLen)
}

// ----------------------- Cart -----------------------
// Generate cart id from integers
func GenerateCartId(userId int) string {
	randInt := rand.Intn(99)
	return fmt.Sprintf("%05d-%02d%02d", userId, procId, randInt)
}

// Get user id by cartId
func GetUserIdByCartId(cartId string) int {
	userId, _ := strconv.Atoi(cartId[:5])
	return userId
}

// Validate cart id.
func ValidateCartId(cartId string) bool {
	return len(cartId) > 5 && cartId[5] == '-'
}

// Create cart.
func CreateCart(w http.ResponseWriter, r *http.Request) {
	user, err := GetUserByAccessToken(r)
	if err != nil {
		if err == ErrInvalidAccessToken {
			ResponseErrAccessToken(w)
			return
		}
		panic(err)
	}
	cartId := GenerateCartId(user.Id)
	w.WriteHeader(http.StatusOK)
	b := FastConcatStringsToBytes(3, `{"cart_id":"`, cartId, `"}`)
	ResponseJsonBytes(w, b)
}

// Add food to cart.
func CartAddFood(w http.ResponseWriter, r *http.Request) {
	user, err := GetUserByAccessToken(r)
	if err != nil {
		if err == ErrInvalidAccessToken {
			ResponseErrAccessToken(w)
			return
		}
		panic(err)
	}
	cartId := r.URL.Path[7:]
	if !ValidateCartId(cartId) {
		ResponseErrCartNotFound(w)
		return
	}
	if GetUserIdByCartId(cartId) != user.Id {
		ResponseErrNotAuthorizedToAcessCart(w)
		return
	}
	req := RequestCartAddFood{}
	err = RequestBind(r, &req)
	if err != nil {
		if err == io.EOF {
			ResponseErrEmptyRequest(w)
		} else {
			ResponseErrMalformedJson(w)
		}
		return
	}
	foodId := req.FoodId
	count := req.Count
	_, ok := food2Price[foodId]
	if !ok {
		ResponseErrFoodNotFound(w)
		return
	}
	if count > 3 {
		ResponseErrFoodOutOfLimit(w)
		return
	}
	cartTKey := "c_" + cartId
	c := pool.Get()
	defer c.Close()
	n, err := redis.Int(c.Do("GET", cartTKey))
	if err != nil && err != redis.ErrNil {
		panic(err)
	}
	if n >= 3 {
		ResponseErrFoodOutOfLimit(w)
		return
	}
	go func() {
		c := pool.Get()
		defer c.Close()
		c.Do("HINCRBY", cartId, foodId, count)
	}()
	go func() {
		c := pool.Get()
		defer c.Close()
		c.Do("INCRBY", cartTKey, count)
	}()
	w.WriteHeader(http.StatusNoContent)
}

// ----------------------- Order -----------------------

// Get order from redis by id.
func NewOrderFromRedis(id string) (Order, error) {
	var order Order
	order.Id = id
	c := pool.Get()
	defer c.Close()
	hashName := fmt.Sprintf("o_%s", id)
	l, err := redis.Ints(c.Do("HGETALL", hashName))
	if err != nil {
		panic(err)
	}
	if len(l) == 0 {
		return order, ErrOrderNil
	}
	order.Items = make([]OrderFoodItem, len(l)/2)
	order.Total = 0
	for i := 0; i < len(l); i += 2 {
		foodId := l[i]
		price := food2Price[foodId]
		count := l[i+1]
		item := OrderFoodItem{FoodId: foodId, Count: count, Price: price}
		order.Items[i] = item
		order.Total = order.Total + count*price
	}
	return order, nil
}

// Get orders
func GetOrders(w http.ResponseWriter, r *http.Request) {
	user, err := GetUserByAccessToken(r)
	if err != nil {
		if err == ErrInvalidAccessToken {
			ResponseErrAccessToken(w)
			return
		}
		panic(err)
	}
	c := pool.Get()
	defer c.Close()
	orders := make([]Order, 0)
	id := userId2OrderId[user.Id]
	order, err := NewOrderFromRedis(id)
	if err != nil {
		if err == ErrOrderNil {
			w.WriteHeader(http.StatusOK)
			ResponseJson(w, orders)
			return
		}
		panic(err)
	}
	orders = append(orders, order)
	w.WriteHeader(http.StatusOK)
	ResponseJson(w, orders)
}

// Make order.
func MakeOrder(w http.ResponseWriter, r *http.Request) {
	user, err := GetUserByAccessToken(r)
	if err != nil {
		if err == ErrInvalidAccessToken {
			ResponseErrAccessToken(w)
			return
		}
		panic(err)
	}
	req := RequestMakeOrder{}
	err = RequestBind(r, &req)
	if err != nil {
		if err == io.EOF {
			ResponseErrEmptyRequest(w)
		} else {
			ResponseErrMalformedJson(w)
		}
		return
	}
	cartId := req.CartId
	if !ValidateCartId(cartId) {
		ResponseErrCartNotFound(w)
		return
	}
	if GetUserIdByCartId(cartId) != user.Id {
		ResponseErrNotAuthorizedToAcessCart(w)
		return
	}
	orderHashName := "o_" + strconv.Itoa(user.Id)
	ch := make(chan int, 3)
	go func() {
		c := pool.Get()
		defer c.Close()
		b, err := redis.Bool(c.Do("EXISTS", orderHashName))
		if err != nil && err != redis.ErrNil {
			panic(err)
		}
		if b {
			ch <- 1
			return
		}
		ch <- 0 // order already made
	}()
	go func() {
		c := pool.Get()
		defer c.Close()
		l, err := redis.Ints(c.Do("HGETALL", cartId))
		if err != nil {
			if err == redis.ErrNil {
				ch <- 2 // cart not found
				return
			}
		}
		if len(l) == 0 {
			ch <- 2 // cart not found
			return
		}
		e := make(chan int, len(l)/2)
		for i := 0; i < len(l); i += 2 {
			foodId := l[i]
			count := l[i+1]
			go func() {
				c := pool.Get()
				defer c.Close()
				remain, err := redis.Int(c.Do("HINCRBY", "f", foodId, 0-count))
				if err != nil {
					panic(err)
				}
				if remain < 0 {
					c.Do("HINCRBY", "f", foodId, count) //rollback
					e <- 1
					return
				}
				e <- 0
			}()
		}
		for i := 0; i < len(l); i += 2 {
			if <-e == 1 {
				ch <- 3 // food out of stock
				return
			}
		}
		ch <- 0
	}()
	for i := 0; i < 2; i++ {
		switch <-ch {
		case 1:
			ResponseErrOrderOutOfLimit(w)
			return
		case 2:
			ResponseErrCartNotFound(w)
			return
		case 3:
			ResponseErrFoodOutOfStock(w)
			return
		}
	}
	go func() {
		c := pool.Get()
		defer c.Close()
		c.Do("RENAME", cartId, orderHashName)
	}()
	orderId := userId2OrderId[user.Id]
	w.WriteHeader(http.StatusOK)
	b := FastConcatStringsToBytes(3, `{"id":"`, orderId, `"}`)
	ResponseJsonBytes(w, b)
}

// Admin get orders
func AdminGetOrders(w http.ResponseWriter, r *http.Request) {
	_, err := GetUserByAccessToken(r)
	if err != nil {
		if err == ErrInvalidAccessToken {
			ResponseErrAccessToken(w)
			return
		}
		panic(err)
	}
	c := pool.Get()
	defer c.Close()
	l, err := redis.Strings(c.Do("KEYS", "o_*"))
	if err != nil {
		panic(err)
	}
	arr := make([]ResponseAdminOrder, len(l))
	for i := 0; i < len(l); i++ {
		e := ResponseAdminOrder{}
		n, err := fmt.Sscanf(l[i], "o_%d", &e.UserId)
		if err != nil {
			panic(err)
		}
		if n != 1 {
			panic(ErrInvalidOrderKeyString)
		}
		id := userId2OrderId[e.UserId]
		order, err := NewOrderFromRedis(id)
		e.Id = order.Id
		e.Items = order.Items
		e.Total = order.Total
		arr[i] = e
	}
	w.WriteHeader(http.StatusOK)
	ResponseJson(w, arr)
}
