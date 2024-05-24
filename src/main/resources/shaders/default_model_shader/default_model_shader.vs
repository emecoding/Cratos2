#version 330 core
layout(location = 0) in vec3 a_pos;
layout(location = 1) in vec2 tex_coord;

uniform mat4 transform_3D;
uniform mat4 camera_3D_view;
uniform mat4 perspective_projection;

out vec2 out_tex_coord;

void main()
{
    gl_Position = perspective_projection * camera_3D_view * transform_3D * vec4(a_pos, 1.0);
    out_tex_coord = tex_coord;
}

