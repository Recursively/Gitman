#version 330 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
// Maximum number of lights that can affect an entity
// is set to 4 to keep performance consistent
in vec3 toLightVector[1];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform sampler2D textureSampler;
uniform vec3 lightColour[1];
uniform vec3 attenuation[1];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

void main(void){

	vec4 blendMapColour = texture(blendMap, pass_textureCoords);

	float backgroundTextureAmount = 1 - (blendMapColour.r, blendMapColour.g, blendMapColour.b);
	vec2 tileCoords = pass_textureCoords * 40.0;
	vec4 backgroundTextureColour = texture(backgroundTexture, tileCoords) * backgroundTextureAmount;
	vec4 rTextureColour = texture(rTexture, tileCoords) * blendMapColour.r;
	vec4 gTextureColour = texture(gTexture, tileCoords) * blendMapColour.g;
	vec4 bTextureColour = texture(bTexture, tileCoords) * blendMapColour.b;

	vec4 totalColour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);

	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	for(int i = 0; i < 1; i++) {
		// lighting calculation
		float distance = length(toLightVector[i]);
		float attenuationFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDot1 = dot(unitNormal, unitLightVector);
		float brightness = max(nDot1, 0.0);
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);
		totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attenuationFactor;
		totalSpecular = totalSpecular + (dampedFactor *  reflectivity * lightColour[i]) / attenuationFactor;
	}

	// This should be maxed to 0.2 in the outdoors scene
	totalDiffuse = max(totalDiffuse, 0.2);

	out_Color = vec4(totalDiffuse, 1.0) * totalColour + vec4(totalSpecular, 1.0);
	out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);

}