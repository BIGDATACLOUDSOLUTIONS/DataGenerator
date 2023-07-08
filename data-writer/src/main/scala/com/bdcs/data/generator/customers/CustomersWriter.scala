package com.bdcs.data.generator.customers

import com.bdcs.data.generator.lib.customer.Customer
import com.bdcs.data.generator.avro.payment.CustomerAvro
import com.bdcs.data.generator.customers.CustomerPayload._
import com.bdcs.data.generator.common.AppConfig._
import com.bdcs.data.generator.common.FileUtils._
import org.apache.avro.file.{DataFileReader, DataFileWriter}
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}

import java.io.{File, IOException}

object CustomersWriter {

  private val customerOutputFilePath: String = s"generated-files/customer.avro"

  def apply(numberOfCustomers: Int = getNoOfMessageToPublish): Unit = {
    Customer(numberOfCustomers)
    val customers = Customer.customers
    customerWriter(customers)
  }

  private def writeCustomerToAvroFile(customers: Array[Customer]): Unit = {

    val datumWriter = new SpecificDatumWriter[CustomerAvro](classOf[CustomerAvro])
    val dataFileWriter: DataFileWriter[CustomerAvro] = new DataFileWriter[CustomerAvro](datumWriter)
    val writer: DataFileWriter[CustomerAvro] = dataFileWriter.create(new CustomerAvro().getSchema, new File(customerOutputFilePath))

    customers.foreach(record => writer.append(getCustomerAvroPayload(record)))
    writer.close()
  }

  private def readCustomerFromAvroFile(): Unit = {
    val datumReader: SpecificDatumReader[CustomerAvro] = new SpecificDatumReader[CustomerAvro](classOf[CustomerAvro])
    try {
      val dataFileReader = new DataFileReader[CustomerAvro](new File(customerOutputFilePath), datumReader)
      while (dataFileReader.hasNext) {
        val readRecords: CustomerAvro = dataFileReader.next
        println(readRecords.toString)
      }
    } catch {
      case e: IOException =>
        e.printStackTrace()
        throw new Exception(s"Error While Reading Avro File: $customerOutputFilePath")
    }
  }

  def customerWriter(customers: Array[Customer]): Unit = {

    println(s"Customers File Path: $customerOutputFilePath")
    createDirIfNotExists(customerOutputFilePath)
    deleteFileIfExists(customerOutputFilePath)

    writeCustomerToAvroFile(customers)
    //if (printOnConsole) readCustomerFromAvroFile()
  }

}
