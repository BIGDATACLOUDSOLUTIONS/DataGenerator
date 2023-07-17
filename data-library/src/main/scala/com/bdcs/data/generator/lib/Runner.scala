package com.bdcs.data.generator.lib

import com.bdcs.data.generator.lib.customer.Customer
import com.bdcs.data.generator.lib.invoice.Invoice
import com.bdcs.data.generator.lib.order.Order
import com.bdcs.data.generator.lib.payment.Payment
import com.bdcs.data.generator.lib.product.Product
import com.bdcs.data.generator.lib.store.Store

/**
 * This is just for testing purpose
 */

object Runner extends App{

  val dataCategory = "payment"

  if (dataCategory.equals("customer")) {
        (1 to 100).foreach(x => Customer.printCustomers())
  }

  if (dataCategory.equals("product")) {
    (1 to 100).foreach(x => Product.printProduct())
  }

  if (dataCategory.equals("store")) {
    (1 to 100).foreach(x => Store.printStore())
  }


  if (dataCategory.equals("order")) {
    Order()
    (1 to 100).foreach(x => Order.printOrder())
  }
  if (dataCategory.equals("invoice")) {
    Invoice()
    (1 to 100).foreach(x => Invoice.printInvoice())

  }
  if (dataCategory.equals("payment")) {
    Payment()
    (1 to 100).foreach(x => Payment.printPayment())
  }


}
