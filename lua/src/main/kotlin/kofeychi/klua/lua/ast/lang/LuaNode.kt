package kofeychi.klua.lua.ast.lang

import kofeychi.klua.lua.sink.LuaSinkContext
import kofeychi.klua.util.ISinkable
import kofeychi.klua.util.IVisitable
import kofeychi.klua.util.StringSink

interface LuaNode : ISinkable<StringSink, LuaSinkContext>, IVisitable<LuaNode>