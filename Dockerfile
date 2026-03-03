# Use Eclipse Temurin JDK 17 (slim, fast)
FROM eclipse-temurin:17-jre-jammy

# Avoid interactive prompts
ENV DEBIAN_FRONTEND=noninteractive

# Install wkhtmltopdf and required fonts
RUN apt-get update && apt-get install -y \
    wget \
    xfonts-75dpi \
    xfonts-base \
    xfonts-encodings \
    xfonts-utils \
    fonts-indic \
    libjpeg-turbo8 \
    libxrender1 \
    libfontconfig1 \
    libxext6 \
    fontconfig \
    && wget https://github.com/wkhtmltopdf/packaging/releases/download/0.12.6.1-2/wkhtmltox_0.12.6.1-2.jammy_amd64.deb \
    && dpkg -i wkhtmltox_0.12.6.1-2.jammy_amd64.deb || true \
    && apt-get install -f -y \
    && rm wkhtmltox_0.12.6.1-2.jammy_amd64.deb \
    && rm -rf /var/lib/apt/lists/*

# Copy the pre-built Spring Boot JAR
COPY medplat-web/target/medplat-web-2.0.jar /app/medplat-web.jar

# Expose application ports
EXPOSE 8080 8181

# Run the Spring Boot app with correct DB and static resource paths
CMD ["java", "-jar", "/app/medplat-web.jar", \
     "--spring.datasource.url=jdbc:postgresql://db:5432/medplat", \
    "--spring.datasource.username=postgres", \
    "--spring.datasource.password=argusadmin", \
    "---Dspring.config.additional-location=file:/usr/ui/"]