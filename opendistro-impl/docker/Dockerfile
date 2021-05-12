FROM openjdk:14-jdk-buster as builder

ARG MAVEN_VERSION=3.8.1
ARG MAVEN_SHA=0ec48eb515d93f8515d4abe465570dfded6fa13a3ceb9aab8031428442d9912ec20f066b2afbf56964ffe1ceb56f80321b50db73cf77a0e2445ad0211fb8e38d
ARG MAVEN_BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

# Install maven
RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz ${MAVEN_BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  && echo "${MAVEN_SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c - \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# Installing dependencies
RUN apt-get update \
	&& apt-get install -y --no-install-recommends \
		git

ARG SOURCE_BRANCH=master
ARG REPO_URL=https://github.com/euler-io/euler-api.git
ARG CLONE_ARGS="--depth 1"
ARG COMPILE_OPTS="-Dmaven.test.skip -DdownloadSources=false -DdownloadJavadocs=false"

# Clone and download dependencies
RUN mkdir /src \
	&& cd /src \
	&& git clone --branch $SOURCE_BRANCH $CLONE_ARGS $REPO_URL /src \
	&& mvn dependency:go-offline

# Package
RUN mvn package $COMPILE_OPTS

FROM debian:buster

ENV JAVA_OPTS=""
ENV APP_ARGS=""

# Installing dependencies
RUN apt-get update \
	&& apt-get install -y --no-install-recommends \
		openjdk-11-jre-headless

# Installing app
RUN mkdir -p /app/lib
COPY --from=builder /src/opendistro-impl/target/opendistro-http-api.jar /app/

# Adding app group and user
RUN addgroup --gid 1100 euler \
  && adduser --uid 1100 --disabled-password --gid 1100 euler --gecos "" \
  && chown euler:euler -R /app

# Cleaning image.
RUN apt-get clean  \
	&& rm -rf /var/lib/apt/lists/* \
	&& apt-get autoremove -y
	
USER euler
WORKDIR /app

CMD java -Dloader.path=/app/lib ${JAVA_OPTS} -jar /app/opendistro-http-api.jar ${APP_ARGS}
