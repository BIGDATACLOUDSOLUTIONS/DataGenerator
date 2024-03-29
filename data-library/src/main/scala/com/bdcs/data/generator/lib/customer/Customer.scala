package com.bdcs.data.generator
package lib.customer

import com.bdcs.data.generator.lib.address.Address
import com.github.javafaker.Faker

import java.text.SimpleDateFormat
import java.util.UUID
import scala.util.Random
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class Customer(customerId: String,
                    title: String,
                    firstName: String,
                    lastName: String,
                    gender: String,
                    dob: String,
                    registrationTimestamp: String,
                    contactNumber: String,
                    email: String,
                    address: Address,
                    customerType: String,
                    customerCardNo: String
                   )

object Customer {
  val faker = new Faker()
  val random: Random.type = Random

  var customers: Array[Customer] = Array[Customer]()

  def apply(numberOfCustomers: Int): Unit = {
    customers = (1 to numberOfCustomers).map(x => Customer.getNextCustomer).toArray
  }

  def getNextCustomer: Customer = {
    val genderField = List("male", "female")(random.nextInt(2))

    Customer(
      customerId = UUID.randomUUID.toString,
      title = if (genderField.equals("male")) "Mr" else "Mrs",
      firstName = faker.name.firstName,
      lastName = faker.name.lastName,
      gender = genderField,
      dob = {
        val formatter = new SimpleDateFormat("yyyy-MM-dd")
        formatter.format(faker.date.birthday)
      },
      registrationTimestamp = {
        val timestamp = LocalDateTime.now().minusDays(faker.number().numberBetween(0, 365))
        timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      },
      contactNumber = faker.phoneNumber().phoneNumber,
      email = faker.internet().emailAddress,
      address = Address.getNextAddress,
      customerType = List("PRIME", "NON-PRIME")(random.nextInt(2)),
      customerCardNo = faker.number().randomNumber(16, false).toString
    )
  }

  def printCustomers(): Unit = println(getNextCustomer)

}
