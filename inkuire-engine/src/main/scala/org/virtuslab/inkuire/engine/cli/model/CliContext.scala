package org.virtuslab.inkuire.engine.cli.model

import java.io.{File, FileOutputStream}
import java.net.{URI, URL}
import java.nio.channels.Channels
import java.nio.file.Paths

case class CliContext(
  dbFiles: List[URL],
  ancestryFiles: List[URL],
  port: Int
)

object CliContext {

  private def toURL(path: String) = new URL(path)
  def empty: CliContext = CliContext(List.empty, List.empty, 8080)
  def create(args: List[CliParam]): CliContext = {

    val dbFiles = args.collect { case DbPath(path) => toURL(path) }
    val ancestryGraphFiles = args.collect { case AncestryGraphPath(path) => toURL(path) }
    val port = args.collectFirst{ case Port(number) => number }.getOrElse(8080)

    CliContext(dbFiles, ancestryGraphFiles, port)
  }
}