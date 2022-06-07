FROM openjdk:18-jdk-alpine
EXPOSE 8080
ARG DEPENDENCY=target/generated-sources
COPY ${DEPENDENCY}/annotations /futechat/annotations
ADD /target/futechat-*.jar futechat.jar
ENTRYPOINT ["java","-cp","futechat:futechat/annotations/*","-jar","futechat.jar"]