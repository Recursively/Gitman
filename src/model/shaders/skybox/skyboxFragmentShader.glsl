#version 330

in vec3 textureCoords;
out vec4 out_Color;

uniform samplerCube cubeMap;
uniform samplerCube cubeMap2;
// 0 -> just cubeMap 1.0 -> just cubeMap2
uniform float blendFactor;
uniform vec3 fogColour;

const float lowerLimit = 0.0;
const float upperLimit = 30.0;

void main(void){

    // gets both skybox textures
    vec4 texture1 = texture(cubeMap, textureCoords);
    vec4 texture2 = texture(cubeMap2, textureCoords);

    // fixes the two
    vec4 final_colour = mix(texture1, texture2, blendFactor);

    // gets a blending factor to blend the textures
    float factor = (textureCoords.y - lowerLimit) / (upperLimit - lowerLimit);
    factor = clamp(factor, 0.0, 1.0);

    // finally outputs
    out_Color = mix(vec4(fogColour, 1.0), final_colour, factor);
}