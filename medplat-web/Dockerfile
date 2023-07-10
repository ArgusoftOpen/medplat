# Base image
FROM ubuntu:latest

# Update packages and install necessary tools
RUN apt-get update -y \
    && apt-get install -y wget curl

# Create directory for Java and navigate to it
RUN mkdir -p /usr/ui/imtecho-ui
RUN mkdir -p /usr/web
WORKDIR /usr/web


# Download and install OpenJDK 13
RUN wget https://download.java.net/java/GA/jdk13.0.2/d4173c853231432d94f001e99d882ca7/8/GPL/openjdk-13.0.2_linux-x64_bin.tar.gz \
   && tar -xzvf openjdk-13.0.2_linux-x64_bin.tar.gz \
   && rm openjdk-13.0.2_linux-x64_bin.tar.gz

# Download and install Apache Maven 3.2.5
RUN wget https://mirrors.estointernet.in/apache/maven/maven-3/3.2.5/binaries/apache-maven-3.2.5-bin.tar.gz \
   && tar -xzvf apache-maven-3.2.5-bin.tar.gz \
   && rm apache-maven-3.2.5-bin.tar.gz

# Set environment variables for Java and Maven
ENV JAVA_HOME /usr/web/jdk-13.0.2
ENV PATH $JAVA_HOME/bin:$PATH
ENV MAVEN_HOME /usr/web/apache-maven-3.2.5
ENV PATH $MAVEN_HOME/bin:$PATH

# Copy application code to the working directory
COPY ../imtecho-web /usr/web
COPY ../imtecho-ui/ /usr/ui/imtecho-ui 

# Install Node.js and npm
RUN apt-get install -y nodejs npm

# Install global dependencies - bower and grunt
RUN npm install -g bower grunt -y 

# Navigate to the application UI directory
WORKDIR /usr/ui/imtecho-ui

# Run grunt task
RUN npm install
RUN bower install
RUN grunt medplat
#RUN npx grunt medplat

# Copy Jar from local
#ADD ./target/imtecho-web-2.0.jar /usr/java/imtecho-web-2.0.jar

# Build the application with Maven
WORKDIR /usr/web
RUN mvn clean install -P docker -Dmaven.test.skip=true

# Expose port 8181
EXPOSE 8181

# Set the entry point command to run the application
CMD ["java", "-jar", "/usr/web/target/medplat-web-2.0.jar"]
#CMD tail -f /dev/null


## to build this image first got to the ImtechoV2 folder and the run this docker build command. - docker build . -f imtecho-web/Dockerfile -t imtecho-image-backend --progress=plain  ##
