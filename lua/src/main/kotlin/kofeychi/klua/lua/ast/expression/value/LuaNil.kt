package kofeychi.klua.lua.ast.expression.value

import kofeychi.klua.lua.ast.expression.LuaValue
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

object LuaNil : LuaValue {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        sink.write("nil")
    }

    override fun writeTo(sink: StringSink) {
        sink.write("nil")
    }
}