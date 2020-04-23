package org.virtuslab.inkuire.plugin.transformers

import org.jetbrains.dokka.model.*
import org.virtuslab.inkuire.plugin.model.*

abstract class DokkaToSerializableModelTransformer {
    fun transform(root: DModule) : SDModule = root.toSerializable()
    abstract fun DModule.toSerializable() : SDModule
    abstract fun DPackage.toSerializable() : SDPackage
    abstract fun DClass.toSerializable() : SDClass
    abstract fun DEnum.toSerializable() : SDEnum
    abstract fun DEnumEntry.toSerializable() : SDEnumEntry
    abstract fun DFunction.toSerializable() : SDFunction
    abstract fun DInterface.toSerializable() : SDInterface
    abstract fun DObject.toSerializable() : SDObject
    abstract fun DAnnotation.toSerializable() : SDAnnotation
    abstract fun DProperty.toSerializable() : SDProperty
    abstract fun DParameter.toSerializable() : SDParameter
    abstract fun DTypeParameter.toSerializable() : SDTypeParameter
    abstract fun DTypeAlias.toSerializable() : SDTypeAlias
    abstract fun DClasslike.toSerializable() : SDClasslike
}