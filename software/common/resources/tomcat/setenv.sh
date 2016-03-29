# Get configurations for catalina.sh

    CATALINA_CFG="../conf/init.conf"

 [ -r "$CATALINA_CFG" ] && . "${CATALINA_CFG}"

JAVA_OPTS="$JAVA_OPTS -Djava.security.auth.login.config=@tomcat.home@/conf/jaas.config"
