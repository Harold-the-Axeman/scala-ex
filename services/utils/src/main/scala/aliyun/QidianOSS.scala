package com.getgua.utils.aliyun

import java.net.URL
import javax.inject.{Inject, Singleton}

import com.aliyun.oss.OSSClient
import com.aliyun.oss.model.ObjectMetadata
import play.api.Configuration

//import com.aliyun.openservices.oss.model.PutObjectResult

import java.io._

//import org.apache.commons.io.FilenameUtils
//import org.apache.commons.io.FileUtils

import org.apache.commons.codec.digest.DigestUtils
/**
  * Created by likaili on 10/8/2016.
  */

@Singleton
class QidianOSS @Inject() (configuration: Configuration) {
  private final val ACCESS_ID: String = configuration.getString("oss.access_id").get
  private final val ACCESS_SECRET: String = configuration.getString("oss.access_secret").get
  private final val END_POINT: String = configuration.getString("oss.end_point").get
  private final val BUCKET_NAME: String = configuration.getString("oss.bucket_name").get

  private final val CLIENT = new OSSClient(END_POINT, ACCESS_ID, ACCESS_SECRET)

  def putString(content: String) = {

  }

  private def putObject(content: InputStream, key_name: String) = {

    val meta = new ObjectMetadata
    //TODO: content length is not need
    //meta.setContentLength(content.available)
    /**
      * set the content type
      */
    meta.setContentType("image/jpeg")

    // TODO: check if file exists
    //val exists =

    if (CLIENT.doesObjectExist(BUCKET_NAME, key_name) == false) {
      CLIENT.putObject(BUCKET_NAME, key_name, content, meta)
    }

    key_name
  }

  private def getKeyName(content: InputStream) = {
    DigestUtils.md5Hex(content)
  }

  //private def

  def putNetworkObject(url: String, extension: String = ".png") = {
    val content = new URL(url).openStream
    val key_name = getKeyName(content) + extension

    putObject(new URL(url).openStream, key_name)
  }

  def putFileObject(filePath: String, extension: String = ".png") = {
    val content = new FileInputStream(filePath)
    val key_name = getKeyName(content) + extension

    putObject(new FileInputStream(filePath), key_name)
  }

/*  def putLocalFileObject(fileName: String, extension: String = ".png") = {
    val content = new File("fileName")

    putObject
  }*/
}
