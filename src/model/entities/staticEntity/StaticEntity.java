package model.entities.staticEntity;

import model.entities.Entity;
import model.models.ModelData;
import model.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a static entity which is an entity in the game world that cannot be moved and may need to
 * be checked for collision.
 *
 * @author Marcel van Workum - 300313949
 */
public abstract class StaticEntity extends Entity {

    /**
     * The Origin.
     */
    protected final Vector3f origin;
    /**
     * The Bounding box.
     */
    protected BoundingBox boundingBox;
    /**
     * The Model data.
     */
    protected final ModelData modelData;
    /**
     * The Radius x.
     */
    protected final float radiusX;
    /**
     * The Radius y.
     */
    protected final float radiusY;
    /**
     * The Radius z.
     */
    protected final float radiusZ;

    /**
     * Instantiates a new Static entity.
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
    public StaticEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
                        int textureIndex, ModelData modelData) {
        super(model, position, rotX, rotY, rotZ, scale, textureIndex);
        this.modelData = modelData;

        boundingBox = this.modelData.getBoundingBox();

        radiusX = Math.abs((boundingBox.getMaxX() - boundingBox.getMinX()) / 2);
        radiusY = Math.abs((boundingBox.getMaxY() - boundingBox.getMinY()) / 2);
        radiusZ = Math.abs((boundingBox.getMaxZ() - boundingBox.getMinZ()) / 2);

        float originX = position.getX() + boundingBox.getMinX() + radiusX;
        float originY = position.getY() + boundingBox.getMinY() + radiusY;
        float originZ = position.getZ() + boundingBox.getMinZ() + radiusZ;

        origin = new Vector3f(originX, originY, originZ);
    }

    /**
     * Checks if the position is colliding with this entity
     *
     * @param position position of player
     * @return result of check
     */
    public abstract boolean checkCollision(Vector3f position);
}
