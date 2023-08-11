package com.bdcs.data.generator.payments

import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}
import io.confluent.kafka.serializers.KafkaAvroSerializer
import com.bdcs.data.generator.common.AppConfig._
import com.bdcs.data.generator.lib.payment.Payment
import com.bdcs.data.generator.avro.payment.PaymentMasterAvro
import com.bdcs.data.generator.json.payment.PaymentMasterJson
import com.bdcs.data.generator.payments.serializer.{JsonSerializer, PaymentMasterJsonSerializer}
import com.bdcs.data.generator.common.AsynchronousProducerCallback
import com.bdcs.data.generator.payments.PaymentJsonPayload.getPaymentMasterJsonPayload
import com.bdcs.data.generator.payments.PaymentAvroPayload.getPaymentMasterAvroPayload


object PaymentsMasterWriter {

  def apply(): Unit = {
    val numberOfPayments = getNoOfMessageToPublish

    Payment()
    paymentsMasterWriter(numberOfPayments)
  }


  private def paymentsMasterWriter(numberOfPayments: Int
                                  ): Unit = {

    val dataFormat: String = conf.getString(PAYMENTS_MASTER_OUTPUT_FORMAT)
    val kafkaTopicName = s"${conf.getString(PAYMENTS_MASTER_TOPIC)}-${dataFormat}"

    val properties = kafkaProducerProperties("payments-master")

    if (dataFormat.equalsIgnoreCase("json")) {
      properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[PaymentMasterJsonSerializer].getName)
    } else {
      properties.setProperty("schema.registry.url", getSchemaRegistryUrl)
      properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
    }

    val paymentsMasterJsonProducer: Producer[String, PaymentMasterJson] = new KafkaProducer[String, PaymentMasterJson](properties)
    val paymentsMasterAvroProducer: Producer[String, PaymentMasterAvro] = new KafkaProducer[String, PaymentMasterAvro](properties)

    var startIndex: Int = 1
    while (startIndex <= numberOfPayments) {
      val nextPayment = Payment.getNextPayment

      if (dataFormat.equalsIgnoreCase("json")) {
        val payment = getPaymentMasterJsonPayload(nextPayment)
        val paymentProducerRecord = new ProducerRecord[String, PaymentMasterJson](kafkaTopicName, payment.getPaymentId, payment)
        paymentsMasterJsonProducer.send(paymentProducerRecord, new AsynchronousProducerCallback)

      } else {
        val payment = getPaymentMasterAvroPayload(nextPayment)
        val paymentProducerRecord = new ProducerRecord[String, PaymentMasterAvro](kafkaTopicName, payment.getPaymentId, payment)
        paymentsMasterAvroProducer.send(paymentProducerRecord, new AsynchronousProducerCallback)
      }
      startIndex += 1
    }
  }

}
