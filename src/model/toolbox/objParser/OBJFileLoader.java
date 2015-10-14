package model.toolbox.objParser;

import model.models.ModelData;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.ResourceLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//TODO Comment

public class OBJFileLoader {

    // Location of the resources folder
    private static final String RES_LOC = "res/";

    /**
     * Attempts to load the object into a model data
     *
     * @param objFileName name of object
     *
     * @return Model data about the object
     */
    public static ModelData loadOBJ(String objFileName) {

        // first get the file reader
        Reader isr = getFileReader(objFileName);

        // inits the structures
        BufferedReader reader = new BufferedReader(isr);
        List<Vertex> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        // Parses the object
        parseObject(reader, vertices, textures, normals, indices);

        // removes unused vertices for performance
        removeUnusedVertices(vertices);

        // init strictures
        float[] verticesArray = new float[vertices.size() * 3];
        float[] texturesArray = new float[vertices.size() * 2];
        float[] normalsArray = new float[vertices.size() * 3];

        // gets furtherest point
        float furthest = convertDataToArrays(vertices, textures, normals, verticesArray,
                texturesArray, normalsArray);

        // creates the indices array
        int[] indicesArray = convertIndicesListToArray(indices);

        return new ModelData(verticesArray, texturesArray, normalsArray, indicesArray,
                furthest);
    }

    /**
     * Parses the obj file
     *
     * @param reader File reader
     * @param vertices vertex points
     * @param textures vertex textures
     * @param normals vertex normals
     * @param indices vertex indices
     */
    private static void parseObject(BufferedReader reader, List<Vertex> vertices, List<Vector2f> textures, List<Vector3f> normals, List<Integer> indices) {
        String line;
        try {
            while (true) {
                line = reader.readLine();

                // vertices
                if (line.startsWith("v ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f vertex = new Vector3f(Float.valueOf(currentLine[1]),
                            Float.valueOf(currentLine[2]),
                            Float.valueOf(currentLine[3]));
                    Vertex newVertex = new Vertex(vertices.size(), vertex);
                    vertices.add(newVertex);

                }

                // vertex textures
                else if (line.startsWith("vt ")) {
                    String[] currentLine = line.split(" ");
                    Vector2f texture = new Vector2f(Float.valueOf(currentLine[1]),
                            Float.valueOf(currentLine[2]));
                    textures.add(texture);
                }

                // vertex normals
                else if (line.startsWith("vn ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f normal = new Vector3f(Float.valueOf(currentLine[1]),
                            Float.valueOf(currentLine[2]),
                            Float.valueOf(currentLine[3]));
                    normals.add(normal);
                }

                // at this point break and parse faces
                else if (line.startsWith("f ")) {
                    break;
                }
            }

            // parse the faces
            parseFaces(reader, vertices, indices, line);

            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file");
        }
    }

    /**
     * Attempts to parse the vertex faces f v/vt/vn
     *
     * @param reader file reader
     * @param vertices vertices
     * @param indices indices
     * @param line line in file
     * @throws IOException :<>
     */
    private static void parseFaces(BufferedReader reader, List<Vertex> vertices, List<Integer> indices, String line) throws IOException {
        while (line != null && line.startsWith("f ")) {
            String[] currentLine = line.split(" ");
            String[] vertex1 = currentLine[1].split("/");
            String[] vertex2 = currentLine[2].split("/");
            String[] vertex3 = currentLine[3].split("/");

            // Actually process the vertices
            processVertex(vertex1, vertices, indices);
            processVertex(vertex2, vertices, indices);
            processVertex(vertex3, vertices, indices);

            // read the next line
            line = reader.readLine();
        }
    }

    /**
     * Gets the file reader for the obj file
     *
     * @param objFileName file name
     *
     * @return FileReader
     */
    private static InputStreamReader getFileReader(String objFileName) {
//        FileReader isr = null;
//        File objFile = new File(RES_LOC + objFileName + ".obj");
//        try {
//            isr = new FileReader(objFile);
//        } catch (FileNotFoundException e) {
//            System.err.println("File not found in res; don't use any extension");
//        }
//        return isr;

        return new InputStreamReader(ResourceLoader.getResourceAsStream(RES_LOC + objFileName + ".obj"));
    }

    /**
     * Processes the vertex face
     *
     * @param vertex vertex
     * @param vertices list of vertices
     * @param indices indices
     */
    private static void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {

        // parse vertex from face
        int index = Integer.parseInt(vertex[0]) - 1;
        Vertex currentVertex = vertices.get(index);

        // parse texture and normal from face
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;

        // checks that vertex is set
        if (!currentVertex.isSet()) {

            // sets the normal and texture
            currentVertex.setTextureIndex(textureIndex);
            currentVertex.setNormalIndex(normalIndex);
            indices.add(index);
        } else {
            // process the index for the first time
            dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
                    vertices);
        }
    }

    /**
     * Converts a list of indices to an indices array
     *
     * @param indices list of indices
     *
     * @return An int[] of indices
     */
    private static int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }

    /**
     * Converts all the face information into data arrays
     *
     * @param vertices list of vertices to convert
     * @param textures list of textures to convert
     * @param normals list of normals to convert
     * @param verticesArray the vertex array
     * @param texturesArray textures array
     * @param normalsArray normals array
     * @return furtherest point
     */
    private static float convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures,
                                             List<Vector3f> normals, float[] verticesArray, float[] texturesArray,
                                             float[] normalsArray) {
        float furthestPoint = 0;

        for (int i = 0; i < vertices.size(); i++) {
            Vertex currentVertex = vertices.get(i);

            // checks for furtherest point
            if (currentVertex.getLength() > furthestPoint) {
                furthestPoint = currentVertex.getLength();
            }

            // parse face
            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
            Vector3f normalVector = normals.get(currentVertex.getNormalIndex());

            // add vertices
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;

            // add textures
            texturesArray[i * 2] = textureCoord.x;
            texturesArray[i * 2 + 1] = 1 - textureCoord.y;

            // add normals
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
        }
        return furthestPoint;
    }

    /**
     * Deals with an already processed vertex point
     *
     * Fixes texture seams
     *
     * @param previousVertex :?
     * @param newTextureIndex new index of the texture
     * @param newNormalIndex new norma index of texture
     * @param indices indices
     * @param vertices vertices
     */
    private static void dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex,
                                                       int newNormalIndex, List<Integer> indices, List<Vertex> vertices) {

        // checks if texture should be seamed
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
        }

        // Performs an edge split, duplicating the vertex
        else {
            Vertex anotherVertex = previousVertex.getDuplicateVertex();

            // if the duplicate already exists, then recursively process
            if (anotherVertex != null) {
                dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex,
                        indices, vertices);
            }

            // set up new vertex
            else {
                Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition());
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
            }

        }
    }

    /**
     * removes unused vertices to improve performance
     *
     * @param vertices List of vertices
     */
    private static void removeUnusedVertices(List<Vertex> vertices){
        for(Vertex vertex:vertices){
            if(!vertex.isSet()){
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }

}