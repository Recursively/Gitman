package model.entities.staticEntity;

import model.models.ModelData;
import model.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Implementation of a static entity which is collidable.
 *
 * Interestingly, this class is pointless and could just be a static entity.
 *
 * @author Marcel van Workum
 */
public class CollidableEntity extends StaticEntity {

    /**
     * Instantiates a new Collidable entity.
     *
     * @param model        the model
     * @param position     the position
     * @param rotX         the rot x
     * @param rotY         the rot y
     * @param rotZ         the rot z
     * @param scale        the scale
     * @param textureIndex the texture index
     * @param modelData    the model data
     */
    public CollidableEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int textureIndex, ModelData modelData) {
        super(model, position, rotX, rotY, rotZ, scale, textureIndex, modelData);
    }

    @Override
    public boolean checkCollision(Vector3f position) {
        float diffX = Math.abs(position.getX() - origin.getX());
        float diffZ = Math.abs(position.getZ() - origin.getZ());

        // Ascending if statements to help terminate early
        if (diffX < radiusX) {
            if (diffZ < radiusZ) {
                return true;
            }
        }
        return false;
    }
}