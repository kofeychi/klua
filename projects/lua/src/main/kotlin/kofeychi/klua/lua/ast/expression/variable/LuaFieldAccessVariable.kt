package kofeychi.klua.lua.ast.expression.variable

import kofeychi.klua.lua.ast.LuaExpression
import kofeychi.klua.lua.ast.expression.LuaVariable
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaFieldAccessVariable(
    val field: String,
    val table: LuaExpression,
) : LuaVariable {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        table.writeTo(sink,context)
        sink.write(".")
        sink.write(field)
    }

    override fun writeTo(sink: StringSink) {
        table.writeTo(sink)
        sink.write(".")
        sink.write(field)
    }

}