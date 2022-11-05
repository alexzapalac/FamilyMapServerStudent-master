package results;

/** Generic result class - will hold a message about the result of an API service */
public class Result {
    private final String message;
    private final boolean success;

    /**
     * Constructs a new Result object
     *
     * @param message A message String
     * @param success success Boolean
     */
    public Result(String message, Boolean success){
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    //public boolean getResult() {return success;}
}

