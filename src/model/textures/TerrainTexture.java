package model.textures;

/**
 * Terrain texture used to model the terrain texture....
 *
 * @author Marcel van Workum
 */
/*

         These Javadoc comments    Full Javadoc is never
             are pointless!                pointless!
     (,        /                            \     ,)
     /|__--__                                __--_|\
     |\' _                                     _ `/|
     .' / `.,_ _________         _________ _,.' \ `.
    /  ||``._.'                           `._.''||  \
  .'   /|                                       |\   `.
.'    /\ \                                     / /\    `.
 ` `,/ / ,\                                   /, \ \,' '
   .'.'  /|                                   |\  `.`.
 .' /   / |                                   | \   \ `,
- `- -- `-'- ------------------------------- -`-' -- -' -

 */
public class TerrainTexture {

    // opengl binding of the texture
    private int textureID;


    /**
     * Instantiates a new Terrain texture.
     *
     * @param textureID the texture iD
     */
    public TerrainTexture(int textureID) {
        this.textureID = textureID;
    }

    /**
     * Gets texture iD.
     *
     * @return the texture iD
     */
    public int getTextureID() {
        return textureID;
    }
}
