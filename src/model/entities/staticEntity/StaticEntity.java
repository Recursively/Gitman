package model.entities.staticEntity;

import model.entities.Entity;
import model.models.ModelData;
import model.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a static entity which is an entity in the game world that cannot be moved and may need to
 * be checked for collision.
 *
 * @author Marcel van Workum
 */
public abstract class StaticEntity extends Entity {

    protected final Vector3f origin;
    protected  BoundingBox boundingBox;
    protected final ModelData modelData;
    protected final float radiusX;
    protected final float radiusY;
    protected final float radiusZ;

    public StaticEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
                        int textureIndex, ModelData modelData) {
        super(model, position, rotX, rotY, rotZ, scale, textureIndex);
        this.modelData = modelData;

        boundingBox = this.modelData.getBoundingBox();

        //TODO this fixes desks but breaks other things
//        boundingBox = new BoundingBox(boundingBox.getMinX() * scale, boundingBox.getMinY() * scale, boundingBox.getMinZ() * scale,
//                boundingBox.getMaxX() * scale, boundingBox.getMaxY() * scale, boundingBox.getMaxZ() * scale);

        //TODO efficiency

        radiusX = Math.abs((boundingBox.getMaxX() - boundingBox.getMinX()) / 2);
        radiusY = Math.abs((boundingBox.getMaxY() - boundingBox.getMinY()) / 2);
        radiusZ = Math.abs((boundingBox.getMaxZ() - boundingBox.getMinZ()) / 2);

        float originX = position.getX() + boundingBox.getMinX() + radiusX;
        float originY = position.getY() + boundingBox.getMinY() + radiusY;
        float originZ = position.getZ() + boundingBox.getMinZ() + radiusZ;

        origin = new Vector3f(originX, originY, originZ);
    }

    public abstract boolean checkCollision(Vector3f position);
}
