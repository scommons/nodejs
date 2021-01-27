package scommons.nodejs

import scommons.nodejs.raw._

import scala.concurrent.{Future, Promise}
import scala.scalajs.js

trait FS {

  def readdir(path: String): Future[Seq[String]] = {
    val p = Promise[Seq[String]]()
    raw.FS.readdir(path, { (error, files) =>
      if (error != null && !js.isUndefined(error)) p.failure(js.JavaScriptException(error))
      else p.success(files)
    })
    p.future
  }

  def lstatSync(path: String): Stats = raw.FS.lstatSync(path)

  def existsSync(path: String): Boolean = raw.FS.existsSync(path)
  
  def unlinkSync(path: String): Unit = raw.FS.unlinkSync(path)
  
  def rmdirSync(path: String): Unit = raw.FS.rmdirSync(path)
  
  def mkdirSync(path: String, mode: js.UndefOr[Int] = js.undefined): Unit = {
    raw.FS.mkdirSync(path, mode)
  }
  
  def mkdtempSync(prefix: String): String = raw.FS.mkdtempSync(prefix)

  def writeFileSync(file: String,
                    data: String,
                    options: js.UndefOr[WriteFileOptions] = js.undefined): Unit = {

    raw.FS.writeFileSync(file, data, options)
  }
  
  def createWriteStream(path: String,
                        options: js.UndefOr[CreateWriteStreamOptions] = js.undefined): WriteStream = {
    
    raw.FS.createWriteStream(path, options)
  }
}
