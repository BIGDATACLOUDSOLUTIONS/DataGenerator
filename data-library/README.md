Lib Schema
```

case class Address(
addressLine: String,
city: String,
state: String,
country: String,
postcode: String
)

case class Store(
storeId: String,
storeName: String,
storeAddress: Address)

case class Customer(customerId: String,
title: String,
firstName: String,
lastName: String,
gender: String,
dob: String,
registrationTimestamp: String,
contactNumber: String,
email: String,
address: Address,
customerType:String,
customerCardNo:String
)

case class Product(
productCode: String,
productDescription: String,
productPrice: Double,
productCategory: String
)


case class OrderLineItem(
orderItemId: String,
product: Product,
quantityOrdered: Int,
totalPrice: Double
)

case class Order(
orderId: String,
orderDate: String,
numberOfItems: Int,
totalOrderAmount: Double,
orderLineItems: Array[OrderLineItem]
)


case class Invoice(
invoiceNumber: String,
createdTime: String,
taxableAmount: Double = 0,
cGST: Double = 0,
sGST: Double = 0,
cESS: Double = 0,
order: Order,
)

case class Payment(
paymentId: String,
paymentDate: String,
posID: String, //POS: Point of Sale Machines
cashierID: String,
paymentMethod: String,
discount: Int,
amountPaid: Double,
invoice: Invoice,
store: Store,
customer: Customer,
deliveryType: String,
deliveryAddress: Address
)

```