package com.bdcs.data.generator.lib.payments

case class PaymentModel(
                        payment_id:String,
                        payment_date:String,
                        payment_method:String,
                        amount:Int,
                        customer_id:String,
                        order_id:String
                        )
