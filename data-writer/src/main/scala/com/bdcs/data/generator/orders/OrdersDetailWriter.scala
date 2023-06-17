package com.bdcs.data.generator.orders

import com.bdcs.data.generator.avro.orders.OrdersDetailAvro
import com.bdcs.data.generator.lib.orders.OrderDetailModel
import com.bdcs.data.generator.orders.OrdersPayload._

import com.bdcs.data.generator.common.Utils._
import com.bdcs.data.generator.common.ConfigureParameters._
import com.bdcs.data.generator.common.FileReaderAndWriter._
import com.bdcs.data.generator.common.KafkaProducerAndConsumer._


import org.apache.avro.file.DataFileWriter
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}

import java.io.{File, IOException}

object OrdersDetailWriter {

  def apply(ordersDetailRecords :Array[OrderDetailModel]): Unit = {
    val numberOfCustomers = getNoOfMessageToPublish.toInt
    val dataFormat: String = getDataFormat
    val targetType = getTarget
    ordersDetailWriter(numberOfCustomers, dataFormat, targetType, ordersDetailRecords,printOnConsole)
  }


  def ordersDetailWriter(numberOfOrders: Int,
                         dataFormat: String,
                         targetType: String,
                         orders: Array[OrderDetailModel],
                         printMessagesOnConsole:Boolean,
                         writeToFileAndKafka: Boolean = false
                        ): Unit = {


    if (targetType.equals("file") || writeToFileAndKafka) {
      println(s"Orders_Detail File Path: $ordersDetailOutputFilePath}")
      createDirIfNotExists(ordersDetailOutputFilePath)
      deleteFileIfExists(ordersDetailOutputFilePath)

      if (dataFormat.equals("avro")) {
        val datumWriter = new SpecificDatumWriter[OrdersDetailAvro](classOf[OrdersDetailAvro])
        val dataFileWriter: DataFileWriter[OrdersDetailAvro] = new DataFileWriter[OrdersDetailAvro](datumWriter)
        implicit val writer: DataFileWriter[OrdersDetailAvro] = dataFileWriter.create(new OrdersDetailAvro().getSchema, new File(ordersDetailOutputFilePath))
        writeAvroToFile[OrdersDetailAvro, OrderDetailModel](
          orders,
          getOrdersDetailAvroPayload)

        implicit val datumReader: SpecificDatumReader[OrdersDetailAvro] = new SpecificDatumReader[OrdersDetailAvro](classOf[OrdersDetailAvro])
        if (printMessagesOnConsole) getJsonStringRecordsFromAvroFile[OrdersDetailAvro](ordersDetailOutputFilePath).foreach(println)

      }
      if (dataFormat.equals("json")) {
        writeJsonToFile[OrderDetailModel](
          orders,
          ordersDetailOutputFilePath,
          getOrdersDetailJsonPayload)
        if (printMessagesOnConsole) getJsonStringRecordsFromJsonFile(ordersDetailOutputFilePath).foreach(println)
      }
    }
    if (targetType.equals("kafka")) {
      val kafkaTopicName = "orders-detail"
      println(s"Orders_Detail Kafka Topic Name: $kafkaTopicName")
      if (dataFormat.equals("avro")) {
        kafkaAvroProducer[OrdersDetailAvro, OrderDetailModel](
          orders,
          getOrdersDetailAvroPayload, kafkaTopicName)
        if (printMessagesOnConsole) kafkaAvroConsoleConsumer[OrdersDetailAvro](numberOfOrders, kafkaTopicName)
      }
      if (dataFormat.equals("json")) {
        kafkaJsonProducer[OrderDetailModel](orders, getOrdersDetailJsonPayload, kafkaTopicName)
        if (printMessagesOnConsole) kafkaJsonConsoleConsumer(numberOfOrders, kafkaTopicName)
      }
    }
  }

}
