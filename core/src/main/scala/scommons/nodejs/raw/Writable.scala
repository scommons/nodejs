package scommons.nodejs.raw

import scala.scalajs.js
import scala.scalajs.js.typedarray.Uint8Array
import scala.scalajs.js.|

/**
  * https://nodejs.org/docs/latest-v12.x/api/stream.html#stream_class_stream_writable
  */
@js.native
trait Writable extends EventEmitter {

  def end(chunk: js.UndefOr[String | Uint8Array] = js.native,
          encoding: js.UndefOr[String] = js.native,
          callback: js.UndefOr[js.Function1[js.Error, Unit]] = js.native): Unit = js.native
  
  def write(chunk: String | Uint8Array,
            encoding: js.UndefOr[String] = js.native,
            callback: js.UndefOr[js.Function1[js.Error, Unit]] = js.native): Boolean = js.native
}
