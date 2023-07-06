package com.bdcs.data.generator.posInvoices

import com.bdcs.data.generator.common.{AsynchronousProducerCallback, JsonSerializer}
import com.bdcs.data.generator.json.posInvoices.PosInvoiceJson
import com.bdcs.data.generator.common.ConfigureParameters._
import com.bdcs.data.generator.common.FileReaderAndWriter._
import com.bdcs.data.generator.common.KafkaProducerAndConsumer._
import com.bdcs.data.generator.common.Utils._
import org.apache.avro.file.DataFileWriter
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}

import java.io.{File, IOException}
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

import java.util
import java.util.Properties


object PosInvoiceWriter {
  def apply(): Unit = {
    val numberOfInvoices = getNoOfMessageToPublish.toInt
    val dataFormat: String = getDataFormat
    val targetType = getTarget

    PosInvoicePayload()
    posInvoiceWriter(numberOfInvoices, dataFormat, targetType, printOnConsole)
  }

  def posInvoiceWriter(
                        numberOfInvoices: Int,
                        dataFormat: String,
                        targetType: String,
                        printMessagesOnConsole: Boolean
                      ): Unit = {


    if (targetType.equals("file")) {

      if (dataFormat.equals("avro")) {

      }

      if (dataFormat.equals("json")) {

      }
    }


    if (targetType.equals("kafka")) {
      println(s"Kafka Topic Name: ${getKafkaTopicName}")

      val properties = new Properties()
      properties.put(ProducerConfig.CLIENT_ID_CONFIG, "pos_invoices")
      properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getKafkaBrokers)
      properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
      properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[JsonSerializer[PosInvoiceJson]].getName)

      println(s"Started Writing PosInvoice Data to Kafka in ${dataFormat} format..")
      if (dataFormat.equals("avro")) {

      }

      if (dataFormat.equals("json")) {

        val producer: Producer[String, PosInvoiceJson] = new KafkaProducer[String, PosInvoiceJson](properties)

        (0 to numberOfInvoices).foreach(x => {
          val posInvoice = PosInvoicePayload.getNextJsonInvoice
          println(posInvoice.getInvoiceNumber)
          val producerRecord = new ProducerRecord[String, PosInvoiceJson](getKafkaTopicName, posInvoice)
          producer.send(producerRecord, new AsynchronousProducerCallback)
        })
      }

      println(s"Finished Writing PosInvoice Data to Kafka..")
    }

  }
}

