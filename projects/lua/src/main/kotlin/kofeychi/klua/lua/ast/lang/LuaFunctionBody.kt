package kofeychi.klua.lua.ast.lang

import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaFunctionBody(
    val parameters: List<String>,
    val isVararg: Boolean,
    val block: LuaBlock
) : LuaNode {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: StringSink) {
        TODO("Not yet implemented")
    }
}