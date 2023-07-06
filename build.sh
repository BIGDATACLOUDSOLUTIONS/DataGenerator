set -ex

#Clean up
rm -rf generated-files
rm -rf docker/data-generator/generated-files
rm -rf avro-data-handler/src/main/java/com/bdcs/data/generator
rm -rf docker/data-generator/input_files.zip
zip -r docker/data-generator/input_files.zip input_files

mvn clean install

cp data-writer/target/package/applications/DataGenerator.jar docker/data-generator/






