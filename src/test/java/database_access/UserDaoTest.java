package database_access;

import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;


import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {

    private Database db;

    private User testUser;
    private UserDao eDao;

    @BeforeEach
    public void setUp() throws DatabaseException {

        db = new Database();

        testUser = new User("username", "password", "email",
                "first", "last", "f", "personID");

        Connection conn = db.openConnection();
        eDao = new UserDao(conn);
        eDao.clearTable();
    }

    @AfterEach
    public void tearDown() {
        // Here we close the connection to the database file, so it can be opened again later.
        // We will set commit to false because we do not want to save the changes to the database
        // between test cases.
        db.closeConnection(false);
    }

    /** 1: We add a new User to the table, clear the table, and then make sure
     *  that the entry we made is gone
     */
    @Test
    public void clearTable() throws DatabaseException{
        boolean noUserFound = false;

        eDao.insert(testUser);
        eDao.clearTable();
        noUserFound = eDao.find("username", testUser.getUsername()).isEmpty();


        assertTrue(noUserFound);
    }

    /** 2: We attempt to clear the User table when it's empty, and make sure that runs normally
     * and doesn't throw an exception, which is the desired behavior
     */
    @Test
    public void clearTableFailing() throws DatabaseException {
        boolean noException = true;


        eDao.clearTable();

        assertTrue(noException);
    }

    /** 3: We insert a User into an empty table and then make sure we can find it */
    @Test
    public void insert() throws DatabaseException{
        boolean userFound = false;

        eDao.insert(testUser);
        userFound = !eDao.find("username", testUser.getUsername()).isEmpty();

        assertTrue(userFound);
    }

    /** 4: We insert a User and then try to insert the same User again, then
     * check to make sure an exception is thrown
     */
    @Test
    public void insertFailing() throws DatabaseException {
        boolean exceptionThrown = false;

        try {
            eDao.insert(testUser);
            eDao.insert(testUser);
        } catch(DatabaseException e) {
            exceptionThrown = true;
        }


        assertTrue(exceptionThrown);
    }

    /** 5: We insert two Users, each associated with a different user. We then
     * remove data associated with one user and make sure that entry is gone while the other
     * entry is not
     */
    @Test
    public void removeUserData() throws DatabaseException {
        boolean testUserFound = false;
        boolean testUser2Found = false;

        User testUser2 = new User("username2", "password2", "email2",
                "first2", "last2", "f", "personID2");


        eDao.insert(testUser);
        eDao.insert(testUser2);

        eDao.removeUserData(testUser.getUsername());

        testUserFound = !eDao.find("username", testUser.getUsername()).isEmpty();
        testUser2Found = !eDao.find("username", testUser2.getUsername()).isEmpty();


        assertFalse(testUserFound);
        assertTrue(testUser2Found);
    }

    /** 6: We attempt to remove User data for a user when there is none in the table,
     * and make sure there is no exception, which is the desired behavior
     */
    @Test
    public void removeUserDataFailing() throws DatabaseException {
        boolean exceptionThrown = false;

        try {
            eDao.removeUserData(testUser.getUsername());

        } catch (DatabaseException e) {
            exceptionThrown = true;
        }

        assertFalse(exceptionThrown);
    }

    /** 7: We insert a User and then make sure we can find it */
    @Test
    public void find() throws DatabaseException {
        boolean userFound = false;


        eDao.insert(testUser);
        userFound = !eDao.find("username", testUser.getUsername()).isEmpty();


        assertTrue(userFound);
    }

    /** 8: We attempt to find a User that doesn't exist and insure we can't find it */
    @Test
    public void findFailing() throws DatabaseException {
        boolean userNotFound = false;


        userNotFound = eDao.find("username", testUser.getUsername()).isEmpty();


        assertTrue(userNotFound);
    }

}
