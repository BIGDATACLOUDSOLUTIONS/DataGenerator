package com.bdcs.data.generator.lib.orders

case class OrderDetailModel(
                             order_item_id: String,
                             product_id: String,
                             quantity:Int,
                             price:Long,
                             order_id:String,
                             store_id:String
                           )
