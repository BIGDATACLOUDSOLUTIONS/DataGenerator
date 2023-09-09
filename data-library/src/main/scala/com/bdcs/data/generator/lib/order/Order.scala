package com.bdcs.data.generator
package lib.order

import com.bdcs.data.generator.lib.Utils.roundValue
import com.bdcs.data.generator.lib.product.Product
import com.github.javafaker.Faker

import java.util.Random
import org.joda.time.{DateTime, format}

import java.util
import scala.collection.JavaConverters._

case class Order(
                  orderId: String,
                  orderDate: String,
                  numberOfItems: Int,
                  totalOrderAmount: Double,
                  orderLineItems: Array[OrderLineItem]
                )


object Order {

  val random: Random = new Random()
  private val formatter = format.DateTimeFormat.forPattern("yyyy-MM-dd")

  val faker = new Faker()
  private val numberOfItems = new Random()

  def apply(): Unit = {
    Product(100)
  }

  private def getNumberOfItems = numberOfItems.nextInt(4) + 1

  def getNextOrder: Order = {

    val items = new util.ArrayList[OrderLineItem]
    val numberOfItems = getNumberOfItems

    var totalAmount: Double = 0.0
    (1 to numberOfItems).foreach(x => {
      val item: OrderLineItem = OrderLineItem.getNextLineItem
      totalAmount = totalAmount + item.totalPrice
      items.add(item)
    })

    Order(
      orderId = faker.number().randomNumber(10, true).toString,
      numberOfItems = numberOfItems,
      orderDate = DateTime.now.minusDays(random.nextInt(5) + 10).toString(formatter),
      totalOrderAmount = roundValue(totalAmount),
      orderLineItems = items.asScala.toArray
    )
  }


  def printOrder(): Unit = println(getNextOrder)


}
