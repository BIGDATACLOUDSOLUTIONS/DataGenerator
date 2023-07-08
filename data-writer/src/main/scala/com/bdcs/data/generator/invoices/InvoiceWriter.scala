package com.bdcs.data.generator.invoices

import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}
import io.confluent.kafka.serializers.KafkaAvroSerializer
import java.util.Random

import com.bdcs.data.generator.lib.customer.Customer
import com.bdcs.data.generator.lib.product.Product
import com.bdcs.data.generator.lib.store.Store
import com.bdcs.data.generator.lib.invoice.Invoice

import com.bdcs.data.generator.customers.CustomersWriter
import com.bdcs.data.generator.products.ProductsWriter
import com.bdcs.data.generator.stores.StoresWriter

import com.bdcs.data.generator.common.AppConfig._

import com.bdcs.data.generator.avro.payment.InvoiceAvro
import com.bdcs.data.generator.json.payment.InvoiceJson

import com.bdcs.data.generator.invoices.serializer.InvoiceJsonSerializer
import com.bdcs.data.generator.common.AsynchronousProducerCallback


object InvoiceWriter {

  private val random: Random = new Random()

  def apply(): Unit = {
    val numberOfInvoices = getNoOfMessageToPublish
    val dataFormat: String = conf.getString(INVOICES_OUTPUT_FORMAT)

    Invoice()
    CustomersWriter.customerWriter(Customer.customers)
    ProductsWriter.productsWriter(Product.products)
    StoresWriter.storesWriter(Store.stores)

    invoiceWriter(numberOfInvoices, dataFormat)
  }

  private def getCustomerIndex: Int = random.nextInt(200)

  private def getStoreIndex: Int = random.nextInt(50)


  private def invoiceWriter(numberOfInvoices: Int,
                            dataFormat: String): Unit = {

    val customer = Customer.customers(getCustomerIndex)
    val store = Store.stores(getStoreIndex)

    val properties = kafkaProducerProperties("invoices")
    if (dataFormat.equalsIgnoreCase("json")) {
      properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[InvoiceJsonSerializer].getName)
    } else {
      properties.setProperty("schema.registry.url", getSchemaRegistryUrl)
      properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
    }

    val invoicesAvroProducer: Producer[String, InvoiceAvro] = new KafkaProducer[String, InvoiceAvro](properties)
    val invoicesJsonProducer: Producer[String, InvoiceJson] = new KafkaProducer[String, InvoiceJson](properties)

    var startIndex: Int = 1
    while (startIndex <= numberOfInvoices) {

      if (dataFormat == "json") {
        val invoice = InvoiceJsonPayload.getInvoice(Invoice.getNextInvoice)
        invoice.setCustomerId(customer.customerId)
        invoice.setStoreId(store.storeId)
        val invoiceProducerRecord = new ProducerRecord[String, InvoiceJson](conf.getString(INVOICES_TOPIC), invoice.getInvoiceNumber, invoice)
        invoicesJsonProducer.send(invoiceProducerRecord, new AsynchronousProducerCallback)
      } else {
        val invoice: InvoiceAvro = {
          InvoiceAvroPayload.getInvoiceAvroBuilder(Invoice.getNextInvoice)
            .setCustomerId(customer.customerId)
            .setStoreId(store.storeId)
            .build()
        }
        val avroKafkaProducerRecord = new ProducerRecord[String, InvoiceAvro](conf.getString(INVOICES_TOPIC), invoice.getInvoiceNumber, invoice)
        invoicesAvroProducer.send(avroKafkaProducerRecord, new AsynchronousProducerCallback)
      }
      startIndex += 1
    }
  }

}
