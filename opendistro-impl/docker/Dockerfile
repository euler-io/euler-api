FROM openjdk:14-jdk-buster as builder

ARG SOURCE_BRANCH=master
ARG COMPILE_OPTS=-Dmaven.test.skip
ARG MAVEN_VERSION=3.6.3
ARG MAVEN_SHA=c35a1803a6e70a126e80b2b3ae33eed961f83ed74d18fcd16909b2d44d7dada3203f1ffe726c17ef8dcca2dcaa9fca676987befeadc9b9f759967a8cb77181c0
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

# Compiling branch
RUN mkdir /src \
	&& cd /src \
	&& git clone --branch $SOURCE_BRANCH --depth 1 https://github.com/euler-io/euler-api.git /src \
	&& mvn clean package $COMPILE_OPTS

FROM debian:buster

ENV JAVA_OPTS=""

# Installing dependencies
RUN apt-get update \
	&& apt-get install -y --no-install-recommends \
		openjdk-11-jdk-headless

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

CMD java -jar ${JAVA_OPTS} /app/opendistro-http-api.jar -classpath /app/lib com.github.euler.api.ESEulerHTTPAPI