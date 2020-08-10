package org.virtuslab.inkuire.plugin

import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.model.*
import org.jetbrains.dokka.pages.*
import org.jetbrains.dokka.transformers.documentation.DocumentableToPageTranslator
import org.virtuslab.inkuire.model.SDRI
import org.virtuslab.inkuire.plugin.content.InkuireContentPage
import org.virtuslab.inkuire.plugin.transformers.DefaultDokkaToSerializableModelTransformer

object InkuireDocumentableToPageTranslator : DocumentableToPageTranslator {

    override fun invoke(module: DModule): ModulePageNode = module.packages.let { packages ->
        packages.flatMap { it.functions } + packages.flatMap { it.classlikes }.flatMap { classlike ->
            classlike.getFunctions()
        }
    }.let { functions ->
        ModulePageNode(
            name = "root",
            content = ContentText("", DCI(emptySet(), ContentKind.Empty), emptySet()),
            children = module.sourceSets.map { sourceSet ->
                with(DefaultDokkaToSerializableModelTransformer) {
                    InkuireContentPage(
                            name = sourceSet.sourceSetID.sourceSetName,
                            functions = functions.filter { sourceSet in it.sourceSets }.map { it.toSerializable() }.distinct(),
                            ancestryGraph = typesAncestryGraph(module, sourceSet)
                    )
                }
            },
            documentable = module
        )
    }

    private fun typesAncestryGraph(documentable: Documentable, sourceSet: DokkaConfiguration.DokkaSourceSet): Map<SDRI, List<SDRI>> {
        with(DefaultDokkaToSerializableModelTransformer) {
            return documentable.children.fold(emptyMap<SDRI, List<SDRI>>()) { acc, elem ->
                acc + typesAncestryGraph(elem, sourceSet)
            } + if (documentable is WithSupertypes)
                listOf(documentable.dri.toSerializable() to (documentable.supertypes[sourceSet]?.map { it.dri.toSerializable() }
                        ?: emptyList())).toMap()
            else
                emptyMap()
        }
    }

    private fun DClasslike.getFunctions(): List<DFunction> = functions + classlikes.flatMap { it.getFunctions() }
}
