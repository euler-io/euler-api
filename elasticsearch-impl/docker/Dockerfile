FROM debian:buster

ARG PROJECT_VERSION
ENV JAVA_OPTS=""

# Installing dependencies
RUN apt-get update \
	&& apt-get install -y --no-install-recommends \
		openjdk-11-jdk-headless \
		curl

# Installing app
COPY ./download-app.sh .
RUN ./download-app.sh

# Adding app group and user
RUN addgroup --gid 1100 euler \
  && adduser --uid 1100 --disabled-password --gid 1100 euler --gecos "" \
  && chown euler:euler -R /app

# Cleaning image.
RUN  apt-get clean  \
	&& rm -rf /var/lib/apt/lists/* \
	&& apt-get purge -y curl \
	&& apt-get autoremove -y \
	&& rm ./download-app.sh
	
USER euler
WORKDIR /app

CMD java -jar ${JAVA_OPTS} /app/opendistro-http-api.jar -classpath /app/lib com.github.euler.api.ESEulerHTTPAPI