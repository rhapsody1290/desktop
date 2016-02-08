#!/usr/bin/env bash

set -e

# env
APP_HOST="0.0.0.0"
APP_PORT="8080"
DB_HOST="localhost"
DB_PORT="3306"
DB_NAME="eleme"
DB_USER="root"
DB_PASS="toor"
REDIS_HOST="localhost"
REDIS_PORT="6379"
PYTHONPATH="/vagrant"
GOPATH="/srv/gopath"
JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64"

echo -e "--- Bootstraping now ---"
export DEBIAN_FRONTEND=noninteractive

# relocate scripts
cp /tmp/gendata                  /usr/local/bin/gendata
cp /tmp/launcher                 /usr/local/bin/launcher
cp /tmp/hackathon-appreload      /usr/local/bin/hackathon-appreload
cp /tmp/hackathon-app.conf       /etc/init/hackathon-app.conf
cp /tmp/hackathon-appreload.conf /etc/init/hackathon-appreload.conf
chmod +x /usr/local/bin/*

# common
echo -e "--- Update packages ---"
sed -i "s/archive.ubuntu.com/mirrors.aliyun.com/g" /etc/apt/sources.list
aptitude -yq update
aptitude -yq upgrade
echo -e "--- Install basic packages ---"
aptitude -yq install git htop build-essential python-dev

# mysql
echo -e "--- Install mysql server ---"
echo "mysql-server mysql-server/root_password password $DB_PASS"       | debconf-set-selections
echo "mysql-server mysql-server/root_password_again password $DB_PASS" | debconf-set-selections
aptitude install -yq mysql-server

sed -i "s/127.0.0.1/0.0.0.0/g" /etc/mysql/my.cnf
mysql -uroot -p$DB_PASS -e "CREATE DATABASE $DB_NAME"
mysql -uroot -p$DB_PASS -e "GRANT ALL PRIVILEGES ON $DB_NAME.* TO '$DB_USER'@'%' identified by ''"
mysql -uroot -p$DB_PASS -e "GRANT ALL PRIVILEGES ON $DB_NAME.* TO '$DB_USER'@'%' identified by '$DB_PASS'"
mysql -uroot -p$DB_PASS -e "FLUSH PRIVILEGES;"

# redis
echo -e "--- Install redis server ---"
aptitude install -yq redis-server
sed -i "s/127.0.0.1/0.0.0.0/g" /etc/redis/redis.conf

echo -e "--- Install basic python package ---"
wget -O /tmp/get-pip.py https://bootstrap.pypa.io/get-pip.py
python /tmp/get-pip.py 
yes | pip install pymysql livereload sh pyyaml formic

# gen mysql data
echo -e "--- Generate hackathon mysql db ---"
/usr/local/bin/gendata

echo -e "--- Set timezone ---"
timedatectl set-timezone Asia/Shanghai

# export envvars
echo -e "--- Add environment variables locally ---"
cat >> /home/vagrant/.bashrc <<EOF
# set envvars
export APP_HOST=$APP_HOST
export APP_PORT=$APP_PORT
export DB_HOST=$DB_HOST
export DB_PORT=$DB_PORT
export DB_NAME=$DB_NAME
export DB_USER=$DB_USER
export DB_PASS=$DB_PASS
export REDIS_HOST=$REDIS_HOST
export REDIS_PORT=$REDIS_PORT
export PYTHONPATH=$PYTHONPATH
export GOPATH=$GOPATH
export JAVA_HOME=$JAVA_HOME
EOF
