package com.bdcs.data.generator

import com.bdcs.data.generator.common.ConfigureParameters._
import com.bdcs.data.generator.customers.CustomerWriter
import com.bdcs.data.generator.stores.StoresWriter
import com.bdcs.data.generator.products.ProductsWriter
import com.bdcs.data.generator.orders.OrdersWriter
import com.bdcs.data.generator.payments.PaymentsWriter
import com.bdcs.data.generator.posInvoices.PosInvoiceWriter
import com.bdcs.data.generator.reviews.ReviewsWriter

object DataGenerator {
  def main(args: Array[String]): Unit = {

    println("Started DataGenerator App..............................")

    dataCategory = args(0).toLowerCase // customer/store/product etc

    val validValuesForCategory = List("customer", "store", "product", "orders", "payments", "reviews", "invoices")

    if (!validValuesForCategory.contains(dataCategory)) {
      throw new Exception(s"DataCategory can be either of ${validValuesForCategory.mkString("/ ")}")
    }

    println(s"NUMBER_OF_MESSAGES_TO_PUBLISH: $getNoOfMessageToPublish")
    println(s"DATA_CATEGORY: $dataCategory")
    println(s"TARGET_TYPE: $getTarget") //Kafka/File
    println(s"OUTPUT_DATA_FORMAT: $getDataFormat") //Avro/json

    if (dataCategory.equals("customer")) CustomerWriter()
    if (dataCategory.equals("store")) StoresWriter()
    if (dataCategory.equals("product")) ProductsWriter()
    if (dataCategory.equals("orders")) OrdersWriter()
    if (dataCategory.equals("payments")) PaymentsWriter()
    if (dataCategory.equals("reviews")) ReviewsWriter()
    if (dataCategory.equals("invoices")) PosInvoiceWriter()

    println("DataGenerator App Completed Successfully..............................")
  }

}


