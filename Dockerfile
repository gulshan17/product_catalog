FROM openjdk:24-jdk-slim
VOLUME /tmp
COPY target/product_catalog-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
