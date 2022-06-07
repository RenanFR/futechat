FROM openjdk:18-jdk-alpine
EXPOSE 8080
ADD /target/futechat-*.jar futechat.jar
ENTRYPOINT ["java","-jar","futechat.jar"]