package com.bdcs.data.generator.common

object ConfigureParameters {

  //Mandatory
  private val NUMBER_OF_MESSAGES_TO_PUBLISH: Option[String] = sys.env.get("NUMBER_OF_MESSAGES_TO_PUBLISH")
  private val TARGET_TYPE: Option[String] = sys.env.get("TARGET_TYPE") //Kafka or File
  private val OUTPUT_DATA_FORMAT: Option[String] = sys.env.get("OUTPUT_DATA_FORMAT") // Avro/Json

  private val KAFKA_BROKERS: Option[String] = sys.env.get("KAFKA_BROKERS")
  private val KAFKA_TOPIC: Option[String] = sys.env.get("KAFKA_TOPIC")
  private val ZOOKEEPER: Option[String] = sys.env.get("ZOOKEEPER")
  private val SCHEMA_REGISTRY_URL: Option[String] = sys.env.get("SCHEMA_REGISTRY_URL")

  private val PRINT_DATA_ON_CONSOLE: Option[String] = sys.env.get("PRINT_DATA_ON_CONSOLE")

  var dataCategory: String = null

  val customerOutputFilePath=s"generated-files/$getDataFormat/customers.$getDataFormat"
  val productOutputFilePath=s"generated-files/$getDataFormat/products.$getDataFormat"
  val storesOutputFilePath=s"generated-files/$getDataFormat/stores.$getDataFormat"
  val ordersHeaderOutputFilePath=s"generated-files/$getDataFormat/ordersHeader.$getDataFormat"
  val ordersDetailOutputFilePath=s"generated-files/$getDataFormat/ordersDetail.$getDataFormat"
  val paymentsOutputFilePath=s"generated-files/$getDataFormat/payments.$getDataFormat"
  val reviewsOutputFilePath=s"generated-files/$getDataFormat/reviews.$getDataFormat"

  def getNoOfMessageToPublish: String = {
    NUMBER_OF_MESSAGES_TO_PUBLISH match {
      case Some(v) => {
        if (v.toInt > 50000) throw new Exception("Number of messages must be <=50000")
        else v
      }
      case None => throw new Exception("No of Required Messages Not defined")
    }
  }


  def getTarget: String = {
    val validValues = List("kafka", "file")
    TARGET_TYPE match {
      case Some(v) => {
        if (validValues.contains(v.toLowerCase)) v.toLowerCase
        else throw new Exception(s"Target can be either of ${validValues.mkString("/ ")}")
      }
      case None => throw new Exception("Target Not defined")
    }
  }

  def getDataFormat: String = {
    val validValues = List("avro", "json")
    OUTPUT_DATA_FORMAT match {
      case Some(v) => {
        if (validValues.contains(v.toLowerCase)) v.toLowerCase
        else throw new Exception(s"Output Data format can be  ${validValues.mkString("/ ")}")
      }
      case None => throw new Exception("Output Data format Not defined ")
    }
  }

  def getKafkaBrokers: String =
    KAFKA_BROKERS match {
      case Some(v) => v
      case None => throw new Exception("Brokers Not defined")
    }

  def getKafkaTopicName: String =
    KAFKA_TOPIC match {
      case Some(v) => v
      case None => throw new Exception("Kafka Topic Name Not defined")
    }

  def getZookeeper: String =
    ZOOKEEPER match {
      case Some(v) => v
      case None => throw new Exception("Zookeeper Host:Port Not defined")
    }

  def getSchemaRegistryUrl: String =
    SCHEMA_REGISTRY_URL match {
      case Some(v) => v
      case None => "NOT_PROVIDED"
    }


  def printOnConsole: Boolean =
    PRINT_DATA_ON_CONSOLE match {
      case Some(v) => v.toBoolean
      case None => false
    }
}
