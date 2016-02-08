# coding: utf-8

import ujson

import time

from .utils import rd, signer


class FoodCache(object):

    KEY = "foods:%s"

    def __init__(self, backend):
        self.backend = backend
        self._load()

    def _load(self):
        # load from cache
        food_ids = self.backend.smembers("foods")
        food_stocks = self.backend.mget(
            "foods:stock:%s" % i for i in food_ids)
        food_prices = self.backend.mget(
            "foods:price:%s" % i for i in food_ids)

        self.cached_foods = {
            int(i): {"id": int(i), "stock": int(s), "price": int(p)}
            for i, s, p in zip(food_ids, food_stocks, food_prices)}

        self.update_time = time.time()

    def __contains__(self, item):
        return item in self.cached_foods

    def get_food(self, food_id):
        return self.cached_foods[food_id]

    def get_all(self, timeout=10):
        if time.time() - self.update_time > timeout:
            self._load()
        return ujson.dumps(list(self.cached_foods.values()))
food_cache = FoodCache(rd)


class CartCache(object):

    KEY = "carts:%s"
    USER_KEY = "carts:user:%s"

    # KEYS = [cart_id]
    # ARGV = [*food_ids]
    # language=Lua
    LUA_ADD_FOOD = ' '.join("""
    local cart_key = 'carts:' .. KEYS[1]
    local count = tonumber(redis.call('LLEN', cart_key))
    local patch = tonumber(table.getn(ARGV))
    if count + patch <= 3 then
        redis.call('LPUSH', cart_key, unpack(ARGV))
        return 0
    else
        return 1
    end
     """.split())

    def __init__(self, backend):
        self.backend = backend

        self.add_food = backend.register_script(self.LUA_ADD_FOOD)

    def get_cart(self, cart_id):
        cart_str = self.backend.get(self.KEY % cart_id, force=True)
        if cart_str:
            return ujson.loads(cart_str)

    def set_cart(self, user_id, cart):
        self.backend.sadd(self.USER_KEY % user_id, cart["id"])
        self.backend.set(self.KEY % cart["id"], ujson.dumps(cart), force=True)

    def patch_cart(self, cart_id, food_id, count):
        if count > 0:
            return self.add_food(keys=[cart_id],
                                 args=[str(food_id)] * count) == 0
        else:
            self.backend.lrem(self.KEY % cart_id, count, str(food_id))
            return True
cart_cache = CartCache(rd)


class UserCache(object):

    KEY = "users:%s"
    TOKEN_KEY = "token:%s"

    def __init__(self, backend):
        self.backend = backend
        self._load()

    def _load(self):
        user_ids = self.backend.smembers("users")
        with self.backend.pipeline() as p:
            for i in user_ids:
                p.hgetall(self.KEY % i)
            res = p.execute()

        self.auth_map = {
            u["name"]: {"id": int(u["id"]), "password": u["password"]}
            for u in res}

        self.user_token = {int(i): signer.dumps(i) for i in user_ids}
        self.token_user = {k: v for v, k in self.user_token.items()}

    def login(self, username, password):
        if username in self.auth_map and \
                self.auth_map[username]["password"] == password:
            user_id = self.auth_map[username]["id"]
            return user_id, self.user_token[user_id]
        else:
            return None, None

    def auth(self, token):
        return self.token_user.get(token)
user_cache = UserCache(rd)


class OrderCache(object):

    # language=Lua
    LUA_MAKE_ORDER = ' '.join("""
    local cart_key = 'carts:' .. KEYS[2]
    local food_ids = redis.call('LRANGE', cart_key, 0, -1)
    if table.getn(food_ids) == 0 then
        return 1
    end

    for _, f in pairs(food_ids) do
        if tonumber(redis.call('GET', 'foods:stock:' .. f)) < 1 then
            return 2
        else
            redis.call('DECR', 'foods:stock:' .. f)
        end
    end

    local user_key = 'orders:user:' .. KEYS[1]
    local order_id = ARGV[1]
    redis.call('RENAME', cart_key, user_key)
    redis.call('LPUSH', user_key, order_id)
    return 0
    """.split())

    ORDER_KEY = "orders:user:%s"

    def __init__(self, backend):
        self.backend = backend

        self.make_order = backend.register_script(self.LUA_MAKE_ORDER)

    def get_order(self, user_id):
        return self.backend.lrange(self.ORDER_KEY % user_id, 0, -1)

    def is_new(self, user_id):
        return not self.backend.exists(self.ORDER_KEY % user_id)

    def get_all(self):
        order_keys = self.backend.keys(self.ORDER_KEY % '*')
        with self.backend.pipeline() as p:
            for k in order_keys:
                p.lrange(k, 0, -1)
            order_info = p.execute()
        return {int(k.split(':')[-1]): info
                for k, info in zip(order_keys, order_info)}
order_cache = OrderCache(rd)
