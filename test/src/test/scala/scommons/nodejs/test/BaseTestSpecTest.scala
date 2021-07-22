package scommons.nodejs.test

import org.scalatest.{Failed, OutcomeOf}

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal

class BaseTestSpecTest extends TestSpec
  with OutcomeOf {

  it should "fail if not an object when assertObject" in {
    //when
    val e = inside(outcomeOf {
      assertObject(1.23, "1.23")
    }) {
      case Failed(e) => e
    }
    
    //then
    e.getMessage shouldBe {
      "Value doesn't match for object" +
        "\n\texpected: object" +
        "\n\tactual:   number"
    }
  }
  
  it should "fail if keys doesn't match when assertObject" in {
    //when
    val e = inside(outcomeOf {
      assertObject(
        literal(key1 = "1", key2 = "2"),
        literal(key1 = "1", key3 = "2")
      )
    }) {
      case Failed(e) => e
    }
    
    //then
    e.getMessage shouldBe {
      "Value doesn't match for object.keys" +
        "\n\texpected: Set(key1, key3)" +
        "\n\tactual:   Set(key1, key2)"
    }
  }
  
  it should "fail if nested object doesn't match when assertObject" in {
    //when
    val e = inside(outcomeOf {
      assertObject(
        literal(key1 = "1", nestedObj = literal(key2 = "2")),
        literal(key1 = "1", nestedObj = literal(key2 = "3"))
      )
    }) {
      case Failed(e) => e
    }
    
    //then
    e.getMessage shouldBe {
      "Value doesn't match for object.nestedObj.key2" +
        "\n\texpected: 3" +
        "\n\tactual:   2"
    }
  }
  
  it should "fail if not a nested array when assertObject" in {
    //when
    val e = inside(outcomeOf {
      assertObject(
        literal(key1 = "1", nestedArr = "1,3"),
        literal(key1 = "1", nestedArr = js.Array("1", "3"))
      )
    }) {
      case Failed(e) => e
    }
    
    //then
    e.getMessage shouldBe {
      "Value doesn't match for isArray(object.nestedArr)" +
        "\n\texpected: true" +
        "\n\tactual:   false"
    }
  }
  
  it should "fail if nested array doesn't match when assertObject" in {
    //when
    val e = inside(outcomeOf {
      assertObject(
        literal(key1 = "1", nestedArr = js.Array("1", "2")),
        literal(key1 = "1", nestedArr = js.Array("1", "3"))
      )
    }) {
      case Failed(e) => e
    }
    
    //then
    e.getMessage shouldBe {
      "Value doesn't match for object.nestedArr" +
        "\n\texpected: List(1, 3)" +
        "\n\tactual:   List(1, 2)"
    }
  }
  
  it should "not fail if match when assertObject" in {
    //when & then
    assertObject(
      literal(key1 = "1", nestedObj = literal(key2 = "2", nestedArr = js.Array("1", "2"))),
      literal(key1 = "1", nestedObj = literal(key2 = "2", nestedArr = js.Array("1", "2")))
    )
  }
}
