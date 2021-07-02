FROM amazoncorretto:11-alpine
WORKDIR /workdir/
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]