package com.bdcs.data.generator
package lib.product

import com.github.javafaker.Faker

case class Product(
                    productCode: String,
                    productDescription: String,
                    productPrice: Double,
                    productCategory: String
                  )

object Product {

  val faker = new Faker()

  def getNextProduct: Product = {

    Product(
      productCode = faker.number().digits(5),
      productDescription = faker.commerce().productName(),
      productPrice = faker.commerce().price().toDouble,
      productCategory = faker.commerce().department()
    )
  }

  def printProduct(): Unit = println(getNextProduct)

}
