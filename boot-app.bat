call mvnw clean package -DskipTests
call cd docker/
call docker-compose build --no-cache
call docker-compose up