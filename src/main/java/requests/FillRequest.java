package requests;

/** Contains information relevant to user request for filling in database information */
public class FillRequest {

    private final int generations;
    private final String username;

    /** Constructs a FillRequest object
     *
     * @param username Username of the user to fill information for
     * @param generations Number of generations of information to fill
     */
    public FillRequest(int generations, String username){
        this.generations = generations;
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public int getGenerations(){
        return generations;
    }
}
