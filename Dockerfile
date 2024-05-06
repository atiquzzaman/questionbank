### Stage 1: Build Maven Project
FROM gradle:7.2.0-jdk17 AS build
WORKDIR /app

# Copy the entire project and build it
COPY . .
RUN ./gradlew build

# Stage 2: Create Final Image
FROM omnifish/glassfish:7.0.7
WORKDIR /app

COPY ./configure-start-glassfish.sh .
COPY --from=build /app/build/libs/questionbank-1.0-SNAPSHOT.war questionbank.war

ENTRYPOINT ["./configure-start-glassfish.sh"]