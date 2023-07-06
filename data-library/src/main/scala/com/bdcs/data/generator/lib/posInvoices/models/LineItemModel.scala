package com.bdcs.data.generator.lib.posInvoices
package models

case class LineItemModel(
                          ItemCode:Integer,
                          ItemDescription:String,
                          ItemPrice:String,
                          ItemQty: Integer
                        )