package kofeychi.klua.lua.ast.expression.variable

import kofeychi.klua.lua.ast.expression.LuaVariable
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaSimpleVariable(
    val name: String,
) : LuaVariable {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        sink.write(name)
    }

    override fun writeTo(sink: StringSink) {
        sink.write(name)
    }
}