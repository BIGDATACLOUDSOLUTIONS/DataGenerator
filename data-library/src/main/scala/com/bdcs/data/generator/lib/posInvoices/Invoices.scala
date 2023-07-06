package com.bdcs.data.generator.lib
package posInvoices

import java.util.Random
import com.bdcs.data.generator.lib.posInvoices.models.InvoiceModel
import com.github.javafaker.Faker

object Invoices {

  private val random = new Random()
  private val invoiceNumber = new Random()
  val faker = new Faker()

  private def getNewInvoiceNumber: Int = invoiceNumber.nextInt(99999999) + 99999

  def getNextInvoice: InvoiceModel = {
    InvoiceModel(
      InvoiceNumber = Integer.toString(getNewInvoiceNumber),
      CreatedTime = System.currentTimeMillis,
      StoreID = s"STR${faker.number().randomNumber(3, false).toString}",
      PosID = s"POS${faker.number().randomNumber(3, false).toString}",
      CashierID = s"OAS${faker.number().randomNumber(3, false).toString}",
      CustomerType = List("PRIME", "NON-PRIME")(random.nextInt(2)),
      CustomerCardNo = faker.number().randomNumber(16, false).toString,
      PaymentMethod = List("CARD", "CASH", "UPI")(random.nextInt(3)),
      DeliveryType = List("HOME-DELIVERY", "TAKEAWAY")(random.nextInt(2))
    )
  }
}

