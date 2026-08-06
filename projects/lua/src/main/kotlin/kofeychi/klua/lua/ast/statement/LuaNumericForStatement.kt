package kofeychi.klua.lua.ast.statement

import kofeychi.klua.lua.ast.LuaExpression
import kofeychi.klua.lua.ast.LuaStatement
import kofeychi.klua.lua.ast.lang.LuaBlock
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaNumericForStatement(
    val variableName: String,
    val start: LuaExpression,
    val limit: LuaExpression,
    val step: LuaExpression?,
    val body: LuaBlock
) : LuaStatement {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: StringSink) {
        TODO("Not yet implemented")
    }
}