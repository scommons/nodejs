import definitions._
import scommons.sbtplugin.project.CommonModule
import scommons.sbtplugin.project.CommonModule.ideExcludedDirectories

lazy val `scommons-nodejs` = (project in file("."))
  .settings(CommonModule.settings: _*)
  .settings(NodejsModule.settings: _*)
  .settings(
    publish / skip := true,
    publish := ((): Unit),
    publishLocal := ((): Unit),
    publishM2 := ((): Unit)
  )
  .settings(
    ideExcludedDirectories += baseDirectory.value / "docs" / "_site"
  )
  .aggregate(
  `scommons-nodejs-core`,
  `scommons-nodejs-test`,
  `scommons-nodejs-showcase`
)

lazy val `scommons-nodejs-core` = NodejsCore.definition
lazy val `scommons-nodejs-test` = NodejsTest.definition
lazy val `scommons-nodejs-showcase` = NodejsShowcase.definition
