package kofeychi.klua.gradle

import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class KLuaGradlePlugin : KotlinCompilerPluginSupportPlugin {
    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        return kotlinCompilation.target.project.provider { emptyList() }
    }

    override fun getCompilerPluginId(): String = "klua"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = KluaGradleBuildData.PLUGIN_GROUP,
        artifactId = KluaGradleBuildData.PLUGIN_NAME,
        version = KluaGradleBuildData.PLUGIN_VERSION,
    )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true
}