#!/bin/bash

set -e

if [ ! -d "$PWD/docker/dev-certificates" ]; then
	OUTPUT=$PWD/docker/dev-certificates $PWD/docker/generate_certificates.sh
fi

CURRENT_UID=$(id -u) CURRENT_GID=$(id -g) docker-compose -f docker-compose-dev.yml up