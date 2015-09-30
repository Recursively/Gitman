package model.entities.staticEntity;

import model.models.ModelData;
import model.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Marcel on 29/09/15.
 */
public class SwipeCard extends StaticEntity {
    public SwipeCard(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int textureIndex, ModelData modelData) {
        super(model, position, rotX, rotY, rotZ, scale, textureIndex, modelData);
    }

    @Override
    public boolean checkCollision(Vector3f position) {
        float diffX = Math.abs(position.getX() - origin.getX());
        float diffZ = Math.abs(position.getZ() - origin.getZ());

        float diffY = Math.abs(position.getY() - origin.getY());

        // Ascending if statements to help terminate early
        if (diffX < radiusX) {
            if (diffZ < radiusZ) {
                return true;
            }
        }
        return false;
    }
}
