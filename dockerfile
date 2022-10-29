FROM openjdk:11 AS build
WORKDIR /workspace/app
COPY . /workspace/app
RUN ./gradlew clean build

FROM openjdk:11
EXPOSE 8080
VOLUME /tmp
RUN mkdir /app
ENV ENV="" DBURL=jdbc:postgresql://houzez-app-database.cqpgagsfsvqr.ap-southeast-2.rds.amazonaws.com:5432/postgres
COPY --from=build /workspace/app/build/libs/*.jar /app/houzez-backend-application.jar
ENTRYPOINT ["sh", "-c", "java -Dspring.datasource.url=$DBURL -jar /app/houzez-backend-application.jar"]