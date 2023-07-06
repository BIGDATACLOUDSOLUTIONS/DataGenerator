package com.bdcs.data.generator.posInvoices

import com.bdcs.data.generator.json.posInvoices.DeliveryAddressJson
import com.bdcs.data.generator.lib.posInvoices.Addresses

import java.util.Random

object DeliveryAddressPayload {

  private val random: Random = new Random()

  private def getIndex: Int = random.nextInt(100)

  def apply(): Unit = {
    Addresses()
  }

  def getNextJsonAddress: DeliveryAddressJson = {

    val address = Addresses.addresses(getIndex)

    val deliveryAddress = new DeliveryAddressJson()
    deliveryAddress.setAddressLine(address.AddressLine)
    deliveryAddress.setCity(address.City)
    deliveryAddress.setState(address.State)
    deliveryAddress.setPinCode(address.PinCode)
    deliveryAddress.setContactNumber(address.ContactNumber)

    deliveryAddress
  }

}
