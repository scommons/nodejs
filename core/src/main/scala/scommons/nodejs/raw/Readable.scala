package scommons.nodejs.raw

import scommons.nodejs.Buffer

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/**
 * https://nodejs.org/docs/latest-v12.x/api/stream.html#stream_class_stream_readable
 */
@js.native
@JSImport("stream", "Readable")
object Readable extends js.Object {
  
  def from[T](iterable: js.Iterable[T],
              options: ReadableOptions = js.native): Readable = js.native
}

@js.native
trait Readable extends EventEmitter {

  def pause(): Readable = js.native
  def resume(): Readable = js.native

  def read(size: js.UndefOr[Int] = js.native): Buffer = js.native

  def destroy(error: js.UndefOr[js.Error] = js.native): Readable = js.native
}

trait ReadableOptions extends js.Object {
  
  val highWaterMark: js.UndefOr[Int] = js.undefined
  val encoding: js.UndefOr[String] = js.undefined
  val objectMode: js.UndefOr[Boolean] = js.undefined
  val emitClose: js.UndefOr[Boolean] = js.undefined
  val read: js.UndefOr[js.Function1[Int, Unit]] = js.undefined
  val destroy: js.UndefOr[
    js.Function2[js.UndefOr[js.Error],
      js.Function1[js.UndefOr[js.Error], Unit], Unit]
  ] = js.undefined
  val autoDestroy: js.UndefOr[Boolean] = js.undefined
}
