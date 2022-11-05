package model;

/** Has the structure of the Events table in the database */
public class Event {
    private final String eventID;
    private final String associatedUsername;
    private final String personID;
    private final Double latitude;
    private final Double longitude;
    private final String country;
    private final String city;
    private final String eventType;
    private final Integer year;


    /** Constructs an Event object
     *
     * @param eventID Unique identifier for this event (non-empty string)
     * @param associatedUsername User (Username) to which this person belongs
     * @param personID ID of person to which this event belongs
     * @param latitude Latitude of event’s location
     * @param longitude Longitude of event’s location
     * @param country Country in which event occurred
     * @param city City in which event occurred
     * @param eventType Type of event (birth, baptism, christening, marriage, death, etc.)
     * @param year Year in which event occurred
     */
    public Event(String eventID, String associatedUsername, String personID, Double latitude, Double longitude, String country, String city, String eventType, Integer year){


        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID(){
        return eventID;
    }

    public String getAssociatedUsername(){
        return associatedUsername;
    }

    public String getPersonID(){
        return personID;
    }

    public Double getLatitude(){
        return latitude;
    }

    public Double getLongitude(){
        return longitude;
    }

    public String getCountry(){
        return country;
    }

    public String getCity(){
        return city;
    }

    public String getEventType(){
        return eventType;
    }

    public Integer getYear(){
        return year;
    }

    public boolean equals(Object o){
        return false;
    }

}
