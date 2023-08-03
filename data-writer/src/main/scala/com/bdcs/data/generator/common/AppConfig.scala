package com.bdcs.data.generator.common

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import com.typesafe.config.{Config, ConfigFactory}

import java.io.File
import java.nio.file.FileSystems
import java.util.Properties


object AppConfig {

  implicit lazy val conf: Config = ConfigFactory.parseFile(new File(s"config/data_generator.conf"))

  //Mandatory
  private val NUMBER_OF_MESSAGES_TO_PUBLISH: String="generator.number_of_message_to_publish"
  private val KAFKA_BROKERS: String="generator.kafka_broker_list"
  private val SCHEMA_REGISTRY_URL: String="generator.schema_registry_url"

  val INVOICES_TOPIC: String = "generator.invoices.topic_name"
  val PRINT_INVOICE_ON_CONSOLE: String = "generator.invoices.print_message"
  val INVOICES_OUTPUT_FORMAT: String = "generator.invoices.output_data_format"

  val PAYMENTS_TOPIC: String = "generator.payments.topic_name"
  val PRINT_PAYMENTS_ON_CONSOLE: String = "generator.payments.print_message"
  val PAYMENTS_OUTPUT_FORMAT: String = "generator.payments.output_data_format"


  val PAYMENTS_MASTER_TOPIC: String = "generator.payments_master.topic_name"
  val PRINT_PAYMENTS_MASTER_ON_CONSOLE: String = "generator.payments_master.print_message"
  val PAYMENTS_MASTER_OUTPUT_FORMAT: String = "generator.payments_master.output_data_format"

  val CUSTOMERS_SURVEY_TARGET:String="generator.customer_survey.target"
  val CUSTOMERS_SURVEY_TOPIC: String = "generator.customer_survey.topic_name"
  val PRINT_CUSTOMERS_SURVEY_ON_CONSOLE: String = "generator.customer_survey.print_message"
  val CUSTOMERS_SURVEY_OUTPUT_FORMAT: String = "generator.customer_survey.output_data_format"


  def getNoOfMessageToPublish: Int = conf.getString(NUMBER_OF_MESSAGES_TO_PUBLISH).toInt
  def getKafkaBrokers: String =conf.getString(KAFKA_BROKERS)
  def getSchemaRegistryUrl: String =conf.getString(SCHEMA_REGISTRY_URL)


  def kafkaProducerProperties(client_id: String): Properties = {
    val properties = new Properties()
    properties.put(ProducerConfig.CLIENT_ID_CONFIG, client_id)
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getKafkaBrokers)
    properties.put(ProducerConfig.ACKS_CONFIG, "all")
    properties.put(ProducerConfig.RETRIES_CONFIG, "10")
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)

    properties
  }




}
