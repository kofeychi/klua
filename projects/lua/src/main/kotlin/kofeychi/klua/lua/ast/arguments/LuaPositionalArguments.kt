package kofeychi.klua.lua.ast.arguments

import kofeychi.klua.lua.ast.LuaArguments
import kofeychi.klua.lua.ast.LuaExpression
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaPositionalArguments(
    val expressions: List<LuaExpression>
) : LuaArguments {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: StringSink) {
        TODO("Not yet implemented")
    }

}