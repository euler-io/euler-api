#!/bin/bash

set -e

mvn clean spring-boot:run \
	-Dmaven.repo.local=/mvn/.m2/repository \
	-Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8000 \
	-Dconfig.file=/src/docker-compose/dev.conf"