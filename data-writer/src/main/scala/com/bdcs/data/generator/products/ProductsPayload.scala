package com.bdcs.data.generator.products

import com.bdcs.data.generator.avro.payment.ProductAvro
import com.bdcs.data.generator.lib.product.Product

import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

object ProductsPayload {

  def getProductsJsonPayload(product: Product): String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    write(product)
  }

  def getProductsAvroPayload(product: Product): ProductAvro = {

    ProductAvro.newBuilder
      .setProductCode(product.productCode)
      .setProductDescription(product.productDescription)
      .setProductPrice(product.productPrice)
      .setProductCategory(product.productCategory)
      .build
  }

}