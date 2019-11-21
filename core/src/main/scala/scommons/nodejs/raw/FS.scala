package scommons.nodejs.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

/**
  * https://nodejs.org/docs/latest-v9.x/api/fs.html
  */
@js.native
@JSImport("fs", JSImport.Default)
object FS extends js.Object {

  def readdir(path: String | URL, callback: js.Function2[js.Error, js.Array[String], Unit]): Unit = js.native
  
  def lstatSync(path: String | URL): Stats = js.native

  def existsSync(path: String | URL): Boolean = js.native
  
  def unlinkSync(path: String | URL): Unit = js.native
  
  def rmdirSync(path: String | URL): Unit = js.native
  
  def mkdirSync(path: String | URL, mode: js.UndefOr[Int]): String = js.native
  
  def mkdtempSync(prefix: String): String = js.native

  def writeFileSync(file: String | URL,
                    data: String,
                    options: js.UndefOr[WriteFileOptions]): Unit = js.native
  
  def createWriteStream(path: String | URL,
                        options: js.UndefOr[CreateWriteStreamOptions]): WriteStream = js.native
}

/**
  * https://nodejs.org/docs/latest-v9.x/api/fs.html#fs_class_fs_stats
  */
@js.native
trait Stats extends js.Object {

  def isDirectory(): Boolean = js.native
  def isFile(): Boolean = js.native
  def isSymbolicLink(): Boolean = js.native

  val mode: Int = js.native
  val size: Double = js.native //The size of the file in bytes.

  val atimeMs: Double = js.native //the last time file data was accessed, in milliseconds
  val mtimeMs: Double = js.native //the last time file data was modified, in milliseconds
  val ctimeMs: Double = js.native //the last time file status was changed, in milliseconds
  val birthtimeMs: Double = js.native //the creation time of file, in milliseconds
}

trait WriteFileOptions extends js.Object {

  val encoding: js.UndefOr[String] = js.undefined
  val mode: js.UndefOr[Int] = js.undefined
  val flag: js.UndefOr[String] = js.undefined
}
