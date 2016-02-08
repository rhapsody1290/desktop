#!/usr/bin/env bash

sudo pip install redis
python ./draenor/sql/utils.py
mvn clean package -Dmaven.test.skip=true -f ./draenor/pom.xml
