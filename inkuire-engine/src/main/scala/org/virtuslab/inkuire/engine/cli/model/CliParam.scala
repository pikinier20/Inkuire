package org.virtuslab.inkuire.engine.cli.model

import org.virtuslab.inkuire.engine.utils.syntax._

sealed trait CliParam
case class DbPath(path: String) extends CliParam
case class AncestryGraphPath(path: String) extends CliParam
case class Port(number: Int) extends CliParam

object CliParam {
  def parseCliOption(opt: String, v: String): Either[String, CliParam] =
    opt match {
      case "-d" | "--database" => DbPath(v).right
      case "-a" | "--ancestry" => AncestryGraphPath(v).right
      case "-p" | "--port" => Port(v.toInt).right
      case _                   => s"Wrong option $opt".left
    }
}
