package com.bdcs.data.generator.lib.posInvoices
package models

case class InvoiceModel (
  InvoiceNumber: String,
  CreatedTime: Long,
  StoreID: String,
  PosID: String,
  CashierID: String,
  CustomerType: String,
  CustomerCardNo: String,
  TotalAmount:Double=0,
  NumberOfItems:Int=0,
  PaymentMethod: String,
  TaxableAmount:Double=0,
  CGST:Double=0,
  SGST:Double=0,
  CESS:Double=0,
  DeliveryType: String
)