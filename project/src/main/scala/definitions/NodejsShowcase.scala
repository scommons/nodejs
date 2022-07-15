package definitions

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._

import scalajsbundler.BundlingMode
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object NodejsShowcase extends ScalaJsModule {

  override val id = "scommons-nodejs-showcase"

  override val base: File = file("showcase")

  override def definition: Project = super.definition
    .settings(
      publish / skip := true,
      publish := ((): Unit),
      publishLocal := ((): Unit),
      publishM2 := ((): Unit),

      scalaJSUseMainModuleInitializer := true,
      webpackBundlingMode := BundlingMode.LibraryOnly()
    )

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    NodejsCore.definition,
    NodejsTest.definition % "test"
  )

  override val superRepoProjectsDependencies: Seq[(String, String, Option[String])] = Nil

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)
}
