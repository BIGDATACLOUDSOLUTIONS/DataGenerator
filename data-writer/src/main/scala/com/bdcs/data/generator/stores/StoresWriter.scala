package com.bdcs.data.generator.stores

import com.bdcs.data.generator.lib.store.Store
import com.bdcs.data.generator.stores.StoresPayload._

import com.bdcs.data.generator.common.AppConfig._
import com.bdcs.data.generator.common.FileUtils._

object StoresWriter {

  private val storesOutputFilePath: String = s"generated-files/store.json"

  def apply(numberOfStores: Int = getNoOfMessageToPublish): Unit = {
    Store(numberOfStores)
    val stores: Array[Store] = Store.stores
    storesWriter(stores)
  }

  def storesWriter(stores: Array[Store]): Unit = {

    println(s"Store File Path: $storesOutputFilePath")
    createDirIfNotExists(storesOutputFilePath)
    deleteFileIfExists(storesOutputFilePath)

    writeJsonToFile[Store](
      stores, storesOutputFilePath, getStoresJsonPayload)
    //if (printOnConsole) getJsonStringRecordsFromJsonFile(storesOutputFilePath).foreach(println)
  }
}
