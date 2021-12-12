package com.husker.minuicore.pipeline.gl

const val VERTICES_ATTRIBUTE = 0
const val TEXTCOORD_ATTRIBUTE = 1

const val GL_POINTS = 0x0
const val GL_LINES = 0x1
const val GL_LINE_LOOP = 0x2
const val GL_LINE_STRIP = 0x3
const val GL_TRIANGLES = 0x4
const val GL_TRIANGLE_STRIP = 0x5
const val GL_TRIANGLE_FAN = 0x6

const val GL_BYTE           = 0x1400
const val GL_UNSIGNED_BYTE  = 0x1401
const val GL_SHORT          = 0x1402
const val GL_UNSIGNED_SHORT = 0x1403
const val GL_INT            = 0x1404
const val GL_UNSIGNED_INT   = 0x1405
const val GL_FLOAT          = 0x1406
const val GL_DOUBLE         = 0x140A

const val GL_COLOR_BUFFER_BIT = 0x4000
const val GL_DEPTH_BUFFER_BIT = 0x100
const val GL_TEXTURE_2D = 0xDE1
const val GL_TEXTURE_WRAP_S = 0x2802
const val GL_TEXTURE_WRAP_T = 0x2803
const val GL_TEXTURE_MAG_FILTER = 0x2800
const val GL_TEXTURE_MIN_FILTER = 0x2801
const val GL_CLAMP_TO_EDGE = 0x812F
const val GL_NEAREST = 0x2600
const val GL_LINEAR = 0x2601
const val GL_RGB = 0x1907
const val GL_RGBA = 0x1908
const val GL_RED = 0x1903
const val GL_TEXTURE_WIDTH = 0x1000
const val GL_TEXTURE_HEIGHT = 0x1001
const val GL_VERTEX_SHADER = 0x8B31
const val GL_COMPILE_STATUS = 0x8B81
const val GL_LINK_STATUS = 0x8B82
const val GL_VALIDATE_STATUS = 0x8B83
const val GL_FRAGMENT_SHADER = 0x8B30
const val GL_ALPHA_TEST = 0xBC0
const val GL_BLEND = 0xBE2
const val GL_SRC_ALPHA = 0x302
const val GL_ONE_MINUS_SRC_ALPHA = 0x303
const val GL_DEPTH_TEST = 0xB71
const val GL_UNPACK_ALIGNMENT = 0xCF5

// New
external fun applyMatrix(matrixElements: FloatArray)
external fun setAttribute(index: Int, length: Int, array: DoubleArray)

external fun glDrawArrays(mode: Int, first: Int, last: Int)
external fun glViewport(x: Int, y: Int, width: Int, height: Int)
external fun glBlendFunc(sfactor: Int, dfactor: Int)
external fun glClear(mask: Int)
external fun glClearColor(r: Float, g: Float, b: Float, a: Float)
external fun glEnable(target: Int)
external fun glFlush()
external fun glGenTextures(): Int
external fun glBindTexture(target: Int, texture: Int)
external fun glTexParameteri(target: Int, pname: Int, param: Int)
external fun glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, address: Long)
external fun glGetTexLevelParameteri(target: Int, level: Int, pname: Int): Int
external fun glPixelStorei(pname: Int, param: Int)
external fun glUniformMatrix4f(location: Int, matrix: FloatArray)

// Shader-related
external fun glUseProgram(program: Int)
external fun glCreateShader(type: Int): Int
external fun glShaderSource(shader: Int, source: ByteArray)
external fun glCompileShader(shader: Int)
external fun glGetShaderi(shader: Int, pname: Int): Int
external fun glAttachShader(program: Int, shader: Int)
external fun glLinkProgram(program: Int)
external fun glGetProgrami(program: Int, pname: Int): Int
external fun glValidateProgram(program: Int)
external fun glDeleteShader(shader: Int)
external fun glGetUniformLocation(program: Int, name: String): Int
external fun glUniform1f(location: Int, v1: Float)
external fun glUniform2f(location: Int, v1: Float, v2: Float)
external fun glUniform3f(location: Int, v1: Float, v2: Float, v3: Float)
external fun glUniform4f(location: Int, v1: Float, v2: Float, v3: Float, v4: Float)
external fun glCreateProgram(): Int
external fun glGetShaderInfoLog(shader: Int): String
external fun glGetProgramInfoLog(shader: Int): String



