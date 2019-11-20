package scommons.nodejs.raw

import scala.scalajs.js

/**
  * https://nodejs.org/docs/latest-v9.x/api/stream.html#stream_class_stream_writable
  */
@js.native
trait Writable extends EventEmitter {

  def end(chunk: js.UndefOr[String] = js.undefined,
          encoding: js.UndefOr[String] = js.undefined,
          callback: js.UndefOr[js.Function1[js.Error, Unit]] = js.undefined): Unit = js.native
  
  def write(chunk: String,
            encoding: js.UndefOr[String] = js.undefined,
            callback: js.UndefOr[js.Function1[js.Error, Unit]] = js.undefined): Boolean = js.native
}
