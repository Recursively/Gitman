package model.entities.staticEntity;

/**
 * Implementation of a bounding box used for checking the collision with {@link CollidableEntity}
 *
 * @author Marcel van Workum
 */
public class BoundingBox {

    private float minX;
    private float minY;
    private float minZ;
    private float maxX;
    private float maxY;
    private float maxZ;

    /**
     * Instantiates a new Bounding box.
     *
     * @param minX the min x
     * @param minY the min y
     * @param minZ the min z
     * @param maxX the max x
     * @param maxY the max y
     * @param maxZ the max z
     */
    public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    /**
     * Gets min x.
     *
     * @return the min x
     */
    public float getMinX() {
        return minX;
    }

    /**
     * Gets min y.
     *
     * @return the min y
     */
    public float getMinY() {
        return minY;
    }

    /**
     * Gets min z.
     *
     * @return the min z
     */
    public float getMinZ() {
        return minZ;
    }

    /**
     * Gets max x.
     *
     * @return the max x
     */
    public float getMaxX() {
        return maxX;
    }

    /**
     * Gets max y.
     *
     * @return the max y
     */
    public float getMaxY() {
        return maxY;
    }

    /**
     * Gets max z.
     *
     * @return the max z
     */
    public float getMaxZ() {
        return maxZ;
    }
}
