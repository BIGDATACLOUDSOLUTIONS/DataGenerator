package com.bdcs.data.generator.orders

import com.bdcs.data.generator.customers.CustomerWriter
import com.bdcs.data.generator.stores.StoresWriter
import com.bdcs.data.generator.products.ProductsWriter
import com.bdcs.data.generator.common.ConfigureParameters._

import com.bdcs.data.generator.lib.orders.Orders

object OrdersWriter {

  def apply(): Unit = {
    val numberOfOrders = getNoOfMessageToPublish.toInt
    val dataFormat: String = getDataFormat
    val targetType = getTarget

    writePrerequisitesData(
      numberOfOrders,
      dataFormat,
      targetType)

    Orders(numberOfOrders)

    OrdersDetailWriter(Orders.ordersDetailsRecords)
    OrdersHeaderWriter(Orders.ordersHeadersRecords)

  }

  def writePrerequisitesData(numberOfOrders: Int,
                                     dataFormat: String,
                                     targetType: String,
                                     printMessagesOnConsole: Boolean = false
                                    ): Unit = {

    val writeToFileAndKafka = if (targetType.equals("file")) false else true

    println("*************** Writing Products, Stores And Customers Data: STARTED ****************")
    ProductsWriter.productsWriter(numberOfOrders, dataFormat, targetType, printMessagesOnConsole, writeToFileAndKafka)
    StoresWriter.storesWriter(numberOfOrders, dataFormat, targetType, printMessagesOnConsole, writeToFileAndKafka)
    CustomerWriter.customerWriter(numberOfOrders, dataFormat, targetType, printMessagesOnConsole, writeToFileAndKafka)
    println("*************** Writing Products, Stores And Customers Data: COMPLETED ****************")

  }

}
