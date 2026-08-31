FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /build
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

RUN chmod +x gradlew && ./gradlew clean war -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache curl && \
    addgroup -S tomcat && adduser -S tomcat -G tomcat

ENV TOMCAT_VERSION=10.1.24
ENV CATALINA_HOME=/opt/tomcat

RUN curl -fsSL https://archive.apache.org/dist/tomcat/tomcat-10/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz \
    -o /tmp/tomcat.tar.gz && \
    mkdir -p ${CATALINA_HOME} && \
    tar xzf /tmp/tomcat.tar.gz -C ${CATALINA_HOME} --strip-components=1 && \
    rm /tmp/tomcat.tar.gz && \
    rm -rf ${CATALINA_HOME}/webapps/* && \
    chown -R tomcat:tomcat ${CATALINA_HOME}

COPY --from=builder /build/build/libs/*.war ${CATALINA_HOME}/webapps/tartaruga-cometa.war

USER tomcat
EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=5s --start-period=30s --retries=5 \
    CMD curl -f http://localhost:8080/tartaruga-cometa/ || exit 1

CMD ["/opt/tomcat/bin/catalina.sh", "run"]
