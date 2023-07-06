package com.bdcs.data.generator
package lib.orders

import com.bdcs.data.generator.lib.customer.Customer
import com.bdcs.data.generator.lib.store.Store
import com.github.javafaker.Faker

import java.util.{Locale, Random}
import java.text.SimpleDateFormat
import java.util

case class Orders(
                   orderId: String,
                   customerId: String,
                   storeId: String,
                   orderDate: String,
                   totalAmount: Double,
                   orderLineDetails: Array[OrderLineDetails]
                 )


object Orders {

  private val sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
  private val startDate = sdf.parse("2022-01-01")
  private val endDate = sdf.parse("2023-06-31")

  val faker = new Faker()
  private val numberOfItems = new Random()

  private def getNumberOfItems = numberOfItems.nextInt(4) + 1

  def getNextOrder: Orders = {

    val customer = Customer.getNextCustomer
    val store = Store.getNextStore
    val items = new util.ArrayList[OrderLineDetails]

    var totalAmount: Double = 0.0
    (1 to getNumberOfItems).foreach(x => {
      val item: OrderLineDetails = OrderLineDetails.getNextLineItem
      totalAmount = totalAmount + item.totalPrice
      items.add(item)
    })

    Orders(
      orderId = faker.number().randomNumber(10, true).toString,
      customerId = customer.customerId,
      storeId = store.storeId,
      orderDate = faker.date().between(startDate, endDate).toString,
      totalAmount = totalAmount,
      orderLineDetails = items.asInstanceOf[Array[OrderLineDetails]]
    )
  }


  def printOrder: Unit = println(getNextOrder)


}
