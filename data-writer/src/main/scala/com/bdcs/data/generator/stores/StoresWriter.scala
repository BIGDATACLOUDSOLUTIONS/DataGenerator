package com.bdcs.data.generator.stores


import com.bdcs.data.generator.avro.stores.StoresAvro
import com.bdcs.data.generator.lib.store.{Store, StoresModel}
import com.bdcs.data.generator.stores.StoresPayload._

import com.bdcs.data.generator.common.Utils._
import com.bdcs.data.generator.common.ConfigureParameters._
import com.bdcs.data.generator.common.FileReaderAndWriter._
import com.bdcs.data.generator.common.KafkaProducerAndConsumer._

import org.apache.avro.file.DataFileWriter
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}

import java.io.{File, IOException}

object StoresWriter {

  def apply(): Unit = {
    val numberOfCustomers = getNoOfMessageToPublish.toInt
    val dataFormat: String = getDataFormat
    val targetType = getTarget
    storesWriter(numberOfCustomers, dataFormat, targetType,printOnConsole)
  }

  def storesWriter(numberOfStores: Int,
                   dataFormat: String,
                   targetType: String,
                   printMessagesOnConsole:Boolean,
                   writeToFileAndKafka: Boolean = false
                  ): Unit = {

    val stores: Array[StoresModel] = {
      Store(numberOfStores)
      Store.storesRecords
    }

    if (targetType.equals("file") || writeToFileAndKafka) {
      println(s"Store File Path: $storesOutputFilePath")
      createDirIfNotExists(storesOutputFilePath)
      deleteFileIfExists(storesOutputFilePath)

      if (dataFormat.equals("avro")) {
        val datumWriter = new SpecificDatumWriter[StoresAvro](classOf[StoresAvro])
        val dataFileWriter: DataFileWriter[StoresAvro] = new DataFileWriter[StoresAvro](datumWriter)
        implicit val writer: DataFileWriter[StoresAvro] = dataFileWriter.create(new StoresAvro().getSchema, new File(storesOutputFilePath))
        writeAvroToFile[StoresAvro, StoresModel](
          stores, getStoresAvroPayload)

        implicit val datumReader: SpecificDatumReader[StoresAvro] = new SpecificDatumReader[StoresAvro](classOf[StoresAvro])
        if (printMessagesOnConsole) getJsonStringRecordsFromAvroFile[StoresAvro](storesOutputFilePath).foreach(println)
      }
      if (dataFormat.equals("json")) {
        writeJsonToFile[StoresModel](
          stores, storesOutputFilePath, getStoresJsonPayload)
        if (printMessagesOnConsole) getJsonStringRecordsFromJsonFile(storesOutputFilePath).foreach(println)
      }
    }

    if (targetType.equals("kafka")) {
      val kafkaTopicName = if (writeToFileAndKafka) "store" else getKafkaTopicName
      println(s"Store Kafka Topic Name: $kafkaTopicName")
      if (dataFormat.equals("avro")) {
        kafkaAvroProducer[StoresAvro, StoresModel](
          stores, getStoresAvroPayload, kafkaTopicName)
        if (printMessagesOnConsole) kafkaAvroConsoleConsumer[StoresAvro](numberOfStores, kafkaTopicName)
      }
      if (dataFormat.equals("json")) {
        kafkaJsonProducer[StoresModel](stores, getStoresJsonPayload, kafkaTopicName)
        if (printMessagesOnConsole) kafkaJsonConsoleConsumer(numberOfStores, kafkaTopicName)
      }
    }
  }
}
