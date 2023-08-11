package com.bdcs.data.generator.survey

import com.bdcs.data.generator.avro.survey.CustomersSurveyAvro
import com.bdcs.data.generator.common.AppConfig._
import com.bdcs.data.generator.common.AsynchronousProducerCallback
import com.bdcs.data.generator.common.FileUtils.{createDirIfNotExists, deleteFileIfExists}
import com.bdcs.data.generator.json.survey.CustomersSurveyJson
import com.bdcs.data.generator.lib.survey.CustomersFeedback
import com.bdcs.data.generator.survey.CustomerSurveyPayload._
import com.bdcs.data.generator.survey.serializer.CustomerSurveyJsonSerializer
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.avro.file.DataFileWriter
import org.apache.avro.specific.SpecificDatumWriter
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}

import java.io.{File, FileWriter}

object CustomerSurveyWriter {

  def apply(): Unit = {
    val numberOfSurveyResponse = getNoOfMessageToPublish
    customerSurveyWriter(numberOfSurveyResponse)

  }

  private def customerSurveyWriter(numberOfSurveyResponse: Int): Unit = {
    val targetType: String = conf.getString(CUSTOMERS_SURVEY_TARGET)
    val printMessagesOnConsole: Boolean = conf.getString(PRINT_CUSTOMERS_SURVEY_ON_CONSOLE).toBoolean
    val outputDataFormat: String = conf.getString(CUSTOMERS_SURVEY_OUTPUT_FORMAT)

    val kafkaTopicName: String = s"${conf.getString(CUSTOMERS_SURVEY_TOPIC)}-$outputDataFormat"

    if (targetType.equalsIgnoreCase("kafka")) {
      val surveyProp = kafkaProducerProperties("customerSurvey")
      if (outputDataFormat.equalsIgnoreCase("avro")) {
        surveyProp.setProperty("schema.registry.url", getSchemaRegistryUrl)
        surveyProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
      } else {
        surveyProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[CustomerSurveyJsonSerializer].getName)
      }

      val surveysAvroProducer: Producer[String, CustomersSurveyAvro] = new KafkaProducer[String, CustomersSurveyAvro](surveyProp)
      val surveysJsonProducer: Producer[String, CustomersSurveyJson] = new KafkaProducer[String, CustomersSurveyJson](surveyProp)
      var startIndex: Int = 1

      while (startIndex <= numberOfSurveyResponse) {
        if (outputDataFormat.equalsIgnoreCase("json")) {
          val nextSurvey = CustomersFeedback.getNextSurveyResponse
          val customerSurveyRecord = getCustomerSurveyJsonPayload(nextSurvey)
          val surveyProducerRecord = new ProducerRecord[String, CustomersSurveyJson](kafkaTopicName, customerSurveyRecord.getResponseId, customerSurveyRecord)
          surveysJsonProducer.send(surveyProducerRecord, new AsynchronousProducerCallback)
        } else {
          val nextSurvey = CustomersFeedback.getNextSurveyResponse
          val customerSurveyRecord = getCustomerSurveyAvroPayload(nextSurvey)
          val surveyProducerRecord = new ProducerRecord[String, CustomersSurveyAvro](kafkaTopicName, customerSurveyRecord.getResponseId, customerSurveyRecord)
          surveysAvroProducer.send(surveyProducerRecord, new AsynchronousProducerCallback)
        }
        startIndex += 1
      }


    } else {
      val customerSurveyFilePath: String = s"generated-files/customer-survey.${outputDataFormat}"

      println(s"Customers File Path: $customerSurveyFilePath")
      createDirIfNotExists(customerSurveyFilePath)
      deleteFileIfExists(customerSurveyFilePath)
      if (outputDataFormat.equalsIgnoreCase("json")) {
        val writer = new FileWriter(customerSurveyFilePath)
        (1 to numberOfSurveyResponse).foreach(x => {
          val nextSurvey = CustomersFeedback.getNextSurveyResponse
          val customerSurveyJsonRecord = getCustomerSurveyJsonJsonStringPayload(nextSurvey)
          writer.write(customerSurveyJsonRecord + "\n")
        })
        writer.close()

      } else {
        val datumWriter = new SpecificDatumWriter[CustomersSurveyAvro](classOf[CustomersSurveyAvro])
        val dataFileWriter: DataFileWriter[CustomersSurveyAvro] = new DataFileWriter[CustomersSurveyAvro](datumWriter)
        val writer: DataFileWriter[CustomersSurveyAvro] = dataFileWriter.create(new CustomersSurveyAvro().getSchema, new File(customerSurveyFilePath))

        (1 to numberOfSurveyResponse).foreach(x => {
          val nextSurvey = CustomersFeedback.getNextSurveyResponse
          val customerSurveyAvroRecord = getCustomerSurveyAvroPayload(nextSurvey)
          writer.append(customerSurveyAvroRecord)
        })
        writer.close()
      }

    }

  }

}
