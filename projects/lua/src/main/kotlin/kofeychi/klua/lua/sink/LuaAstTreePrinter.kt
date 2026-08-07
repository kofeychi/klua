package kofeychi.klua.lua.sink

import java.lang.reflect.*
import java.util.*

class TreePrinter {
    fun print(obj: Any?) {
        if (obj == null) {
            println("null")
            return
        }
        val visited = IdentityHashMap<Any, Boolean>()
        printInternal(obj, "", true, visited)
    }

    private fun printInternal(
        obj: Any?,
        indent: String,
        isLast: Boolean,
        visited: IdentityHashMap<Any, Boolean>
    ) {
        if (obj == null) {
            println(indent + getPrefix(isLast) + "null")
            return
        }

        println(indent + getPrefix(isLast) + getNodeName(obj))

        if (visited.containsKey(obj)) {
            println(indent + getChildIndent(isLast) + "|> [Circular Reference]")
            return
        }

        if (!isSimpleType(obj::class.java)) {
            visited[obj] = true
        }

        val childIndent = indent + getChildIndent(isLast)

        when {
            obj is Collection<*> -> handleCollection(obj, childIndent, visited)
            obj is Map<*, *> -> handleMap(obj, childIndent, visited)
            obj.javaClass.isArray -> handleArray(obj, childIndent, visited)
            isSimpleType(obj.javaClass) -> {}
            else -> handleObjectFields(obj, childIndent, visited)
        }
    }

    private fun getPrefix(isLast: Boolean): String {
        return if (isLast) "+> " else "|> "
    }

    private fun getChildIndent(isLast: Boolean): String {
        return if (isLast) "    " else "|   "
    }

    private fun getNodeName(obj: Any): String {
        val clazz = obj.javaClass
        return when {
            isSimpleType(clazz) -> "[value=$obj]"
            obj is Collection<*> -> "${clazz.simpleName} (size=${obj.size})"
            obj is Map<*, *> -> "${clazz.simpleName} (size=${obj.size})"
            clazz.isArray -> "${clazz.simpleName} (length=${java.lang.reflect.Array.getLength(obj)})"
            else -> clazz.simpleName
        }
    }

    private fun handleCollection(collection: Collection<*>, indent: String, visited: IdentityHashMap<Any, Boolean>) {
        val iterator = collection.iterator()
        var i = 0
        val size = collection.size
        while (iterator.hasNext()) {
            val element = iterator.next()
            val isLastChild = (i == size - 1)
            printInternal(element, indent, isLastChild, visited)
            i++
        }
    }

    private fun handleMap(map: Map<*, *>, indent: String, visited: IdentityHashMap<Any, Boolean>) {
        val iterator = map.entries.iterator()
        var i = 0
        val size = map.size
        while (iterator.hasNext()) {
            val (key, value) = iterator.next()
            val isLastChild = (i == size - 1)

            println(indent + getPrefix(isLastChild) + "[key=$key]")
            val valueIndent = indent + getChildIndent(isLastChild)
            printInternal(value, valueIndent, true, visited)
            i++
        }
    }

    private fun handleArray(array: Any, indent: String, visited: IdentityHashMap<Any, Boolean>) {
        val length = java.lang.reflect.Array.getLength(array)
        for (i in 0 until length) {
            val element = java.lang.reflect.Array.get(array, i)
            val isLastChild = (i == length - 1)
            printInternal(element, indent, isLastChild, visited)
        }
    }

    private fun handleObjectFields(obj: Any, indent: String, visited: IdentityHashMap<Any, Boolean>) {
        val fields = getAllFields(obj.javaClass)
            .filter { !Modifier.isStatic(it.modifiers) }
            .filter { !Modifier.isTransient(it.modifiers) }
            .toList()

        for (i in fields.indices) {
            val field = fields[i]
            field.isAccessible = true
            val isLastChild = (i == fields.size - 1)

            try {
                val value = field.get(obj)

                if (value != null && isSimpleType(value.javaClass)) {
                    println(indent + getPrefix(isLastChild) + "${field.name}: [value=$value]")
                } else {
                    println(indent + getPrefix(isLastChild) + "${field.name}:")
                    val valueIndent = indent + getChildIndent(isLastChild)
                    printInternal(value, valueIndent, true, visited)
                }

            } catch (e: IllegalAccessException) {
                println(indent + getPrefix(isLastChild) + "${field.name}: [Access Denied]")
            }
        }
    }

    private fun getAllFields(type: Class<*>): Sequence<Field> {
        var currentLevel: Class<*>? = type
        return sequence {
            while (currentLevel != null && currentLevel != Any::class.java) {
                yieldAll(currentLevel !!.declaredFields.toList())
                currentLevel = currentLevel !!.superclass
            }
        }
    }

    private fun isSimpleType(clazz: Class<*>): Boolean {
        return clazz.isPrimitive ||
                clazz == String::class.java ||
                Number::class.java.isAssignableFrom(clazz) ||
                clazz == Boolean::class.javaObjectType ||
                clazz == Char::class.javaObjectType ||
                clazz.isEnum ||
                clazz == java.util.Date::class.java
    }
}