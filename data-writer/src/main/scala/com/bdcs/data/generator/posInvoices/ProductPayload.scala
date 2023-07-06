package com.bdcs.data.generator.posInvoices

import com.bdcs.data.generator.json.posInvoices.LineItemJson
import com.bdcs.data.generator.lib.posInvoices.Products

import java.util.Random


object ProductPayload {

  private val random: Random = new Random()
  private val qty: Random = new Random()

  private def getIndex: Int = random.nextInt(100)

  private def getQuantity: Int = qty.nextInt(2) + 1

  def apply(): Unit = {
    Products()
  }


  def getNextJsonProduct: LineItemJson = {
    val product = Products.products(getIndex)

    val lineItem: LineItemJson = new LineItemJson()
    lineItem.setItemCode(product.ItemCode.toString)
    lineItem.setItemDescription(product.ItemDescription)
    lineItem.setItemPrice(product.ItemPrice.toLong)
    lineItem.setItemQty(getQuantity)
    lineItem.setTotalValue(lineItem.getItemPrice * lineItem.getItemQty)
    lineItem
  }

}
