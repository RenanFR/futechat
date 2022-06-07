FROM openjdk:18-jdk-alpine
ARG DEPENDENCY=target/generated-sources
COPY ${DEPENDENCY}/annotations /futechat/annotations
ENTRYPOINT ["java","-cp","futechat:futechat/annotations/*","br.com.futechat.discord.FutechatApplication"]