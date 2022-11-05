package requests;

/** Holds information about a user's login request */
public class LoginRequest {

    private final String username;
    private final String password;

    /** Constructs a LoginRequest object
     *
     * @param username User's username
     * @param password User's password
     */
    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUserName(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
