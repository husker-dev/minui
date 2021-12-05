#version 330 core


in vec4 color;
out vec4 out_color;

uniform sampler2D sampler;
uniform vec4 textColor;

void main()
{
    out_color = texture(sampler, gl_FragCoord.xy);
}