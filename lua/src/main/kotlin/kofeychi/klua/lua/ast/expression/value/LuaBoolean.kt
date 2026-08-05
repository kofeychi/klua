package kofeychi.klua.lua.ast.expression.value

import kofeychi.klua.lua.ast.expression.LuaValue
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaBoolean(
    val luaValue: Boolean
) : LuaValue {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        sink.write(luaValue.toString())
    }

    override fun writeTo(sink: StringSink) {
        sink.write(luaValue.toString())
    }
}