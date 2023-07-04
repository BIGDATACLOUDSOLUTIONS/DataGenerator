package com.bdcs.data.generator.reviews

import com.bdcs.data.generator.avro.reviews.ReviewsAvro
import com.bdcs.data.generator.lib.reviews.ReviewModel
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

object ReviewsPayload {

  def getReviewsJsonPayload(review: ReviewModel): String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    write(review)
  }

  def getReviewsAvroPayload(review: ReviewModel): ReviewsAvro = {

    ReviewsAvro.newBuilder
      .setMarketplace(review.marketplace)
      .setCustomerId(review.customer_id)
      .setReviewId(review.review_id)
      .setProductId(review.product_id)
      .setProductName(review.product_name.orNull)
      .setCategory(review.category)
      .setUnitPrice(review.unit_price)
      .setMarketPrice(review.market_price)
      .setStarRating(review.star_rating)
      .setHelpfulVotes(review.helpful_votes)
      .setTotalVotes(review.total_votes)
      .setVerifiedPurchase(review.verified_purchase.orNull)
      .setReviewDate(review.review_date)
      .build
  }
}
