# Packer Image Builder

We use packer to build the vagrant boxes for hackathon app development.

## Install packer-io

Download bin from [packer-io](http://packer.io), then execute

```sh
$ packer build -var 'ubuntu_trusty64_ovf=/path/to/base/box.ovf' hackathon-py.json
```

It will generate a fresh build `.vbox` file in current directory, which
can be used by vagrant.


## Components

### scripts

The `scripts` directory contains:

* `base.sh`

  the shell script to run on a raw ubuntu server to build the basic
  env.

* `python.sh`, `go,sh`, `java.sh`

  Additional script to build the envs

* `tune.sh`

  sysctl tuning

### files

The `files` directory contains:

* `gendata`

  a python script to regenerate random mysql tables, including `food`
  and `user` tables.

* `launcher`

  shell script to boot up the application.

  it boot the application server based on `app.yml` file locate in
  the source directory.

  all console output were redirected to `app.log`, where you can check
  for error in case of problems.

* `hackathon-app.conf`

  run `launcher` script as daemon.

* `hackathon-appreload`

  the shell script used to livereload `hackathon-app` service

  the script will monitor the `/vagrant` directory, where the
  source code locates, and restart the `hackathon-app` service if any
  files changed.

* `hackathon-appreload.conf`

  run `hackathon-appreload` script as daemon.
