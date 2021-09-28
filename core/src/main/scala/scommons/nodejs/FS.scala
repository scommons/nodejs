package scommons.nodejs

import scommons.nodejs.raw._

import scala.concurrent.{Future, Promise}
import scala.scalajs.js
import scala.scalajs.js.typedarray.Uint8Array

trait FS {

  def readdir(path: String): Future[Seq[String]] = {
    val p = Promise[Seq[String]]()
    raw.FS.readdir(path, { (error, files) =>
      if (error != null && !js.isUndefined(error)) p.failure(js.JavaScriptException(error))
      else p.success(files.toSeq)
    })
    p.future
  }

  def rename(oldPath: String, newPath: String): Future[Unit] = {
    val p = Promise[Unit]()
    raw.FS.rename(oldPath, newPath, { error =>
      if (error != null && !js.isUndefined(error)) p.failure(js.JavaScriptException(error))
      else p.success(())
    })
    p.future
  }

  def openSync(path: String, flags: Int): Int = raw.FS.openSync(path, flags)
  
  def closeSync(fd: Int): Unit = raw.FS.closeSync(fd)
  
  def lstatSync(path: String): Stats = raw.FS.lstatSync(path)

  def existsSync(path: String): Boolean = raw.FS.existsSync(path)
  
  def unlinkSync(path: String): Unit = raw.FS.unlinkSync(path)
  
  def rmdirSync(path: String): Unit = raw.FS.rmdirSync(path)
  
  def mkdirSync(path: String, mode: js.UndefOr[Int] = js.undefined): Unit = {
    raw.FS.mkdirSync(path, mode)
  }
  
  def mkdtempSync(prefix: String): String = raw.FS.mkdtempSync(prefix)

  def futimesSync(fd: Int, atimeSec: Double, mtimeSec: Double): Unit = {
    raw.FS.futimesSync(fd, atimeSec, mtimeSec)
  }

  def read(fd: Int,
           buffer: Uint8Array,
           offset: Int,
           length: Int,
           position: js.UndefOr[Double] = js.undefined): Future[Int] = {
    
    val p = Promise[Int]()
    raw.FS.read(fd, buffer, offset, length, position, { (error, bytesRead, _) =>
      if (error != null && !js.isUndefined(error)) p.failure(js.JavaScriptException(error))
      else p.success(bytesRead)
    })
    p.future
  }

  def write(fd: Int,
            buffer: Uint8Array,
            offset: Int,
            length: Int,
            position: js.UndefOr[Double] = js.undefined): Future[Int] = {
    
    val p = Promise[Int]()
    raw.FS.write(fd, buffer, offset, length, position, { (error, bytesWritten, _) =>
      if (error != null && !js.isUndefined(error)) p.failure(js.JavaScriptException(error))
      else p.success(bytesWritten)
    })
    p.future
  }

  def readFileSync(file: String, options: js.UndefOr[FileOptions] = js.undefined): String = {
    raw.FS.readFileSync(file, options)
  }
  
  def writeFileSync(file: String,
                    data: String,
                    options: js.UndefOr[FileOptions] = js.undefined): Unit = {

    raw.FS.writeFileSync(file, data, options)
  }
  
  def createWriteStream(path: String,
                        options: js.UndefOr[CreateWriteStreamOptions] = js.undefined): WriteStream = {
    
    raw.FS.createWriteStream(path, options)
  }
}
