FROM hseeberger/scala-sbt:8u151-2.12.4-1.1.0

COPY ./Gatling-Kinesis-master /Gatling-Kinesis-master/
RUN ls /
WORKDIR /Gatling-Kinesis-master
