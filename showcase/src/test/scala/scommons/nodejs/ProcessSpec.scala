package scommons.nodejs

import scommons.nodejs.test.TestSpec

import scala.scalajs.js

class ProcessSpec extends TestSpec {

  it should "return current working directory" in {
    //when & then
    process.cwd() should not be empty
  }
  
  it should "change current working directory" in {
    //given
    val currDir = process.cwd()
    currDir should not be os.homedir()
    
    //when
    process.chdir(os.homedir())
    
    //then
    process.cwd() shouldBe os.homedir()
    
    //cleanup
    process.chdir(currDir)
  }
  
  it should "return eventEmitter interface when stdin" in {
    //when
    val eventEmitter = process.stdin
    
    //then
    var expectedKey: js.Dynamic = null
    eventEmitter.once("keypress", { (_, key) =>
      expectedKey = key
    }: js.Function2[js.Object, js.Dynamic, Unit])
    
    eventEmitter.emit("keypress", js.undefined, js.Dynamic.literal(name = "a"))
    expectedKey.name shouldBe "a"
  }
}
