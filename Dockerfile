FROM maven:3.9.6-eclipse-temurin-17 as builder
COPY . /app
WORKDIR /app
RUN mvn clean package
FROM tomcat:10.0-jdk17-openjdk-slim
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /app/target/support-service-new.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]