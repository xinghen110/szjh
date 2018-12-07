MAINTAINER hexin <xin.he@magustek.com>

FROM openjdk:8-jre

VOLUME /tmp

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

ADD wait-for-it.sh /wait-for-it.sh
RUN bash -c 'chmod 777 /wait-for-it.sh'

ADD entrypoint.sh /entrypoint.sh
RUN bash -c 'chmod 777 /entrypoint.sh'

ADD szjh-0.0.1-SNAPSHOT.jar /app/
CMD ["/entrypoint.sh"]

EXPOSE 21010
EXPOSE 5005