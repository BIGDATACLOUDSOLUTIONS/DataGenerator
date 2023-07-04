package com.bdcs.data.generator.lib.reviews

case class ReviewModel(marketplace: String,
                       customer_id: Int,
                       review_id: Int,
                       product_id: Int,
                       product_name: Option[String]=None,
                       category: String,
                       unit_price: Double,
                       market_price: Double,
                       star_rating: String,
                       helpful_votes: Int,
                       total_votes: Int,
                       verified_purchase: Option[String]=None,
                       review_date: String)

