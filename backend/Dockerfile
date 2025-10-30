# Étape 1 : Build avec Maven
FROM maven:3.9.3-eclipse-temurin-17 AS build

# Forcer l’encodage UTF-8 (important sur Windows)
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8"

WORKDIR /app

# Copier uniquement les fichiers nécessaires au build
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Compiler le projet en évitant les tests
RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# Étape 2 : Exécution
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copier le jar généré depuis l’étape précédente
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Forcer l’encodage Java à UTF-8 à l’exécution aussi
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"

ENTRYPOINT ["java","-jar","app.jar"]
