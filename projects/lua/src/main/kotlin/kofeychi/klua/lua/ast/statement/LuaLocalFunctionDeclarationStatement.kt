package kofeychi.klua.lua.ast.statement

import kofeychi.klua.lua.ast.LuaStatement
import kofeychi.klua.lua.ast.lang.LuaFunctionBody
import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.StringSink

data class LuaLocalFunctionDeclarationStatement(
    val name: String,
    val body: LuaFunctionBody
) : LuaStatement {
    override fun writeTo(sink: StringSink, context: LuaSinkContext) {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: StringSink) {
        TODO("Not yet implemented")
    }
}