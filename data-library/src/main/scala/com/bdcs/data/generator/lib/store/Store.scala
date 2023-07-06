package com.bdcs.data.generator.lib.store

import com.bdcs.data.generator.lib.address.Address
import com.github.javafaker.Faker

case class Store(
                  storeId: String,
                  storeName: String,
                  storeAddress: Address)


object Store {

  val faker = new Faker()

  def getNextStore: Store = {

    Store(
      storeId = s"STR${faker.number().randomNumber(3, false).toString}",
      storeName = faker.company().name(),
      storeAddress = Address.getNextAddress,
    )

  }

  def printStores(): Unit = println(getNextStore)

}
