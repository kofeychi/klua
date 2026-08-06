package kofeychi.klua.lua

/**
 * Represents binary operators in Lua 5.2.
 */
enum class LuaBinaryOperator {
    ADD, SUB, MUL, DIV, MOD, POW,       // Arithmetic: + - * / % ^
    CONCAT,                             // String: ..
    EQ, NEQ, LT, GT, LTE, GTE,          // Relational: == ~= < > <= >=
    AND, OR                             // Logical: and or
}

/**
 * Represents unary operators in Lua 5.2.
 */
enum class LuaUnaryOperator {
    MINUS, NOT, LEN                     // - not #
}

/**
 * Standard Visitor interface for Lua AST nodes.
 */
interface IVisitable {
    fun accept(visitor: LuaVisitor)
}

/**
 * Dumpable interface working similarly to IVisitable but carrying a context object
 * for printing, formatting, or building string representations.
 */
interface IDumpable {
    fun <C> dump(dumper: LuaDumper<C>, context: C)
}

/**
 * Visits all types of Lua AST nodes without state context.
 */
interface LuaVisitor {
    fun visit(node: LuaChunk)
    fun visit(node: LuaBlock)
    fun visit(node: LuaReturnStatement)
    fun visit(node: LuaEmptyStatement)
    fun visit(node: LuaAssignmentStatement)
    fun visit(node: LuaLabelStatement)
    fun visit(node: LuaBreakStatement)
    fun visit(node: LuaGotoStatement)
    fun visit(node: LuaDoStatement)
    fun visit(node: LuaWhileStatement)
    fun visit(node: LuaRepeatStatement)
    fun visit(node: LuaElseIfClause)
    fun visit(node: LuaIfStatement)
    fun visit(node: LuaNumericForStatement)
    fun visit(node: LuaGenericForStatement)
    fun visit(node: LuaFunctionDeclarationStatement)
    fun visit(node: LuaLocalFunctionDeclarationStatement)
    fun visit(node: LuaLocalDeclarationStatement)
    fun visit(node: LuaNil)
    fun visit(node: LuaBoolean)
    fun visit(node: LuaNumber)
    fun visit(node: LuaString)
    fun visit(node: LuaVararg)
    fun visit(node: LuaFunctionLiteral)
    fun visit(node: LuaTableConstructor)
    fun visit(node: LuaBinaryExpression)
    fun visit(node: LuaUnaryExpression)
    fun visit(node: LuaParenthesizedExpression)
    fun visit(node: LuaSimpleVariable)
    fun visit(node: LuaTableAccessVariable)
    fun visit(node: LuaFieldAccessVariable)
    fun visit(node: LuaStandardFunctionCall)
    fun visit(node: LuaMethodFunctionCall)
    fun visit(node: LuaFunctionName)
    fun visit(node: LuaFunctionBody)
    fun visit(node: LuaPositionalArguments)
    fun visit(node: LuaTableArgument)
    fun visit(node: LuaStringArgument)
    fun visit(node: LuaKeyedField)
    fun visit(node: LuaNamedField)
    fun visit(node: LuaPositionalField)
}

/**
 * Dumps or processes Lua AST nodes while carrying a context payload.
 */
interface LuaDumper<C> {
    fun dump(node: LuaChunk, context: C)
    fun dump(node: LuaBlock, context: C)
    fun dump(node: LuaReturnStatement, context: C)
    fun dump(node: LuaEmptyStatement, context: C)
    fun dump(node: LuaAssignmentStatement, context: C)
    fun dump(node: LuaLabelStatement, context: C)
    fun dump(node: LuaBreakStatement, context: C)
    fun dump(node: LuaGotoStatement, context: C)
    fun dump(node: LuaDoStatement, context: C)
    fun dump(node: LuaWhileStatement, context: C)
    fun dump(node: LuaRepeatStatement, context: C)
    fun dump(node: LuaElseIfClause, context: C)
    fun dump(node: LuaIfStatement, context: C)
    fun dump(node: LuaNumericForStatement, context: C)
    fun dump(node: LuaGenericForStatement, context: C)
    fun dump(node: LuaFunctionDeclarationStatement, context: C)
    fun dump(node: LuaLocalFunctionDeclarationStatement, context: C)
    fun dump(node: LuaLocalDeclarationStatement, context: C)
    fun dump(node: LuaNil, context: C)
    fun dump(node: LuaBoolean, context: C)
    fun dump(node: LuaNumber, context: C)
    fun dump(node: LuaString, context: C)
    fun dump(node: LuaVararg, context: C)
    fun dump(node: LuaFunctionLiteral, context: C)
    fun dump(node: LuaTableConstructor, context: C)
    fun dump(node: LuaBinaryExpression, context: C)
    fun dump(node: LuaUnaryExpression, context: C)
    fun dump(node: LuaParenthesizedExpression, context: C)
    fun dump(node: LuaSimpleVariable, context: C)
    fun dump(node: LuaTableAccessVariable, context: C)
    fun dump(node: LuaFieldAccessVariable, context: C)
    fun dump(node: LuaStandardFunctionCall, context: C)
    fun dump(node: LuaMethodFunctionCall, context: C)
    fun dump(node: LuaFunctionName, context: C)
    fun dump(node: LuaFunctionBody, context: C)
    fun dump(node: LuaPositionalArguments, context: C)
    fun dump(node: LuaTableArgument, context: C)
    fun dump(node: LuaStringArgument, context: C)
    fun dump(node: LuaKeyedField, context: C)
    fun dump(node: LuaNamedField, context: C)
    fun dump(node: LuaPositionalField, context: C)
}

