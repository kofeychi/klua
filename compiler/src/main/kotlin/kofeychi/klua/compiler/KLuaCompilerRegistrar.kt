package kofeychi.klua.compiler

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import java.lang.IO.println

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CompilerPluginRegistrar::class)
@Suppress("unused")
class KLuaCompilerRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean = true

    override val pluginId: String
        get() = "klua"

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        println("Привет из Kotlin Compiler Plugin!")
    }
}
