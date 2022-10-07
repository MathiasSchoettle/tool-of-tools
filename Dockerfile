
FROM amazonlinux:2
ARG version=18.0.2.9-1
ENV JAVA_HOME=/usr/lib/jvm/java-18-amazon-corretto
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
