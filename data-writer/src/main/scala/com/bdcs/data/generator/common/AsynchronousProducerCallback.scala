package com.bdcs.data.generator.common

import org.apache.kafka.clients.producer.{Callback, RecordMetadata}

class AsynchronousProducerCallback extends Callback {
  @Override
  def onCompletion(recordMetadata: RecordMetadata, e: Exception): Unit = {
    if (e != null) {
      e.printStackTrace()
      println("AsynchronousProducer failed with an exception")
    }
    else println(recordMetadata)
  }
}