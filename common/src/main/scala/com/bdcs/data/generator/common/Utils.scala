package com.bdcs.data.generator.common
import java.io.File
import java.nio.file.{FileSystems, Files, Paths}
import scala.reflect.io.Directory

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

}
