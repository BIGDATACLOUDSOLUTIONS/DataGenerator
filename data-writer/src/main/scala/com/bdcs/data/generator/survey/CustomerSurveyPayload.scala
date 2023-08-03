package com.bdcs.data.generator.survey

import com.bdcs.data.generator.json.survey.CustomersSurveyJson
import com.bdcs.data.generator.avro.survey.CustomersSurveyAvro
import com.bdcs.data.generator.lib.survey.CustomersFeedback
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

object CustomerSurveyPayload {

  def getCustomerSurveyAvroPayload(customerSurvey: CustomersFeedback): CustomersSurveyAvro = {
    CustomersSurveyAvro.newBuilder
      .setResponseId(customerSurvey.responseId)
      .setCustomerId(customerSurvey.customerId)
      .setOverallSatisfactions(customerSurvey.overallSatisfactions)
      .setRecommendToOthers(customerSurvey.recommendToOthers)
      .setStoreVisitFrequency(customerSurvey.storeVisitFrequency)
      .setFrequentDepartmentShopped(customerSurvey.frequentDepartmentShopped)
      .setProductVarietyRating(customerSurvey.productVarietyRating)
      .setIssuesFaced(customerSurvey.issuesFaced)
      .setLastVisitRating(customerSurvey.lastVisitRating)
      .setPricingCompetitive(customerSurvey.pricingCompetitive)
      .setOnlineVisitFrequency(customerSurvey.onlineVisitFrequency)
      .setLoyaltyProgramRating(customerSurvey.loyaltyProgramRating)
      .setMarketingChannels(customerSurvey.marketingChannels)
      .setImprovements(customerSurvey.improvements)
      .setEcoFriendlyPractices(customerSurvey.ecoFriendlyPractices)
      .setInfluencingFactorToShop(customerSurvey.influencingFactorToShop)
      .setCommunicationPreference(customerSurvey.communicationPreference)
      .setDeliveryPreference(customerSurvey.deliveryPreference)
      .setPaymentModePreference(customerSurvey.paymentModePreference)
      .setDateOfSurvey(customerSurvey.dateOfSurvey)
      .build

  }

  def getCustomerSurveyJsonPayload(customerSurvey: CustomersFeedback): CustomersSurveyJson = {
    val customerSurveyJson = new CustomersSurveyJson()
    customerSurveyJson.setResponseId(customerSurvey.responseId)
    customerSurveyJson.setCustomerId(customerSurvey.customerId)
    customerSurveyJson.setOverallSatisfactions(customerSurvey.overallSatisfactions)
    customerSurveyJson.setRecommendToOthers(customerSurvey.recommendToOthers)
    customerSurveyJson.setStoreVisitFrequency(customerSurvey.storeVisitFrequency)
    customerSurveyJson.setFrequentDepartmentShopped(customerSurvey.frequentDepartmentShopped)
    customerSurveyJson.setProductVarietyRating(customerSurvey.productVarietyRating)
    customerSurveyJson.setIssuesFaced(customerSurvey.issuesFaced)
    customerSurveyJson.setLastVisitRating(customerSurvey.lastVisitRating)
    customerSurveyJson.setPricingCompetitive(customerSurvey.pricingCompetitive)
    customerSurveyJson.setOnlineVisitFrequency(customerSurvey.onlineVisitFrequency)
    customerSurveyJson.setLoyaltyProgramRating(customerSurvey.loyaltyProgramRating)
    customerSurveyJson.setMarketingChannels(customerSurvey.marketingChannels)
    customerSurveyJson.setImprovements(customerSurvey.improvements)
    customerSurveyJson.setEcoFriendlyPractices(customerSurvey.ecoFriendlyPractices)
    customerSurveyJson.setInfluencingFactorToShop(customerSurvey.influencingFactorToShop)
    customerSurveyJson.setCommunicationPreference(customerSurvey.communicationPreference)
    customerSurveyJson.setDeliveryPreference(customerSurvey.deliveryPreference)
    customerSurveyJson.setPaymentModePreference(customerSurvey.paymentModePreference)
    customerSurveyJson.setDateOfSurvey(customerSurvey.dateOfSurvey)

    customerSurveyJson
  }


  def getCustomerSurveyJsonJsonStringPayload(customerSurvey: CustomersFeedback): String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    write(customerSurvey)
  }
}



