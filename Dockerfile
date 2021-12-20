FROM maven:3.5-jdk-8-alpine
COPY src /app/src
COPY pom.xml /app/pom.xml
WORKDIR /app
RUN mvn clean package

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=0 /app/target/SudokuP2P-1-jar-with-dependencies.jar /app/SudokuP2P.jar
ENV MASTERIP=127.0.0.1
ENV ID=0
CMD java -jar SudokuP2P.jar -m $MASTERIP -id $ID
