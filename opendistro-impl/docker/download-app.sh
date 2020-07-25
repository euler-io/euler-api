#!/bin/bash

set -e

mkdir -p /app/lib

if [[ ${PROJECT_VERSION} == *"SNAPSHOT"* ]]; then
	snapshotVersion=$(curl -s https://oss.sonatype.org/content/repositories/snapshots/com/github/euler-io/opendistro-impl/${PROJECT_VERSION}/maven-metadata.xml | grep '<value>' | head -1 | sed "s/.*<value>\([^<]*\)<\/value>.*/\1/")
	repo="https://oss.sonatype.org/content/repositories/snapshots/com/github/euler-io/opendistro-impl/${PROJECT_VERSION}/opendistro-impl-${snapshotVersion}.jar"
else
	repo="https://repo1.maven.org/maven2/com/github/euler-io/opendistro-impl/${PROJECT_VERSION}/opendistro-impl-${PROJECT_VERSION}.jar"
fi

curl ${repo} -o /app/opendistro-http-api.jar