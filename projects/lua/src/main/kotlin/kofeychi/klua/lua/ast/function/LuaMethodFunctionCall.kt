package kofeychi.klua.lua.ast.function

import kofeychi.klua.lua.ast.LuaArguments
import kofeychi.klua.lua.ast.LuaExpression
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaMethodFunctionCall(
    val receiver: LuaExpression,
    val methodName: String,
    val arguments: LuaArguments
) : LuaFunctionCall {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: StringSink) {
        TODO("Not yet implemented")
    }

}