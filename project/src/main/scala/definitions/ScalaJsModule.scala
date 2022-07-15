package definitions

import org.scalajs.sbtplugin.ScalaJSPlugin
import sbt._
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
import scommons.sbtplugin.project.CommonNodeJsModule

trait ScalaJsModule extends NodejsModule {

  override def definition: Project = {
    super.definition
      .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
      .settings(CommonNodeJsModule.settings: _*)
  }
}
