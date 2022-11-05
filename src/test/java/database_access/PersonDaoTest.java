package database_access;

import model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDaoTest {

    private Database db;
    private Person testPerson;
    private PersonDao eDao;

    @BeforeEach
    public void setUp() throws DatabaseException{

        db = new Database();
        testPerson = new Person("personID", "descendant",
                "firstName", "lastName", "m", "father",
                "mother", "spouse");

        Connection conn = db.openConnection();
        eDao = new PersonDao(conn);
        eDao.clearTable();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    /** 1: We add a new Person to the table, clear the table, and then make sure
     *  that the entry we made is gone
     */
    @Test
    public void clearTable() throws DatabaseException {
        boolean noPersonFound = false;

        eDao.insert(testPerson);
        eDao.clearTable();
        noPersonFound = eDao.find("personID", testPerson.getPersonID()).isEmpty();

        assertTrue(noPersonFound);
    }

    /** 2: We attempt to clear the Persons table when it's empty, and make sure that runs normally
     * and doesn't throw an exception, which is the desired behavior
     */
    @Test
    public void clearTableFailing() throws DatabaseException {
        boolean noException = true;

        try {
            eDao.clearTable();
        } catch (DatabaseException e) {
            noException = false;
        }

        assertTrue(noException);
    }

    /** 3: We insert a Person into an empty table and then make sure we can find it */
    @Test
    public void insert() throws DatabaseException {
        boolean personFound = false;

        eDao.insert(testPerson);
        personFound = !eDao.find("personID", testPerson.getPersonID()).isEmpty();

        assertTrue(personFound);
    }

    /** 4: We insert a Person and then try to insert the same Person again, then
     * check to make sure an exception is thrown
     */
    @Test
    public void insertFailing() throws DatabaseException {
        boolean exceptionThrown = false;

        try {
            eDao.insert(testPerson);
            eDao.insert(testPerson);
        } catch (DatabaseException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    /** 5: We insert two Persons, each associated with a different user. We then
     * remove data associated with one user and make sure that entry is gone while the other
     * entry is not
     */
    @Test
    public void removeUserData() throws DatabaseException {
        boolean testPersonFound = false;
        boolean testPerson2Found = false;

        Person testPerson2 = new Person("personID2", "descendant2",
                "firstName2", "lastName2", "f", "father2",
                "mother2", "spouse2");


        eDao.insert(testPerson);
        eDao.insert(testPerson2);

        eDao.removeUserData(testPerson.getAssociatedUsername());

        testPersonFound = !eDao.find("personID", testPerson.getPersonID()).isEmpty();
        testPerson2Found = !eDao.find("personID", testPerson2.getPersonID()).isEmpty();

        assertFalse(testPersonFound);
        assertTrue(testPerson2Found);
    }

    /** 6: We attempt to remove Person data for a user when there is none in the table,
     * and make sure there is no exception, which is the desired behavior
     */
    @Test
    public void removeUserDataFailing() {
        boolean exceptionThrown = false;

        try {
            eDao.removeUserData(testPerson.getAssociatedUsername());
        } catch (DatabaseException e) {
            exceptionThrown = true;
        }

        assertFalse(exceptionThrown);
    }

    /** 7: We insert a Person and then make sure we can find it */
    @Test
    public void find() throws DatabaseException {
        boolean personFound = false;

        eDao.insert(testPerson);
        personFound = !eDao.find("personID", testPerson.getPersonID()).isEmpty();


        assertTrue(personFound);
    }

    /** 8: We attempt to find a Person that doesn't exist and insure we can't find it */
    @Test
    public void findFailing() throws DatabaseException {
        boolean personNotFound = false;

        personNotFound = eDao.find("personID", testPerson.getPersonID()).isEmpty();

        assertTrue(personNotFound);
    }


}
