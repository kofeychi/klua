package kofeychi.klua.lua.ast.lang

import kofeychi.klua.lua.ast.LuaStatement
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaBlock(
    val statements: List<LuaStatement>,
    val returnStatement: LuaReturnStatement? = null,
) : LuaNode {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: StringSink) {
        TODO("Not yet implemented")
    }

}