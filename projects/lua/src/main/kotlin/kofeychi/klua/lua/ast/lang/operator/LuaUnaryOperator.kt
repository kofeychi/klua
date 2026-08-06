package kofeychi.klua.lua.ast.lang.operator

enum class LuaUnaryOperator(
    val representation: String
) {
    MINUS("-"),
    NOT("not"),
    LEN("#")
}