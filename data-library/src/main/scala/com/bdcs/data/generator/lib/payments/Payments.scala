package com.bdcs.data.generator
package lib.payments

import com.bdcs.data.generator.common.FileReaderAndWriter._
import com.bdcs.data.generator.common.ConfigureParameters._
import com.bdcs.data.generator.avro.orders.OrdersHeaderAvro
import com.bdcs.data.generator.lib.orders.OrderHeaderModel
import org.apache.avro.specific.SpecificDatumReader
import com.github.javafaker.Faker
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse

import java.text.SimpleDateFormat
import java.util.Locale


object Payments {

  var paymentRecords: Array[PaymentModel] = Array[PaymentModel]()

  private val sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
  private val startDate = sdf.parse("2022-01-01")
  private val endDate = sdf.parse("2023-06-31")
  implicit val formats: DefaultFormats.type = DefaultFormats

  def apply(numberOfPayments: Int): Unit = {

    val ordersHeaderFilePath: String = ordersHeaderOutputFilePath
    val ordersHeaderList: Array[String] = if (getDataFormat.equals("json")) getJsonStringRecordsFromJsonFile(ordersHeaderFilePath)
    else {
      implicit val datumReader: SpecificDatumReader[OrdersHeaderAvro] =
        new SpecificDatumReader[OrdersHeaderAvro](classOf[OrdersHeaderAvro])
      getJsonStringRecordsFromAvroFile(ordersHeaderFilePath)
    }

    paymentRecords = generatePaymentRecords(numberOfPayments,
      ordersHeaderList.map(jsonString => parse(jsonString).extract[OrderHeaderModel])
    )
  }

  private def generatePaymentRecords(numberOfPayments: Int,
                                     ordersHeader: Array[OrderHeaderModel]): Array[PaymentModel] = {

    val faker = new Faker()


    (1 to numberOfPayments).map(x => {
      val ordersHeaderObject = ordersHeader(faker.number().numberBetween(1, ordersHeader.length - 1))

      val paymentDate = faker.date().between(startDate, endDate)
      val paymentDateString = sdf.format(paymentDate)

      PaymentModel(
        payment_id = faker.random().hex(8),
        payment_date = paymentDateString,
        payment_method = faker.business().creditCardType(),
        amount = ordersHeaderObject.total_price,
        customer_id = ordersHeaderObject.customer_id,
        order_id = ordersHeaderObject.order_id
      )
    }).toArray
  }

  def printPayments(): Unit = paymentRecords.foreach(println)

}
