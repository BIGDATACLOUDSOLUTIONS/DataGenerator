FROM ubuntu:latest
LABEL maintainer="Rajesh_"

RUN apt update

#Install Java 8
RUN echo "y" | apt install openjdk-8-jdk

# Define commonly used JAVA_HOME variable
RUN export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# Install CURL
RUN echo "y" | apt install curl

#Install zip
RUN echo "y" | apt install zip

#Install unzip
RUN apt install unzip

COPY ./entrypoint.sh /root/entrypoint.sh
COPY ./DataGenerator.jar /root/DataGenerator.jar
COPY ./input_files.zip /root/input_files.zip

# Define working directory.
WORKDIR /root

# Define default command.
RUN chmod +x /root/entrypoint.sh
ENTRYPOINT ["/root/entrypoint.sh"]
CMD ["default"]
