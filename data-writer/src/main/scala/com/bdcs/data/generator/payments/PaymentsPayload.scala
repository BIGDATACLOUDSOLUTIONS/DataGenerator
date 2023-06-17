package com.bdcs.data.generator.payments

import com.bdcs.data.generator.avro.payments.PaymentsAvro
import com.bdcs.data.generator.lib.payments.PaymentModel

import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

object PaymentsPayload {

  def getPaymentsJsonPayload(payment: PaymentModel): String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    write(payment)
  }

  def getPaymentsAvroPayload(payment: PaymentModel): PaymentsAvro = {

    PaymentsAvro.newBuilder
      .setPaymentId(payment.payment_id)
      .setPaymentDate(payment.payment_date)
      .setPaymentMethod(payment.payment_method)
      .setAmount(payment.amount)
      .setOrderId(payment.order_id)
      .setCustomerId(payment.customer_id)
      .build
  }


}









