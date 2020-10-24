FROM openjdk:11-jre-slim

RUN mkdir -p /opt/app
ADD restapi/target/scala-2.13/phanes.jar /opt/app/
ADD logback/* /opt/app/

EXPOSE 8080 9000

WORKDIR /opt/app

ENTRYPOINT exec java $JAVA_OPTS -jar phanes.jar
