package com.bdcs.data.generator
package lib.product

import com.bdcs.data.generator.lib.Utils.roundValue
import com.github.javafaker.Faker

case class Product(
                    productCode: String,
                    productDescription: String,
                    productPrice: Double,
                    productCategory: String
                  )

object Product {
  var products: Array[Product] = Array[Product]()
  val faker = new Faker()

  def apply(numberOfProducts: Int): Unit = {
    products = (1 to numberOfProducts).map(x => Product.getNextProduct).toArray
  }

  def getNextProduct: Product = {

    Product(
      productCode = faker.number().digits(5),
      productDescription = faker.commerce().productName(),
      productPrice = roundValue(faker.commerce().price().toDouble),
      productCategory = faker.commerce().department()
    )
  }

  def printProduct(): Unit = println(getNextProduct)

}
