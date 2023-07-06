package com.bdcs.data.generator.stores

import com.bdcs.data.generator.avro.stores.StoresAvro
import com.bdcs.data.generator.lib.store.StoresModel

import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

object StoresPayload {

  def getStoresJsonPayload(stores: StoresModel): String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    write(stores)
  }

  def getStoresAvroPayload(store: StoresModel): StoresAvro = {

    StoresAvro.newBuilder
      .setStoreId(store.store_id)
      .setStoreName(store.store_name)
      .setAddress(store.address)
      .setCity(store.city)
      .setCountry(store.country)
      .build
  }

}
