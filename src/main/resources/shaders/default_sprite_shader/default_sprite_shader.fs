#version 330 core

in vec2 out_tex_coord;

uniform sampler2D texture_sampler;

void main()
{
    gl_FragColor = texture(texture_sampler, out_tex_coord);
}