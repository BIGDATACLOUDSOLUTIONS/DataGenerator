package com.bdcs.data.generator.reviews

import com.bdcs.data.generator.avro.reviews.ReviewsAvro
import com.bdcs.data.generator.common.ConfigureParameters._
import com.bdcs.data.generator.common.FileReaderAndWriter._
import com.bdcs.data.generator.common.KafkaProducerAndConsumer._
import com.bdcs.data.generator.common.Utils._
import com.bdcs.data.generator.lib.reviews.{ReviewModel, Reviews}
import com.bdcs.data.generator.reviews.ReviewsPayload._
import org.apache.avro.file.DataFileWriter
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}

import java.io.File

object ReviewsWriter {

  def apply(): Unit = {
    val numberOfReviews = getNoOfMessageToPublish.toInt
    val dataFormat: String = getDataFormat
    val targetType = getTarget
    reviewsWriter(numberOfReviews, dataFormat, targetType, printOnConsole)
  }

  def reviewsWriter(numberOfReviews: Int,
                    dataFormat: String,
                    targetType: String,
                    printMessagesOnConsole: Boolean,
                    writeToFileAndKafka: Boolean = false
                   ): Unit = {

    val reviews: Array[ReviewModel] = {
      Reviews(numberOfReviews)
      Reviews.reviewRecords
    }

    if (targetType.equals("file") || writeToFileAndKafka) {
      println(s"Reviews File Path: $reviewsOutputFilePath")
      createDirIfNotExists(reviewsOutputFilePath)
      deleteFileIfExists(reviewsOutputFilePath)

      if (dataFormat.equals("avro")) {
        val datumWriter = new SpecificDatumWriter[ReviewsAvro](classOf[ReviewsAvro])
        val dataFileWriter: DataFileWriter[ReviewsAvro] = new DataFileWriter[ReviewsAvro](datumWriter)
        implicit val writer: DataFileWriter[ReviewsAvro] = dataFileWriter.create(new ReviewsAvro().getSchema, new File(reviewsOutputFilePath))
        writeAvroToFile[ReviewsAvro, ReviewModel](
          reviews, getReviewsAvroPayload)
        implicit val datumReader: SpecificDatumReader[ReviewsAvro] = new SpecificDatumReader[ReviewsAvro](classOf[ReviewsAvro])
        if (printMessagesOnConsole) getJsonStringRecordsFromAvroFile[ReviewsAvro](reviewsOutputFilePath).foreach(println)
      }
      if (dataFormat.equals("json")) {
        writeJsonToFile[ReviewModel](
          reviews, reviewsOutputFilePath, getReviewsJsonPayload)
        if (printMessagesOnConsole) getJsonStringRecordsFromJsonFile(reviewsOutputFilePath).foreach(println)
      }
    }

    if (targetType.equals("kafka")) {
      val kafkaTopicName = if (writeToFileAndKafka) "reviews" else getKafkaTopicName
      println(s"Reviews Kafka Topic Name: $kafkaTopicName")
      if (dataFormat.equals("avro")) {
        kafkaAvroProducer[ReviewsAvro, ReviewModel](
          reviews,
          getReviewsAvroPayload, kafkaTopicName)
        if (printMessagesOnConsole) kafkaAvroConsoleConsumer[ReviewsAvro](numberOfReviews, kafkaTopicName)
      }
      if (dataFormat.equals("json")) {
        kafkaJsonProducer[ReviewModel](reviews, getReviewsJsonPayload, kafkaTopicName)
        if (printMessagesOnConsole) kafkaJsonConsoleConsumer(numberOfReviews, kafkaTopicName)
      }
    }
  }

}
