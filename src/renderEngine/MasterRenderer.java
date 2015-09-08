package renderEngine;

import entities.Entity;
import models.TexturedModel;
import shaders.StaticShader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    private StaticShader shader = new StaticShader();
    private Renderer renderer = new Renderer(shader);

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
}
