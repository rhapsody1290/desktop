#!/usr/bin/env bash

set -e

export DEBIAN_FRONTEND=noninteractive

# java env
echo -e "--- Install java env ---"
add-apt-repository -y ppa:openjdk-r/ppa

aptitude update
aptitude install -yq openjdk-8-jdk maven

# default to java 8
update-alternatives --config java <<< 2
