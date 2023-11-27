FROM eclipse-temurin:21.0.1_12-jdk-ubi9-minimal

WORKDIR /app
RUN ls -al

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
COPY hooks ./hooks
RUN ls -al
# RUN ./mvnw dependency:resolve # not sure about the necessity of this command 
RUN ./mvnw package spring-boot:repackage
RUN cd target && ls

CMD ["java", "-jar", "./target/order2gatherBE-0.0.1-SNAPSHOT.jar"]