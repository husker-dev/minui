#version 330 core

out vec2 TexCoords;

void main(){
    //gl_Position = vec4(vertex, 1.0);
    TexCoords = vertex.xy;
}  