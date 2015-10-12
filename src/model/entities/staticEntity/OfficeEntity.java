package model.entities.staticEntity;

import model.models.ModelData;
import model.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Marcel on 12/10/15.
 */
public class OfficeEntity extends CollidableEntity {

    public OfficeEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int textureIndex, ModelData modelData) {
        super(model, position, rotX, rotY, rotZ, scale, textureIndex, modelData);
        super.boundingBox = new BoundingBox(boundingBox.getMinX() * scale, boundingBox.getMinY() * scale, boundingBox.getMinZ() * scale,
                boundingBox.getMaxX() * scale, boundingBox.getMaxY() * scale, boundingBox.getMaxZ() * scale);
    }
}
