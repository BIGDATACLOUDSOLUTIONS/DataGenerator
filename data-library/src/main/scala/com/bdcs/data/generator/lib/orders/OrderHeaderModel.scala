package com.bdcs.data.generator.lib.orders

case class OrderHeaderModel(
                       order_id: String,
                       order_date: String,
                       total_price: Int,
                       customer_id: String
                     )
