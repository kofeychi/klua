package kofeychi.klua.lua.test

import kofeychi.klua.lua.ast.*
import kofeychi.klua.lua.ast.arguments.*
import kofeychi.klua.lua.ast.expression.*
import kofeychi.klua.lua.ast.expression.value.*
import kofeychi.klua.lua.ast.expression.variable.*
import kofeychi.klua.lua.ast.function.*
import kofeychi.klua.lua.ast.lang.*
import kofeychi.klua.lua.ast.lang.operator.*
import kofeychi.klua.lua.ast.statement.*
import kofeychi.klua.lua.ast.table.*

// ── Lexer ──────────────────────────────────────────────────────────────────

private enum class TT {
    NAME, NUMBER, STRING, TRUE, FALSE, NIL, VARARG,
    PLUS, MINUS, STAR, SLASH, PERCENT, CARET, HASH,
    AMPAMP, PIPEPIPE, DOTDOT, LT, GT, LEQ, GEQ, EQ, NEQ,
    AND, OR, NOT,
    LPAREN, RPAREN, LBRACE, RBRACE, LBRACKET, RBRACKET,
    SEMI, COLON, COMMA, DOT, ASSIGN,
    COLONCOLON,
    DO, END, WHILE, REPEAT, UNTIL, IF, THEN, ELSEIF, ELSE,
    FOR, IN, FUNCTION, LOCAL, RETURN, BREAK, GOTO,
    EOF
}

private data class Token(val type: TT, val value: String, val pos: Int)

private val KEYWORDS = mapOf(
    "and" to TT.AND, "break" to TT.BREAK, "do" to TT.DO, "else" to TT.ELSE,
    "elseif" to TT.ELSEIF, "end" to TT.END, "false" to TT.FALSE,
    "for" to TT.FOR, "function" to TT.FUNCTION, "goto" to TT.GOTO,
    "if" to TT.IF, "in" to TT.IN, "local" to TT.LOCAL, "nil" to TT.NIL,
    "not" to TT.NOT, "or" to TT.OR, "repeat" to TT.REPEAT,
    "return" to TT.RETURN, "then" to TT.THEN, "true" to TT.TRUE,
    "until" to TT.UNTIL, "while" to TT.WHILE
)

private class Lexer(private val src: String) {
    private var pos = 0
    private val tokens = mutableListOf<Token>()
    private var idx = 0

    fun tokenize(): List<Token> {
        while (pos < src.length) {
            skipWhitespaceAndComments()
            if (pos >= src.length) break
            val start = pos
            val c = src[pos]
            when {
                c.isLetter() || c == '_' -> readName(start)
                c.isDigit() -> readNumber(start)
                c == '"' || c == '\'' -> readShortString(c, start)
                c == '[' && (peek(1) == '[' || peek(1) == '=') -> {
                    val level = longBracketLevel()
                    if (level >= 0) readLongString(level, start) else emit(TT.LBRACKET, start)
                }
                else -> readSymbol(start)
            }
        }
        tokens.add(Token(TT.EOF, "", pos))
        return tokens
    }

    private fun peek(offset: Int = 1) = if (pos + offset < src.length) src[pos + offset] else '\u0000'

    private fun skipWhitespaceAndComments() {
        while (pos < src.length) {
            val c = src[pos]
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') { pos++; continue }
            if (c == '-' && peek() == '-') {
                pos += 2
                if (pos < src.length && src[pos] == '[') {
                    val level = longBracketLevel()
                    if (level >= 0) { skipLongString(level); continue }
                }
                while (pos < src.length && src[pos] != '\n') pos++
                continue
            }
            break
        }
    }

    private fun longBracketLevel(): Int {
        var i = pos
        if (i >= src.length || src[i] != '[') return -1
        i++
        var level = 0
        while (i < src.length && src[i] == '=') { level++; i++ }
        return if (i < src.length && src[i] == '[') level else -1
    }

    private fun skipLongString(level: Int) {
        pos += level + 2 // skip opening [=*[
        val close = "]" + "=".repeat(level) + "]"
        val end = src.indexOf(close, pos)
        pos = if (end < 0) src.length else end + close.length
    }

    private fun readLongString(level: Int, start: Int) {
        pos += level + 2
        val close = "]" + "=".repeat(level) + "]"
        val end = src.indexOf(close, pos)
        val content = if (end < 0) { val s = src.substring(pos); pos = src.length; s }
                      else { val s = src.substring(pos, end); pos = end + close.length; s }
        tokens.add(Token(TT.STRING, content.trimStart('\n'), start))
    }

