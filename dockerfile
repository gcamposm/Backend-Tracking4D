FROM openjdk:8-jre
COPY /target/backend-0.0.1-SNAPSHOT.war /usr/app/
WORKDIR /usr/app
EXPOSE 9443
ENV TZ America/Santiago
ENTRYPOINT ["java", "-jar", "backend-0.0.1-SNAPSHOT.war"]