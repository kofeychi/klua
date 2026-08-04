package kofeychi.klua.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.*

@Suppress("unused")
class KLuaGradlePlugin : KotlinCompilerPluginSupportPlugin {
    
    override fun apply(target: Project) {

    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    override fun getCompilerPluginId(): String = "klua"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "klua-root",
        artifactId = "compiler-plugin",
        version = "unspecified"
    )

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>
    ): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        return project.provider { emptyList() }
    }
}
