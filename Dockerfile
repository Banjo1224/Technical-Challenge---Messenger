FROM  ubuntu:latest

MAINTAINER  Brandon Bates <author@email.com>

RUN apt-get update -y

RUN apt-get install default-jre -y

ADD ./Messenger.jar messenger.jar

EXPOSE 8080

CMD java -jar messenger.jar
