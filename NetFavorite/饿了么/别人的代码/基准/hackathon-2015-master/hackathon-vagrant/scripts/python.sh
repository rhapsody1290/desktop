#!/usr/bin/env bash

set -e

export DEBIAN_FRONTEND=noninteractive

# python env
echo -e "--- Install python env ---"
aptitude install -yq python3-dev
aptitude install -yq pypy pypy-dev

# make virtualenvs
echo -e "--- Install python virtualenvs ---"
yes | pip install virtualenv
virtualenv -p /usr/bin/python2 /srv/virtualenvs/py2
virtualenv -p /usr/bin/python3 /srv/virtualenvs/py3
virtualenv -p /usr/bin/pypy /srv/virtualenvs/pypy

echo -e "--- Install cython in virtualenvs ---"
yes | /srv/virtualenvs/py2/bin/pip install cython
yes | /srv/virtualenvs/py3/bin/pip install cython