/**
 * Base contract for all tree nodes. Source position tracking is intentionally omitted.
 */
sealed interface LuaNode : IVisitable, IDumpable

/**
 * Represents constructs that execute sequentially or alter control flow.
 */
sealed interface LuaStatement : LuaNode

/**
 * Base contract for expressions that evaluate to runtime values.
 */
sealed interface LuaExpression : LuaNode

/**
 * Represents assignable lvalues; usable directly wherever an expression is required.
 */
sealed interface LuaVariable : LuaExpression

/**
 * Abstract interface for direct, self-evaluating literal constants.
 */
sealed interface LuaValue : LuaExpression

/**
 * Models function invocations that can serve as either statements or expressions.
 */
sealed interface LuaFunctionCall : LuaStatement, LuaExpression

/**
 * Base abstraction for call argument passing syntaxes.
 */
sealed interface LuaArguments : LuaNode

/**
 * Interface for field initialization entries in table literals.
 */
sealed interface LuaTableField : LuaNode



data class LuaChunk(
    val block: LuaBlock
) : LuaNode {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaBlock(
    val statements: List<LuaStatement>,
    val returnStatement: LuaReturnStatement?
) : LuaNode {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaReturnStatement(
    val values: List<LuaExpression>
) : LuaNode {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}



object LuaEmptyStatement : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaAssignmentStatement(
    val targets: List<LuaVariable>,
    val values: List<LuaExpression>
) : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaLabelStatement(
    val name: String
) : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

object LuaBreakStatement : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaGotoStatement(
    val targetLabel: String
) : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaDoStatement(
    val block: LuaBlock
) : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}



data class LuaWhileStatement(
    val condition: LuaExpression,
    val body: LuaBlock
) : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaRepeatStatement(
    val body: LuaBlock,
    val condition: LuaExpression
) : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaElseIfClause(
    val condition: LuaExpression,
    val thenBlock: LuaBlock
) : LuaNode {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaIfStatement(
    val condition: LuaExpression,
    val thenBlock: LuaBlock,
    val elseIfClauses: List<LuaElseIfClause>,
    val elseBlock: LuaBlock?
) : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}



data class LuaNumericForStatement(
    val variableName: String,
    val start: LuaExpression,
    val limit: LuaExpression,
    val step: LuaExpression?,
    val body: LuaBlock
) : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaGenericForStatement(
    val variableNames: List<String>,
    val iteratorExpressions: List<LuaExpression>,
    val body: LuaBlock
) : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaFunctionDeclarationStatement(
    val name: LuaFunctionName,
    val body: LuaFunctionBody
) : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaLocalFunctionDeclarationStatement(
    val name: String,
    val body: LuaFunctionBody
) : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaLocalDeclarationStatement(
    val names: List<String>,
    val initializers: List<LuaExpression>
) : LuaStatement {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}



object LuaNil : LuaValue {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaBoolean(
    val value: Boolean
) : LuaValue {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaNumber(
    val value: Double,
    val rawRepresentation: String
) : LuaValue {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaString(
    val value: String
) : LuaValue {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}



object LuaVararg : LuaExpression {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaFunctionLiteral(
    val body: LuaFunctionBody
) : LuaExpression {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaTableConstructor(
    val fields: List<LuaTableField>
) : LuaExpression {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaBinaryExpression(
    val left: LuaExpression,
    val operator: LuaBinaryOperator,
    val right: LuaExpression
) : LuaExpression {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaUnaryExpression(
    val operator: LuaUnaryOperator,
    val operand: LuaExpression
) : LuaExpression {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaParenthesizedExpression(
    val expression: LuaExpression
) : LuaExpression {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}



data class LuaSimpleVariable(
    val name: String
) : LuaVariable {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaTableAccessVariable(
    val table: LuaExpression,
    val key: LuaExpression
) : LuaVariable {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaFieldAccessVariable(
    val table: LuaExpression,
    val field: String
) : LuaVariable {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}



data class LuaStandardFunctionCall(
    val callee: LuaExpression,
    val arguments: LuaArguments
) : LuaFunctionCall {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaMethodFunctionCall(
    val receiver: LuaExpression,
    val methodName: String,
    val arguments: LuaArguments
) : LuaFunctionCall {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}



data class LuaFunctionName(
    val baseName: String,
    val path: List<String>,
    val methodName: String?
) : LuaNode {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaFunctionBody(
    val parameters: List<String>,
    val isVararg: Boolean,
    val block: LuaBlock
) : LuaNode {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}



data class LuaPositionalArguments(
    val expressions: List<LuaExpression>
) : LuaArguments {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaTableArgument(
    val table: LuaTableConstructor
) : LuaArguments {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaStringArgument(
    val string: LuaString
) : LuaArguments {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}



data class LuaKeyedField(
    val key: LuaExpression,
    val value: LuaExpression
) : LuaTableField {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaNamedField(
    val name: String,
    val value: LuaExpression
) : LuaTableField {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}

data class LuaPositionalField(
    val value: LuaExpression
) : LuaTableField {
    override fun accept(visitor: LuaVisitor) = visitor.visit(this)
    override fun <C> dump(dumper: LuaDumper<C>, context: C) = dumper.dump(this, context)
}