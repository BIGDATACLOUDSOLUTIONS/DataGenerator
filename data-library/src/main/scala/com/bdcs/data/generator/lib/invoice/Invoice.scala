package com.bdcs.data.generator.lib.invoice

import com.bdcs.data.generator.lib.Utils.roundValue
import com.bdcs.data.generator.lib.customer.Customer
import com.bdcs.data.generator.lib.order.Order
import com.bdcs.data.generator.lib.product.Product
import com.bdcs.data.generator.lib.store.Store
import com.github.javafaker.Faker

import java.util.Random
import org.joda.time.{DateTime, format}

case class Invoice(
                    invoiceNumber: String,
                    createdTime: String,
                    taxableAmount: Double = 0,
                    cGST: Double = 0,
                    sGST: Double = 0,
                    cESS: Double = 0,
                    order: Order,
                  )

object Invoice {

  private val invoiceNumber = new Random()
  val faker = new Faker()
  val random: Random = new Random()

  def apply(): Unit = {
    Customer(200)
    Product(100)
    Store(50)
  }

  private val formatter = format.DateTimeFormat.forPattern("yyyy-MM-dd")

  private def getInvoiceDate(orderDate: String): String = {
    DateTime.parse(orderDate, formatter).plusDays(random.nextInt(15)).toString(formatter)
  }

  private def getNewInvoiceNumber: Int = invoiceNumber.nextInt(99999999) + 99999

  def getNextInvoice: Invoice = {

    val order = Order.getNextOrder
    val totalAmount = order.totalOrderAmount

    Invoice(
      invoiceNumber = Integer.toString(getNewInvoiceNumber),
      createdTime = getInvoiceDate(order.orderDate),
      taxableAmount = totalAmount,
      cGST = roundValue(totalAmount * 0.025),
      sGST = roundValue(totalAmount * 0.025),
      cESS = roundValue(totalAmount * 0.00125),
      order = order
    )
  }

  def printInvoice(): Unit = println(getNextInvoice)

}
