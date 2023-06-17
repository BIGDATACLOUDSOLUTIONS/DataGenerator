package com.bdcs.data.generator
package lib.customers

case class CustomerModel(customer_id: String,
                         gender: String,
                         name: Name,
                         address: Address,
                         email: String,
                         dob: Dob,
                         registration_timestamp: String,
                         phone: String
                        )

case class Name(title: String,
                first: String,
                last: String)

case class Address(street: Street,
                   city: String,
                   state: String,
                   country: String,
                   postcode: Long
                  )

case class Street(number: Int,
                  name: String)

case class Dob(date: String,
               age: Int)

