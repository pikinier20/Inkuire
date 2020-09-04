package org.virtuslab.inkuire.engine.service

import cats.implicits.catsSyntaxOptionId
import org.virtuslab.inkuire.engine.BaseInkuireTest
import org.virtuslab.inkuire.engine.model.Type.StringTypeOps
import org.virtuslab.inkuire.engine.model.{Covariance, DRI, ExternalSignature, GenericType, InkuireDb, Invariance, Signature, SignatureContext, Type, TypeVariable, Variance}
import org.virtuslab.inkuire.engine.service.FluffMatchServiceTest.Fixture

class FluffMatchServiceTest extends BaseInkuireTest {
  it should "match simple function" in new Fixture {
    //given
    val fluffMatchService = new FluffMatchService(
      InkuireDb(
        Seq(
          mapGetSignature
        ),
        ancestryGraph.nodes
      )
    )
    //when
    val res: Seq[ExternalSignature] = fluffMatchService |??| mapGetSignature.signature

    //then
    res should matchTo[Seq[ExternalSignature]](Seq(mapGetSignature))
  }
}

object FluffMatchServiceTest{
  trait Fixture {
    import com.softwaremill.quicklens._
    def generateDriForName(name: String): DRI = DRI("test".some, s"test-$name".some, None, "original")
    def createGenericType(name: String, vars: Seq[Variance], nullable: Boolean = false): GenericType =
      GenericType(
        name.concreteType
          .modify(_.nullable)
          .setTo(nullable)
          .modify(_.dri)
          .setTo(generateDriForName(name).some),
        vars
      )
    def createTypeVariable(name: String, nullable: Boolean = false): TypeVariable =
      TypeVariable(
        name,
        nullable,
        generateDriForName(name).some
      )
    val mapType: Type = createGenericType(
      "Map",
      Seq(
        Invariance(
          createTypeVariable("K")
        ),
        Covariance(
          createTypeVariable("V")
        )
      )
    )

    val ancestryGraph: AncestryGraph = AncestryGraph(
      Map(
        mapType.dri.get -> (mapType, Seq.empty)
      )
    )

    val mapGetSignature: ExternalSignature = {
      ExternalSignature(
        Signature(
          mapType.some,
          Seq(
            createTypeVariable("K")
          ),
          createTypeVariable("V", nullable = true),
          SignatureContext(
            Set("K","V"),
            Map.empty
          )
        ),
        "get",
        "uri"
      )
    }
  }
}