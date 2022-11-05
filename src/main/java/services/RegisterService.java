package services;

import database_access.AuthTokenDao;
import database_access.Database;
import database_access.DatabaseException;
import model.AuthToken;
import model.User;
import requests.FillRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;
import results.Result;
import utils.UuidGenerator;

import java.util.UUID;

import static java.util.UUID.*;

/** Service responsible for creating a new user account, generating 4 generations of ancestor
 * data for the new user, logging the user in, and returning an auth token
 */
public class RegisterService {

    /** Creates a new user, generates 4 generations of ancestor data for the new user,
     * logs the user in and returns an auth token
     *
     * @param req A RegisterRequest object containing information about the registration request
     * @return A RegisterResult object with registration information or an error message
     */
    public RegisterResult register(RegisterRequest req) throws DatabaseException {
        boolean commit = false;

        Database db = new Database();
        String token = UUID.randomUUID().toString();
        AuthTokenDao aDao = new AuthTokenDao(db.getConn());
        aDao.insert(new AuthToken(token, req.getUserName()));
        db.closeConnection(true);
        RegisterResult result = null;

        // Create a new user
        String newPersonID = new UuidGenerator().generateUUID();
        User userToInsert = new User(req.getUserName(), req.getPassword(), req.getEmail(),
                req.getFirstName(), req.getLastName(), req.getGender(), newPersonID);

        // Insert the user
        try {
            db.openConnection();
            db.getUserDao().insert(userToInsert);
            commit = true;
        } catch (DatabaseException e) {
            result = new RegisterResult(e.getMessage(), false);
        } finally {
            db.closeConnection(commit);
        }

        // If we had exceptional behavior we return a message about it
        if (result != null) {
            return result;
        }

        // Generate 4 generations of ancestor data
        FillRequest fillRequest = new FillRequest(4, req.getUserName());
        FillService fillService = new FillService();
        Result fillResult = fillService.fill(fillRequest);

        // If fillResult is exceptional we should return it
        if (!fillResult.getMessage().contains("Successfully")) {

            return new RegisterResult(fillResult.getMessage(), false);
        }

        // Log the user in
        LoginRequest loginRequest = new LoginRequest(req.getUserName(), req.getPassword());
        LoginService loginService = new LoginService();
        LoginResult loginResult = loginService.login(loginRequest); //TODO: Should this be creating two authTokens when running RegisterServiceTest:

        // If loginResult is exceptional we should return it
        if (loginResult.getMessage() != null) {
            return new RegisterResult(loginResult.getMessage(), false);
        }

        result = new RegisterResult(loginResult.getAuthToken(),
                loginResult.getUserName(),
                loginResult.getPersonID());

        return result;
    }
}