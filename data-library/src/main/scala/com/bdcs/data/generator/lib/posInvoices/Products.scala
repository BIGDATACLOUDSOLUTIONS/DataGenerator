package com.bdcs.data.generator.lib
package posInvoices

import com.bdcs.data.generator.common.Utils.readJsonFile
import com.bdcs.data.generator.lib.posInvoices.models.LineItemModel


object Products {

  var products: Array[LineItemModel] = Array[LineItemModel]()

  private val DATAFILE: String = "input_files/pos_invoice/product.json"

  def apply(): Unit =
    products = readJsonFile[LineItemModel](DATAFILE)

}

