package com.bdcs.data.generator.payments.deserializer

import com.bdcs.data.generator.json.payment.PaymentJson
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer

import java.util

class PaymentJsonDeserializer extends Deserializer[PaymentJson] {

  private val objectMapper: ObjectMapper = new ObjectMapper()


  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
    // No configuration needed
  }


  /**
   * Deserialize the byte array into an object of type T
   *
   * @param topic Kafka topic name
   * @param data  serialized data as byte array
   * @return T
   */
  override def deserialize(topic: String, data: Array[Byte]): PaymentJson = {
    try {
      if (data == null) {
        println("Null received at deserializing")
        return null
      }
      println("Deserializing...")
      objectMapper.readValue(new String(data, "UTF-8"), classOf[PaymentJson])
    } catch {
      case e: Exception =>
        throw new SerializationException("Error when deserializing byte[] to MessageDto", e)
    }
  }


  override def close(): Unit = {
    // Close any resources if needed
  }

}
