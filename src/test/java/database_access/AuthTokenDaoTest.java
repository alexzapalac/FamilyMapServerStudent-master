package database_access;

import model.AuthToken;
import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDaoTest {

    private Database db;
    private AuthToken testToken;
    private AuthTokenDao eDao;

    @BeforeEach
    public void setUp() throws DatabaseException{

        db = new Database();

        testToken = new AuthToken("authToken", "username");
        Connection conn = db.openConnection();
        eDao = new AuthTokenDao(conn);
        eDao.clearTable();
    }

    @AfterEach
    public void tearDown(){
        // Here we close the connection to the database file, so it can be opened again later.
        // We will set commit to false because we do not want to save the changes to the database
        // between test cases.
        db.closeConnection(false);
    }

    @Test
    public void clearTable() throws DatabaseException{
        boolean noTokenFound = false;
        eDao.insert(testToken);
        eDao.clearTable();
        noTokenFound = eDao.find("authToken", testToken.getAuthToken()).isEmpty();

        assertTrue(noTokenFound);
    }

    @Test
    public void clearTableFailing() throws DatabaseException{
        boolean noException = true;
        try{
            eDao.clearTable();
        } catch (DatabaseException e){
            noException = false;
        }

        assertTrue(noException);
    }

    @Test
    public void insert() throws DatabaseException{
        boolean tokenFound = false;
        eDao.insert(testToken);
        tokenFound = !eDao.find("authToken", testToken.getAuthToken()).isEmpty();

        assertTrue(tokenFound);
    }

    @Test
    public void insertFailing() throws DatabaseException{
        boolean exceptionThrown = false;

        try {
            eDao.insert(testToken);
            eDao.insert(testToken);
        } catch(DatabaseException e) {
            exceptionThrown = true;
        }


        assertTrue(exceptionThrown);
    }


    @Test
    public void removeUserData() throws DatabaseException {
        boolean testTokenFound = false;
        boolean testToken2Found = false;

        AuthToken testToken2 = new AuthToken("authToken2", "username2");


        eDao.insert(testToken);
        eDao.insert(testToken2);

        eDao.removeUserData(testToken.getUserName());

        testTokenFound = !eDao.find("username", testToken.getUserName()).isEmpty();
        testToken2Found = !eDao.find("username", testToken2.getUserName()).isEmpty();


        assertFalse(testTokenFound);
        assertTrue(testToken2Found);
    }


    @Test
    public void removeUserDataFailing() throws DatabaseException {
        boolean exceptionThrown = false;

        try {
            eDao.removeUserData(testToken.getUserName());

        } catch (DatabaseException e) {
            exceptionThrown = true;
        }

        assertFalse(exceptionThrown);
    }


    @Test
    public void find() throws DatabaseException {
        boolean tokenFound = false;


        eDao.insert(testToken);
        tokenFound = !eDao.find("username", testToken.getUserName()).isEmpty();


        assertTrue(tokenFound);
    }


    @Test
    public void findFailing() throws DatabaseException {
        boolean tokenNotFound = false;


        tokenNotFound = eDao.find("username", testToken.getUserName()).isEmpty();


        assertTrue(tokenNotFound);
    }
}
