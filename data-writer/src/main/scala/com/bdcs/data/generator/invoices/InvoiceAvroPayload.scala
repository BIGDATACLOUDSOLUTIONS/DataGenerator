package com.bdcs.data.generator.invoices

import com.bdcs.data.generator.avro.payment._
import com.bdcs.data.generator.lib.address.Address
import com.bdcs.data.generator.lib.customer.Customer
import com.bdcs.data.generator.lib.invoice.Invoice
import com.bdcs.data.generator.lib.order.{Order, OrderLineItem}
import com.bdcs.data.generator.lib.product.Product
import com.bdcs.data.generator.lib.store.Store

import scala.collection.JavaConverters._

object InvoiceAvroPayload {

  def getAddress(address: Address): AddressAvro = {
    new AddressAvro(
      address.addressLine,
      address.city,
      address.state,
      address.country,
      address.postcode
    )
  }

  def getCustomer(customer: Customer): CustomerAvro = {
    new CustomerAvro(
      customer.customerId,
      customer.title,
      customer.firstName,
      customer.lastName,
      customer.gender,
      customer.dob,
      customer.registrationTimestamp,
      customer.contactNumber,
      customer.email,
      getAddress(customer.address),
      customer.customerType,
      customer.customerCardNo)
  }

  def getProduct(product: Product): ProductAvro = {
    new ProductAvro(
      product.productCode,
      product.productDescription,
      product.productPrice,
      product.productCategory)
  }

  def getStore(store: Store): StoreAvro = {
    new StoreAvro(
      store.storeId,
      store.storeName,
      getAddress(store.storeAddress))
  }

  def getOrderLineDetail(orderLine: OrderLineItem): OrderLineItemAvro = {
    new OrderLineItemAvro(
      orderLine.orderItemId,
      getProduct(orderLine.product),
      orderLine.quantityOrdered,
      orderLine.totalPrice)
  }


  def getOrder(order: Order): OrderAvro = {

    val lineItems = order.orderLineItems.map(x => getOrderLineDetail(x))
    new OrderAvro(
      order.orderId,
      order.orderDate,
      order.numberOfItems,
      order.totalOrderAmount,
      lineItems.toList.asJava)
  }

  def getInvoice(invoice: Invoice): InvoiceAvro = {
    new InvoiceAvro(
      invoice.invoiceNumber,
      invoice.createdTime,
      invoice.taxableAmount,
      invoice.cGST,
      invoice.sGST,
      invoice.cESS,
      getOrder(invoice.order),
      null,
      null,
      System.currentTimeMillis.toString)
  }

  def getInvoiceAvroBuilder(invoice: Invoice): InvoiceAvro.Builder = {
    InvoiceAvro.newBuilder
      .setInvoiceNumber(invoice.invoiceNumber)
      .setCreatedTime(invoice.createdTime)
      .setTaxableAmount(invoice.taxableAmount)
      .setCGST(invoice.cGST)
      .setSGST(invoice.sGST)
      .setCESS(invoice.cESS)
      .setOrder(getOrder(invoice.order))
      .setEventTimestamp(System.currentTimeMillis.toString)
  }

}
