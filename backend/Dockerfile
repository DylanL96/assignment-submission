# Stage 1: Build the application
# openjdk11 is an image that will contain our JDK installed on it
# this is because to run java, we need an environment to run our java code on
# give it an alias called build
FROM adoptopenjdk/openjdk11:ubi as build

# Set the current working directory inside the image
# this will create the app folder on the image we are pulling from, if it does not exist already
# this is also where we can deploy our app as well
# this app directory will be set as the new base working directory
# equivalent to # mkdir app && cd app
WORKDIR /app

# copy maven executable to the image
# copying from our machine (grabbing files in the backend) and copy it to the working directory "app"
COPY mvnw .
COPY .mvn .mvn

# copy pom.xml file to the app folder
COPY pom.xml .

# build all the dependencies in preparation to go offline
# assume we do not have internet access, make sure everythig is on this machine so it can still run
# separate step so the dependencies will be cached unless
# pom.xml has changed
RUN ./mvnw dependency:go-offline -B

# copy the project source
COPY src src

# package the application
# this is going to run and package everything together, skip unit and integration tests (-DskipTests)
# so even if test fails, it will still package

# make directory where all of the stuff will be moved into
# if successful, we will CD into it and then run the jar -xf ../*jar command
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Stage 2: A minimal docker image with command to run the app
FROM adoptopenjdk/openjdk11:ubi

ARG DEPENDENCY=/app/target/dependency

# copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
 
ENTRYPOINT ["java","-cp","app:app/lib/*","com.example.backend.BackendApplication"]