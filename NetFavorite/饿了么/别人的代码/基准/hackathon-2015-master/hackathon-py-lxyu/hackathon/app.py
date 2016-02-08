# coding: utf-8

import falcon

from .error import error_serializer
from .middleware import (
    AuthMiddleware,
    JSONMiddleware,
)
from .resource import (
    AdminResource,
    CartResource,
    CartsResource,
    FoodResource,
    OrderResource,
    UserResource,
)

# middleware
app = falcon.API(middleware=[
    AuthMiddleware(),
    JSONMiddleware(),
])
app.set_error_serializer(error_serializer)

# resources
app.add_route('/login', UserResource())
app.add_route('/carts', CartsResource())
app.add_route('/carts/{cart_id}', CartResource())
app.add_route('/orders', OrderResource())
app.add_route('/foods', FoodResource())
app.add_route('/admin/orders', AdminResource())