    private fun readName(start: Int) {
        while (pos < src.length && (src[pos].isLetterOrDigit() || src[pos] == '_')) pos++
        val word = src.substring(start, pos)
        val tt = KEYWORDS[word] ?: TT.NAME
        tokens.add(Token(tt, word, start))
    }

    private fun readNumber(start: Int) {
        if (src[pos] == '0' && pos + 1 < src.length && (src[pos+1] == 'x' || src[pos+1] == 'X')) {
            pos += 2
            while (pos < src.length && (src[pos].isDigit() || src[pos] in 'a'..'f' || src[pos] in 'A'..'F')) pos++
        } else {
            while (pos < src.length && src[pos].isDigit()) pos++
            if (pos < src.length && src[pos] == '.') {
                pos++
                while (pos < src.length && src[pos].isDigit()) pos++
            }
            if (pos < src.length && (src[pos] == 'e' || src[pos] == 'E')) {
                pos++
                if (pos < src.length && (src[pos] == '+' || src[pos] == '-')) pos++
                while (pos < src.length && src[pos].isDigit()) pos++
            }
        }
        tokens.add(Token(TT.NUMBER, src.substring(start, pos), start))
    }

    private fun readShortString(quote: Char, start: Int) {
        pos++ // skip opening quote
        val sb = StringBuilder()
        while (pos < src.length && src[pos] != quote) {
            if (src[pos] == '\\') {
                pos++
                when (if (pos < src.length) src[pos] else '\u0000') {
                    'n' -> sb.append('\n')
                    't' -> sb.append('\t')
                    'r' -> sb.append('\r')
                    '\\' -> sb.append('\\')
                    '\'' -> sb.append('\'')
                    '"' -> sb.append('"')
                    '0' -> sb.append('\u0000')
                    else -> { sb.append('\\'); sb.append(src[pos]) }
                }
                pos++
            } else {
                sb.append(src[pos++])
            }
        }
        if (pos < src.length) pos++ // closing quote
        tokens.add(Token(TT.STRING, sb.toString(), start))
    }

    private fun readSymbol(start: Int) {
        val c = src[pos]
        fun two(next: Char, tt2: TT, tt1: TT): TT = if (peek() == next) { pos++; tt2 } else tt1
        val tt = when (c) {
            '+' -> TT.PLUS
            '*' -> TT.STAR
            '/' -> TT.SLASH
            '%' -> TT.PERCENT
            '^' -> TT.CARET
            '#' -> TT.HASH
            '(' -> TT.LPAREN
            ')' -> TT.RPAREN
            '{' -> TT.LBRACE
            '}' -> TT.RBRACE
            '[' -> TT.LBRACKET
            ']' -> TT.RBRACKET
            ';' -> TT.SEMI
            ',' -> TT.COMMA
            '-' -> TT.MINUS
            '<' -> two('=', TT.LEQ, TT.LT)
            '>' -> two('=', TT.GEQ, TT.GT)
            '=' -> two('=', TT.EQ, TT.ASSIGN)
            '~' -> if (peek() == '=') { pos++; TT.NEQ } else error("unexpected '~' at $pos")
            ':' -> two(':', TT.COLONCOLON, TT.COLON)
            '.' -> when {
                peek() == '.' && pos + 2 < src.length && src[pos+2] == '.' -> { pos += 2; TT.VARARG }
                peek() == '.' -> { pos++; TT.DOTDOT }
                peek().isDigit() -> { readNumber(start); return }
                else -> TT.DOT
            }
            else -> error("unexpected char '$c' at $pos")
        }
        pos++
        tokens.add(Token(tt, src.substring(start, pos), start))
    }

    private fun emit(tt: TT, start: Int) { pos++; tokens.add(Token(tt, src.substring(start, pos), start)) }
}

// ── Parser ─────────────────────────────────────────────────────────────────

class LuaParser(src: String) {
    private val tokens: List<Token> = Lexer(src).tokenize()
    private var pos = 0

    private fun peek() = tokens[pos]
    private fun peek(offset: Int) = tokens[minOf(pos + offset, tokens.size - 1)]
    private fun advance() = tokens[pos++]
    private fun check(tt: TT) = peek().type == tt
    private fun match(tt: TT): Boolean { if (check(tt)) { advance(); return true }; return false }
    private fun expect(tt: TT): Token {
        if (!check(tt)) error("expected $tt but got ${peek().type} ('${peek().value}') at pos ${peek().pos}")
        return advance()
    }

