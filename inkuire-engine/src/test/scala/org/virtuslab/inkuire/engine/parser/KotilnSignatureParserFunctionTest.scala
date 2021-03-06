package org.virtuslab.inkuire.engine.parser

import cats.implicits.catsSyntaxOptionId
import org.virtuslab.inkuire.engine.BaseInkuireTest
import org.virtuslab.inkuire.engine.model.{GenericType, Signature, SignatureContext, UnresolvedVariance}
import org.virtuslab.inkuire.engine.model.Type._

//TODO fix
class KotilnSignatureParserFunctionTest extends BaseInkuireTest {

  val parser = new KotlinSignatureParserService

  it should "parse signature with a function receiver in parentheses" in {
    //given
    val str = "((Float) -> Double).(String)->Int"

    //when
    val res = parser.parse(str)

    //then
    val expectedRes =
      Right(
        Signature(
          GenericType(
            "Function1".concreteType,
            Seq(
              UnresolvedVariance(
                "Float".concreteType
              ),
              UnresolvedVariance(
                "Double".concreteType
              )
            )
          ).some,
          Seq(
            "String".concreteType
          ),
          "Int".concreteType,
          SignatureContext.empty
        )
      )

    res should matchTo[Either[String, Signature]](expectedRes)
  }

  it should "parse signature with a nullable function receiver in parentheses" in {
    //given
    val str = "(()->Int)?.() -> Unit"

    //when
    val res = parser.parse(str)

    //then
    val expectedRes =
      Right(
        Signature(
          GenericType(
            "Function0".concreteType,
            Seq(
              UnresolvedVariance(
                "Int".concreteType
              )
            )
          ).?.some,
          Seq.empty,
          "Unit".concreteType,
          SignatureContext.empty
        )
      )

    res should matchTo[Either[String, Signature]](expectedRes)
  }

  it should "parse signature with a function result" in {
    //given
    val str = "Long . (String) -> (Float)->Double"

    //when
    val res = parser.parse(str)

    //then
    val expectedRes =
      Right(
        Signature(
          "Long".concreteType.some,
          Seq(
            "String".concreteType
          ),
          GenericType(
            "Function1".concreteType,
            Seq(
              UnresolvedVariance(
                "Float".concreteType
              ),
              UnresolvedVariance(
                "Double".concreteType
              )
            )
          ),
          SignatureContext.empty
        )
      )

    res should matchTo[Either[String, Signature]](expectedRes)
  }

  it should "parse signature with a nested function args" in {
    //given
    val str = "Long.(Int.(Float.() -> Long) -> Unit, Double) -> Float"

    //when
    val res = parser.parse(str)

    //then
    val expectedRes =
      Right(
        Signature(
          "Long".concreteType.some,
          Seq(
            GenericType(
              "Function2".concreteType,
              Seq(
                UnresolvedVariance(
                  "Int".concreteType
                ),
                UnresolvedVariance(
                  GenericType(
                    "Function1".concreteType,
                    Seq(
                      UnresolvedVariance(
                        "Float".concreteType
                      ),
                      UnresolvedVariance(
                        "Long".concreteType
                      )
                    )
                  )
                ),
                UnresolvedVariance(
                  "Unit".concreteType
                )
              )
            ),
            "Double".concreteType
          ),
          "Float".concreteType,
          SignatureContext.empty
        )
      )

    res should equal(expectedRes)
    res should matchTo[Either[String, Signature]](expectedRes)
  }

  it should "parse signature with piped functions as return types" in {
    //given
    val str = "Long.(String) -> (Float) -> Int.() -> Double.(Float) -> Unit"

    //when
    val res = parser.parse(str)

    //then
    val expectedRes =
      Right(
        Signature(
          "Long".concreteType.some,
          Seq(
            "String".concreteType
          ),
          GenericType(
            "Function1".concreteType,
            Seq(
              UnresolvedVariance("Float".concreteType),
              UnresolvedVariance(
                GenericType(
                  "Function1".concreteType,
                  Seq(
                    UnresolvedVariance(
                      "Int".concreteType
                    ),
                    UnresolvedVariance(
                      GenericType(
                        "Function2".concreteType,
                        Seq(
                          UnresolvedVariance(
                            "Double".concreteType
                          ),
                          UnresolvedVariance(
                            "Float".concreteType
                          ),
                          UnresolvedVariance(
                            "Unit".concreteType
                          )
                        )
                      )
                    )
                  )
                )
              )
            )
          ),
          SignatureContext.empty
        )
      )

    res should matchTo[Either[String, Signature]](expectedRes)
  }
}
