FROM openjdk:8-jre-slim
MAINTAINER xuxueli
label version=2.0.0
ENV PARAMS=""

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD ./*.jar /github-xxl-job.jar

ENTRYPOINT ["sh","-c","java -jar /github-xxl-job.jar  ${PARAMS}"]
