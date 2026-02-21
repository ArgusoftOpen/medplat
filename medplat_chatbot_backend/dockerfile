# Use official Rasa image
FROM rasa/rasa:3.6.20-full

ARG ENV=development
ENV ENV=${ENV}

WORKDIR /app

USER root
COPY rasa_project/requirements.txt .

RUN pip install --break-system-packages -r requirements.txt

COPY rasa_project/ /app/rasa_project/
COPY start.sh /app/start.sh

WORKDIR /app
RUN chmod +x start.sh

USER 1001
EXPOSE 5005 5055

# Fix to override default Rasa entrypoint
ENTRYPOINT ["/bin/bash"]
CMD ["./start.sh"]

