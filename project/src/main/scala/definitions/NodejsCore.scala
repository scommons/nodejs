package definitions

import common.{Libs, TestLibs}
import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys.coverageExcludedPackages

object NodejsCore extends ScalaJsModule {

  override val id: String = "scommons-nodejs-core"

  override val base: File = file("core")

  override def definition: Project = super.definition
    .settings(
      description := "Scala.js facades for Node.js platform",
      coverageExcludedPackages :=
        "scommons.nodejs.raw" +
          ";scommons.nodejs.Buffer" +
          ";scommons.nodejs.util.StreamReader" + // avoid "Found a dangling UndefinedParam" during test with coverage
          ";scommons.nodejs.util.SubProcess" // avoid "Found a dangling UndefinedParam" during test with coverage
    )

  override val internalDependencies: Seq[ClasspathDep[ProjectReference]] = Nil

  override val runtimeDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Nil)

  override val testDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting(Seq(
    TestLibs.scalaTestJs.value,
    TestLibs.scalaMockJs.value
  ).map(_ % "test"))
}
