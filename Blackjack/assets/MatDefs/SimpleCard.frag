varying vec2 texCoord;

uniform sampler2D m_ColorMap;
uniform sampler2D m_ColorMap2;
uniform vec4 m_Color;

uniform float g_Time;

void main() {
    vec2 uv = texCoord;
    vec4 texColor;
    vec4 color;

    if (uv.x > 0.5){
        uv.x = (uv.x - 0.5) *2.0;
        texColor = texture2D(m_ColorMap, uv);
    } else {
        uv.x = uv.x *2.0;
        texColor = texture2D(m_ColorMap2, uv);
    }
    color = texColor;
    gl_FragColor = color;
}

