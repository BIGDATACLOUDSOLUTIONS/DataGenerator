## Application Summary

This Scala application generates various types of data, such as customer data, stores data, survey data, product data, and order data. It provides flexibility through configurable parameters to control the data generation process.

### Configuration Parameters

The application supports the following configurable parameters:

- **NUMBER_OF_MESSAGES_TO_PUBLISH**: Specifies the number of messages to generate. The value must be less than or equal to 10,000.
- **DATA_CATEGORY**: Determines the type of data to generate. It can be set to "customers", "stores", or any other supported data category.
- **TARGET_TYPE**: Specifies the target destination for the generated data. It can be set to "kafka" or "files".
- **OUTPUT_DATA_FORMAT**: Defines the output format of the generated data. It can be set to "avro" or "json".
- **PRINT_DATA_ON_CONSOLE**: Print the output on console if set to true.
- **KAFKA_BROKERS**: Specify the Kafka Brokers as a list if the TARGET_TYPE is set to "kafka".
- **KAFKA_TOPIC**: Specify the name of the Kafka Topic where you wish to send the data, applicable only if TARGET_TYPE is set to "kafka".
- **ZOOKEEPER**: Provide the Zookeeper Host:Port information, applicable only if TARGET_TYPE is set to "kafka".
- **SCHEMA_REGISTRY_URL**: Specify the URL of the Schema Registry service if you intend to write data to Kafka in the Avro format. If set, it will send the Avro data schema to the Schema Registry service.


### Docker Image

A Docker image has been created for this application, which includes the application's JAR file. This allows for easy deployment and execution using Docker.

### Docker Compose

To run the application, a Docker Compose file is provided. It simplifies the setup and orchestration of the application's containers. By using Docker Compose, you can easily start the application and configure the necessary environment variables.

Ensure that you set the appropriate values for the configurable parameters mentioned above in the Docker Compose file to customize the data generation process.

Please note that the application is still a work in progress, with the customer data generation functionality already implemented. Additional data generation modules for stores, survey, product, and order are yet to be implemented.

## Usage

1. Install Docker and Docker Compose on your system.
2. Clone the repository and navigate to the project directory.
3. Modify the Docker Compose file (docker-compose.yml) to configure the desired values for the configurable parameters.
4. Build the code locally
   ```
   sh -x build.sh
   ```
5. Start Kafka Cluster in a separate terminal
    ```
    docker compose -f docker/kafka-cluster/docker-compose.yml up
    ```
6After building the code, you can run the code locally or on docker container
   <br></br>
   - **Run locally**: Update the variables of the below script and run the same
     ```
     sh -x run_data_generator_locally.sh
     ```
      The output will be populated in a folder named generated-files at base location of project
      <br></br>
   - **Run on Docker Container**: Build the image and run the container using docker compose
        1. Build the Docker image: This will build the image and copy the relevant files to the image
           ```
           cd docker/data-generator
           sh -x build_docker_image.sh --build
           ```
        2. Start the application: Using the setting in docker-compose.yml, generate the data
           ```
           docker-compose up
           ```
        The output will be populated in a folder named generated-files based on volume mounting in docker compose


The application will start generating data based on the provided configurations.

Please ensure that you have sufficient resources allocated to Docker to handle the data generation requirements. Also, refer to the application's documentation for any additional details or specific usage instructions.

## Future Development

Future development of the application will focus on implementing data generation modules for stores, survey, product, and order. These additions will enhance the application's capabilities and enable the generation of a broader range of data types.

If you encounter any issues or have suggestions for improvement, please feel free to contribute to the project or reach out to the project maintainers.

We hope you find this application useful for generating data and exploring its various functionalities!