#!/usr/bin/env bash

set -e

export DEBIAN_FRONTEND=noninteractive

# go env
apt-add-repository -y ppa:evarlast/golang1.5

apt-get update
apt-get install -y golang
