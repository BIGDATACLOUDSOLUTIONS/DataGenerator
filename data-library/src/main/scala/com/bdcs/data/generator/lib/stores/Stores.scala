package com.bdcs.data.generator.lib.stores

import com.github.javafaker.Faker

object Stores {

  var storesRecords: Array[StoresModel] = Array[StoresModel]()

  def apply(numberOfStores: Int): Unit = {
    storesRecords = generateStoreRecords(numberOfStores)
  }

  private def generateStoreRecords(numberOfStores: Int): Array[StoresModel] = {
    val faker = new Faker()

    (1 to numberOfStores).map(x => {
      StoresModel(
        store_id = faker.number().randomNumber(4, true).toString,
        store_name = faker.company().name(),
        address = faker.address().fullAddress(),
        city = faker.address().city(),
        country = faker.address().country()
      )
    }).toArray
  }

  def printStores(): Unit = storesRecords.foreach(println)

}
