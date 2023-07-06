package com.bdcs.data.generator.common

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Serializer

import java.util

class JsonSerializer[T] extends Serializer[T] {
  private val objectMapper: ObjectMapper = new ObjectMapper()

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
  override def serialize(topic: String, data: T): Array[Byte] = {
    if (data == null) {
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