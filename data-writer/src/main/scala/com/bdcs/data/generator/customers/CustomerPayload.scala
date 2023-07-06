package com.bdcs.data.generator.customers

import com.bdcs.data.generator.avro.customers._
import com.bdcs.data.generator.lib.customer.CustomerModel
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

object CustomerPayload {
  def getCustomerJsonPayload(customer: CustomerModel): String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    write(customer)
  }

  def getCustomerAvroPayload(customer: CustomerModel): CustomerAvro = {

    val street = new Street(customer.address.street.number, customer.address.street.name)
    CustomerAvro.newBuilder
      .setCustomerId(customer.customer_id)
      .setGender(customer.gender)
      .setName(new Name(customer.name.title, customer.name.first, customer.name.last))
      .setAddress(new Address(
        street,
        customer.address.city,
        customer.address.state,
        customer.address.country,
        customer.address.postcode
      ))
      .setEmail(customer.email)
      .setDob(new Dob(
        customer.dob.date,
        customer.dob.age
      ))
      .setRegistrationTimestamp(customer.registration_timestamp)
      .setPhone(customer.phone)
      .build
  }


}
