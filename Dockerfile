FROM eclipse-temurin:21-jre
WORKDIR /var/lib/jenkins/workspace/jars
ADD target/springboot-conf-eureka-server-0.0.1-SNAPSHOT.jar springboot-conf-eureka-server.jar
COPY target/springboot-conf-eureka-server-0.0.1-SNAPSHOT.jar springboot-conf-eureka-server-0.0.1-SNAPSHOT.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "springboot-conf-eureka-server-0.0.1-SNAPSHOT.jar"]