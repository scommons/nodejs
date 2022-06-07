package scommons.nodejs.util

import scommons.nodejs._
import scommons.nodejs.stream.Readable
import scommons.nodejs.test.AsyncTestSpec

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal

class SubProcessSpec extends AsyncTestSpec {

  it should "return successful Future when wrap" in {
    //given
    val expectedContent = "test content"
    val stdoutStream = Readable.from(Buffer.from(expectedContent))
    val onceMock = mockFunction[String, js.Function, raw.EventEmitter]
    val childProcess = literal(
      "stdout" -> stdoutStream,
      "stderr" -> Readable.from(Buffer.from("")),
      "once" -> onceMock
    ).asInstanceOf[raw.ChildProcess]

    var exitCallback: js.Function1[Int, js.Any] = null
    onceMock.expects("exit", *).onCall { (_, callback) =>
      exitCallback = callback.asInstanceOf[js.Function1[Int, js.Any]]
      childProcess
    }
    onceMock.expects("error", *)
    
    //when
    val resultF = SubProcess.wrap(childProcess)
    
    //then
    (for {
      result <- resultF
      output <- loop(result.stdout, "")
      _ = exitCallback(0)
      _ <- result.exitF
    } yield {
      result.child shouldBe childProcess
      result.stdout.readable shouldBe stdoutStream
      output shouldBe expectedContent
    })
  }

  it should "return failed Future if error when wrap" in {
    //given
    val stdoutStream = Readable.from(Buffer.from("test output"))
    val onceMock = mockFunction[String, js.Function, raw.EventEmitter]
    val childProcess = literal(
      "stdout" -> stdoutStream,
      "stderr" -> Readable.from(Buffer.from("")),
      "once" -> onceMock
    ).asInstanceOf[raw.ChildProcess]

    var exitCallback: js.Function1[Int, js.Any] = null
    onceMock.expects("exit", *).onCall { (_, callback) =>
      exitCallback = callback.asInstanceOf[js.Function1[Int, js.Any]]
      childProcess
    }
    onceMock.expects("error", *).onCall { (_, callback) =>
      val errorCallback = callback.asInstanceOf[js.Function1[js.Error, js.Any]]
      global.setImmediate({ () =>
        errorCallback(js.Error("test error"))
      })
      childProcess
    }
    
    //when
    val resultF = SubProcess.wrap(childProcess)
    
    //then
    (for {
      result <- resultF.failed
      _ = stdoutStream.destroy()
      _ = exitCallback(0)
    } yield {
      inside(result) { case js.JavaScriptException(error: js.Error) => 
        error.message shouldBe "test error"
      }
    })
  }

  it should "return failed exit Future with stderr when wrap" in {
    //given
    val expectedOutput = "test content"
    val expectedError = "test error"
    val stdoutStream = Readable.from(Buffer.from(expectedOutput))
    val stderrStream = Readable.from(Buffer.from(expectedError))
    val onceMock = mockFunction[String, js.Function, raw.EventEmitter]
    val childProcess = literal(
      "stdout" -> stdoutStream,
      "stderr" -> stderrStream,
      "once" -> onceMock
    ).asInstanceOf[raw.ChildProcess]

    var exitCallback: js.Function1[Int, js.Any] = null
    onceMock.expects("exit", *).onCall { (_, callback) =>
      exitCallback = callback.asInstanceOf[js.Function1[Int, js.Any]]
      childProcess
    }
    onceMock.expects("error", *)

    //when
    val resultF = SubProcess.wrap(childProcess)

    //then
    (for {
      result <- resultF
      output <- loop(result.stdout, "")
      _ = exitCallback(1)
      exitError <- result.exitF.failed
    } yield {
      inside(exitError) { case js.JavaScriptException(error: js.Error) =>
        error.message shouldBe expectedError
      }
      result.child shouldBe childProcess
      result.stdout.readable shouldBe stdoutStream
      output shouldBe expectedOutput
    })
  }

  it should "return failed exit Future with generic error when wrap" in {
    //given
    val expectedOutput = "test content"
    val stdoutStream = Readable.from(Buffer.from(expectedOutput))
    val stderrStream = Readable.from(new js.Array[String](0))
    val onceMock = mockFunction[String, js.Function, raw.EventEmitter]
    val childProcess = literal(
      "spawnargs" -> js.Array("app", "arg1", "arg2"),
      "stdout" -> stdoutStream,
      "stderr" -> stderrStream,
      "once" -> onceMock
    ).asInstanceOf[raw.ChildProcess]

    var exitCallback: js.Function1[Int, js.Any] = null
    onceMock.expects("exit", *).onCall { (_, callback) =>
      exitCallback = callback.asInstanceOf[js.Function1[Int, js.Any]]
      childProcess
    }
    onceMock.expects("error", *)

    //when
    val resultF = SubProcess.wrap(childProcess)

    //then
    (for {
      result <- resultF
      output <- loop(result.stdout, "")
      _ = exitCallback(1)
      exitError <- result.exitF.failed
    } yield {
      inside(exitError) { case js.JavaScriptException(error: js.Error) =>
        error.message shouldBe {
          """sub-process exited with code=1
            |command:
            |app arg1 arg2""".stripMargin
        }
      }
      result.child shouldBe childProcess
      result.stdout.readable shouldBe stdoutStream
      output shouldBe expectedOutput
    })
  }

  it should "return failed exit Future with recovered error when wrap" in {
    //given
    val expectedOutput = "test content"
    val stdoutStream = Readable.from(Buffer.from(expectedOutput))
    val stderrStream = Readable.from(Buffer.from(""))
    stderrStream.destroy(js.Error("test stream error"))
    val onceMock = mockFunction[String, js.Function, raw.EventEmitter]
    val childProcess = literal(
      "spawnargs" -> js.Array("app", "arg1", "arg2"),
      "stdout" -> stdoutStream,
      "stderr" -> stderrStream,
      "once" -> onceMock
    ).asInstanceOf[raw.ChildProcess]

    var exitCallback: js.Function1[Int, js.Any] = null
    onceMock.expects("exit", *).onCall { (_, callback) =>
      exitCallback = callback.asInstanceOf[js.Function1[Int, js.Any]]
      childProcess
    }
    onceMock.expects("error", *)

    //when
    val resultF = SubProcess.wrap(childProcess)

    //then
    (for {
      result <- resultF
      output <- loop(result.stdout, "")
      _ = exitCallback(1)
      exitError <- result.exitF.failed
    } yield {
      inside(exitError) { case js.JavaScriptException(error: js.Error) =>
        error.message shouldBe {
          """sub-process exited with code=1
            |command:
            |app arg1 arg2""".stripMargin
        }
      }
      result.child shouldBe childProcess
      result.stdout.readable shouldBe stdoutStream
      output shouldBe expectedOutput
    })
  }

  private def loop(reader: StreamReader, result: String): Future[String] = {
    reader.readNextBytes(256).flatMap {
      case None => Future.successful(result)
      case Some(content) =>
        loop(reader, result + content.toString)
    }
  }
}
