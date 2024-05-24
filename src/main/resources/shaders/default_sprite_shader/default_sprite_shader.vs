#version 330 core
layout(location = 0) in vec2 a_pos;
layout(location = 1) in vec2 tex_coord;

uniform mat4 transform_2D;
uniform mat4 camera_2D_view;
uniform mat4 ortho_projection;

out vec2 out_tex_coord;

void main()
{
    gl_Position = ortho_projection * camera_2D_view * transform_2D * vec4(a_pos, 1.0, 1.0);
    out_tex_coord = tex_coord;
}

