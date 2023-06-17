set -ex

#Clean up
rm -rf generated-files
rm -rf docker/data-generator/generated-files
rm -rf docker/data-generator/input_files.zip
zip -r docker/data-generator/input_files.zip input_files

mvn clean install

cp data-writer/target/package/applications/DataGenerator.jar docker/data-generator/






