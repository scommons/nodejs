package scommons.nodejs.util

import scommons.nodejs
import scommons.nodejs.raw

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.scalajs.js
import scala.util.control.NonFatal

case class SubProcess(child: raw.ChildProcess,
                      stdout: StreamReader,
                      exitF: Future[Unit])

object SubProcess {
  
  def wrap(childProcess: raw.ChildProcess): Future[SubProcess] = {
    val stdout = new StreamReader(childProcess.stdout)

    val exitPromise = Promise[Unit]()
    childProcess.once("exit", { code: Int =>
      if (code == 0) exitPromise.success(())
      else {
        val stderr = new StreamReader(childProcess.stderr)
        stderr.readNextBytes(8096).recover {
          case NonFatal(_) => None
        }.foreach { content =>
          stderr.readable.destroy()
          val error = content.map(_.toString).getOrElse {
            s"""sub-process exited with code=$code
               |command:
               |${childProcess.spawnargs.mkString(" ")}""".stripMargin
          }
          exitPromise.failure(js.JavaScriptException(js.Error(error)))
        }
      }
    })

    val spawnPromise = Promise[SubProcess]()
    childProcess.once("error", { error: js.Error =>
      spawnPromise.tryFailure(js.JavaScriptException(error))
    })
    nodejs.global.setImmediate({ () =>
      spawnPromise.trySuccess(SubProcess(childProcess, stdout, exitPromise.future))
    })
    spawnPromise.future
  }
}
