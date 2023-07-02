package com.bdcs.data.generator.common

import com.bdcs.data.generator.common.ConfigureParameters._
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.avro.specific.SpecificRecord

import org.apache.kafka.clients.consumer.KafkaConsumer
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.serialization.StringDeserializer

import java.util.Collections
import java.util.Properties
import java.time.Duration


object KafkaProducerAndConsumer {

  implicit val producer_props: Properties = new Properties
  implicit val consumer_props: Properties = new Properties

  // Producer Properties
  producer_props.setProperty("bootstrap.servers", getKafkaBrokers)
  producer_props.setProperty("acks", "all")
  producer_props.setProperty("retries", "10")

  // Consumer Properties
  consumer_props.setProperty("bootstrap.servers", getKafkaBrokers)

  consumer_props.put("auto.commit.enable", "false")
  consumer_props.put("auto.offset.reset", "earliest")

  if (!getSchemaRegistryUrl.equalsIgnoreCase("NOT_PROVIDED")
    && getDataFormat.equalsIgnoreCase("avro")
  ) {
    producer_props.setProperty("schema.registry.url", getSchemaRegistryUrl)
    consumer_props.setProperty("schema.registry.url", getSchemaRegistryUrl)
  }

  def kafkaAvroProducer[T <: SpecificRecord, V](
                                                 rawMessages: Array[V],
                                                 getAvroPayload: V => T,
                                                 topicName:String
                                               ): Unit = {

    producer_props.setProperty("key.serializer", classOf[StringSerializer].getName)
    producer_props.setProperty("value.serializer", classOf[KafkaAvroSerializer].getName)

    val producer: Producer[String, T] = new KafkaProducer[String, T](producer_props)

    rawMessages.foreach(message => {
      val producerRecord = new ProducerRecord[String, T](topicName, getAvroPayload(message))
      producer.send(producerRecord, new AsynchronousProducerCallback)
    })
    producer.flush()
    producer.close()
  }

  def kafkaJsonProducer[T](rawMessages: Array[T],
                           getJsonPayload: T => String,
                           topicName:String
                          ): Unit = {

    consumer_props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    consumer_props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer: Producer[String, String] = new KafkaProducer[String, String](consumer_props)

    rawMessages.foreach(message => {
      val producerRecord = new ProducerRecord[String, String](topicName, getJsonPayload(message))
      producer.send(producerRecord, new AsynchronousProducerCallback)
    })
    producer.flush()
    producer.close()

  }

  def kafkaAvroConsoleConsumer[T](numberOfRecords: Int,
                                  topicName:String): Unit = {
    consumer_props.put("group.id", topicName)
    consumer_props.setProperty("key.deserializer", classOf[StringDeserializer].getName)
    consumer_props.setProperty("value.deserializer", classOf[KafkaAvroDeserializer].getName)
    consumer_props.setProperty("specific.avro.reader", "true")

    val kafkaConsumer = new KafkaConsumer[String, T](consumer_props)
    kafkaConsumer.subscribe(Collections.singleton(topicName))
    println("Waiting for data...")
    var counter: Int = 1
    while (counter <= numberOfRecords) {
      System.out.println("Polling")
      val records: ConsumerRecords[String, T] = kafkaConsumer.poll(Duration.ofMillis(1000))

      records.forEach(record => {
        val reviews = record.value()
        println(reviews)
        counter += 1
      })
      kafkaConsumer.commitSync()
    }
  }

  def kafkaJsonConsoleConsumer(numberOfRecords: Int,
                               topicName:String
                              ): Unit = {
    consumer_props.put("group.id", topicName)
    consumer_props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    consumer_props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

    val kafkaConsumer = new KafkaConsumer[String, String](consumer_props)
    kafkaConsumer.subscribe(Collections.singleton(topicName))
    println("Waiting for data...")
    var counter: Int = 1
    while (counter <= numberOfRecords) {
      System.out.println("Polling")
      val records: ConsumerRecords[String, String] = kafkaConsumer.poll(Duration.ofMillis(1000))
      records.forEach(record => {
        val reviews = record.value()
        println(reviews)
        counter += 1
      })
      kafkaConsumer.commitSync()
    }
    kafkaConsumer.close()
  }

}
