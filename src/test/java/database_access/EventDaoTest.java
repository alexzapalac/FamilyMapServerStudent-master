package database_access;


import model.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventDaoTest {

    private Database db;
    private Event testEvent;
    private EventDao eDao;

    @BeforeEach
    public void setUp() throws DatabaseException{

        db = new Database();

        testEvent = new Event("eventID", "associatedUsername", "personID", 0.0, 0.0, "country", "city", "eventType", 0);
        Connection conn = db.openConnection();
        eDao = new EventDao(conn);
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
        boolean noEventFound = false;
        eDao.insert(testEvent);
        eDao.clearTable();
        noEventFound = eDao.find("eventID", testEvent.getEventID()).isEmpty();

        assertTrue(noEventFound);
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
        boolean EventFound = false;
        eDao.insert(testEvent);
        EventFound = !eDao.find("eventID", testEvent.getEventID()).isEmpty();

        assertTrue(EventFound);
    }

    @Test
    public void insertFailing() throws DatabaseException{
        boolean exceptionThrown = false;

        try {
            eDao.insert(testEvent);
            eDao.insert(testEvent);
        } catch(DatabaseException e) {
            exceptionThrown = true;
        }


        assertTrue(exceptionThrown);
    }


    @Test
    public void removeUserData() throws DatabaseException {
        boolean testEventFound = false;
        boolean testEvent2Found = false;

        Event testEvent2 = new Event ("eventID2", "associatedUsername2", "personID2", 0., 0., "country2", "city2", "eventType2", 0);


        eDao.insert(testEvent);
        eDao.insert(testEvent2);

        eDao.removeUserData(testEvent.getAssociatedUsername());

        testEventFound = !eDao.find("eventID", testEvent.getEventID()).isEmpty();
        testEvent2Found = !eDao.find("eventID", testEvent2.getEventID()).isEmpty();


        assertFalse(testEventFound);
        assertTrue(testEvent2Found);
    }


    @Test
    public void removeUserDataFailing() throws DatabaseException {
        boolean exceptionThrown = false;

        try {
            eDao.removeUserData(testEvent.getAssociatedUsername());

        } catch (DatabaseException e) {
            exceptionThrown = true;
        }

        assertFalse(exceptionThrown);
    }


    @Test
    public void find() throws DatabaseException {
        boolean EventFound = false;


        eDao.insert(testEvent);
        EventFound = !eDao.find("eventID", testEvent.getEventID()).isEmpty();


        assertTrue(EventFound);
    }


    @Test
    public void findFailing() throws DatabaseException {
        boolean EventNotFound = false;


        EventNotFound = eDao.find("eventID", testEvent.getEventID()).isEmpty();


        assertTrue(EventNotFound);
    }
    
}