    fun parseChunk(): LuaChunk = LuaChunk(parseBlock()).also { expect(TT.EOF) }

    private fun parseBlock(): LuaBlock {
        val stmts = mutableListOf<LuaStatement>()
        var ret: LuaReturnStatement? = null
        while (true) {
            skipSemis()
            if (isBlockEnd()) break
            if (check(TT.RETURN)) {
                ret = parseReturnStatement()
                match(TT.SEMI)
                break
            }
            val s = parseStatement() ?: break
            stmts.add(s)
        }
        return LuaBlock(stmts, ret)
    }

    private fun skipSemis() { while (check(TT.SEMI)) advance() }

    private fun isBlockEnd() = when (peek().type) {
        TT.EOF, TT.END, TT.ELSE, TT.ELSEIF, TT.UNTIL -> true
        else -> false
    }

    private fun parseReturnStatement(): LuaReturnStatement {
        expect(TT.RETURN)
        val exprs = if (!isBlockEnd() && !check(TT.SEMI)) parseExpressionList() else emptyList()
        return LuaReturnStatement(exprs)
    }

    private fun parseStatement(): LuaStatement? = when (peek().type) {
        TT.SEMI -> { advance(); LuaEmptyStatement }
        TT.IF -> parseIf()
        TT.WHILE -> parseWhile()
        TT.DO -> parseDo()
        TT.FOR -> parseFor()
        TT.REPEAT -> parseRepeat()
        TT.FUNCTION -> parseFunctionDecl()
        TT.LOCAL -> parseLocal()
        TT.GOTO -> { advance(); LuaGotoStatement(expect(TT.NAME).value) }
        TT.BREAK -> { advance(); LuaBreakStatement }
        TT.COLONCOLON -> parseLabel()
        else -> parseExpressionStatement()
    }

    private fun parseIf(): LuaIfStatement {
        expect(TT.IF)
        val cond = parseExpression()
        expect(TT.THEN)
        val thenBlock = parseBlock()
        val elseifs = mutableListOf<LuaElseIfClause>()
        var elseBlock: LuaBlock? = null
        while (check(TT.ELSEIF)) {
            advance()
            val ec = parseExpression()
            expect(TT.THEN)
            elseifs.add(LuaElseIfClause(ec, parseBlock()))
        }
        if (match(TT.ELSE)) elseBlock = parseBlock()
        expect(TT.END)
        return LuaIfStatement(cond, thenBlock, elseifs, elseBlock)
    }

    private fun parseWhile(): LuaWhileStatement {
        expect(TT.WHILE)
        val cond = parseExpression()
        expect(TT.DO)
        val body = parseBlock()
        expect(TT.END)
        return LuaWhileStatement(cond, body)
    }

    private fun parseDo(): LuaDoStatement {
        expect(TT.DO)
        val b = parseBlock()
        expect(TT.END)
        return LuaDoStatement(b)
    }

    private fun parseFor(): LuaStatement {
        expect(TT.FOR)
        val firstName = expect(TT.NAME).value
        return if (check(TT.ASSIGN)) {
            advance()
            val start = parseExpression()
            expect(TT.COMMA)
            val limit = parseExpression()
            val step = if (match(TT.COMMA)) parseExpression() else null
            expect(TT.DO)
            val body = parseBlock()
            expect(TT.END)
            LuaNumericForStatement(firstName, start, limit, step, body)
        } else {
            val names = mutableListOf(firstName)
            while (match(TT.COMMA)) names.add(expect(TT.NAME).value)
            expect(TT.IN)
            val iters = parseExpressionList()
            expect(TT.DO)
            val body = parseBlock()
            expect(TT.END)
            LuaGenericForStatement(names, iters, body)
        }
    }

    private fun parseRepeat(): LuaRepeatStatement {
        expect(TT.REPEAT)
        val body = parseBlock()
        expect(TT.UNTIL)
        val cond = parseExpression()
        return LuaRepeatStatement(body, cond)
    }

    private fun parseFunctionDecl(): LuaFunctionDeclarationStatement {
        expect(TT.FUNCTION)
        val name = parseFunctionName()
        val body = parseFunctionBody(name.methodName != null)
        return LuaFunctionDeclarationStatement(name, body)
    }

    private fun parseFunctionName(): LuaFunctionName {
        val base = expect(TT.NAME).value
        val path = mutableListOf<String>()
        var method: String? = null
        while (check(TT.DOT)) {
            advance()
            path.add(expect(TT.NAME).value)
        }
        if (check(TT.COLON)) {
            advance()
            method = expect(TT.NAME).value
        }
        return LuaFunctionName(base, path, method)
    }

