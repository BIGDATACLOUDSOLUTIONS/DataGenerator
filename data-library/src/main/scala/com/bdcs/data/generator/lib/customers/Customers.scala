package com.bdcs.data.generator
package lib.customers

import com.github.javafaker.Faker
import java.util.UUID
import scala.util.Random
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Customers {

  var customerRecords: Array[CustomerModel] = Array[CustomerModel]()

  def apply(numberOfCustomers: Int): Unit = {
    customerRecords = generateCustomerRecords(numberOfCustomers)
  }

  private def generateCustomerRecords(numberOfCustomers: Int): Array[CustomerModel] = {

    val faker = new Faker()
    val random: Random.type = Random

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // Define the desired timestamp format

    (1 to numberOfCustomers).map(x => {

      val genderField = List("male", "female")(random.nextInt(2))

      CustomerModel(
        customer_id = UUID.randomUUID().toString,
        gender = genderField,
        name = Name(
          title = if (genderField.equals("male")) "Mr" else "Mrs",
          first = faker.name().firstName(),
          last = faker.name().lastName()
        ),
        address = Address(
          street = Street(
            number = faker.number().randomDigitNotZero(),
            name = faker.address().streetName()
          ),
          city = faker.address().city(),
          state = faker.address().state(),
          country = faker.address().country(),
          postcode = faker.number().randomNumber(5, true)
        ),
        email = faker.internet().emailAddress(),
        dob = Dob(
          date = faker.date().birthday().toInstant.toString,
          age = faker.number().numberBetween(18, 100)
        ),
        registration_timestamp = {
          val timestamp = LocalDateTime.now().minusDays(faker.number().numberBetween(0, 365))
          timestamp.format(formatter)
        },
        phone = faker.phoneNumber().phoneNumber()
      )
    }).toArray
  }

  def printCustomers(): Unit = customerRecords.foreach(println)

}
