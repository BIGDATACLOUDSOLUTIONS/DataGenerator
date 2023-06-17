package com.bdcs.data.generator
package lib.products

import com.github.javafaker.Faker

object Products {

  var productRecords: Array[ProductModel] = Array[ProductModel]()

  def apply(numProducts: Int): Unit = {
    productRecords = generateProductRecords(numProducts)

  }

  private def generateProductRecords(numProducts: Int
                                    ): Array[ProductModel] = {

    val faker = new Faker()

    (1 to numProducts).map(x => {
      ProductModel(
        product_id = faker.number().digits(5),
        product_name = faker.commerce().productName(),
        unit_price = faker.commerce().price().toDouble.toInt,
        category = faker.commerce().department()
      )
    }).toArray

  }

  def printProducts(): Unit = productRecords.foreach(println)

}
