package kofeychi.klua.lua.ast.expression

import kofeychi.klua.lua.ast.LuaExpression
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaParenthesizedExpression(
    val expression: LuaExpression,
) : LuaExpression {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        sink.write("(")
        expression.writeTo(sink, context)
        sink.write(")")
    }

    override fun writeTo(sink: StringSink) {
        sink.write("(")
        expression.writeTo(sink)
        sink.write(")")
    }

}