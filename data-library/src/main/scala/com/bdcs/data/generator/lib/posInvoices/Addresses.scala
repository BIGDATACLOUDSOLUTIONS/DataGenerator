package com.bdcs.data.generator.lib
package posInvoices

import com.bdcs.data.generator.common.Utils.readJsonFile
import com.bdcs.data.generator.lib.posInvoices.models.DeliveryAddressModel


object Addresses {

  var addresses: Array[DeliveryAddressModel] = Array[DeliveryAddressModel]()

  private val DATAFILE: String = "input_files/pos_invoice/address.json"

  def apply(): Unit = {
    addresses = readJsonFile[DeliveryAddressModel](DATAFILE)
  }


}
