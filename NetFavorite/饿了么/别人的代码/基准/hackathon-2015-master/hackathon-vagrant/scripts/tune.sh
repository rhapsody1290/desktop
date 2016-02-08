#!/usr/bin/env bash

set -e

cat << EOF >> /etc/security/limits.conf
* soft nofile 65535
* hard nofile 65535
EOF

cat << EOF >> /etc/pam.d/common-session
session required pam_limits.so
EOF

cat << EOF > /etc/sysctl.conf
fs.file-max = 65535
fs.inotify.max_user_watches = 65535

kernel.shmall = 2097152
kernel.shmmax = 2147483648
kernel.sem = "250 32000 256 512"

net.core.netdev_max_backlog = 65535
net.core.somaxconn = 65535
net.core.rmem_default = 8388608
net.core.rmem_max = 16777216
net.core.wmem_default = 8388608
net.core.wmem_max = 16777216

net.ipv4.tcp_fastopen = 3
net.ipv4.tcp_tw_recycle = 1
net.ipv4.tcp_tw_reuse = 1
net.ipv4.tcp_rmem = 4096 65535 67108864
net.ipv4.tcp_wmem = 4096 65535 67108864
net.ipv4.tcp_low_latency = 1
net.ipv4.tcp_congestion_control = cubic

# disable ipv6
net.ipv6.conf.all.disable_ipv6 = 1
net.ipv6.conf.default.disable_ipv6 = 1
net.ipv6.conf.lo.disable_ipv6 = 1
EOF
