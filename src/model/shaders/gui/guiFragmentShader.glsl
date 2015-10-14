#version 140

// Marcel van Workum - 300313949

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;

void main(void){
	// ezi e
	out_Color = texture(guiTexture,textureCoords);
}