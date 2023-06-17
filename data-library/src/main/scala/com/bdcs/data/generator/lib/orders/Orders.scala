package com.bdcs.data.generator.lib.orders

import com.bdcs.data.generator.avro.customers.CustomerAvro
import com.bdcs.data.generator.common.FileReaderAndWriter._
import com.bdcs.data.generator.common.ConfigureParameters._
import com.bdcs.data.generator.avro.products.ProductsAvro
import com.bdcs.data.generator.lib.products.ProductModel
import com.bdcs.data.generator.avro.stores.StoresAvro
import com.bdcs.data.generator.lib.customers.CustomerModel
import com.bdcs.data.generator.lib.stores.StoresModel
import com.github.javafaker.Faker
import org.apache.avro.specific.SpecificDatumReader
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse

import java.util.Locale
import java.text.SimpleDateFormat


object Orders {

  var ordersHeadersRecords: Array[OrderHeaderModel] = Array[OrderHeaderModel]()
  var ordersDetailsRecords: Array[OrderDetailModel] = Array[OrderDetailModel]()

  private val sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
  private val startDate = sdf.parse("2022-01-01")
  private val endDate = sdf.parse("2023-06-31")
  implicit val formats: DefaultFormats.type = DefaultFormats

  def apply(numberOfOrders: Int): Unit = {

    val productList: Array[String] = if (getDataFormat.equals("json")) getJsonStringRecordsFromJsonFile(productOutputFilePath)
    else {
      implicit val datumReader: SpecificDatumReader[ProductsAvro] =
        new SpecificDatumReader[ProductsAvro](classOf[ProductsAvro])
      getJsonStringRecordsFromAvroFile(productOutputFilePath)
    }

    println(s"Number of records in productList: ${productList.length}")

    val storesList: Array[String] = if (getDataFormat.equals("json")) getJsonStringRecordsFromJsonFile(storesOutputFilePath)
    else {
      implicit val datumReader: SpecificDatumReader[StoresAvro] =
        new SpecificDatumReader[StoresAvro](classOf[StoresAvro])
      getJsonStringRecordsFromAvroFile(storesOutputFilePath)
    }

    println(s"Number of records in storesList: ${storesList.length}")

    val customersList: Array[String] = if (getDataFormat.equals("json")) getJsonStringRecordsFromJsonFile(customerOutputFilePath)
    else {
      implicit val datumReader: SpecificDatumReader[CustomerAvro] =
        new SpecificDatumReader[CustomerAvro](classOf[CustomerAvro])
      getJsonStringRecordsFromAvroFile(customerOutputFilePath)
    }

    println(s"Number of records in customersList: ${customersList.length}")

    val orders = generateOrderRecords(
      numberOfOrders,
      productList.map(jsonString => parse(jsonString).extract[ProductModel]),
      storesList.map(jsonString => parse(jsonString).extract[StoresModel]),
      customersList.map(jsonString => parse(jsonString).extract[CustomerModel])
    )
    ordersHeadersRecords = orders.map(x => x._1)
    ordersDetailsRecords = orders.flatMap(x => x._2)

  }

  private def generateOrderRecords(numberOfOrders: Int,
                                   products: Array[ProductModel],
                                   stores: Array[StoresModel],
                                   customers: Array[CustomerModel]): Array[(OrderHeaderModel, Array[OrderDetailModel])] = {
    val faker = new Faker()

    (1 to numberOfOrders).map(x => {
      val numberOfRecordsOfOrdersDetailsForOneOrder = faker.number().numberBetween(1, 5)
      val orderId = faker.number().randomNumber(10, true).toString

      var total: Int = 0
      val orderDetails: Array[OrderDetailModel] =
        (1 to numberOfRecordsOfOrdersDetailsForOneOrder).map(y => {
          val randomIndex=faker.number().numberBetween(1, products.length - 1)
          val productObject = products(randomIndex)
          total += productObject.unit_price

          OrderDetailModel(
            order_item_id = faker.number().randomNumber(8, true).toString,
            product_id = productObject.product_id,
            quantity = faker.number().numberBetween(1, 10),
            price = productObject.unit_price,
            order_id = orderId,
            store_id = stores(faker.number().numberBetween(1, stores.length - 1)).store_id
          )
        }).toArray

      val customer = customers(faker.number().numberBetween(1, customers.length - 1))

      val orderDate = faker.date().between(startDate, endDate)
      val orderDateString = sdf.format(orderDate)

      val orders = OrderHeaderModel(
        order_id = orderId,
        order_date = orderDateString,
        total_price = total,
        customer_id = customer.customer_id
      )
      (orders, orderDetails)
    }).toArray
  }

  def printOrdersDetails(): Unit = ordersHeadersRecords.foreach(println)

  def printOrders(): Unit = ordersDetailsRecords.foreach(println)

  def main(args:Array[String]):Unit={

    import scala.sys.process._
    // Set environment variable
    sys.props += ("NUMBER_OF_MESSAGES_TO_PUBLISH" -> "10")
    sys.props += ("TARGET_TYPE" -> "file")
    sys.props += ("OUTPUT_DATA_FORMAT" -> "json")
    sys.props += ("PRINT_DATA_ON_CONSOLE" -> "true")

    // Retrieve environment variable
    val retrievedValue = sys.env.get("NUMBER_OF_MESSAGES_TO_PUBLISH")
    retrievedValue.foreach(println)

  }

}
