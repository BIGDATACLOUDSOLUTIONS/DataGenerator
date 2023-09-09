package com.bdcs.data.generator.lib.survey

import com.github.javafaker.Faker
import org.joda.time.{DateTime, format}

import java.util.UUID
import scala.util.Random


case class CustomersFeedback(
                              responseId: String,
                              customerId: String,
                              overallSatisfactions: Int,
                              recommendToOthers: Int,
                              storeVisitFrequency: Int,
                              frequentDepartmentShopped: Int,
                              productVarietyRating: Int,
                              issuesFaced: String,
                              lastVisitRating: Int,
                              pricingCompetitive: String,
                              onlineVisitFrequency: Int,
                              loyaltyProgramRating: Int,
                              marketingChannels: Int,
                              improvements: String,
                              ecoFriendlyPractices: Int,
                              influencingFactorToShop: Int,
                              communicationPreference: Int,
                              deliveryPreference: Int,
                              paymentModePreference: Int,
                              dateOfSurvey: String
                            )

object CustomersFeedback {

  val faker = new Faker()
  val random: Random.type = Random

  private val formatter = format.DateTimeFormat.forPattern("yyyy-MM-dd")

  var surveyResponses: Array[CustomersFeedback] = Array[CustomersFeedback]()

  def apply(numberOfSurveyResponses: Int): Unit = {
    surveyResponses = (1 to numberOfSurveyResponses).map(x => CustomersFeedback.getNextSurveyResponse).toArray
  }

  private def getRandomNumber: Int = Random.nextInt(5) + 1

  def getNextSurveyResponse: CustomersFeedback = {

    CustomersFeedback(
      responseId = faker.number().digits(6),
      customerId = UUID.randomUUID.toString,
      overallSatisfactions = getRandomNumber,
      recommendToOthers = getRandomNumber,
      storeVisitFrequency = Random.nextInt(4) + 1,
      frequentDepartmentShopped = getRandomNumber,
      productVarietyRating = getRandomNumber,
      issuesFaced = null,
      lastVisitRating = getRandomNumber,
      pricingCompetitive = List("Yes", "No", "Not Sure")(random.nextInt(3)),
      onlineVisitFrequency = Random.nextInt(3) + 1,
      loyaltyProgramRating = getRandomNumber,
      marketingChannels = Random.nextInt(4) + 1,
      improvements = {
        List(null, List(getRandomNumber, getRandomNumber).mkString(","))(random.nextInt(2))
      },
      ecoFriendlyPractices = getRandomNumber,
      influencingFactorToShop = Random.nextInt(8) + 1,
      communicationPreference = getRandomNumber,
      deliveryPreference = Random.nextInt(4) + 1,
      paymentModePreference = Random.nextInt(4) + 1,
      dateOfSurvey = DateTime.now.toString(formatter)
    )
  }

}