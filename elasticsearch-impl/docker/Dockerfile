FROM debian:buster

ARG PROJECT_VERSION

# Installing dependencies
RUN apt-get update \
	&& apt-get install -y --no-install-recommends \
		openjdk-11-jdk-headless \
		curl

# Installing app
COPY ./download-app.sh .
RUN ./download-app.sh

RUN addgroup --gid 1100 euler \
  && adduser --uid 1100 --disabled-password --gid 1100 euler --gecos "" \
  && chown euler:euler -R /app

# Cleaning image.
RUN  apt-get clean  \
	&& rm -rf /var/lib/apt/lists/* \
	&& apt-get purge -y curl \
	&& apt-get autoremove -y \
	&& rm ./download-app.sh