    private fun parseFunctionBody(withSelf: Boolean = false): LuaFunctionBody {
        expect(TT.LPAREN)
        val params = mutableListOf<String>()
        var vararg = false
        if (withSelf) params.add("self")
        if (!check(TT.RPAREN)) {
            if (check(TT.VARARG)) { advance(); vararg = true }
            else {
                params.add(expect(TT.NAME).value)
                while (match(TT.COMMA)) {
                    if (check(TT.VARARG)) { advance(); vararg = true; break }
                    params.add(expect(TT.NAME).value)
                }
            }
        }
        expect(TT.RPAREN)
        val block = parseBlock()
        expect(TT.END)
        return LuaFunctionBody(params, vararg, block)
    }

    private fun parseLocal(): LuaStatement {
        expect(TT.LOCAL)
        return if (check(TT.FUNCTION)) {
            advance()
            val name = expect(TT.NAME).value
            val body = parseFunctionBody()
            LuaLocalFunctionDeclarationStatement(name, body)
        } else {
            val names = mutableListOf(expect(TT.NAME).value)
            while (match(TT.COMMA)) names.add(expect(TT.NAME).value)
            val inits = if (match(TT.ASSIGN)) parseExpressionList() else emptyList()
            LuaLocalDeclarationStatement(names, inits)
        }
    }

    private fun parseLabel(): LuaLabelStatement {
        expect(TT.COLONCOLON)
        val name = expect(TT.NAME).value
        expect(TT.COLONCOLON)
        return LuaLabelStatement(name)
    }

    private fun parseExpressionStatement(): LuaStatement? {
        val expr = parseSuffixedExpression() ?: return null
        // assignment
        if (check(TT.ASSIGN) || check(TT.COMMA)) {
            val targets = mutableListOf<LuaVariable>()
            targets.add(expr as? LuaVariable ?: error("invalid assignment target"))
            while (match(TT.COMMA)) {
                targets.add(parseSuffixedExpression() as? LuaVariable ?: error("invalid assignment target"))
            }
            expect(TT.ASSIGN)
            val values = parseExpressionList()
            return LuaAssignStatement(targets, values)
        }
        // function call as statement
        if (expr is LuaFunctionCall) return expr as LuaStatement
        error("unexpected expression statement at ${peek().pos}")
    }

    private fun parseExpressionList(): List<LuaExpression> {
        val list = mutableListOf(parseExpression())
        while (match(TT.COMMA)) list.add(parseExpression())
        return list
    }

    // ── Expression parsing (Pratt-style) ──

    private fun parseExpression(): LuaExpression = parseOr()

    private fun parseOr(): LuaExpression {
        var left = parseAnd()
        while (check(TT.OR)) { advance(); left = LuaBinaryExpression(left, LuaBinaryOperator.OR, parseAnd()) }
        return left
    }

    private fun parseAnd(): LuaExpression {
        var left = parseComparison()
        while (check(TT.AND)) { advance(); left = LuaBinaryExpression(left, LuaBinaryOperator.AND, parseComparison()) }
        return left
    }

    private fun parseComparison(): LuaExpression {
        var left = parseConcat()
        while (true) {
            val op = when (peek().type) {
                TT.LT -> LuaBinaryOperator.LOWER
                TT.GT -> LuaBinaryOperator.GREATER
                TT.LEQ -> LuaBinaryOperator.LOWER_EQUALS
                TT.GEQ -> LuaBinaryOperator.GREATER_EQUALS
                TT.EQ -> LuaBinaryOperator.EQUALS
                TT.NEQ -> LuaBinaryOperator.NOT_EQUALS
                else -> break
            }
            advance(); left = LuaBinaryExpression(left, op, parseConcat())
        }
        return left
    }

    private fun parseConcat(): LuaExpression {
        val left = parseAddSub()
        if (check(TT.DOTDOT)) { advance(); return LuaBinaryExpression(left, LuaBinaryOperator.CONCAT, parseConcat()) }
        return left
    }

    private fun parseAddSub(): LuaExpression {
        var left = parseMulDiv()
        while (true) {
            val op = when (peek().type) {
                TT.PLUS -> LuaBinaryOperator.ADD
                TT.MINUS -> LuaBinaryOperator.SUB
                else -> break
            }
            advance(); left = LuaBinaryExpression(left, op, parseMulDiv())
        }
        return left
    }

