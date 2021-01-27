package scommons

package object nodejs {

  type URL = raw.URL
  type Stats = raw.Stats
  type PathObject = raw.PathObject
  type Timeout = raw.Timeout

  lazy val path: raw.Path = raw.Path
  lazy val fs: FS = new FS {}
  lazy val os: raw.OS = raw.OS
  lazy val process: Process = new Process {}
  lazy val child_process: ChildProcess = new ChildProcess {}
  
  lazy val global: raw.NodeJs = raw.NodeJs
}
