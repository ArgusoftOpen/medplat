# Base image
FROM ubuntu:22.04

# Update packages and install necessary tools
RUN apt-get update -y \
    && apt-get install -y wget curl git unzip

# Create directory for Java and navigate to it
RUN mkdir -p /usr/ui/medplat-ui
RUN mkdir -p /usr/web
RUN mkdir -p /usr/android
RUN mkdir -p /usr/Repository 

# Download and install OpenJDK 13
RUN wget https://download.java.net/java/GA/jdk13.0.2/d4173c853231432d94f001e99d882ca7/8/GPL/openjdk-13.0.2_linux-x64_bin.tar.gz \
    && tar -xzvf openjdk-13.0.2_linux-x64_bin.tar.gz \
    && rm openjdk-13.0.2_linux-x64_bin.tar.gz

# Download and install Apache Maven 3.2.5
RUN wget https://mirrors.estointernet.in/apache/maven/maven-3/3.2.5/binaries/apache-maven-3.2.5-bin.tar.gz \
    && tar -xzvf apache-maven-3.2.5-bin.tar.gz \
    && rm apache-maven-3.2.5-bin.tar.gz

# WORKDIR /usr/web

# Set environment variables for Java and Maven
USER root
ENV JAVA_HOME /jdk-13.0.2
ENV PATH $JAVA_HOME/bin:$PATH
ENV MAVEN_HOME /apache-maven-3.2.5
ENV PATH $MAVEN_HOME/bin:$PATH

# Install wkhtmltopdf
RUN apt-get install -y xfonts-75dpi
RUN wget https://github.com/wkhtmltopdf/packaging/releases/download/0.12.6.1-2/wkhtmltox_0.12.6.1-2.jammy_amd64.deb
RUN apt install -f -y ./wkhtmltox_0.12.6.1-2.jammy_amd64.deb
RUN dpkg -i wkhtmltox_0.12.6.1-2.jammy_amd64.deb
RUN cd /usr/share/fonts/truetype
RUN apt-get install -y fonts-indic

# Copy application code to the working directory
COPY entrypoint.sh /usr/

# Install Node.js and npm
RUN curl -fsSL https://deb.nodesource.com/setup_16.x | bash - \
    && apt-get install -y nodejs

RUN npm install -g npm@8.5.0


# Install global dependencies - bower and grunt
RUN npm install -g bower grunt -y 

EXPOSE 8181

ENTRYPOINT [ "/usr/entrypoint.sh" ]
