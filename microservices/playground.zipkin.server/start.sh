#!/usr/bin/env bash

java -jar zipkin-web-all.jar -zipkin.web.port=:9412 -zipkin.web.rootUrl=/ -zipkin.web.query.dest=lalhost:9411