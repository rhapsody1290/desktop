#!/usr/bin/env pypy
# -*- coding: utf-8 -*-

import pymysql
import redis
import time

from hackathon.settings import config


def warm():
    rd = redis.StrictRedis(
        config.redis_host,
        config.redis_port,
        decode_responses=True
    )

    db = pymysql.connect(
        host=config.db_host,
        port=config.db_port,
        user=config.db_user,
        password=config.db_pass,
        db=config.db_name)

    # clean all before start
    try:
        with rd.lock("lockit", timeout=10):
            if rd.exists("foods"):
                return

            cur = db.cursor()

            # load users
            cur.execute("SELECT id, name, password FROM user;")
            with rd.pipeline() as p:
                for i, name, pw in cur.fetchall():
                    p.hmset("users:%s" % i,
                            {"id": i, "name": name, "password": pw})
                    p.sadd("users", i)
                p.execute()

            # load foods
            cur.execute("SELECT id, stock, price FROM food;")
            with rd.pipeline() as p:
                for i, stock, price in cur.fetchall():
                    p.set("foods:stock:%s" % i, stock)
                    p.set("foods:price:%s" % i, price)
                    p.sadd("foods", i)
                p.execute()

            cur.close()
            db.close()
    except:
        pass

    for _ in range(10):
        if rd.exists("foods"):
            break
        time.sleep(1)


if __name__ == "__main__":
    warm()
