package com.example.recyclerviewdatabaseproject;


/**
 * Class that represents the data contained in one record from the database
 */
public class MyListData{
    private int infoImvId; // the id of the clickable icon that the user clicks when they want to view all the data in a record
    private String eventTime, serialNum, empId, logger; // fields from the record that are in text form
    private String eventAdditionalDesc, appId; // fields from the record that are in text form
    private int editImvId; // the id of the clickable icon that the user clicks when they want to edit the record
    private int deleteImvId; // the id of the clickable icon that the user clicks when they want to delete the record
    private int id, location, route, day, eventNum, eventAdditionalNum; // fields from the record that are numeric

    /**
     * Constructor that initializes the fields based off the information contained in the database
     *
     * @param id the id of the event
     * @param eventTime the time of the event
     * @param serialNum the serial number of the user's device
     * @param empId the id of the employee/partner
     * @param location the location number that the event occurred at
     * @param route the route number
     * @param day the day of the week that the event happened on
     * @param logger any information pertaining to logging
     * @param eventNum the event number that represents the event that happened
     * @param eventAdditionalDesc any additional information about the event in text form
     * @param eventAdditionalNum any additional information about the event in numeric form
     * @param infoImvId the XML id of the icon for displaying all the data in the record
     * @param editImvId the XML id of the icon for editing a particular record
     * @param deleteImvId the XML id of the icon for deleting a particular record
     */
    public MyListData(int id, String eventTime, String serialNum, String appId, String empId, int location,
                      int route, int day, String logger, int eventNum, String eventAdditionalDesc,
                      int eventAdditionalNum, int infoImvId, int editImvId, int deleteImvId) {
        this.id = id;
        this.eventTime = eventTime;
        this.serialNum = serialNum;
        this.appId = appId;
        this.empId = empId;
        this.location = location;
        this.route = route;
        this.day = day;
        this.logger = logger;
        this.eventNum = eventNum;
        this.eventAdditionalDesc = eventAdditionalDesc;
        this.eventAdditionalNum = eventAdditionalNum;
        this.infoImvId = infoImvId;
        this.editImvId = editImvId;
        this.deleteImvId = deleteImvId;
    }


    /**
     * Getter method for the id of the record
     *
     * @return the record's id
     */
    public int getId() {
        return this.id;
    }


    /**
     * Getter method for the event time of the record
     *
     * @return the record's event time
     */
    public String getEventTime() {
        return eventTime;
    }


    /**
     * Getter method for the serial number of the record
     *
     * @return the record's serial number
     */
    public String getSerialNum() {
        return serialNum;
    }


    /**
     * Getter method for the employee id of the record
     *
     * @return the record's employee id
     */
    public String getEmpId() {
        return empId;
    }


    /**
     * Getter method for the location of the record
     *
     * @return the record's location number
     */
    public int getLocation() {
        return location;
    }


    /**
     * Getter method for the route of the record
     *
     * @return the record's route number
     */
    public int getRoute() {
        return route;
    }


    /**
     * Getter method for the day of the record
     *
     * @return the record's day
     */
    public int getDay() {
        return day;
    }


    /**
     * Getter method for the logging information of the record
     *
     * @return the record's logging information
     */
    public String getLogger() {
        return logger;
    }


    /**
     * Getter method for the event number of the record
     *
     * @return the record's event number
     */
    public int getEventNum() {
        return eventNum;
    }


    /**
     * Getter method for the additional textual information pertaining to the record
     *
     * @return the record's additional textual information
     */
    public String getEventAdditionalDesc() {
        return eventAdditionalDesc;
    }


    /**
     * Getter method for the additional numeric information pertaining to the record
     *
     * @return the record's additional numeric information
     */
    public int getEventAdditionalNum() {
        return eventAdditionalNum;
    }


    /**
     * Getter method for the XML id of the icon for displaying all the data in the record
     *
     * @return the XML id
     */
    public int getInfoImvId() {
        return infoImvId;
    }


    /**
     * Getter method for the XML id of the icon for editing a particular record
     *
     * @return the XML id
     */
    public int getEditImvId() {
        return editImvId;
    }


    /**
     * Getter method for the XML id of the icon for deleting a particular record
     *
     * @return the XML id
     */
    public int getDeleteImvId() {
        return deleteImvId;
    }
}
