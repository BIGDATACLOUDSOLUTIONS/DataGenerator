package com.bdcs.data.generator.products

import com.bdcs.data.generator.lib.product.Product
import com.bdcs.data.generator.products.ProductsPayload._

import com.bdcs.data.generator.common.AppConfig._
import com.bdcs.data.generator.common.FileUtils._

object ProductsWriter {

  private val productOutputFilePath: String = "generated-files/product.json"

  def apply(numberOfProducts: Int = getNoOfMessageToPublish): Unit = {
    Product(numberOfProducts)
    val products: Array[Product] = Product.products
    productsWriter(products)
  }

  def productsWriter(products: Array[Product]): Unit = {

    println(s"Products File Path: $productOutputFilePath")
    createDirIfNotExists(productOutputFilePath)
    deleteFileIfExists(productOutputFilePath)

    writeJsonToFile[Product](
      products, productOutputFilePath, getProductsJsonPayload)
    //if (printOnConsole) getJsonStringRecordsFromJsonFile(productOutputFilePath).foreach(println)
  }

}
