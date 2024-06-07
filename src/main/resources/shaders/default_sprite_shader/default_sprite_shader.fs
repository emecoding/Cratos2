#version 330 core

in vec2 out_tex_coord;

uniform sampler2D texture_sampler;
uniform int flip_horizontally;
uniform int flip_vertically;

void main()
{
    vec2 texCoords = vec2(out_tex_coord);
    if(flip_horizontally == 1)
        texCoords.x = 1.0 - out_tex_coord.x;
    if(flip_vertically == 1)
        texCoords.y = 1.0 - out_tex_coord.y;


    gl_FragColor = texture(texture_sampler, texCoords);
}