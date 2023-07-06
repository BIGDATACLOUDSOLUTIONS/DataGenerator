package com.bdcs.data.generator.common

import java.io.File
import java.nio.file.{FileSystems, Files, Paths}
import scala.reflect.io.Directory
import org.json4s.DefaultFormats
import org.json4s._
import org.json4s.jackson.JsonMethods.parse

import scala.io.Source

object Utils {

  val moduleRootDir: String = FileSystems.getDefault.getPath("").toAbsolutePath.toString + "/kafka-avro/"

  def deleteNonEmptyDir(directory: String): Unit = {
    val dir = new Directory(new File(directory))
    dir.deleteRecursively()
  }

  def deleteFileIfExists(filePath: String): Unit = {
    Files.deleteIfExists(Paths.get(filePath))
  }

  def createDirIfNotExists(outputDir: String): Unit = {
    val path = Paths.get(outputDir)
    if (!Files.exists(path)) {
      Files.createDirectories(path)
      println("Directory created: " + outputDir)
    } else {
      println("Directory already exists: " + outputDir)
    }
  }

  def recreateOutputDir(outputDir: String): Unit = {
    val path = Paths.get(outputDir)
    if (Files.exists(path)) deleteNonEmptyDir(outputDir)
    Files.createDirectories(path)
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


}
