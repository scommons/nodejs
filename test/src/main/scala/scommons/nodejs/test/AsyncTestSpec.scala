package scommons.nodejs.test

import org.scalactic.source.Position
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.Assertion
import org.scalatest.concurrent.PatienceConfiguration
import org.scalatest.exceptions.{StackDepthException, TestFailedDueToTimeoutException}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.time.{Millis, Seconds, Span}
import scommons.nodejs._

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.scalajs.concurrent.JSExecutionContext
import scala.util.control.NonFatal

trait AsyncTestSpec extends AsyncFlatSpec
  with BaseTestSpec
  with AsyncMockFactory
  with PatienceConfiguration {

  implicit override val executionContext: ExecutionContext = JSExecutionContext.queue

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(
    timeout = scaled(Span(5, Seconds)),
    interval = scaled(Span(100, Millis))
  )

  def eventually(mayFailBlock: => Assertion)(implicit config: PatienceConfig, pos: Position): Future[Assertion] = {
    val startMillis = System.currentTimeMillis()
    val timeout = config.timeout
    val interval = config.interval

    var timer: Timeout = null
    var lastEx: Throwable = null
    var attempt = 0

    val promise = Promise[Assertion]()

    timer = global.setInterval({ () =>
      try {
        attempt += 1
        val durationMillis = System.currentTimeMillis() - startMillis
        if (durationMillis < timeout.millisPart) {
          lastEx = null

          promise.success(mayFailBlock)
        }
        else {
          val durationSpan = Span(1, Millis) scaledBy durationMillis // Use scaledBy to get pretty units
          val failure = new TestFailedDueToTimeoutException(
            { (_: StackDepthException) =>
              val error =
                if (lastEx.getMessage == null) lastEx.toString
                else lastEx.getMessage

              Some(
                "Did not eventually succeeded" +
                  s", attempts: $attempt" +
                  s", duration: ${durationSpan.prettyString}" +
                  s", error: $error"
              )
            },
            Some(lastEx),
            Left(pos),
            None,
            config.timeout
          )

          promise.failure(failure)
        }

        global.clearInterval(timer)
      }
      catch {
        case NonFatal(ex) => lastEx = ex
      }
    }, interval.millisPart)

    promise.future
  }

  def executeAfterDelay(millis: Int)(block: => Assertion): Future[Assertion] = {
    val promise = Promise[Assertion]()

    global.setTimeout({ () =>
      try {
        promise.success(block)
      } catch {
        case NonFatal(ex) => promise.failure(ex)
      }
    }, millis.toDouble)

    promise.future
  }
}
