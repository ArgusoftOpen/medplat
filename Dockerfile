# Base image
FROM ubuntu:22.04

# Update packages and install necessary tools
RUN apt-get update -y \
    && apt-get install -y wget curl git unzip build-essential procps file 

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

WORKDIR /usr/web

# Set up Linuxbrew user and install Homebrew
RUN useradd -m -s /bin/bash linuxbrew && \
   echo 'linuxbrew ALL=(ALL) NOPASSWD:ALL' >>/etc/sudoers

USER linuxbrew
RUN /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install.sh)"

ENV PATH /home/linuxbrew/.linuxbrew/bin:$PATH

# Install Gradle using Homebrew
RUN brew install gradle

# Set environment variables for Java and Maven
USER root
ENV JAVA_HOME /jdk-13.0.2
ENV PATH $JAVA_HOME/bin:$PATH
ENV MAVEN_HOME /apache-maven-3.2.5
ENV PATH $MAVEN_HOME/bin:$PATH

ENV ANDROID_HOME /sdk
ENV ANDROID_SDK_VERSION 32
ENV ANDROID_BUILD_TOOLS_VERSION 34.0.0

# Install wkhtmltopdf
RUN apt-get install -y xfonts-75dpi
RUN wget https://github.com/wkhtmltopdf/packaging/releases/download/0.12.6.1-2/wkhtmltox_0.12.6.1-2.jammy_amd64.deb
RUN apt install -f -y ./wkhtmltox_0.12.6.1-2.jammy_amd64.deb
RUN dpkg -i wkhtmltox_0.12.6.1-2.jammy_amd64.deb
RUN cd /usr/share/fonts/truetype
RUN apt-get install -y fonts-indic

# Copy application code to the working directory
# COPY medplat-web /usr/web
# COPY medplat-ui/ /usr/ui/medplat-ui
COPY ../medplat-android/ /usr/android 
COPY entrypoint.sh /usr/
# COPY /home/argus/.m2 /root/.m2

# Install Node.js and npm
RUN curl -fsSL https://deb.nodesource.com/setup_16.x | bash - \
    && apt-get install -y nodejs

RUN npm install -g npm@8.5.0


# Install global dependencies - bower and grunt
RUN npm install -g bower grunt -y 

WORKDIR /usr/android


RUN wget --output-document=android-sdk.zip https://dl.google.com/android/repository/commandlinetools-linux-7583922_latest.zip && \
   unzip -d android-sdk-linux android-sdk.zip
RUN mkdir -p /sdk/cmdline-tools/latest
RUN mv android-sdk-linux/cmdline-tools/* /sdk/cmdline-tools/latest/

ENV PATH $ANDROID_HOME/cmdline-tools/latest/bin:$PATH
ENV PATH ${PATH}:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools

RUN sdkmanager --update && yes | sdkmanager --licenses
RUN sdkmanager "platforms;android-32" "build-tools;34.0.0" "extras;google;m2repository" "extras;android;m2repository"

RUN echo "sdk.dir=$ANDROID_HOME" > local.properties
RUN sed -i 's#https://demo.medplat.org/#http://dpg.argusoft.com/#' gradle.properties
RUN chmod +x gradlew
RUN gradle wrapper --gradle-version 7.2
RUN ./gradlew assembleDebug --stacktrace


# # Navigate to the application UI directory
# WORKDIR /usr/ui/medplat-ui

# Run grunt task
# RUN npm install
# RUN bower install
# RUN grunt medplat
# RUN npx grunt medplat



# Copy Jar from local
# ADD ./target/imtecho-web-2.0.jar /usr/java/imtecho-web-2.0.jar

# Build the application with Maven
# WORKDIR /usr/web
# RUN mvn clean install -P docker -Dmaven.test.skip=true

# # Expose port 8181
EXPOSE 8181

ENTRYPOINT [ "/usr/entrypoint.sh" ]


# Set the entry point command to run the application
# CMD ["java", "-jar", "/usr/web/target/medplat-web-2.0.jar"]
#CMD tail -f /dev/null


## to build this image first got to the ImtechoV2 folder and the run this docker build command. - docker build . -f imtecho-web/Dockerfile -t imtecho-image-backend --progress=plain  ##

