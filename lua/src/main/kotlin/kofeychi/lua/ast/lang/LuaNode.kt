package kofeychi.lua.ast.lang

import kofeychi.lua.sink.LuaSinkContext
import kofeychi.util.*

interface LuaNode : ISinkable<StringSink, LuaSinkContext>, IVisitable<LuaNode>