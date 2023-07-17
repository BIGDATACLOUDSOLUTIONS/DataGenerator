package com.bdcs.data.generator

import com.bdcs.data.generator.customers.CustomersWriter
import com.bdcs.data.generator.invoices.InvoiceWriter
import com.bdcs.data.generator.payments.{PaymentsMasterWriter, PaymentsWriter}
import com.bdcs.data.generator.products.ProductsWriter
import com.bdcs.data.generator.stores.StoresWriter

/**
 * This is just for testing purpose
 */

object Runner extends App {

  val dataCategory = "payments"

  if (dataCategory.equals("Customers")) {
    CustomersWriter()
  }
  if (dataCategory.equals("Products")) {
    ProductsWriter()
  }
  if (dataCategory.equals("Stores")) {
    StoresWriter()
  }

  if (dataCategory.equals("Invoices")) {
    InvoiceWriter()
  }

  if (dataCategory.equals("Payments")) {
    PaymentsWriter()
  }
  if (dataCategory.equals("PaymentsMaster")) {
    PaymentsMasterWriter()
  }

}
