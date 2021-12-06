package com.husker.minuicore.pipeline.gl

import com.husker.minuicore.nGetBufferAddress
import java.nio.Buffer



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
const val GL_UNSIGNED_BYTE = 0x1401
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
const val GL_PROJECTION = 0x1701
const val GL_QUADS = 0x7

// deprecated \/
external fun glMatrixMode(mode: Int)
external fun glOrtho(l: Double, r: Double, b: Double, t: Double, n: Double, f: Double)
external fun glViewport(x: Int, y: Int, width: Int, height: Int)
external fun glBegin(a: Int)
external fun glEnd()
external fun glVertex2d(x: Double, y: Double)
external fun glColor3d(r: Double, g: Double, b: Double)
external fun glLoadIdentity()
external fun glBlendFunc(sfactor: Int, dfactor: Int)
// deprecated /\

external fun glClear(mask: Int)
external fun glClearColor(r: Float, g: Float, b: Float, a: Float)

external fun glEnable(target: Int)
external fun glFlush()

external fun glGenTextures(): Int
external fun glBindTexture(target: Int, texture: Int)
external fun glTexParameteri(target: Int, pname: Int, param: Int)
external fun glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, address: Long)
external fun glGetTexLevelParameteri(target: Int, level: Int, pname: Int): Int

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

fun glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, buffer: Buffer){
    glTexImage2D(target, level, internalformat, width, height, border, format, type, nGetBufferAddress(buffer))
}