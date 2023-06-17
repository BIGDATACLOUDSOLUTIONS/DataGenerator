package com.bdcs.data.generator.orders

import com.bdcs.data.generator.avro.orders.OrdersHeaderAvro
import com.bdcs.data.generator.lib.orders.OrderHeaderModel
import com.bdcs.data.generator.avro.orders.OrdersDetailAvro
import com.bdcs.data.generator.lib.orders.OrderDetailModel

import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write


object OrdersPayload {

  def getOrdersHeaderJsonPayload(ordersHeader: OrderHeaderModel): String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    write(ordersHeader)
  }


  def getOrdersHeaderAvroPayload(ordersHeader: OrderHeaderModel): OrdersHeaderAvro = {

    OrdersHeaderAvro.newBuilder
      .setOrderId(ordersHeader.order_id)
      .setOrderDate(ordersHeader.order_date)
      .setTotalPrice(ordersHeader.total_price)
      .setCustomerId(ordersHeader.customer_id)
      .build
  }

  def getOrdersDetailJsonPayload(ordersDetail: OrderDetailModel): String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    write(ordersDetail)
  }

  def getOrdersDetailAvroPayload(ordersDetail: OrderDetailModel): OrdersDetailAvro = {

    OrdersDetailAvro.newBuilder
      .setOrderItemId(ordersDetail.order_item_id)
      .setProductId(ordersDetail.product_id)
      .setQuantity(ordersDetail.quantity)
      .setPrice(ordersDetail.price)
      .setOrderId(ordersDetail.order_id)
      .setStoreId(ordersDetail.store_id)
      .build
  }

}


