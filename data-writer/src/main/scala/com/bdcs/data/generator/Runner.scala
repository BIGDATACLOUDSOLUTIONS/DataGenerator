package com.bdcs.data.generator

import com.bdcs.data.generator.customers.CustomersWriter
import com.bdcs.data.generator.invoices.InvoiceWriter
import com.bdcs.data.generator.payments.{PaymentsMasterWriter, PaymentsWriter}
import com.bdcs.data.generator.products.ProductsWriter
import com.bdcs.data.generator.stores.StoresWriter

object Runner extends App {

  val dataCategory = "payments"

  if (dataCategory.equals("customers")) {
    CustomersWriter()
  }
  if (dataCategory.equals("products")) {
    ProductsWriter()
  }
  if (dataCategory.equals("stores")) {
    StoresWriter()
  }

  if (dataCategory.equals("invoices")) {
    InvoiceWriter()
  }

  if (dataCategory.equals("payments")) {
    PaymentsWriter()
  }
  if (dataCategory.equals("payments-master")) {
    PaymentsMasterWriter()
  }

}
