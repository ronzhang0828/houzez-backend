FROM openjdk:17 AS build
WORKDIR /workspace/app
COPY . /workspace/app
RUN microdnf install findutils
RUN chmod +x ./gradlew
RUN ./gradlew clean build

FROM openjdk:17
EXPOSE 8080
VOLUME /tmp
RUN mkdir /app
ENV ENV="" DBURL=jdbc:postgresql://houzez-app-database.cqpgagsfsvqr.ap-southeast-2.rds.amazonaws.com:5432/postgres
COPY --from=build /workspace/app/build/libs/*SNAPSHOT.jar /app/houzez-backend-application.jar
ENTRYPOINT ["sh", "-c", "java -Dspring.datasource.url=$DBURL -jar /app/houzez-backend-application.jar"]