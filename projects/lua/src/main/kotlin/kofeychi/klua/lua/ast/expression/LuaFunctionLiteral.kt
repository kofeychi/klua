package kofeychi.klua.lua.ast.expression

import kofeychi.klua.lua.ast.LuaExpression
import kofeychi.klua.lua.ast.lang.LuaFunctionBody
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaFunctionLiteral(
    val body: LuaFunctionBody
) : LuaExpression {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: StringSink) {
        TODO("Not yet implemented")
    }

}