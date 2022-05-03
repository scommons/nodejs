package scommons.nodejs.util

import scommons.nodejs.Buffer
import scommons.nodejs.raw.Readable

import scala.concurrent.{Future, Promise}
import scala.scalajs.js

class StreamReader(val readable: Readable) {

  import scala.concurrent.ExecutionContext.Implicits.global

  private var ready = Promise[Unit]()
  private var isEnd = false

  private val readableListener: js.Function = { () =>
    ready.trySuccess(())
  }
  private val errorListener: js.Function = { error: js.Error =>
    ready.tryFailure(js.JavaScriptException(error))
  }
  private val endListener: js.Function0[Unit] = { () =>
    isEnd = true
    ready.trySuccess(())
    readable.removeListener("readable", readableListener)
    readable.removeListener("error", errorListener)
  }
  readable.on("readable", readableListener)
  readable.on("error", errorListener)
  readable.once("end", endListener)
  readable.once("close", endListener)

  def readNextBytes(size: Int): Future[Option[Buffer]] = {

    def loop(): Future[Option[Buffer]] = {
      ready.future.flatMap { _ =>
        if (isEnd) Future.successful(None)
        else {
          val content = readable.read(size)
          if (content != null) Future.successful(Some(content))
          else {
            ready = Promise[Unit]()
            loop()
          }
        }
      }
    }

    loop()
  }

  def readAllLines(onNextLine: String => Unit): Future[Unit] = {
    var chunks = new js.Array[Buffer](0)

    @annotation.tailrec
    def loopOverBuffer(buf: Buffer): Unit = {
      val newLineIndex = buf.indexOf('\n'.toInt)
      if (newLineIndex < 0) chunks.push(buf)
      else {
        chunks.push(buf.subarray(0, newLineIndex))
        onNextLine(Buffer.concat(chunks).toString)

        chunks = new js.Array[Buffer](0)
        if ((newLineIndex + 1) < buf.length) {
          loopOverBuffer(buf.subarray(newLineIndex + 1, buf.length))
        }
      }
    }
    
    def loop(): Future[Unit] = {
      readNextBytes(16).flatMap {
        case None =>
          if (chunks.length != 0) {
            onNextLine(Buffer.concat(chunks).toString)
          }
          Future.unit
        case Some(buf) =>
          loopOverBuffer(buf)
          loop()
      }
    }
    
    loop()
  }
}
