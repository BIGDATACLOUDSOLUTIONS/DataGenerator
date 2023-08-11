package com.bdcs.data.generator.payments

import com.bdcs.data.generator.json.payment._

import com.bdcs.data.generator.lib.payment.Payment
import com.bdcs.data.generator.invoices.InvoiceJsonPayload._

object PaymentJsonPayload {

  def getPaymentMasterJsonPayload(payment: Payment): PaymentMasterJson = {

    val paymentMaster = new PaymentMasterJson()
    paymentMaster.setPaymentId(payment.paymentId)
    paymentMaster.setPaymentDate(payment.paymentDate)
    paymentMaster.setPosID(payment.posID)
    paymentMaster.setCashierID(payment.cashierID)
    paymentMaster.setPaymentMethod(payment.paymentMethod)
    paymentMaster.setDiscount(payment.discount)
    paymentMaster.setAmountPaid(payment.amountPaid)
    paymentMaster.setInvoice(getInvoice(payment.invoice))
    paymentMaster.setStore(getStore(payment.store))
    paymentMaster.setCustomer(getCustomer(payment.customer))
    paymentMaster.setDeliveryType(payment.deliveryType)
    paymentMaster.setEventTimestamp(System.currentTimeMillis.toString)

    if ("HOME-DELIVERY".equalsIgnoreCase(paymentMaster.getDeliveryType)) {
      val deliveryAddress = getAddress(payment.deliveryAddress)
      paymentMaster.setDeliveryAddress(deliveryAddress)
    }
    paymentMaster
  }


  def getPaymentJsonPayload(payment: Payment): PaymentJson = {
    val deliveryAddress = getAddress(payment.deliveryAddress)

    val paymentSummary = new PaymentJson()
    paymentSummary.setPaymentId(payment.paymentId)
    paymentSummary.setPaymentDate(payment.paymentDate)
    paymentSummary.setPosID(payment.posID)
    paymentSummary.setCashierID(payment.cashierID)
    paymentSummary.setPaymentMethod(payment.paymentMethod)
    paymentSummary.setDiscount(payment.discount)
    paymentSummary.setAmountPaid(payment.amountPaid)
    paymentSummary.setInvoiceNumber(payment.invoice.invoiceNumber)
    paymentSummary.setStoreId(payment.store.storeId)
    paymentSummary.setCustomerId(payment.customer.customerId)
    paymentSummary.setDeliveryType(payment.deliveryType)
    paymentSummary.setEventTimestamp(System.currentTimeMillis.toString)

    if ("HOME-DELIVERY".equalsIgnoreCase(paymentSummary.getDeliveryType)) {
      paymentSummary.setDeliveryAddress(deliveryAddress)
    }
    paymentSummary
  }


}
