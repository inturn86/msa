FROM azul/zulu-openjdk:18.0.2.1-jre
VOLUME /tmp
COPY target/gateway-service-1.0.jar GatewayService.jar
ENTRYPOINT ["java", "-jar", "GatewayService.jar"]