# coding: utf-8

import falcon


def error_serializer(req, ex):
    return "application/json", \
           '{"code": "%s", "message": "%s"}' % (ex.title, ex.description)


EmptyRequestError = falcon.HTTPError(
    falcon.HTTP_400, "EMPTY_REQUEST", "请求体为空")

MalformedJsonError = falcon.HTTPError(
    falcon.HTTP_400, "MALFORMED_JSON", "格式错误")

LoginError = falcon.HTTPError(
    falcon.HTTP_403, "USER_AUTH_FAIL", "用户名或密码错误")

AuthError = falcon.HTTPError(
    falcon.HTTP_401, "INVALID_ACCESS_TOKEN", "无效的令牌")

CartNotFoundError = falcon.HTTPError(
    falcon.HTTP_404, "CART_NOT_FOUND", "篮子不存在")

CartExceedLimitError = falcon.HTTPError(
    falcon.HTTP_403, "FOOD_OUT_OF_LIMIT", "篮子中食物数量超过了三个")

CartNotOwnedError = falcon.HTTPError(
    falcon.HTTP_401, "NOT_AUTHORIZED_TO_ACCESS_CART", "无权限访问指定的篮子")

FoodNotExistsError = falcon.HTTPError(
    falcon.HTTP_404, "FOOD_NOT_FOUND", "食物不存在")

FoodOutOfStockError = falcon.HTTPError(
    falcon.HTTP_403, "FOOD_OUT_OF_STOCK", "食物库存不足")

OrderOutOfLimitError = falcon.HTTPError(
    falcon.HTTP_403, "ORDER_OUT_OF_LIMIT", "每个用户只能下一单")
