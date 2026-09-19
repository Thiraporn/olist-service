FROM eclipse-temurin:17-jdk

ARG GITHUB_USERNAME
ARG GITHUB_TOKEN

ENV GITHUB_USERNAME=$GITHUB_USERNAME
ENV GITHUB_TOKEN=$GITHUB_TOKEN

WORKDIR /app

COPY . .

RUN apt-get update && apt-get install -y maven

RUN mkdir -p /root/.m2
RUN cp .m2/settings.xml /root/.m2/settings.xml

RUN mvn --settings /root/.m2/settings.xml clean package -Dmaven.test.skip=true

EXPOSE 8080

CMD ["java","-jar","target/olist-service-1.0.0.jar"]