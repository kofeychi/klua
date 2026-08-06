package kofeychi.klua.lua.ast.table

import kofeychi.klua.lua.ast.LuaExpression
import kofeychi.klua.lua.ast.LuaTableField
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaPositionalField(
    val value: LuaExpression
) : LuaTableField {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: StringSink) {
        TODO("Not yet implemented")
    }
}