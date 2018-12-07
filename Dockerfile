FROM openjdk:8-alpine
MAINTAINER hexin <xin.he@magustek.com>

VOLUME /tmp

#RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

#ADD wait-for-it.sh /wait-for-it.sh
#RUN bash -c 'chmod 777 /wait-for-it.sh'

#ADD entrypoint.sh /entrypoint.sh
#RUN bash -c 'chmod 777 /entrypoint.sh'

ADD target/szjh-0.0.1-SNAPSHOT.jar /app/
#CMD ["/entrypoint.sh"]
#ENTRYPOINT ["java","-cp","app:app/lib/*","hello.Application"]
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/szjh-0.0.1-SNAPSHOT.jar

EXPOSE 21010
EXPOSE 5005
