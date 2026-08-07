package kofeychi.klua.lua.sink

data class LuaPrintSinkContext(
    val indentChar: String = "  ",
    val useTabs: Boolean = false,
    val omitSemicolons: Boolean = true
) : LuaSinkContext {
    override val isPrint: Boolean = true

    private var indentSize = 0;

    fun getIndent(): String = buildString {
        repeat(indentSize) {
            if (useTabs) append('\t') else append(indentChar)
        }
    }

    fun indent() {
        indentSize++
    }

    fun dedent() {
        indentSize--
        if(indentSize < 0) throw IllegalStateException("Cant pop non existing indent")
    }

    companion object {
        val default = LuaPrintSinkContext()
    }
}