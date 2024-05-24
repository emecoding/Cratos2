#version 330 core
layout(location = 0) in vec2 a_pos;

uniform mat4 transform_2D;
uniform mat4 camera_2D_view;
uniform mat4 ortho_projection;


void main()
{
    gl_Position = ortho_projection * camera_2D_view * transform_2D * vec4(a_pos, 1.0, 1.0);
}

