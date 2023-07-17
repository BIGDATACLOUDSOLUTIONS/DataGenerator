package com.bdcs.data.generator.stores

import com.bdcs.data.generator.avro.payment._
import com.bdcs.data.generator.lib.store.Store
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

object StoresPayload {

  def getStoresJsonPayload(stores: Store): String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    write(stores)
  }

  def getStoresAvroPayload(store: Store): StoreAvro = {

    StoreAvro.newBuilder
      .setStoreId(store.storeId)
      .setStoreName(store.storeName)
      .setStoreAddress(new AddressAvro(
        store.storeAddress.addressLine,
        store.storeAddress.city,
        store.storeAddress.state,
        store.storeAddress.country,
        store.storeAddress.postcode
      )).build
  }

}
