package kofeychi.klua.lua.ast.expression.value

import kofeychi.klua.lua.ast.expression.LuaValue
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaString(
    val luaValue: String
) : LuaValue {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        sink.write("\"$luaValue\"")
    }

    override fun writeTo(sink: StringSink) {
        sink.write("\"$luaValue\"")
    }
}