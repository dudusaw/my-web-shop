FROM adoptopenjdk:11-jre-hotspot
ARG PROJECT_DIR
WORKDIR $PROJECT_DIR/build/
COPY *.jar app.jar
ENTRYPOINT ["java", "-XX:+UseG1GC", "-jar", "app.jar"]
