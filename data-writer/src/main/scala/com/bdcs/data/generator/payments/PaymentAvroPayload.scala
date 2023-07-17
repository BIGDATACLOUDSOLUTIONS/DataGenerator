package com.bdcs.data.generator.payments

import com.bdcs.data.generator.avro.payment._
import com.bdcs.data.generator.lib.payment.Payment

import com.bdcs.data.generator.invoices.InvoiceAvroPayload._
object PaymentAvroPayload {

  def getPaymentMasterAvroPayload(payment: Payment): PaymentMasterAvro = {
    val paymentMaster = PaymentMasterAvro.newBuilder
      .setPaymentId(payment.paymentId)
      .setPaymentDate(payment.paymentDate)
      .setPosID(payment.posID)
      .setCashierID(payment.cashierID)
      .setPaymentMethod(payment.paymentMethod)
      .setDiscount(payment.discount)
      .setAmountPaid(payment.amountPaid)
      .setInvoice(getInvoice(payment.invoice))
      .setStore(getStore(payment.store))
      .setCustomer(getCustomer(payment.customer))
      .setDeliveryType(payment.deliveryType)

    if ("HOME-DELIVERY".equalsIgnoreCase(paymentMaster.getDeliveryType)) {
      val deliveryAddress = getAddress(payment.deliveryAddress)
      paymentMaster.setDeliveryAddress(deliveryAddress)
    }
    paymentMaster.build
  }


  def getPaymentAvroPayload(payment: Payment): PaymentAvro = {

    val deliveryAddress = getAddress(payment.deliveryAddress)

    val paymentSummary = PaymentAvro.newBuilder
      .setPaymentId(payment.paymentId)
      .setPaymentDate(payment.paymentDate)
      .setPosID(payment.posID)
      .setCashierID(payment.cashierID)
      .setPaymentMethod(payment.paymentMethod)
      .setDiscount(payment.discount)
      .setAmountPaid(payment.amountPaid)
      .setInvoiceNumber(payment.invoice.invoiceNumber)
      .setStoreId(payment.store.storeId)
      .setCustomerId(payment.customer.customerId)
      .setDeliveryType(payment.deliveryType)
    if ("HOME-DELIVERY".equalsIgnoreCase(paymentSummary.getDeliveryType)) {
      paymentSummary.setDeliveryAddress(deliveryAddress)
    }
    paymentSummary.build
  }



}
