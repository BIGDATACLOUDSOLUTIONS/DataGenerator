package com.bdcs.data.generator
package lib.order

import com.bdcs.data.generator.lib.Utils.roundValue
import com.bdcs.data.generator.lib.product.Product
import com.github.javafaker.Faker

import java.util.Random

case class OrderLineItem(
                          orderItemId: String,
                          product: Product,
                          quantityOrdered: Int,
                          totalPrice: Double
                        )

object OrderLineItem {
  val faker = new Faker()
  private val random: Random = new Random()

  private def getQuantityOrdered: Int = faker.number().numberBetween(1, 10)

  private def getProductIndex: Int = random.nextInt(50)

  def getNextLineItem: OrderLineItem = {

    val product = Product.products(getProductIndex)
    val qty = getQuantityOrdered

    OrderLineItem(
      orderItemId = faker.number().digits(3),
      product = product,
      quantityOrdered = qty,
      totalPrice = roundValue(product.productPrice * qty)
    )
  }

  def printLineItem: Unit = println(getNextLineItem)


}