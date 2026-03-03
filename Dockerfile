# Base image
FROM ubuntu:22.04

# Update packages and install necessary tools
RUN apt-get update -y \
    && apt-get install -y wget curl git unzip openjdk-17-jdk-headless maven

# Create directory for Java and navigate to it
RUN mkdir -p /usr/ui/medplat-ui
RUN mkdir -p /usr/web
RUN mkdir -p /usr/android
RUN mkdir -p /usr/Repository
RUN mkdir -p /usr/target

# Set environment variables for Java and Maven
USER root
ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

# Install wkhtmltopdf
RUN apt-get install -y xfonts-75dpi
RUN wget https://github.com/wkhtmltopdf/packaging/releases/download/0.12.6.1-2/wkhtmltox_0.12.6.1-2.jammy_amd64.deb
RUN apt install -f -y ./wkhtmltox_0.12.6.1-2.jammy_amd64.deb
RUN dpkg -i wkhtmltox_0.12.6.1-2.jammy_amd64.deb
RUN cd /usr/share/fonts/truetype
RUN apt-get install -y fonts-indic

# Install Node.js and npm
RUN mkdir -p /usr/local/nvm
ENV NVM_DIR /usr/local/nvm
ENV NODE_VERSION v16.16.0
RUN curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.1/install.sh | bash
RUN /bin/bash -c "source $NVM_DIR/nvm.sh && nvm install $NODE_VERSION && nvm use --delete-prefix $NODE_VERSION"
ENV NODE_PATH $NVM_DIR/versions/node/$NODE_VERSION/bin
ENV PATH $NODE_PATH:$PATH

RUN npm install --location=global npm@8.11.0

# Install global dependencies - bower
RUN npm install --location=global bower -y

# Copy application code to the working directory
COPY entrypoint.sh /usr/

EXPOSE 8181

# Fix Windows line endings and make executable
RUN sed -i 's/\r$//' /usr/entrypoint.sh && chmod +x /usr/entrypoint.sh

ENTRYPOINT [ "/usr/entrypoint.sh" ]
