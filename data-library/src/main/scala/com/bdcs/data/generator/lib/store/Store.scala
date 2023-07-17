package com.bdcs.data.generator.lib.store

import com.bdcs.data.generator.lib.address.Address
import com.github.javafaker.Faker

case class Store(
                  storeId: String,
                  storeName: String,
                  storeAddress: Address)


object Store {

  var stores: Array[Store] = Array[Store]()
  val faker = new Faker()

  def apply(numberOfStores: Int): Unit = {
    stores = (1 to numberOfStores).map(x => Store.getNextStore).toArray
  }

  def getNextStore: Store = {

    Store(
      storeId = s"STR${faker.number().randomNumber(3, false).toString}",
      storeName = faker.company().name(),
      storeAddress = Address.getNextAddress,
    )
  }

  def printStore(): Unit = println(getNextStore)

}
