package org.virtuslab.inkuire.engine.http

import org.virtuslab.inkuire.engine.model.ExternalSignature
import org.virtuslab.inkuire.engine.service.SignaturePrettifier
import org.virtuslab.inkuire.model.{Match, OutputFormat}
import collection.JavaConverters._

class OutputFormatter(val prettifier: SignaturePrettifier) {
  def createOutput(query: String, signatures: Seq[ExternalSignature]): OutputFormat = new OutputFormat(
    query,
    fromSignatures(signatures).asJava
  )


  private def fromSignatures(signatures: Seq[ExternalSignature]): Seq[Match] = signatures
    .zip(prettifier.prettify(signatures).split("\n"))
    .map{
      case (sgn, pretty) =>
        new Match(
          pretty,
          sgn.name,
          sgn.uri
        )
    }
}
