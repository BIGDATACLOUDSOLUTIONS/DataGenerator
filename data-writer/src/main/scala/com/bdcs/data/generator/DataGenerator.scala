package com.bdcs.data.generator

import com.bdcs.data.generator.common.ConfigureParameters._
import com.bdcs.data.generator.customers.CustomerWriter
import com.bdcs.data.generator.stores.StoresWriter
import com.bdcs.data.generator.products.ProductsWriter
import com.bdcs.data.generator.orders.OrdersWriter
import com.bdcs.data.generator.payments.PaymentsWriter
import com.bdcs.data.generator.reviews.ReviewsWriter

object DataGenerator {
  def main(args: Array[String]): Unit = {

    println("Started DataGenerator App..............................")

    dataCategory = args(0).toLowerCase // customers/stores/product etc

    val validValuesForCategory = List("customers", "stores", "products", "orders", "payments", "reviews")

    if (!validValuesForCategory.contains(dataCategory)) {
      throw new Exception(s"DataCategory can be either of ${validValuesForCategory.mkString("/ ")}")
    }

    println(s"NUMBER_OF_MESSAGES_TO_PUBLISH: $getNoOfMessageToPublish")
    println(s"DATA_CATEGORY: $dataCategory")
    println(s"TARGET_TYPE: $getTarget") //Kafka/File
    println(s"OUTPUT_DATA_FORMAT: $getDataFormat") //Avro/json

    if (dataCategory.equals("customers")) CustomerWriter()
    if (dataCategory.equals("stores")) StoresWriter()
    if (dataCategory.equals("products")) ProductsWriter()
    if (dataCategory.equals("orders")) OrdersWriter()
    if (dataCategory.equals("payments")) PaymentsWriter()
    if (dataCategory.equals("reviews")) ReviewsWriter()

    println("DataGenerator App Completed Successfully..............................")
  }

}


