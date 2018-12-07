#!/bin/bash
Denable_job=$enable_job
debugArgs=""
if test -z "$Denable_job"
then
    Denable_job=false
fi
echo "enable_job=$Denable_job"

if test -z "$debug"
then
    echo " >>> debug mode off >>> "
else
    debugArgs="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
    echo " ################  Enable remote debug, port: 5005 ############### "
fi

java -Xmx256m -Xms256m -Djava.security.egd=file:/dev/./urandom -Duser.timezone=GMT+8 -Denable_job=$Denable_job $debugArgs -jar /app/szjh-0.0.1-SNAPSHOT.jar