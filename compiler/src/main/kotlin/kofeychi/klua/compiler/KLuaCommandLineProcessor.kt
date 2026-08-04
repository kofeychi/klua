package kofeychi.klua.compiler

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import java.util.Collections.emptyList

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CommandLineProcessor::class)
@Suppress("unused")
class KLuaCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String = "klua"
    override val pluginOptions: Collection<AbstractCliOption> = emptyList()

    override fun processOption(
        option: AbstractCliOption,
        value: String,
        configuration: CompilerConfiguration
    ) {

    }
}