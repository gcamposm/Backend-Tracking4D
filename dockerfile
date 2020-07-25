FROM openjdk:8-jre
COPY /target/nimbolu-2.1.5.RELEASE.war /usr/app/
WORKDIR /usr/app
EXPOSE 9443
ENV TZ America/Santiago
ENTRYPOINT ["java", "-jar", "nimbolu-2.1.5.RELEASE.war"]