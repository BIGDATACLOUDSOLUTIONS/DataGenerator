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
    val dataFormat: String = conf.getString(PAYMENTS_OUTPUT_FORMAT)

    Payment()
    CustomersWriter.customerWriter(Customer.customers)
    ProductsWriter.productsWriter(Product.products)
    StoresWriter.storesWriter(Store.stores)

    paymentsWriter(numberOfPayments, dataFormat)
  }


  private def paymentsWriter(numberOfPayments: Int,
                             dataFormat: String
                            ): Unit = {

    val paymentsProp = kafkaProducerProperties("payments")
    val invoicesProp = kafkaProducerProperties("invoices")
    if (dataFormat.equalsIgnoreCase("json")) {
      paymentsProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[PaymentJsonSerializer].getName)
      invoicesProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[InvoiceJsonSerializer].getName)
    } else {
      paymentsProp.setProperty("schema.registry.url", getSchemaRegistryUrl)
      invoicesProp.setProperty("schema.registry.url", getSchemaRegistryUrl)
      paymentsProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
      invoicesProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
    }

    val invoicesAvroProducer: Producer[String, InvoiceAvro] = new KafkaProducer[String, InvoiceAvro](paymentsProp)
    val invoicesJsonProducer: Producer[String, InvoiceJson] = new KafkaProducer[String, InvoiceJson](invoicesProp)

    val paymentsAvroProducer: Producer[String, PaymentAvro] = new KafkaProducer[String, PaymentAvro](paymentsProp)
    val paymentsJsonProducer: Producer[String, PaymentJson] = new KafkaProducer[String, PaymentJson](paymentsProp)

    var startIndex: Int = 1
    while (startIndex <= numberOfPayments) {
      if (dataFormat.equalsIgnoreCase("json")) {
        val nextPayment = Payment.getNextPayment
        val paymentSummaryRecord = getPaymentJsonPayload(nextPayment)

        val invoiceRecord: InvoiceJson = InvoiceJsonPayload.getInvoice(nextPayment.invoice)
        invoiceRecord.setCustomerId(nextPayment.customer.customerId)
        invoiceRecord.setStoreId(nextPayment.store.storeId)

        val invoiceProducerRecord = new ProducerRecord[String, InvoiceJson](conf.getString(INVOICES_TOPIC), invoiceRecord.getInvoiceNumber, invoiceRecord)
        invoicesJsonProducer.send(invoiceProducerRecord, new AsynchronousProducerCallback)

        val paymentsProducerRecord = new ProducerRecord[String, PaymentJson](conf.getString(PAYMENTS_TOPIC), paymentSummaryRecord.getPaymentId, paymentSummaryRecord)
        paymentsJsonProducer.send(paymentsProducerRecord, new AsynchronousProducerCallback)
      } else {
        val nextPayment = Payment.getNextPayment
        val paymentSummaryRecord = getPaymentAvroPayload(nextPayment)

        val invoiceRecord: InvoiceAvro = {
          InvoiceAvroPayload.getInvoiceAvroBuilder(nextPayment.invoice)
            .setCustomerId(nextPayment.customer.customerId)
            .setStoreId(nextPayment.store.storeId)
            .build()
        }

        val invoiceProducerRecord = new ProducerRecord[String, InvoiceAvro](conf.getString(INVOICES_TOPIC), invoiceRecord.getInvoiceNumber, invoiceRecord)
        invoicesAvroProducer.send(invoiceProducerRecord, new AsynchronousProducerCallback)

        val paymentsProducerRecord = new ProducerRecord[String, PaymentAvro](conf.getString(PAYMENTS_TOPIC), paymentSummaryRecord.getPaymentId, paymentSummaryRecord)
        paymentsAvroProducer.send(paymentsProducerRecord, new AsynchronousProducerCallback)
      }
      startIndex += 1
    }
  }
}
