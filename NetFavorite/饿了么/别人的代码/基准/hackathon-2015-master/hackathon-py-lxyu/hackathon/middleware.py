# coding: utf-8

import ujson

import falcon
import itsdangerous

from .utils import signer
from .error import (
    MalformedJsonError,
    EmptyRequestError,
    AuthError,
)


class AuthMiddleware(object):

    def process_request(self, req, resp):

        # bypass login req
        if req.path in ('/', "/login"):
            return

        token = req.get_header("Access-Token") or req.get_param("access_token")
        if not token:
            raise AuthError

        try:
            req.context["user_id"] = signer.loads(token)
        except itsdangerous.BadSignature:
            raise AuthError


class JSONMiddleware(object):

    def process_request(self, req, resp):

        if not req.content_length:
            return

        body = req.stream.read()
        if not body:
            raise EmptyRequestError

        try:
            req.context["json"] = ujson.loads(body.decode("utf-8"))
        except (ValueError, UnicodeDecodeError):
            raise MalformedJsonError

    def process_response(self, req, resp, resource):

        if resp.body is None:
            resp.status = falcon.HTTP_204
        else:
            resp.status = falcon.HTTP_200
            if isinstance(resp.body, (dict, list)):
                resp.body = ujson.dumps(resp.body)
