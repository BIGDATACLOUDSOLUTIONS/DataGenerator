package com.bdcs.data.generator.customers

import com.bdcs.data.generator.lib.customers.{CustomerModel, Customers}
import com.bdcs.data.generator.avro.customers.CustomerAvro
import com.bdcs.data.generator.customers.CustomerPayload._

import com.bdcs.data.generator.common.ConfigureParameters._
import com.bdcs.data.generator.common.FileReaderAndWriter._
import com.bdcs.data.generator.common.KafkaProducerAndConsumer._
import com.bdcs.data.generator.common.Utils._

import org.apache.avro.file.DataFileWriter
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}
import java.io.{File, IOException}

object CustomerWriter {

  def apply(): Unit = {
    val numberOfCustomers = getNoOfMessageToPublish.toInt
    val dataFormat: String = getDataFormat
    val targetType = getTarget

    customerWriter(numberOfCustomers, dataFormat, targetType,printOnConsole)
  }

  def customerWriter(
                      numberOfCustomers: Int,
                      dataFormat: String,
                      targetType: String,
                      printMessagesOnConsole:Boolean,
                      writeToFileAndKafka: Boolean = false
                    ): Unit = {

    val customers: Array[CustomerModel] = {
      Customers(numberOfCustomers)
      Customers.customerRecords
    }

    if (targetType.equals("file") || writeToFileAndKafka) {
      println(s"Customers File Path: $customerOutputFilePath")
      createDirIfNotExists(customerOutputFilePath)
      deleteFileIfExists(customerOutputFilePath)

      if (dataFormat.equals("avro")) {
        val datumWriter = new SpecificDatumWriter[CustomerAvro](classOf[CustomerAvro])
        val dataFileWriter: DataFileWriter[CustomerAvro] = new DataFileWriter[CustomerAvro](datumWriter)
        implicit val writer: DataFileWriter[CustomerAvro] = dataFileWriter.create(new CustomerAvro().getSchema, new File(customerOutputFilePath))
        writeAvroToFile[CustomerAvro, CustomerModel](customers, getCustomerAvroPayload)
        implicit val datumReader: SpecificDatumReader[CustomerAvro] = new SpecificDatumReader[CustomerAvro](classOf[CustomerAvro])
        if (printMessagesOnConsole) getJsonStringRecordsFromAvroFile[CustomerAvro](customerOutputFilePath).foreach(println)
      }

      if (dataFormat.equals("json")) {
        writeJsonToFile[CustomerModel](
          customers,
          customerOutputFilePath,
          getCustomerJsonPayload)
        if (printMessagesOnConsole) getJsonStringRecordsFromJsonFile(customerOutputFilePath).foreach(println)
      }
    }

    if (targetType.equals("kafka")) {
      val kafkaTopicName = if (writeToFileAndKafka) "customers" else getKafkaTopicName
      println(s"Customers Kafka Topic Name: $kafkaTopicName")

      if (dataFormat.equals("avro")) {
        kafkaAvroProducer[CustomerAvro, CustomerModel](
          customers, getCustomerAvroPayload, kafkaTopicName)
        if (printMessagesOnConsole) kafkaAvroConsoleConsumer[CustomerAvro](numberOfCustomers, kafkaTopicName)
      }
      if (dataFormat.equals("json")) {
        kafkaJsonProducer[CustomerModel](customers, getCustomerJsonPayload, kafkaTopicName)
        if (printMessagesOnConsole) kafkaJsonConsoleConsumer(numberOfCustomers, kafkaTopicName)
      }
    }

  }

}