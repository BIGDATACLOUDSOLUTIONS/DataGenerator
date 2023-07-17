package com.bdcs.data.generator.common

import java.nio.file.{FileSystems, Files, Paths}
import org.json4s.DefaultFormats
import org.json4s._
import org.json4s.jackson.JsonMethods.parse

import scala.io.Source
import java.io.FileWriter

object FileUtils {

  val moduleRootDir: String = FileSystems.getDefault.getPath("").toAbsolutePath.toString

  def deleteFileIfExists(filePath: String): Unit = {
    Files.deleteIfExists(Paths.get(filePath))
  }

  def createDirIfNotExists(outputDir: String): Unit = {
    val path = Paths.get(outputDir)
    if (!Files.exists(path)) {
      Files.createDirectories(path)
      //println("Directory created: " + outputDir)
    } else {
      //println("Directory already exists: " + outputDir)
      println()
    }
  }

  def readJsonFile[T: Manifest](filePath: String): Array[T] = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    val source = Source.fromFile(filePath)
    val result: Array[T] = source.getLines.map(line => {
      val jsValue = parse(s"""$line""")
      jsValue.extract[T]
    }).toArray
    result
  }

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

}
