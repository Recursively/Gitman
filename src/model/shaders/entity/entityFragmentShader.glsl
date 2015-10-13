#version 330 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
// Maximum number of lights that can affect an entity
// is set to 4 to keep performance consistent
in vec3 toLightVector[5];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColour[5];
uniform vec3 attenuation[5];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

void main(void){

	// parse normals
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);

	// init lighting
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);


	for (int i = 0; i < 5; i++) {
			// Lighting calculation
			float distance = length(toLightVector[i]);

			// get the attentation factor
			float attenuationFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
        	vec3 unitLightVector = normalize(toLightVector[i]);
        	float nDot1 = dot(unitNormal, unitLightVector);
        	float brightness = max(nDot1, 0.0);

        	// light vector is opposite to normal
        	vec3 lightDirection = -unitLightVector;

        	// calculate reflective lighting
        	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
        	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
        	specularFactor = max(specularFactor, 0.0);
        	float dampedFactor = pow(specularFactor, shineDamper);

        	// finally calculate diffuse and specular
        	totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attenuationFactor;
        	totalSpecular = totalSpecular + (dampedFactor *  reflectivity * lightColour[i]) / attenuationFactor;
	}

	// clamp the diffuse so that it can't be super dark
	totalDiffuse = max(totalDiffuse, 0.2);

	// Gets the texture colour and checks the alpha
	vec4 textureColour = texture(textureSampler, pass_textureCoords);
	if(textureColour.a < 0.5) {
		discard;
	}

	// final output
	out_Color = vec4(totalDiffuse, 1.0) * textureColour + vec4(totalSpecular, 1.0);
	out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);

}