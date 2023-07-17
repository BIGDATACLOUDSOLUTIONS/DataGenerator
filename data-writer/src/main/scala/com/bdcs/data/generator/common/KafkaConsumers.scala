package com.bdcs.data.generator.common

import com.bdcs.data.generator.avro.payment.{InvoiceAvro, PaymentAvro, PaymentMasterAvro}
import com.bdcs.data.generator.common.AppConfig._
import com.bdcs.data.generator.invoices.deserializer.InvoiceJsonDeserializer
import com.bdcs.data.generator.json.payment.{InvoiceJson, PaymentJson, PaymentMasterJson}
import com.bdcs.data.generator.payments.deserializer.{PaymentJsonDeserializer, PaymentMasterJsonDeserializer}
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

import java.time.Duration
import java.util.{Collections, Properties}

object KafkaConsumers {
  def kafkaConsumerProperties(groupId: String): Properties = {
    val properties = new Properties()
    properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "DataGenerator-Consumer")
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getKafkaBrokers)
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    properties.setProperty("auto.commit.enable", "false")

    properties
  }

  def invoiceJsonKafkaConsoleConsumer(topicName: String): Unit = {
    val properties = kafkaConsumerProperties(topicName)
    //properties.setProperty("schema.registry.url", getSchemaRegistryUrl)
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[InvoiceJsonDeserializer].getName)

    val kafkaConsumer = new KafkaConsumer[String, InvoiceJson](properties)
    kafkaConsumer.subscribe(Collections.singleton(topicName))

    while (true) {
      println("Polling Invoice Json Records....")
      val record: ConsumerRecords[String, InvoiceJson] = kafkaConsumer.poll(Duration.ofMillis(1000))
      record.forEach(record => {
        println(record.value().toString)
      })
    }
  }

  def invoiceAvroKafkaConsoleConsumer(topicName: String): Unit = {
    val properties = kafkaConsumerProperties(topicName)
    properties.setProperty("schema.registry.url", getSchemaRegistryUrl)
    properties.setProperty("specific.avro.reader", "true")
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer].getName)

    val kafkaConsumer = new KafkaConsumer[String, InvoiceAvro](properties)
    kafkaConsumer.subscribe(Collections.singleton(topicName))

    while (true) {
      println("Polling Invoice Avro Records....")
      val record: ConsumerRecords[String, InvoiceAvro] = kafkaConsumer.poll(Duration.ofMillis(1000))
      record.forEach(record => {
        val result = record.value()
        println(result)
      })
    }
  }

  def paymentJsonKafkaConsoleConsumer(topicName: String): Unit = {
    val properties = kafkaConsumerProperties(topicName)
    //properties.setProperty("schema.registry.url", getSchemaRegistryUrl)
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[PaymentJsonDeserializer].getName)

    val kafkaConsumer = new KafkaConsumer[String, PaymentJson](properties)
    kafkaConsumer.subscribe(Collections.singleton(topicName))

    while (true) {
      println("Polling Payment Json Records....")
      val record: ConsumerRecords[String, PaymentJson] = kafkaConsumer.poll(Duration.ofMillis(1000))
      record.forEach(record => {
        println(record.value().toString)
      })
    }
  }

  def paymentAvroKafkaConsoleConsumer(topicName: String): Unit = {
    val properties = kafkaConsumerProperties(topicName)
    properties.setProperty("schema.registry.url", getSchemaRegistryUrl)
    properties.setProperty("specific.avro.reader", "true")
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer].getName)

    val kafkaConsumer = new KafkaConsumer[String, PaymentAvro](properties)
    kafkaConsumer.subscribe(Collections.singleton(topicName))

    while (true) {
      println("Polling Payment Avro Records....")
      val record: ConsumerRecords[String, PaymentAvro] = kafkaConsumer.poll(Duration.ofMillis(1000))
      record.forEach(record => {
        val result = record.value()
        println(result)
      })
    }
  }

  def paymentsMasterJsonKafkaConsoleConsumer(topicName: String): Unit = {
    val properties = kafkaConsumerProperties(topicName)
    //properties.setProperty("schema.registry.url", getSchemaRegistryUrl)
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[PaymentMasterJsonDeserializer].getName)

    val kafkaConsumer = new KafkaConsumer[String, PaymentMasterJson](properties)
    kafkaConsumer.subscribe(Collections.singleton(topicName))

    while (true) {
      println("Polling Payment Master Json Records....")
      val record: ConsumerRecords[String, PaymentMasterJson] = kafkaConsumer.poll(Duration.ofMillis(1000))
      record.forEach(record => {
        println(record.value().toString)
      })
    }
  }

  def paymentsMasterAvroKafkaConsoleConsumer(topicName: String): Unit = {
    val properties = kafkaConsumerProperties(topicName)
    properties.setProperty("schema.registry.url", getSchemaRegistryUrl)
    properties.setProperty("specific.avro.reader", "true")
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer].getName)

    val kafkaConsumer = new KafkaConsumer[String, PaymentMasterAvro](properties)
    kafkaConsumer.subscribe(Collections.singleton(topicName))

    while (true) {
      println("Polling Payment Master Avro Records....")
      val record: ConsumerRecords[String, PaymentMasterAvro] = kafkaConsumer.poll(Duration.ofMillis(1000))
      record.forEach(record => {
        val result = record.value()
        println(result)
      })
    }
  }


}
