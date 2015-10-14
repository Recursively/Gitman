package model.toolbox;

import model.entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * A collection of useful mathematical functions that assist the RenderEngine
 *
 * @author Marcel van Workum - 300313949
 */
public class Maths {

    /**
     * Calculates the y terrain height of a given point on a vertex triangles
     *
     * @param p1  point 1 of triangle (corner)
     * @param p2  point 2 of triangle (corner)
     * @param p3  point 3 of triangle (corner)
     * @param pos x/z position of the point on the triangle
     * @return y terrain height of pos
     */
    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    /**
     * Creates a 2D transformation matrix, which translates and scales
     *
     * @param translation Translation vector
     * @param scale       Scale vector
     * @return 4x4 Transformation matrix
     */
    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();

        // initialise the matrix
        matrix.setIdentity();

        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
        return matrix;
    }

    /**
     * Creates a 4x4 transformation matrix from a 3d vector translation
     *
     * @param translation vector translation
     * @param rx          x rotation
     * @param ry          y rotation
     * @param rz          z rotation
     * @param scale       matrix scale
     * @return 4x4 transformation matrix
     */
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();

        // initialise the matrix
        matrix.setIdentity();

        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        return matrix;
    }

    /**
     * Creates a 4x4 view matrix for the Camera's view
     *
     * @param camera Camera to create matrix for
     * @return 4x4 View matrix
     */
    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();

        // initialise the matrix
        viewMatrix.setIdentity();

        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 1, 1), viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        // translates the matrix based on the parameters
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);

        return viewMatrix;
    }

}
