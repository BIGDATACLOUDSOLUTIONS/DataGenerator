package com.bdcs.data.generator.lib.address

import com.github.javafaker.Faker


case class Address(
                    addressLine: String,
                    city: String,
                    state: String,
                    country: String,
                    postcode: String
                  )

object Address {

  val faker = new Faker()

  def getNextAddress: Address = {
    Address(
      addressLine = s"${faker.number().randomDigitNotZero()}-${faker.address().streetName()}",
      city = faker.address().city(),
      state = faker.address().state(),
      country = faker.address().country(),
      postcode = faker.number().randomNumber(5, true).toString
    )
  }

  def printAddress: Unit = println(getNextAddress)

}
