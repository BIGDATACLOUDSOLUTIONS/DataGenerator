package com.bdcs.data.generator

import com.bdcs.data.generator.common.AppConfig._
import com.bdcs.data.generator.invoices.InvoiceWriter
import com.bdcs.data.generator.payments.{PaymentsMasterWriter, PaymentsWriter}

object DataGenerator {
  def main(args: Array[String]): Unit = {

    println("Started DataGenerator App..............................")

    val dataCategory = args(0).toLowerCase // customer/store/product etc

    val validValuesForCategory = List(
      "invoices",
      "reviews",
      "payments",
      "payments-master")

    if (!validValuesForCategory.contains(dataCategory)) {
      throw new Exception(s"DataCategory can be either of ${validValuesForCategory.mkString("/ ")}")
    }

    println(s"NUMBER_OF_MESSAGES_TO_PUBLISH: $getNoOfMessageToPublish")
    println(s"DATA_CATEGORY: $dataCategory")

    if (dataCategory.equals("invoices")) InvoiceWriter()
    if (dataCategory.equals("payments")) PaymentsWriter()
    if (dataCategory.equals("payments-master")) PaymentsMasterWriter()

    println("DataGenerator App Completed Successfully..............................")
  }

}


