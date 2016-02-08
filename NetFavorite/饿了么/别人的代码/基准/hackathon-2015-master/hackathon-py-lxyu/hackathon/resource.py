# coding: utf-8

import collections
import ujson
import uuid

import itsdangerous

from .cache import food_cache, order_cache, user_cache, cart_cache
from .error import (
    AuthError,
    CartExceedLimitError,
    CartNotFoundError,
    CartNotOwnedError,
    EmptyRequestError,
    FoodNotExistsError,
    FoodOutOfStockError,
    LoginError,
    MalformedJsonError,
    OrderOutOfLimitError,
)
from .utils import signer


class UserResource(object):

    def on_post(self, req, resp):
        if "json" not in req.context:
            raise EmptyRequestError
        jreq = req.context["json"]
        assert jreq is not None

        if jreq.get("username") and jreq.get("password"):
            user_id, token = user_cache.login(jreq["username"],
                                              jreq["password"])
            if token:
                resp.body = {
                    "user_id": user_id,
                    "username": jreq.get("username"),
                    "access_token": token
                }
                return

        raise LoginError


class FoodResource(object):

    def on_get(self, req, resp):
        resp.body = food_cache.get_all()


class CartsResource(object):

    MAXIMUM_FOODS = 3

    def on_post(self, req, resp):
        user_id = req.context["user_id"]
        cart_id = uuid.uuid4().hex
        signed = signer.dumps("%s:%s" % (user_id, cart_id))
        resp.body = '{"cart_id": "%s"}' % signed


class CartResource(object):

    def on_patch(self, req, resp, cart_id):
        user_id = req.context["user_id"]

        try:
            _uid, _cart_id = signer.loads(cart_id).split(':')
            if _uid != user_id:
                raise CartNotOwnedError
        except itsdangerous.BadSignature:
            raise CartNotFoundError

        food_patch = req.context["json"]
        try:
            food_id, count = food_patch["food_id"], food_patch["count"]
        except KeyError:
            raise MalformedJsonError

        if count == 0:
            return

        if count > 3:
            raise CartExceedLimitError

        if count < 0:
            count = max(count, -3)

        if food_id not in food_cache:
            raise FoodNotExistsError

        if not cart_cache.patch_cart(_cart_id, food_id, count):
            raise CartExceedLimitError


class OrderResource(object):

    def on_get(self, req, resp):
        user_id = req.context["user_id"]

        if order_cache.is_new(user_id):
            resp.body = "[]"
            return

        order_str = order_cache.get_order(user_id)
        order_id, food_ids = order_str[0], order_str[1:]

        food_counts = collections.Counter(food_ids)
        order = {
            "id": order_id,
            "items": [
                {"food_id": int(f), "count": c} for f, c in food_counts.items()
            ],
            "total": sum(
                food_cache.get_food(int(f))["price"] * c
                for f, c in food_counts.items()
            ),
        }
        resp.body = "[%s]" % ujson.dumps(order)

    def on_post(self, req, resp):
        user_id = req.context["user_id"]

        if not order_cache.is_new(user_id):
            raise OrderOutOfLimitError

        req_j = req.context["json"]
        if "cart_id" not in req_j:
            raise MalformedJsonError

        cart_str = req_j["cart_id"]

        try:
            _uid, cart_id = signer.loads(cart_str).split(':')
            if _uid != user_id:
                raise CartNotOwnedError
        except itsdangerous.BadSignature:
            raise CartNotFoundError

        order_id = uuid.uuid4().hex
        result = order_cache.make_order(
            keys=[user_id, cart_id],
            args=[order_id])

        if result == 0:
            resp.body = '{"id": "%s"}' % order_id
        else:
            raise FoodOutOfStockError


class AdminResource(object):

    ADMIN_ID = 1

    def on_get(self, req, resp):
        if int(req.context["user_id"]) == self.ADMIN_ID:
            orders = []
            for user_id, order_info in order_cache.get_all().items():
                order_id, food_ids = order_info[0], order_info[1:]

                food_counts = collections.Counter(food_ids)
                order = {
                    "id": order_id,
                    "user_id": user_id,
                    "items": [
                        {"food_id": int(f), "count": c}
                        for f, c in food_counts.items()],
                    "total": sum(
                        food_cache.get_food(int(f))["price"] * c
                        for f, c in food_counts.items()),
                }
                orders.append(order)
            resp.body = ujson.dumps(orders)
        else:
            raise AuthError
