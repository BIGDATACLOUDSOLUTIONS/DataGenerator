package com.bdcs.data.generator.payments

import com.bdcs.data.generator.avro.payments.PaymentsAvro
import com.bdcs.data.generator.lib.payments.{PaymentModel, Payments}
import com.bdcs.data.generator.payments.PaymentsPayload._
import com.bdcs.data.generator.common.Utils._
import com.bdcs.data.generator.common.ConfigureParameters._
import com.bdcs.data.generator.common.FileReaderAndWriter._
import com.bdcs.data.generator.common.KafkaProducerAndConsumer._
import com.bdcs.data.generator.lib.orders.Orders
import com.bdcs.data.generator.orders.{OrdersDetailWriter, OrdersHeaderWriter, OrdersWriter}
import org.apache.avro.file.DataFileWriter
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}

import java.io.{File, IOException}

object PaymentsWriter {

  def apply(): Unit = {
    val numberOfPayments=getNoOfMessageToPublish.toInt
    val dataFormat: String = getDataFormat
    val targetType = getTarget

    OrdersWriter.writePrerequisitesData(
      numberOfPayments,
      dataFormat,
      targetType)

    writePrerequisitesData(
      numberOfPayments,
      dataFormat,
      targetType)

    paymentsWriter(numberOfPayments, dataFormat, targetType)
  }


  private def writePrerequisitesData(numberOfPayments: Int,
                                     dataFormat: String,
                                     targetType: String,
                                     printMessagesOnConsole: Boolean = false
                                    ): Unit = {

    val writeToFileAndKafka = if (targetType.equals("file")) false else true

    println("*************** Writing Orders_Header and Orders_Details Data: STARTED ****************")
    Orders(numberOfPayments)
    OrdersHeaderWriter.ordersHeaderWriter(numberOfPayments, dataFormat, targetType, Orders.ordersHeadersRecords, printMessagesOnConsole, writeToFileAndKafka)
    OrdersDetailWriter.ordersDetailWriter(numberOfPayments, dataFormat, targetType, Orders.ordersDetailsRecords, printMessagesOnConsole, writeToFileAndKafka)
    println("*************** Writing Orders_Header and Orders_Details Data: COMPLETED ****************")

  }


  def paymentsWriter(numberOfPayments: Int,
                     dataFormat: String,
                     targetType: String
                    ): Unit = {

    val payments: Array[PaymentModel] = {
      Payments(numberOfPayments)
      Payments.paymentRecords
    }

    if (targetType.equals("file")) {
      println(s"Payments File Path: $paymentsOutputFilePath}")
      createDirIfNotExists(paymentsOutputFilePath)
      deleteFileIfExists(paymentsOutputFilePath)

      if (dataFormat.equals("avro")) {
        val datumWriter = new SpecificDatumWriter[PaymentsAvro](classOf[PaymentsAvro])
        val dataFileWriter: DataFileWriter[PaymentsAvro] = new DataFileWriter[PaymentsAvro](datumWriter)
        implicit val writer: DataFileWriter[PaymentsAvro] = dataFileWriter.create(new PaymentsAvro().getSchema, new File(paymentsOutputFilePath))
        writeAvroToFile[PaymentsAvro, PaymentModel](
          payments,
          getPaymentsAvroPayload)

        implicit val datumReader: SpecificDatumReader[PaymentsAvro] = new SpecificDatumReader[PaymentsAvro](classOf[PaymentsAvro])
        if(printOnConsole) getJsonStringRecordsFromAvroFile[PaymentsAvro](paymentsOutputFilePath).foreach(println)
      }
      if (dataFormat.equals("json")) {
        writeJsonToFile[PaymentModel](
          payments,
          paymentsOutputFilePath,
          getPaymentsJsonPayload)
        if(printOnConsole) getJsonStringRecordsFromJsonFile(paymentsOutputFilePath).foreach(println)
      }
    }

    if (targetType.equals("kafka")) {
      println(s"Payments Kafka Topic Name: $getKafkaTopicName")
      if (dataFormat.equals("avro")) {
        kafkaAvroProducer[PaymentsAvro, PaymentModel](
          payments,
          getPaymentsAvroPayload, getKafkaTopicName)
        if(printOnConsole) kafkaAvroConsoleConsumer[PaymentsAvro](numberOfPayments, getKafkaTopicName)
      }
      if (dataFormat.equals("json")) {
        kafkaJsonProducer[PaymentModel](payments, getPaymentsJsonPayload, getKafkaTopicName)
        if(printOnConsole) kafkaJsonConsoleConsumer(numberOfPayments, getKafkaTopicName)
      }
    }
  }

}
