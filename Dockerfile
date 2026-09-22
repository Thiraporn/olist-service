FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY . .

RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

RUN mkdir -p /root/.m2
COPY .m2/settings.xml /root/.m2/settings.xml

ARG GITHUB_USERNAME
ARG GITHUB_TOKEN

ENV GITHUB_USERNAME=${GITHUB_USERNAME}
ENV GITHUB_TOKEN=${GITHUB_TOKEN}

RUN mvn --settings /root/.m2/settings.xml clean package -DskipTests


FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/olist-service-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]


# FROM eclipse-temurin:17-jdk AS build
#
# WORKDIR /app
#
# EXPOSE 8080
# EXPOSE 8081
#
# COPY . .
#
# RUN apt-get update && \
#     apt-get install -y maven && \
#     rm -rf /var/lib/apt/lists/*
#
# RUN mkdir -p /root/.m2
# COPY .m2/settings.xml /root/.m2/settings.xml
#
# RUN mvn --settings /root/.m2/settings.xml clean package -DskipTests
#
#
# FROM eclipse-temurin:17-jdk
#
# WORKDIR /app
#
# COPY --from=build /app/target/olist-service-1.0.0.jar app.jar
#
#
#
# ENTRYPOINT ["java", "-jar", "app.jar"]

# FROM eclipse-temurin:17-jdk AS build
#
# WORKDIR /app
# EXPOSE 8080
# EXPOSE 8081
#
# COPY . .
#
# RUN apt-get update && \
#     apt-get install -y maven && \
#     rm -rf /var/lib/apt/lists/*
#
# RUN mvn clean package -DskipTests
#
#
# FROM eclipse-temurin:17-jdk
#
# WORKDIR /app
#
# COPY --from=build /app/target/olist-service-1.0.0.jar app.jar
#
# EXPOSE 8080
#
# ENTRYPOINT ["java", "-jar", "app.jar"]