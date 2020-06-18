#!/bin/bash

CURRENT_UID=$(id -u) CURRENT_GID=$(id -g) docker-compose -f docker-compose-dev.yml up