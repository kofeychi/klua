package kofeychi.klua.util

interface IVisitable<out S : IVisitable<S>> {
    fun visit(visitFunction: (S) -> Unit)
}

interface ISink<in T> {
    fun write(item: T)
}

interface IStringSink : ISink<CharSequence> {
    fun writeLine(line: CharSequence) {
        write(line)
        write("\n")
    }

    operator fun plusAssign(text: CharSequence) {
        write(text)
    }
}

interface ISinkable<in S : ISink<*>, in C> {
    fun writeTo(sink: S, context: C)
    fun writeTo(sink: S)
}

class StringSink(
    private val builder: StringBuilder = StringBuilder()
) : IStringSink {
    override fun write(item: CharSequence) {
        builder.append(item)
    }
    fun result(): String = builder.toString()
    override fun toString(): String = result()
}