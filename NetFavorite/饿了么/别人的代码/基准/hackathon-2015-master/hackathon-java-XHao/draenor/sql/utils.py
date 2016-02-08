# coding: utf-8

import pymysql
import redis
import uuid

from settings import config

rd = redis.Redis(
    config.redis_host,
    config.redis_port)

db = pymysql.connect(
    host=config.db_host,
    port=config.db_port,
    user=config.db_user,
    password=config.db_pass,
    db=config.db_name)

def main():
    lock = rd.setnx("init:eleme", "false")  
    if lock == 0:
        while 1:
            lock=rd.get("init:eleme")
            if lock=='true':
                break
        return

    cur = db.cursor()

    # load users
    cur.execute("SELECT id, name, password FROM user;")
    with rd.pipeline() as p:
        for i, name, pw in cur.fetchall():
            token=uuid.uuid4().hex
            p.set("user:%s" % name,
                    '{"user_id": %s, "username": "%s", "password": "%s", "access_token": "%s"}' % (i, name, pw, token))
            p.lpush("user:list", "user:%s" %name)
        p.execute()

    # load foods
    cur.execute("SELECT id, stock, price FROM food;")
    with rd.pipeline() as p:
        for i, stock, price in cur.fetchall():
            p.hmset("food:stock", {i:stock})
            detail='{"price":%s,"id":%s}' % (price,i)
            p.set("food:%s" % i, detail)
            p.sadd("food:list", i)
        p.execute()

    rd.set("init:eleme", "true")

if __name__ == '__main__':
    main()
