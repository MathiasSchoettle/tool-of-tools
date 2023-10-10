FROM amazoncorretto:21-alpine
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY target/app.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
