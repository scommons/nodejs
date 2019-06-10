package definitions

import common.TestLibs
import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys.coverageExcludedPackages

object NodejsTest extends ScalaJsModule {

  override val id: String = "scommons-nodejs-test"

  override val base: File = file("test")

  override def definition: Project = super.definition
    .settings(
      description := "Core Scala.js testing utilities on Node.js platform",
      coverageExcludedPackages := "scommons.nodejs.test.raw"
    )

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Seq(
    NodejsCore.definition
  )

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scalaTestJs.value,
    TestLibs.scalaMockJs.value
  ))

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)
}
