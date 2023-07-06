package com.bdcs.data.generator.lib.invoice

import com.github.javafaker.Faker

import java.util.Random

case class Invoice(
                    InvoiceNumber: String,
                    CreatedTime: Long,
                    StoreID: String,
                    PosID: String,
                    CashierID: String,
                    CustomerType: String,
                    CustomerCardNo: String,
                    TotalAmount: Double = 0,
                    NumberOfItems: Int = 0,
                    PaymentMethod: String,
                    TaxableAmount: Double = 0,
                    CGST: Double = 0,
                    SGST: Double = 0,
                    CESS: Double = 0,
                    DeliveryType: String
                  )

object Invoice {

  private val random = new Random()
  private val invoiceNumber = new Random()
  val faker = new Faker()

  private def getNewInvoiceNumber: Int = invoiceNumber.nextInt(99999999) + 99999

  def getNextInvoice: Invoice = {
    Invoice(
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

