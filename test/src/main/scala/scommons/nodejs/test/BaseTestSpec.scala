package scommons.nodejs.test

import org.scalactic.source.Position
import org.scalatest._

import scala.scalajs.js

trait BaseTestSpec extends Matchers
  with Inside {

  def assertObject(result: js.Any, expected: js.Any)(implicit pos: Position): Assertion = {
    assertObject("object", result, expected.asInstanceOf[js.Object with js.Dynamic])
  }
  
  private def assertObject(name: String,
                           result: js.Any,
                           expected: js.Object with js.Dynamic
                          )(implicit pos: Position): Assertion = {

    assertValue(name, js.typeOf(result), "object")

    if (result != expected) {
      val resultObject = result.asInstanceOf[js.Object with js.Dynamic]
      val resultKeys = js.Object.keys(resultObject).toSet
      val expectedKeys = js.Object.keys(expected).toSet
      assertValue(s"$name.keys", resultKeys, expectedKeys)

      for (key <- expectedKeys) {
        val resultValue = resultObject.selectDynamic(key)
        val expectedValue = expected.selectDynamic(key)
        if (js.Array.isArray(expectedValue))
          assertArray(s"$name.$key", resultValue, expectedValue.asInstanceOf[js.Array[_]])
        else if (js.typeOf(expectedValue) == "object")
          assertObject(s"$name.$key", resultValue, expectedValue.asInstanceOf[js.Object with js.Dynamic])
        else
          assertValue(s"$name.$key", resultValue, expectedValue)
      }
    }

    Succeeded
  }

  private def assertArray(name: String,
                          result: js.Any,
                          expected: js.Array[_])(implicit pos: Position): Unit = {

    assertValue(s"isArray($name)", js.Array.isArray(result), true)

    if (result != expected) {
      val resultList = result.asInstanceOf[js.Array[_]].toList
      val expectedList = expected.toList
      assertValue(name, resultList, expectedList)
    }
  }

  private def assertValue(name: String,
                          resultValue: Any,
                          expectedValue: Any)(implicit pos: Position): Unit = {

    if (resultValue != expectedValue) {
      fail(s"Value doesn't match for $name" +
        s"\n\texpected: $expectedValue" +
        s"\n\tactual:   $resultValue")
    }
  }
}
