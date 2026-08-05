package kofeychi.klua.lua.ast.expression

import kofeychi.klua.lua.ast.LuaExpression
import kofeychi.klua.lua.ast.lang.operator.LuaUnaryOperator
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaUnaryExpression(
    val operand: LuaUnaryOperator,
    val operator: LuaExpression,
) : LuaExpression {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: StringSink) {
        TODO("Not yet implemented")
    }
}