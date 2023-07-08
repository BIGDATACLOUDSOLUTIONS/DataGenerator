package com.bdcs.data.generator.customers

import com.bdcs.data.generator.avro.payment._
import com.bdcs.data.generator.lib.customer.Customer
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

object CustomerPayload {
  def getCustomerJsonPayload(customer: Customer): String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    write(customer)
  }

  def getCustomerAvroPayload(customer: Customer): CustomerAvro = {

    CustomerAvro.newBuilder
      .setCustomerId(customer.customerId)
      .setTitle(customer.title)
      .setFirstName(customer.firstName)
      .setLastName(customer.lastName)
      .setGender(customer.gender)
      .setDob(customer.dob)
      .setRegistrationTimestamp(customer.registrationTimestamp)
      .setContactNumber(customer.contactNumber)
      .setEmail(customer.email)
      .setAddress(new AddressAvro(
        customer.address.addressLine,
        customer.address.city,
        customer.address.state,
        customer.address.country,
        customer.address.postcode
      ))
      .setCustomerType(customer.customerType)
      .setCustomerCardNo(customer.customerCardNo)
      .build
  }

}