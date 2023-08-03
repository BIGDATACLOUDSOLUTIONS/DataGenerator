package com.bdcs.data.generator

import com.bdcs.data.generator.common.AppConfig._
import com.bdcs.data.generator.invoices.InvoiceWriter
import com.bdcs.data.generator.payments.{PaymentsMasterWriter, PaymentsWriter}
import com.bdcs.data.generator.survey.CustomerSurveyWriter

object DataGenerator {
  def main(args: Array[String]): Unit = {

    println("Started DataGenerator App..............................")

    val dataCategory = args(0) // customer/store/product etc

    println(s"NUMBER_OF_MESSAGES_TO_PUBLISH: $getNoOfMessageToPublish")
    println(s"DATA_CATEGORY: $dataCategory")

    if (dataCategory.equalsIgnoreCase("Invoices")) InvoiceWriter()
    else if (dataCategory.equalsIgnoreCase("Payments")) PaymentsWriter()
    else if (dataCategory.equalsIgnoreCase("PaymentsMaster")) PaymentsMasterWriter()
    else if (dataCategory.equalsIgnoreCase("CustomerSurvey")) CustomerSurveyWriter()
    else {
      val validValuesForCategory = List(
        "Invoices",
        "Payments",
        "PaymentsMaster",
        "CustomerSurvey")

      throw new Exception(s"DataCategory can be either of ${validValuesForCategory.mkString("/ ")}")
    }

    println("DataGenerator App Completed Successfully..............................")
  }

}


