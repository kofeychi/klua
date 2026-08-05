package kofeychi.klua.lua.ast.expression.variable

import kofeychi.klua.lua.ast.LuaExpression
import kofeychi.klua.lua.ast.expression.LuaVariable
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaTableAccessVariable(
    val table: LuaExpression,
    val key: LuaExpression,
) : LuaVariable {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: StringSink) {
        TODO("Not yet implemented")
    }
}