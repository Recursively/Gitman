package model.network;

/**
 * Simple wrapper class to hold the components of an update that is sent across
 * the network
 *
 * @author Reuben Puketapu
 */
public class Update {

    /**
     * The Update.
     */
    public int update;
    /**
     * The Id.
     */
    public int id;
    /**
     * The Uid.
     */
    public int uid;

    /**
     * Constructor for an update
     *
     * @param update the type of update being sent
     * @param id     the id of the entity
     * @param uid    the user ID of the player who sent the update
     */
    public Update(int update, int id, int uid) {
        this.update = update;
        this.id = id;
        this.uid = uid;
    }
}
