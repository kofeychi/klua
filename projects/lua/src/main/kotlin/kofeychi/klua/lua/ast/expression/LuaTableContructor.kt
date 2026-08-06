package kofeychi.klua.lua.ast.expression

import kofeychi.klua.lua.ast.LuaExpression
import kofeychi.klua.lua.ast.LuaTableField
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaTableContructor(
    val field: List<LuaTableField>
) : LuaExpression {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: StringSink) {
        TODO("Not yet implemented")
    }
}