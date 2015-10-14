#version 140

// Marcel van Workum - 300313949

in vec2 position;

out vec2 textureCoords;

uniform mat4 transformationMatrix;

void main(void){

	// Applies the transformation scale and position and gets the texture
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
	textureCoords = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);
}