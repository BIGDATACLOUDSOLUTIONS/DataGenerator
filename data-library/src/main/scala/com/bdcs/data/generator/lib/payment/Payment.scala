package com.bdcs.data.generator
package lib.payment

import com.bdcs.data.generator.lib.Utils.roundValue
import com.bdcs.data.generator.lib.address.Address
import com.bdcs.data.generator.lib.customer.Customer
import com.bdcs.data.generator.lib.invoice.Invoice
import com.bdcs.data.generator.lib.product.Product
import com.bdcs.data.generator.lib.store.Store
import com.github.javafaker.Faker

import java.util.Random
import org.joda.time.{DateTime, format}

case class Payment(
                    paymentId: String,
                    paymentDate: String,
                    posID: String, //POS: Point of Sale Machines
                    cashierID: String,
                    paymentMethod: String,
                    discount: Int,
                    amountPaid: Double,
                    invoice: Invoice,
                    store: Store,
                    customer: Customer,
                    deliveryType: String,
                    deliveryAddress: Address
                  )


object Payment {

  val faker = new Faker()
  val random: Random = new Random()

  private val formatter = format.DateTimeFormat.forPattern("yyyy-MM-dd")

  private def getCustomerIndex: Int = random.nextInt(200)

  private def getStoreIndex: Int = random.nextInt(50)

  def apply(): Unit = {
    Customer(200)
    Product(100)
    Store(50)
  }

  private def getNextPaymentDate(invoiceCreateDate: String): String = {
    DateTime.parse(invoiceCreateDate, formatter).plusDays(random.nextInt(9)).toString(formatter)
  }


  def getNextPayment: Payment = {
    val invoice = Invoice.getNextInvoice

    val customer = Customer.customers(getCustomerIndex)
    val store = Store.stores(getStoreIndex)

    val discount = if (customer.customerType.equals("PRIME")) 10 else 5
    val amountToBePaid = roundValue(((invoice.taxableAmount + invoice.cGST + invoice.sGST + invoice.cESS) * discount) / 100)

    Payment(
      paymentId = faker.random().hex(8),
      paymentDate = getNextPaymentDate(invoice.createdTime),
      posID = s"POS${faker.number().randomNumber(3, false).toString}",
      cashierID = s"OAS${faker.number().randomNumber(3, false).toString}",
      paymentMethod = List("CARD", "CASH", "UPI")(random.nextInt(3)),
      discount = discount,
      amountPaid = amountToBePaid,
      invoice = invoice,
      store = store,
      customer = customer,
      deliveryType = List("HOME-DELIVERY", "TAKEAWAY")(random.nextInt(2)),
      deliveryAddress = customer.address
    )
  }

  def printPayment(): Unit = println(getNextPayment)

}



