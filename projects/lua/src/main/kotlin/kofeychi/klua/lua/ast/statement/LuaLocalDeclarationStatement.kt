package kofeychi.klua.lua.ast.statement

import kofeychi.klua.lua.ast.LuaExpression
import kofeychi.klua.lua.ast.LuaStatement
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink


data class LuaLocalDeclarationStatement(
    val names: List<String>,
    val initializers: List<LuaExpression>
) : LuaStatement {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: StringSink) {
        TODO("Not yet implemented")
    }
}