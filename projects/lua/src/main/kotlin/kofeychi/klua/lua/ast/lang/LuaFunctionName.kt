package kofeychi.klua.lua.ast.lang

import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink


data class LuaFunctionName(
    val baseName: String,
    val path: List<String>,
    val methodName: String?
) : LuaNode {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: StringSink) {
        TODO("Not yet implemented")
    }
}