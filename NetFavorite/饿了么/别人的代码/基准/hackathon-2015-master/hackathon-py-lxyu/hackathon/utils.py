# coding: utf-8

import itsdangerous
import redis

from .settings import config

signer = itsdangerous.URLSafeSerializer("notasecurekey")

rd = redis.StrictRedis(
    config.redis_host,
    config.redis_port,
    decode_responses=True
)
