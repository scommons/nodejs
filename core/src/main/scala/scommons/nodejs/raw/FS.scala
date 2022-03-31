package scommons.nodejs.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.typedarray.Uint8Array
import scala.scalajs.js.|

/**
  * https://nodejs.org/dist/latest-v12.x/docs/api/fs.html
  */
@js.native
@JSImport("fs", JSImport.Default)
object FS extends js.Object {

  def readdir(path: String | URL, callback: js.Function2[js.Error, js.Array[String], Unit]): Unit = js.native
  
  def rename(oldPath: String | URL, newPath: String | URL, callback: js.Function1[js.Error, Unit]): Unit = js.native

  def ftruncate(fd: Int, len: Double, callback: js.Function1[js.Error, Unit]): Unit = js.native

  def openSync(path: String | URL, flags: Int): Int = js.native

  def closeSync(fd: Int): Unit = js.native

  def lstatSync(path: String | URL): Stats = js.native

  def existsSync(path: String | URL): Boolean = js.native
  
  def unlinkSync(path: String | URL): Unit = js.native
  
  def rmdirSync(path: String | URL): Unit = js.native
  
  def mkdirSync(path: String | URL, mode: js.UndefOr[Int]): String = js.native
  
  def mkdtempSync(prefix: String): String = js.native

  def futimesSync(fd: Int, atime: Double, mtime: Double): Unit = js.native

  def read(fd: Int,
           buffer: Uint8Array,
           offset: Int,
           length: Int,
           position: js.UndefOr[Double],
           callback: js.Function3[js.Error, Int, Uint8Array, Unit]): Unit = js.native

  def write(fd: Int,
            buffer: Uint8Array,
            offset: Int,
            length: Int,
            position: js.UndefOr[Double],
            callback: js.Function3[js.Error, Int, Uint8Array, Unit]): Unit = js.native

  def readFileSync(file: String | URL,
                   options: js.UndefOr[FileOptions]): String = js.native
  
  def writeFileSync(file: String | URL,
                    data: String,
                    options: js.UndefOr[FileOptions]): Unit = js.native
  
  def createReadStream(path: String | URL,
                       options: js.UndefOr[String | CreateReadStreamOptions] = js.native): ReadStream = js.native
  
  def createWriteStream(path: String | URL,
                        options: js.UndefOr[String | CreateWriteStreamOptions] = js.native): WriteStream = js.native
}

/**
  * https://nodejs.org/dist/latest-v12.x/docs/api/fs.html#fs_class_fs_stats
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

trait FileOptions extends js.Object {

  val encoding: js.UndefOr[String] = js.undefined
  val mode: js.UndefOr[Int] = js.undefined
  val flag: js.UndefOr[String] = js.undefined
}
