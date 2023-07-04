package com.bdcs.data.generator
package lib.reviews

import scala.util.Random
import com.github.javafaker.Faker

import java.text.SimpleDateFormat
import java.util.Locale

object Reviews {
  var reviewRecords: Array[ReviewModel] = Array[ReviewModel]()

  def apply(numReviews: Int): Unit = {
    reviewRecords = generateReviewRecords(numReviews)

  }

  private def generateReviewRecords(numReviews: Int
                                   ): Array[ReviewModel] = {

    val sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val startDate = sdf.parse("2022-01-01")
    val endDate = sdf.parse("2023-06-31")


    val random: Random.type = Random
    val faker = new Faker()

    (1 to numReviews).map(x => {
      ReviewModel(
        marketplace = faker.country().name(),
        customer_id = faker.random().nextInt(1000),
        review_id = faker.random().nextInt(1000),
        product_id = faker.random().nextInt(1000),
        product_name = Option(faker.commerce().productName()),
        category = faker.commerce().department(),
        unit_price = faker.random().nextInt(100),
        market_price = faker.random().nextDouble() * 100,
        star_rating = faker.random().nextInt(1, 6).toString,
        helpful_votes = random.nextInt(5000),
        total_votes = random.nextInt(10000),
        verified_purchase = Some(List("true", "false", null)(random.nextInt(3))),
        review_date = faker.date().between(startDate, endDate).toString)
    }).toArray
  }

  def printReviews(): Unit = reviewRecords.foreach(println)

}
