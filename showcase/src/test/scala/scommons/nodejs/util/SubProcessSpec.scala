package scommons.nodejs.util

import org.scalatest.Assertion
import scommons.nodejs._
import scommons.nodejs.test.AsyncTestSpec

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal

class SubProcessSpec extends AsyncTestSpec {

  it should "return successful Future when wrap" in {
    //given
    val expectedContent = "test content"
    val (tmpDir, file) = createTempFile(expectedContent)
    val stdoutStream = fs.createReadStream(file)
    val onceMock = mockFunction[String, js.Function, raw.EventEmitter]
    val childProcess = literal(
      "stdout" -> stdoutStream,
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
    }).andThen {
      case _ => cleanupTempFile(tmpDir, file)
    }
  }

  it should "return failed Future if error when wrap" in {
    //given
    val (tmpDir, file) = createTempFile("test content")
    val stdoutStream = fs.createReadStream(file)
    val onceMock = mockFunction[String, js.Function, raw.EventEmitter]
    val childProcess = literal(
      "stdout" -> stdoutStream,
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
    }).andThen {
      case _ => cleanupTempFile(tmpDir, file)
    }
  }

  it should "return failed exit Future with stderr when wrap" in {
    //given
    val expectedOutput = "test content"
    val expectedError = "test error"
    val (tmpDir1, file1) = createTempFile(expectedOutput)
    val (tmpDir2, file2) = createTempFile(expectedError)
    val stdoutStream = fs.createReadStream(file1)
    val stderrStream = fs.createReadStream(file2)
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
    }).andThen {
      case _ =>
        cleanupTempFile(tmpDir1, file1)
        cleanupTempFile(tmpDir2, file2)
    }
  }

  it should "return failed exit Future with generic error when wrap" in {
    //given
    val expectedOutput = "test content"
    val (tmpDir1, file1) = createTempFile(expectedOutput)
    val (tmpDir2, file2) = createTempFile("")
    val stdoutStream = fs.createReadStream(file1)
    val stderrStream = fs.createReadStream(file2)
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
    }).andThen {
      case _ =>
        cleanupTempFile(tmpDir1, file1)
        cleanupTempFile(tmpDir2, file2)
    }
  }

  it should "return failed exit Future with recovered error when wrap" in {
    //given
    val expectedOutput = "test content"
    val (tmpDir1, file1) = createTempFile(expectedOutput)
    val (tmpDir2, file2) = createTempFile("test error")
    val stdoutStream = fs.createReadStream(file1)
    val stderrStream = fs.createReadStream(file2)
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
      _ = stderrStream.destroy(js.Error("test stream error"))
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
    }).andThen {
      case _ =>
        cleanupTempFile(tmpDir1, file1)
        cleanupTempFile(tmpDir2, file2)
    }
  }

  private def loop(reader: StreamReader, result: String): Future[String] = {
    reader.readNextBytes(256).flatMap {
      case None => Future.successful(result)
      case Some(content) =>
        loop(reader, result + content.toString)
    }
  }
  
  private def createTempFile(content: String, name: String = "example.txt"): (String, String) = {
    val tmpDir = fs.mkdtempSync(path.join(os.tmpdir(), "scommons-nodejs-"))
    val file = path.join(tmpDir, name)
    fs.writeFileSync(file, content)
    (tmpDir, file)
  }

  private def cleanupTempFile(tmpDir: String, file: String): Assertion = {
    fs.unlinkSync(file)
    fs.existsSync(file) shouldBe false

    fs.rmdirSync(tmpDir)
    fs.existsSync(tmpDir) shouldBe false
  }
}
