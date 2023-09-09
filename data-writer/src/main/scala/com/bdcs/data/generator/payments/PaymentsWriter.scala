package com.bdcs.data.generator.payments

import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}
import io.confluent.kafka.serializers.KafkaAvroSerializer
import com.bdcs.data.generator.lib.customer.Customer
import com.bdcs.data.generator.lib.product.Product
import com.bdcs.data.generator.lib.store.Store
import com.bdcs.data.generator.lib.payment.Payment
import com.bdcs.data.generator.customers.CustomersWriter
import com.bdcs.data.generator.products.ProductsWriter
import com.bdcs.data.generator.stores.StoresWriter
import com.bdcs.data.generator.common.AppConfig._
import com.bdcs.data.generator.avro.payment.{InvoiceAvro, PaymentAvro}
import com.bdcs.data.generator.json.payment.{InvoiceJson, PaymentJson}
import com.bdcs.data.generator.invoices.serializer.InvoiceJsonSerializer
import com.bdcs.data.generator.payments.serializer.PaymentJsonSerializer
import com.bdcs.data.generator.common.AsynchronousProducerCallback
import com.bdcs.data.generator.invoices.{InvoiceAvroPayload, InvoiceJsonPayload}
import com.bdcs.data.generator.payments.PaymentJsonPayload.{getPaymentJsonPayload, getPaymentMasterJsonPayload}
import com.bdcs.data.generator.payments.PaymentAvroPayload.{getPaymentAvroPayload, getPaymentMasterAvroPayload}


object PaymentsWriter {

  def apply(): Unit = {
    val numberOfPayments = getNoOfMessageToPublish

    Payment()
    CustomersWriter.customerWriter(Customer.customers)
    ProductsWriter.productsWriter(Product.products)
    StoresWriter.storesWriter(Store.stores)

    paymentsWriter(numberOfPayments)
  }


  private def paymentsWriter(numberOfPayments: Int
                            ): Unit = {

    val paymentsProp = kafkaProducerProperties("payments")
    val invoicesProp = kafkaProducerProperties("invoices")

    val paymentsDataFormat: String = conf.getString(PAYMENTS_OUTPUT_FORMAT)
    val invoicesDataFormat: String = conf.getString(INVOICES_OUTPUT_FORMAT)

    val paymentsTopicName = s"${conf.getString(PAYMENTS_TOPIC)}-$paymentsDataFormat"
    val invoicesTopicName = s"${conf.getString(INVOICES_TOPIC)}-$invoicesDataFormat"

    if (paymentsDataFormat.equalsIgnoreCase("json")) {
      paymentsProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[PaymentJsonSerializer].getName)
    } else {
      paymentsProp.setProperty("schema.registry.url", getSchemaRegistryUrl)
      paymentsProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
    }

    if (invoicesDataFormat.equalsIgnoreCase("json")) {
      invoicesProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[InvoiceJsonSerializer].getName)
    } else {
      invoicesProp.setProperty("schema.registry.url", getSchemaRegistryUrl)
      invoicesProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
    }

    val invoicesAvroProducer: Producer[String, InvoiceAvro] = new KafkaProducer[String, InvoiceAvro](invoicesProp)
    val invoicesJsonProducer: Producer[String, InvoiceJson] = new KafkaProducer[String, InvoiceJson](invoicesProp)

    val paymentsAvroProducer: Producer[String, PaymentAvro] = new KafkaProducer[String, PaymentAvro](paymentsProp)
    val paymentsJsonProducer: Producer[String, PaymentJson] = new KafkaProducer[String, PaymentJson](paymentsProp)

    var startIndex: Int = 1
    while (startIndex <= numberOfPayments) {
      val nextPayment = Payment.getNextPayment

      if (paymentsDataFormat.equalsIgnoreCase("json")) {
        val paymentSummaryRecord = getPaymentJsonPayload(nextPayment)
        val paymentsProducerRecord = new ProducerRecord[String, PaymentJson](paymentsTopicName, paymentSummaryRecord.getPaymentId, paymentSummaryRecord)
        paymentsJsonProducer.send(paymentsProducerRecord, new AsynchronousProducerCallback)
      } else {
        val paymentSummaryRecord = getPaymentAvroPayload(nextPayment)
        val paymentsProducerRecord = new ProducerRecord[String, PaymentAvro](paymentsTopicName, paymentSummaryRecord.getPaymentId, paymentSummaryRecord)
        paymentsAvroProducer.send(paymentsProducerRecord, new AsynchronousProducerCallback)
      }

      if (invoicesDataFormat.equalsIgnoreCase("json")) {
        val invoiceRecord: InvoiceJson = InvoiceJsonPayload.getInvoice(nextPayment.invoice)
        val invoiceProducerRecord = new ProducerRecord[String, InvoiceJson](invoicesTopicName, invoiceRecord.getInvoiceNumber, invoiceRecord)
        invoicesJsonProducer.send(invoiceProducerRecord, new AsynchronousProducerCallback)
      } else {
        val invoiceRecord: InvoiceAvro = {
          InvoiceAvroPayload.getInvoice(nextPayment.invoice)
        }
        val invoiceProducerRecord = new ProducerRecord[String, InvoiceAvro](invoicesTopicName, invoiceRecord.getInvoiceNumber, invoiceRecord)
        invoicesAvroProducer.send(invoiceProducerRecord, new AsynchronousProducerCallback)
      }
      startIndex += 1
    }
  }
}
