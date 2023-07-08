package com.bdcs.data.generator.invoices.serializer

import com.bdcs.data.generator.json.payment.InvoiceJson
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Serializer

import java.util

class InvoiceJsonSerializer extends Serializer[InvoiceJson] {
  private val objectMapper: ObjectMapper = new ObjectMapper()
  //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

  override def configure(config: util.Map[String, _], isKey: Boolean): Unit = {
    // Nothing to configure
  }

  /**
   * Serialize JsonNode
   *
   * @param topic Kafka topic name
   * @param data  data as JsonNode
   * @return byte[]
   */
  override def serialize(topic: String, data: InvoiceJson): Array[Byte] = {
    if (data == null) {
      println("Null received at serializing")
      return null
    }
    try {
      objectMapper.writeValueAsBytes(data)
    } catch {
      case e: Exception => throw new SerializationException("Error serializing JSON message", e)
    }
  }

  override def close(): Unit = {
    // Close any resources if needed
  }
}