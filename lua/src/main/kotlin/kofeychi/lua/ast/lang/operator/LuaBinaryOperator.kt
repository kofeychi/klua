package kofeychi.lua.ast.lang.operator

enum class LuaBinaryOperator(
    val representation: String
) {
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/"),
    MOD("%"),
    POW("^"),

    CONCAT(".."),

    EQUALS("=="),
    NOT_EQUALS("~="),
    LOWER("<"),
    GREATER(">"),
    LOWER_EQUALS("<="),
    GREATER_EQUALS(">="),

    AND("and"),
    OR("or")
}