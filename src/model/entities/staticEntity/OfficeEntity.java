package model.entities.staticEntity;

import model.models.ModelData;
import model.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Implementation of a collidable entity which has a scaled collision hit box.
 *
 * Again, this is some what pointless. These classes would have been much more useful if more time had been put into
 * collision detection.
 *
 * @author Marcel van Workum
 */
public class OfficeEntity extends CollidableEntity {

    /**
     * Instantiates a new Office entity.
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
    public OfficeEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int textureIndex, ModelData modelData) {
        super(model, position, rotX, rotY, rotZ, scale, textureIndex, modelData);
        super.boundingBox = new BoundingBox(boundingBox.getMinX() * scale, boundingBox.getMinY() * scale, boundingBox.getMinZ() * scale,
                boundingBox.getMaxX() * scale, boundingBox.getMaxY() * scale, boundingBox.getMaxZ() * scale);
    }
}
