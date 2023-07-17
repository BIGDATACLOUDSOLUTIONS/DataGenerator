package com.bdcs.data.generator.invoices

import com.bdcs.data.generator.json.payment._
import com.bdcs.data.generator.lib.address.Address
import com.bdcs.data.generator.lib.customer.Customer
import com.bdcs.data.generator.lib.invoice.Invoice
import com.bdcs.data.generator.lib.order.{Order, OrderLineItem}
import com.bdcs.data.generator.lib.product.Product
import com.bdcs.data.generator.lib.store.Store

import scala.collection.JavaConverters._

object InvoiceJsonPayload {

  def getAddress(address: Address): AddressJson = {
    val deliveryAddress = new AddressJson()

    deliveryAddress.setAddressLine(address.addressLine)
    deliveryAddress.setCity(address.city)
    deliveryAddress.setState(address.state)
    deliveryAddress.setCountry(address.country)
    deliveryAddress.setPostcode(address.postcode)
    deliveryAddress
  }

  def getCustomer(customer: Customer): CustomerJson = {
    val customerData = new CustomerJson()
    customerData.setCustomerId(customer.customerId)
    customerData.setTitle(customer.title)
    customerData.setFirstName(customer.firstName)
    customerData.setLastName(customer.lastName)
    customerData.setGender(customer.gender)
    customerData.setDob(customer.dob)
    customerData.setRegistrationTimestamp(customer.registrationTimestamp)
    customerData.setContactNumber(customer.contactNumber)
    customerData.setEmail(customer.email)
    customerData.setAddress(getAddress(customer.address))
    customerData.setCustomerType(customer.customerType)
    customerData.setCustomerCardNo(customer.customerCardNo)
    customerData
  }

  def getStore(store: Store): StoreJson = {
    val storeData = new StoreJson()
    storeData.setStoreId(store.storeId)
    storeData.setStoreName(store.storeName)
    storeData.setStoreAddress(getAddress(store.storeAddress))
    storeData
  }


  def getProduct(product: Product): ProductJson = {
    val productData = new ProductJson()
    productData.setProductCode(product.productCode)
    productData.setProductDescription(product.productDescription)
    productData.setProductPrice(product.productPrice)
    productData.setProductCategory(product.productCategory)
    productData
  }

  def getOrderLineDetail(orderLine: OrderLineItem): OrderLineItemJson = {
    val orderLineData = new OrderLineItemJson()
    orderLineData.setOrderItemId(orderLine.orderItemId)
    orderLineData.setProduct(getProduct(orderLine.product))
    orderLineData.setQuantityOrdered(orderLine.quantityOrdered)
    orderLineData.setTotalPrice(orderLine.totalPrice)
    orderLineData
  }


  def getOrder(order: Order): OrderJson = {
    val orderData = new OrderJson()

    val lineItems = order.orderLineItems.map(x => getOrderLineDetail(x))

    orderData.setOrderId(order.orderId)
    orderData.setOrderDate(order.orderDate)
    orderData.setNumberOfItems(order.numberOfItems)
    orderData.setTotalOrderAmount(order.totalOrderAmount)
    orderData.setOrderLineItems(lineItems.toList.asJava)

    orderData
  }

  def getInvoice(invoice: Invoice): InvoiceJson = {
    val invoiceData = new InvoiceJson()
    invoiceData.setInvoiceNumber(invoice.invoiceNumber)
    invoiceData.setCreatedTime(invoice.createdTime)
    invoiceData.setTaxableAmount(invoice.taxableAmount)
    invoiceData.setCGST(invoice.cGST)
    invoiceData.setSGST(invoice.sGST)
    invoiceData.setCESS(invoice.cESS)
    invoiceData.setOrder(getOrder(invoice.order))
    invoiceData
  }


}
