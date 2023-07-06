package com.bdcs.data.generator
package lib.orders

import com.github.javafaker.Faker
import com.bdcs.data.generator.lib.product.Product

case class OrderLineDetails(
                             orderItemId: String,
                             productCode: String,
                             productDescription: String,
                             productPrice: Double,
                             quantityOrdered: Int,
                             totalPrice: Double
                           )

object OrderLineDetails {
  val faker = new Faker()

  private def getQuantityOrdered: Int = faker.number().numberBetween(1, 10)

  def getNextLineItem: OrderLineDetails = {

    val product = Product.getNextProduct
    val qty = getQuantityOrdered

    OrderLineDetails(
      orderItemId = faker.number().digits(3),
      productCode = product.productCode,
      productDescription = product.productDescription,
      productPrice = product.productPrice,
      quantityOrdered = qty,
      totalPrice = product.productPrice * qty
    )
  }

  def printLineItem: Unit = println(getNextLineItem)


}