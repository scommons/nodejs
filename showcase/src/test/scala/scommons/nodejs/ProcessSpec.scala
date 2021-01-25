package scommons.nodejs

import scommons.nodejs.Process._
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

  it should "call native exit when process.exit" in {
    //given
    val nativeProcess = raw.NodeJs.process.asInstanceOf[js.Dynamic]
    val nativeExit = nativeProcess.exit
    val exitMock = mockFunction[Int, Unit]
    nativeProcess.exit = exitMock
    val code = 123
    
    //then
    exitMock.expects(code)

    //when
    process.exit(code)

    //cleanup
    nativeProcess.exit = nativeExit
  }

  it should "return current platform" in {
    //when & then
    process.platform.asInstanceOf[String] should not be empty
  }
  
  it should "provide Platform enum" in {
    //when & then
    Platform.aix shouldBe "aix"
    Platform.darwin shouldBe "darwin"
    Platform.freebsd shouldBe "freebsd"
    Platform.linux shouldBe "linux"
    Platform.openbsd shouldBe "openbsd"
    Platform.sunos shouldBe "sunos"
    Platform.win32 shouldBe "win32"
  }
}
