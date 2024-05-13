#version 330 core

in vec2 out_tex_coord;

uniform sampler2D texture_sampler;

uniform vec4 diffuse_color;
uniform int has_texture;

//TODO lighting
//TODO struct Material

void main()
{
    gl_FragColor = diffuse_color;
    if(has_texture == 1)
    {
        gl_FragColor *= texture(texture_sampler, out_tex_coord);
    }
}