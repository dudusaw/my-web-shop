FROM adoptopenjdk:11
WORKDIR ~/workdir/
COPY *.jar app.jar
EXPOSE 8443
ENTRYPOINT ['java', '-jar', 'app.jar']