package com.bdcs.data.generator.orders

import com.bdcs.data.generator.avro.orders.OrdersHeaderAvro
import com.bdcs.data.generator.lib.orders.OrderHeaderModel
import com.bdcs.data.generator.orders.OrdersPayload._

import com.bdcs.data.generator.common.Utils._
import com.bdcs.data.generator.common.ConfigureParameters._
import com.bdcs.data.generator.common.FileReaderAndWriter._
import com.bdcs.data.generator.common.KafkaProducerAndConsumer._

import org.apache.avro.file.DataFileWriter
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}

import java.io.{File, IOException}

object OrdersHeaderWriter {

  def apply(ordersHeaderRecords: Array[OrderHeaderModel]): Unit = {
    val numberOfCustomers = getNoOfMessageToPublish.toInt
    val dataFormat: String = getDataFormat
    val targetType = getTarget
    ordersHeaderWriter(numberOfCustomers, dataFormat, targetType, ordersHeaderRecords,printOnConsole)
  }

  def ordersHeaderWriter(numberOfOrders: Int,
                         dataFormat: String,
                         targetType: String,
                         orders: Array[OrderHeaderModel],
                         printMessagesOnConsole:Boolean,
                         writeToFileAndKafka: Boolean = false
                        ): Unit = {

    if (targetType.equals("file") || writeToFileAndKafka) {
      println(s"Orders_Header File Path: $ordersHeaderOutputFilePath}")
      createDirIfNotExists(ordersHeaderOutputFilePath)
      deleteFileIfExists(ordersHeaderOutputFilePath)

      if (dataFormat.equals("avro")) {
        val datumWriter = new SpecificDatumWriter[OrdersHeaderAvro](classOf[OrdersHeaderAvro])
        val dataFileWriter: DataFileWriter[OrdersHeaderAvro] = new DataFileWriter[OrdersHeaderAvro](datumWriter)
        implicit val writer: DataFileWriter[OrdersHeaderAvro] = dataFileWriter.create(new OrdersHeaderAvro().getSchema, new File(ordersHeaderOutputFilePath))
        writeAvroToFile[OrdersHeaderAvro, OrderHeaderModel](
          orders,
          getOrdersHeaderAvroPayload)
        implicit val datumReader: SpecificDatumReader[OrdersHeaderAvro] = new SpecificDatumReader[OrdersHeaderAvro](classOf[OrdersHeaderAvro])
        if (printMessagesOnConsole) getJsonStringRecordsFromAvroFile[OrdersHeaderAvro](ordersHeaderOutputFilePath).foreach(println)

      }
      if (dataFormat.equals("json")) {
        writeJsonToFile[OrderHeaderModel](
          orders,
          ordersHeaderOutputFilePath,
          getOrdersHeaderJsonPayload)
        if (printMessagesOnConsole) getJsonStringRecordsFromJsonFile(ordersHeaderOutputFilePath).foreach(println)
      }
    }

    if (targetType.equals("kafka")) {
      val kafkaTopicName = if (writeToFileAndKafka) "orders-header" else getKafkaTopicName
      println(s"Orders_Header Kafka Topic Name: $kafkaTopicName")

      if (dataFormat.equals("avro")) {
        kafkaAvroProducer[OrdersHeaderAvro, OrderHeaderModel](
          orders,
          getOrdersHeaderAvroPayload, kafkaTopicName)
        if (printMessagesOnConsole) kafkaAvroConsoleConsumer[OrdersHeaderAvro](numberOfOrders, kafkaTopicName)
      }
      if (dataFormat.equals("json")) {
        kafkaJsonProducer[OrderHeaderModel](orders, getOrdersHeaderJsonPayload, kafkaTopicName)
        if (printMessagesOnConsole) kafkaJsonConsoleConsumer(numberOfOrders, kafkaTopicName)
      }
    }
  }

}
