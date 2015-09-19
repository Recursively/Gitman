package model.toolbox;

import model.models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loader class to handle the loading of .obj object models into the engine
 *
 * @author Marcel van Workum
 */
public final class OBJLoader {

    //TODO THIS IS THE OLD ONE. We should probably delete this..

    private static List<Vector3f> vertices;
    private static List<Vector2f> textures;
    private static List<Integer> indices;
    private static List<Vector3f> normals;
    private static float[] verticesArray;
    private static float[] normalsArray;
    private static float[] texturesArray;
    private static int[] indicesArray;

    /**
     * Creates a new RawModel from an wavefront (.obj) file
     *
     * @param fileName FileName
     * @param loader Loader to load the object into a RawModel
     *
     * @return RawModel created
     */
    public static RawModel loadObjModel(String fileName, Loader loader) {

        // initialises the obj data structures
        initDataStructures();

        // reads the object file
        BufferedReader reader = null;
        try {
            reader = getObjectFile(fileName);
        } catch (FileNotFoundException e) {
            System.err.println("Failed to load object model :"+fileName);
        }

        // parses the obj object
        parseObject(reader);

        // initialises vertices and indices arrays after file is read to know sizes
        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        // finally load the vertices and indices
        loadVertices();
        loadIndices();

        // returns the obj raw model loaded by the loader
        return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
    }

    /**
     * Simply initialises the data structures
     */
    private static void initDataStructures() {
        vertices = new ArrayList<>();
        textures = new ArrayList<>();
        normals = new ArrayList<>();
        indices = new ArrayList<>();
        normalsArray = null;
        texturesArray = null;
    }

    /**
     * Attempts to read the wavefront file
     *
     * @param fileName obj file name
     *
     * @return Buffered reader containing the file
     * @throws FileNotFoundException If the file failed to read
     */
    private static BufferedReader getObjectFile(String fileName) throws FileNotFoundException {
        FileReader fr = null;
        try {
            fr = new FileReader(new File("res/"+fileName+".obj"));
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load file!");
            e.printStackTrace();
        }

        // fail shouldn't be null
        if (fr != null) {
            return new BufferedReader(fr);
        }

        // however if it is, shit has gone wrong
        throw new FileNotFoundException("Failed to load object model");
    }

    /**
     * Parses the .obj wavefront into the internal data structures
     *
     * @param reader buffered reader with .obj file
     */
    private static void parseObject(BufferedReader reader) {
        String line;
        try {
            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");

                // vertex location
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                }

                // vertex texture location
                else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                }

                // vertex normal
                else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                }

                // end of vertex information
                else if (line.startsWith("f ")) {
                    texturesArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while (line != null) {
                // if there is an empty line
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }

                // read the face values and process
                // face == f 3/1/2 etc
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                // process the three faces on each line
                processVertex(vertex1, textures, normals);
                processVertex(vertex2, textures, normals);
                processVertex(vertex3, textures, normals);

                // read next line
                line = reader.readLine();
            }
            reader.close();
        }catch (Exception e) {
            e.printStackTrace();
            System.err.println("Filed to load object model in the OBJLoader");
        }
    }

    /**
     * Loads the indices
     */
    private static void loadIndices() {
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
    }

    /**
     * Loads the xyz vertex points into the vector array
     */
    private static void loadVertices() {
        int vertexPointer = 0;
        for(Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }
    }

    /**
     * Processes each of the obj faces into a corresponding vertex
     *
     * @param vertexData Vertex points
     * @param textures vertex textures
     * @param normals vertex normals
     */
    private static void processVertex(String[] vertexData, List<Vector2f> textures,
                                      List<Vector3f> normals) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) -1);
        texturesArray[currentVertexPointer * 2] = currentTexture.x;
        texturesArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;
        Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNormal.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
    }

}
