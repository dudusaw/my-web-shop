FROM adoptopenjdk:11-jre-hotspot
WORKDIR opt/my-app/workdir/
COPY *.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "-XX:+UseG1GC"]