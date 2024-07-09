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
RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.4/binaries/apache-maven-3.9.4-bin.tar.gz \
    && tar -xzvf apache-maven-3.9.4-bin.tar.gz \
    && rm apache-maven-3.9.4-bin.tar.gz


# Set environment variables for Java and Maven
USER root
ENV JAVA_HOME /jdk-13.0.2
ENV PATH $JAVA_HOME/bin:$PATH
ENV MAVEN_HOME /apache-maven-3.9.4
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
RUN mkdir -p /usr/local/nvm
ENV NVM_DIR /usr/local/nvm
ENV NODE_VERSION v16.16.0
RUN curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.1/install.sh | bash
RUN /bin/bash -c "source $NVM_DIR/nvm.sh && nvm install $NODE_VERSION && nvm use --delete-prefix $NODE_VERSION"
ENV NODE_PATH $NVM_DIR/versions/node/$NODE_VERSION/bin
ENV PATH $NODE_PATH:$PATH

RUN bash - | npm install --location=global npm@8.11.0

# Install global dependencies - bower and grunt
RUN npm install --location=global bower grunt -y 

EXPOSE 8181

RUN chmod +x /usr/entrypoint.sh

ENTRYPOINT [ "/usr/entrypoint.sh" ]
