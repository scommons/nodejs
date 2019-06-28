package scommons.nodejs

import scommons.nodejs.test.TestSpec

import scala.scalajs.js

class EventEmitterSpec extends TestSpec {

  it should "call listener during each emit when addListener" in {
    //given
    val listenerMock = mockFunction[Unit]
    val listener: js.Function2[js.Object, js.Object, Unit] = { (_, _) =>
      listenerMock()
    }
    val eventEmitter = process.stdin
    
    //then
    listenerMock.expects().twice()
    
    //when
    eventEmitter.addListener("keypress", listener)
    eventEmitter.emit("keypress", js.undefined, js.Dynamic.literal(name = "a"))
    eventEmitter.emit("keypress", js.undefined, js.Dynamic.literal(name = "a"))
    
    //cleanup
    eventEmitter.removeListener("keypress", listener)
  }
  
  it should "not call listener when removeListener" in {
    //given
    val listenerMock = mockFunction[Unit]
    val listener: js.Function2[js.Object, js.Object, Unit] = { (_, _) =>
      listenerMock()
    }
    val eventEmitter = process.stdin
    eventEmitter.addListener("keypress", listener)
    
    //then
    listenerMock.expects().never()
    
    //when
    eventEmitter.removeListener("keypress", listener)
    eventEmitter.emit("keypress", js.undefined, js.Dynamic.literal(name = "a"))
    eventEmitter.emit("keypress", js.undefined, js.Dynamic.literal(name = "a"))
  }
  
  it should "call listener during each emit when on" in {
    //given
    val listenerMock = mockFunction[Unit]
    val listener: js.Function2[js.Object, js.Object, Unit] = { (_, _) =>
      listenerMock()
    }
    val eventEmitter = process.stdin
    
    //then
    listenerMock.expects().twice()
    
    //when
    eventEmitter.on("keypress", listener)
    eventEmitter.emit("keypress", js.undefined, js.Dynamic.literal(name = "a"))
    eventEmitter.emit("keypress", js.undefined, js.Dynamic.literal(name = "a"))
    
    //cleanup
    eventEmitter.removeListener("keypress", listener)
  }
  
  it should "call listener only once when once" in {
    //given
    val listenerMock = mockFunction[Unit]
    val listener: js.Function2[js.Object, js.Object, Unit] = { (_, _) =>
      listenerMock()
    }
    val eventEmitter = process.stdin
    
    //then
    listenerMock.expects().once()
    
    //when
    eventEmitter.once("keypress", listener)
    eventEmitter.emit("keypress", js.undefined, js.Dynamic.literal(name = "a"))
    eventEmitter.emit("keypress", js.undefined, js.Dynamic.literal(name = "a"))
  }
}
