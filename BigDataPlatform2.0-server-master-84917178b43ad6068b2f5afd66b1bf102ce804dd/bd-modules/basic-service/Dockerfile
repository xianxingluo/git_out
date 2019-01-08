# Docker for {name}
# From
FROM reg.hw.io/runtime/java:8

# Who
MAINTAINER "Info......"

# Env
ENV JAVA_OPTS=""

# Add jar
ADD target/*.jar /app.jar

# Exposure to port
EXPOSE 8080

# Commend
CMD [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
