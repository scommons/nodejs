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
}
