# Stage 1: build WAR
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: run với Tomcat 10.1 (Jakarta Servlet 6.0)
FROM tomcat:11.0-jdk17

# Xóa app mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR đã build vào ROOT
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
