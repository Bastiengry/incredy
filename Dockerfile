FROM alpine/java:21-jdk
RUN apk --no-cache add curl
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY target/incredy-*.jar incredy.jar
EXPOSE 8081
EXPOSE 9081
ENTRYPOINT ["java", "-jar", "/incredy.jar"]
