package com.husker.minuicore.pipeline.gl

import com.husker.minuicore.MCore
import com.husker.minuicore.transform.Matrix
import com.husker.minuicore.platform.c_str
import java.nio.charset.StandardCharsets



class GLShader(vertex: String = "", fragment: String = "") {

    companion object{

        fun clearShaders(){
            glUseProgram(0)
        }

        fun fromResource(vertexPath: String = "", fragmentPath: String = ""): GLShader{
            var vertex = ""
            var fragment = ""
            if(vertexPath.isNotEmpty())
                vertex = this::class.java.getResourceAsStream((if(vertexPath.startsWith("/")) "" else "/") + vertexPath)!!
                    .readBytes().toString(StandardCharsets.UTF_8)
            if(fragmentPath.isNotEmpty())
                fragment = this::class.java.getResourceAsStream((if(fragmentPath.startsWith("/")) "" else "/") + fragmentPath)!!
                    .readBytes().toString(StandardCharsets.UTF_8)
            return GLShader(vertex, fragment)
        }
    }

    var program = 0

    init{
        (MCore.pipeline.resourceFactory as GLResourceFactory).invokeSync {
            program = glCreateProgram()
            var vx = 0
            var fs = 0

            if(vertex.isNotEmpty()){
                vx = glCreateShader(GL_VERTEX_SHADER)
                glShaderSource(vx, vertex.c_str)
                glCompileShader(vx)
                if(glGetShaderi(vx, GL_COMPILE_STATUS) != 1)
                    throw ShaderException("Vertex shader compilation error", glGetShaderInfoLog(vx))
                glAttachShader(program, vx)
            }
            if(fragment.isNotEmpty()){
                fs = glCreateShader(GL_FRAGMENT_SHADER)
                glShaderSource(fs, fragment.c_str)
                glCompileShader(fs)
                if(glGetShaderi(fs, GL_COMPILE_STATUS) != 1)
                    throw ShaderException("Fragment shader compilation error", glGetShaderInfoLog(fs))
                glAttachShader(program, fs)
            }

            glLinkProgram(program)
            if(glGetProgrami(program, GL_LINK_STATUS) != 1)
                throw ShaderException("Shader linking error", glGetProgramInfoLog(program))
            glValidateProgram(program)
            if(glGetProgrami(program, GL_VALIDATE_STATUS) != 1)
                throw ShaderException("Shader validation error", glGetProgramInfoLog(program))

            if(vertex.isNotEmpty())
                glDeleteShader(vx)
            if(fragment.isNotEmpty())
                glDeleteShader(fs)
        }
    }

    fun setMatrixVariable(name: String, matrix: Matrix){
        val location = glGetUniformLocation(program, name)
        glUniformMatrix4f(location, matrix.elements)
    }

    fun setVariable(name: String, vararg values: Float){
        val location = glGetUniformLocation(program, name)
        when (values.size) {
            1 -> glUniform1f(location, values[0])
            2 -> glUniform2f(location, values[0], values[1])
            3 -> glUniform3f(location, values[0], values[1], values[2])
            4 -> glUniform4f(location, values[0], values[1], values[2], values[3])
            else -> throw UnsupportedOperationException("Too much values")
        }
    }

    fun use(){
        glUseProgram(program)
    }
}

class ShaderException(title: String, log: String): Exception(title + "\n\t" + log.replace("\n", "\n\t"))