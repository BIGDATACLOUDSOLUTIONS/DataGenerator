package com.bdcs.data.generator.products

import com.bdcs.data.generator.avro.products.ProductsAvro
import com.bdcs.data.generator.lib.product.ProductModel

import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

object ProductsPayload {

  def getProductsJsonPayload(product: ProductModel): String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    write(product)
  }

  def getProductsAvroPayload(product: ProductModel): ProductsAvro = {

    ProductsAvro.newBuilder
      .setProductId(product.product_id)
      .setProductName(product.product_name)
      .setUnitPrice(product.unit_price)
      .setCategory(product.category)
      .build
  }
}

