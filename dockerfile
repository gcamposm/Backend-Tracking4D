FROM openjdk:8-jre
COPY /target/tracking4d-1.0.0.RELEASE.war /usr/app/
WORKDIR /usr/app
EXPOSE 9443
ENV TZ America/Santiago
ENTRYPOINT ["java", "-jar", "tracking4d-1.0.0.RELEASE.war"]