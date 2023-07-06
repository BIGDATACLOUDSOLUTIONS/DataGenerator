package com.bdcs.data.generator.posInvoices

import com.bdcs.data.generator.json.posInvoices.{LineItemJson, PosInvoiceJson}
import com.bdcs.data.generator.lib.posInvoices.Invoices

import java.util
import java.util.Random

object PosInvoicePayload {

  private val numberOfItems = new Random()

  private def getNoOfItems: Int = numberOfItems.nextInt(4) + 1

  def apply():Unit={
    ProductPayload()
    DeliveryAddressPayload()
  }

  def getNextJsonInvoice: PosInvoiceJson = {
    val invoice = Invoices.getNextInvoice

    val posInvoice=new PosInvoiceJson()
    posInvoice.setInvoiceNumber(invoice.InvoiceNumber)
    posInvoice.setCreatedTime(invoice.CreatedTime)
    posInvoice.setStoreID(invoice.StoreID)
    posInvoice.setPosID(invoice.PosID)
    posInvoice.setCashierID(invoice.CashierID)
    posInvoice.setCustomerType(invoice.CustomerType)
    posInvoice.setCustomerCardNo(invoice.CustomerCardNo)

    val itemCount: Int = getNoOfItems
    var totalAmount: Double = 0.0
    val items = new util.ArrayList[LineItemJson]

    (0 to itemCount).foreach(x => {
      val item: LineItemJson = ProductPayload.getNextJsonProduct
      totalAmount = totalAmount + item.getTotalValue
      items.add(item)
    })

    posInvoice.setTotalAmount(totalAmount)
    posInvoice.setNumberOfItems(itemCount)
    posInvoice.setPaymentMethod(invoice.PaymentMethod)
    posInvoice.setTaxableAmount(totalAmount)
    posInvoice.setCGST(totalAmount * 0.025)
    posInvoice.setSGST(totalAmount * 0.025)
    posInvoice.setCESS(totalAmount * 0.00125)

    posInvoice.setDeliveryType(invoice.DeliveryType)
    if ("HOME-DELIVERY".equalsIgnoreCase(posInvoice.getDeliveryType)) {
      val deliveryAddress = DeliveryAddressPayload.getNextJsonAddress
      posInvoice.setDeliveryAddress(deliveryAddress)
    }

    posInvoice.setInvoiceLineItems(items)
    posInvoice
  }

}



