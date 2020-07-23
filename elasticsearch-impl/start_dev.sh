#!/bin/bash

set -e

if [ ! -d "$PWD/docker-compose/dev-certificates" ]; then
	OUTPUT=$PWD/docker-compose/dev-certificates $PWD/docker-compose/generate_certificates.sh
fi

CURRENT_UID=$(id -u) CURRENT_GID=$(id -g) docker-compose -f docker-compose-dev.yml up