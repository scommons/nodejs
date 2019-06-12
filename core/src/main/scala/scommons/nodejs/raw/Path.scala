package scommons.nodejs.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/**
  * https://nodejs.org/docs/latest-v9.x/api/path.html
  */
@js.native
@JSImport("path", JSImport.Default)
object Path extends Path

@js.native
trait Path extends js.Object {

  def basename(path: String): String = js.native
  def basename(path: String, ext: String): String = js.native
  
  def dirname(path: String): String = js.native
  
  def extname(path: String): String = js.native
  
  def format(pathObject: PathObject): String = js.native
  
  def isAbsolute(path: String): Boolean = js.native
  
  def join(paths: String*): String = js.native
  
  def normalize(path: String): String = js.native
  
  def parse(path: String): PathObject = js.native
  
  def relative(from: String, to: String): String = js.native
  
  def resolve(paths: String*): String = js.native
  
  val delimiter: String = js.native
  val sep: String = js.native
  
  val win32: Path = js.native //when working with Windows file paths on any operating system
  val posix: Path = js.native //when working with POSIX file paths on any operating system
}

/**
  * When providing properties to the pathObject remember that there are combinations
  * where one property has priority over another:
  * 
  *   - `pathObject.root` is ignored if `pathObject.dir` is provided
  *   - `pathObject.ext` and `pathObject.name` are ignored if `pathObject.base` exists
  */
trait PathObject extends js.Object {

  val dir: js.UndefOr[String] = js.undefined
  val root: js.UndefOr[String] = js.undefined
  val base: js.UndefOr[String] = js.undefined
  val name: js.UndefOr[String] = js.undefined
  val ext: js.UndefOr[String] = js.undefined
}
