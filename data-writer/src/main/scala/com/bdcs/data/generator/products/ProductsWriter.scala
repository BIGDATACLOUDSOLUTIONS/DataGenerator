package com.bdcs.data.generator.products

import com.bdcs.data.generator.avro.products.ProductsAvro
import com.bdcs.data.generator.lib.product.{Products, ProductModel}
import com.bdcs.data.generator.products.ProductsPayload._

import com.bdcs.data.generator.common.Utils._
import com.bdcs.data.generator.common.ConfigureParameters._
import com.bdcs.data.generator.common.FileReaderAndWriter._
import com.bdcs.data.generator.common.KafkaProducerAndConsumer._

import org.apache.avro.file.DataFileWriter
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}

import java.io.{File, IOException}

object ProductsWriter {

  def apply(): Unit = {
    val numberOfCustomers = getNoOfMessageToPublish.toInt
    val dataFormat: String = getDataFormat
    val targetType = getTarget
    productsWriter(numberOfCustomers, dataFormat, targetType, printOnConsole)
  }

  def productsWriter(numberOfProducts: Int,
                     dataFormat: String,
                     targetType: String,
                     printMessagesOnConsole: Boolean,
                     writeToFileAndKafka: Boolean = false
                    ): Unit = {

    val products: Array[ProductModel] = {
      Products(numberOfProducts)
      Products.productRecords
    }

    if (targetType.equals("file") || writeToFileAndKafka) {
      println(s"Products File Path: $productOutputFilePath")
      createDirIfNotExists(productOutputFilePath)
      deleteFileIfExists(productOutputFilePath)

      if (dataFormat.equals("avro")) {
        val datumWriter = new SpecificDatumWriter[ProductsAvro](classOf[ProductsAvro])
        val dataFileWriter: DataFileWriter[ProductsAvro] = new DataFileWriter[ProductsAvro](datumWriter)
        implicit val writer: DataFileWriter[ProductsAvro] = dataFileWriter.create(new ProductsAvro().getSchema, new File(productOutputFilePath))
        writeAvroToFile[ProductsAvro, ProductModel](
          products, getProductsAvroPayload)
        implicit val datumReader: SpecificDatumReader[ProductsAvro] = new SpecificDatumReader[ProductsAvro](classOf[ProductsAvro])
        if (printMessagesOnConsole) getJsonStringRecordsFromAvroFile[ProductsAvro](productOutputFilePath).foreach(println)
      }
      if (dataFormat.equals("json")) {
        writeJsonToFile[ProductModel](
          products, productOutputFilePath, getProductsJsonPayload)
        if (printMessagesOnConsole) getJsonStringRecordsFromJsonFile(productOutputFilePath).foreach(println)
      }
    }

    if (targetType.equals("kafka")) {
      val kafkaTopicName = if (writeToFileAndKafka) "product" else getKafkaTopicName
      println(s"Products Kafka Topic Name: $kafkaTopicName")
      if (dataFormat.equals("avro")) {
        kafkaAvroProducer[ProductsAvro, ProductModel](
          products,
          getProductsAvroPayload, kafkaTopicName)
        if (printMessagesOnConsole) kafkaAvroConsoleConsumer[ProductsAvro](numberOfProducts, kafkaTopicName)
      }
      if (dataFormat.equals("json")) {
        kafkaJsonProducer[ProductModel](products, getProductsJsonPayload, kafkaTopicName)
        if (printMessagesOnConsole) kafkaJsonConsoleConsumer(numberOfProducts, kafkaTopicName)
      }
    }
  }

}