    private fun parseMulDiv(): LuaExpression {
        var left = parseUnary()
        while (true) {
            val op = when (peek().type) {
                TT.STAR -> LuaBinaryOperator.MUL
                TT.SLASH -> LuaBinaryOperator.DIV
                TT.PERCENT -> LuaBinaryOperator.MOD
                else -> break
            }
            advance(); left = LuaBinaryExpression(left, op, parseUnary())
        }
        return left
    }

    private fun parseUnary(): LuaExpression = when (peek().type) {
        TT.NOT -> { advance(); LuaUnaryExpression(LuaUnaryOperator.NOT, parseUnary()) }
        TT.MINUS -> { advance(); LuaUnaryExpression(LuaUnaryOperator.MINUS, parseUnary()) }
        TT.HASH -> { advance(); LuaUnaryExpression(LuaUnaryOperator.LEN, parseUnary()) }
        else -> parsePower()
    }

    private fun parsePower(): LuaExpression {
        val base = parseSuffixedExpression() ?: error("expected expression at ${peek().pos}")
        if (check(TT.CARET)) { advance(); return LuaBinaryExpression(base, LuaBinaryOperator.POW, parseUnary()) }
        return base
    }

    private fun parseSuffixedExpression(): LuaExpression? {
        var expr = parsePrimaryExpression() ?: return null
        while (true) {
            expr = when {
                check(TT.DOT) -> {
                    advance()
                    val field = expect(TT.NAME).value
                    LuaFieldAccessVariable(field, expr)
                }
                check(TT.LBRACKET) -> {
                    advance()
                    val key = parseExpression()
                    expect(TT.RBRACKET)
                    LuaTableAccessVariable(expr, key)
                }
                check(TT.COLON) -> {
                    advance()
                    val method = expect(TT.NAME).value
                    val args = parseArguments()
                    LuaMethodFunctionCall(expr, method, args)
                }
                check(TT.LPAREN) || check(TT.LBRACE) || check(TT.STRING) -> {
                    val args = parseArguments()
                    LuaStandardFunctionCall(expr, args)
                }
                else -> break
            }
        }
        return expr
    }

    private fun parsePrimaryExpression(): LuaExpression? = when (peek().type) {
        TT.NAME -> { val name = advance().value; LuaSimpleVariable(name) }
        TT.LPAREN -> {
            advance()
            val inner = parseExpression()
            expect(TT.RPAREN)
            LuaParenthesizedExpression(inner)
        }
        TT.NUMBER -> {
            val v = advance().value
            LuaNumber(v.toDoubleOrNull() ?: v.toLong(16).toDouble())
        }
        TT.STRING -> LuaString(advance().value)
        TT.TRUE -> { advance(); LuaBoolean(true) }
        TT.FALSE -> { advance(); LuaBoolean(false) }
        TT.NIL -> { advance(); LuaNil }
        TT.VARARG -> { advance(); LuaVararg }
        TT.FUNCTION -> { advance(); LuaFunctionLiteral(parseFunctionBody()) }
        TT.LBRACE -> parseTableConstructor()
        else -> null
    }

    private fun parseArguments(): LuaArguments = when {
        check(TT.LPAREN) -> {
            advance()
            val exprs = if (!check(TT.RPAREN)) parseExpressionList() else emptyList()
            expect(TT.RPAREN)
            LuaPositionalArguments(exprs)
        }
        check(TT.LBRACE) -> LuaTableArgument(parseTableConstructor())
        check(TT.STRING) -> LuaStringArgument(LuaString(advance().value))
        else -> error("expected function arguments at ${peek().pos}")
    }

    private fun parseTableConstructor(): LuaTableContructor {
        expect(TT.LBRACE)
        val fields = mutableListOf<LuaTableField>()
        while (!check(TT.RBRACE)) {
            fields.add(parseTableField())
            if (!match(TT.COMMA) && !match(TT.SEMI)) break
        }
        expect(TT.RBRACE)
        return LuaTableContructor(fields)
    }

    private fun parseTableField(): LuaTableField = when {
        check(TT.LBRACKET) -> {
            advance()
            val key = parseExpression()
            expect(TT.RBRACKET)
            expect(TT.ASSIGN)
            LuaKeyedField(key, parseExpression())
        }
        check(TT.NAME) && peek(1).type == TT.ASSIGN -> {
            val name = advance().value
            advance() // ASSIGN
            LuaNamedField(name, parseExpression())
        }
        else -> LuaPositionalField(parseExpression())
    }
}

// ── Entry point ─────────────────────────────────────────────────────────────

fun parseLua(src: String): LuaChunk = LuaParser(src).parseChunk()