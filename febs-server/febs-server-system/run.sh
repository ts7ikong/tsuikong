#!/usr/bin/env bash
mvn package

docker build -t febs-server-system .
