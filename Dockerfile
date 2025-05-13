FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/AirTicketSales-0.0.1-SNAPSHOT.jar /app/AirTicketSales.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/AirTicketSales.jar"]