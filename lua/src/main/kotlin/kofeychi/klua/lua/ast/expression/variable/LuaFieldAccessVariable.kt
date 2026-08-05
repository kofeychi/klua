package kofeychi.klua.lua.ast.expression.variable

import kofeychi.klua.lua.ast.LuaExpression

data class LuaFieldAccessVariable(
    val field: String,
    val table: LuaExpression,
)