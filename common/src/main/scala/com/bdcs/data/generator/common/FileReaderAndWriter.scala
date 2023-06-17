package com.bdcs.data.generator.common

import com.bdcs.data.generator.common.Utils._

import scala.io.Source
import java.io.FileWriter
import org.apache.avro.file.{DataFileReader, DataFileWriter}
import org.apache.avro.specific.SpecificDatumReader
import org.apache.avro.specific.SpecificRecord

import java.io.{File, IOException}
import scala.collection.mutable.ListBuffer

object FileReaderAndWriter {

  def writeJsonToFile[T](arrayOfPayload: Array[T],
                         jsonFilePath: String,
                         getJsonPayload: T => String): Unit = {

    createDirIfNotExists(jsonFilePath)
    deleteFileIfExists(jsonFilePath)

    val writer = new FileWriter(jsonFilePath)
    try {
      arrayOfPayload.foreach(record =>
        writer.write(getJsonPayload(record) + "\n"))
    } finally {
      writer.close()
    }
  }

  def getJsonStringRecordsFromJsonFile(jsonFilePath: String): Array[String] = {
    println(s"Reading json record from file: $jsonFilePath")
    val source = Source.fromFile(jsonFilePath)
    val allRecords = source.getLines
    allRecords.toArray
  }


  def writeAvroToFile[T <: SpecificRecord, V](arrayOfPayload: Array[V],
                                              getAvroPayload: V => T)
                                             (implicit writer: DataFileWriter[T]): Unit = {

    arrayOfPayload.foreach(record => writer.append(getAvroPayload(record)))
    writer.close()
  }


  def getJsonStringRecordsFromAvroFile[T <: SpecificRecord](avroFilePath: String)
                                       (implicit datumReader: SpecificDatumReader[T]
                                        ): Array[String] = {
    val allRecords = ListBuffer[String]()
    try {
      println(s"Reading avro record from file: $avroFilePath")
      val dataFileReader = new DataFileReader[T](new File(avroFilePath), datumReader)
      while (dataFileReader.hasNext) {
        val readRecords: T = dataFileReader.next
        allRecords += readRecords.toString
      }
      allRecords.toArray
    } catch {
      case e: IOException =>
        e.printStackTrace()
        throw new Exception(s"Error While Reading Avro File: $avroFilePath")
    }
  }

}