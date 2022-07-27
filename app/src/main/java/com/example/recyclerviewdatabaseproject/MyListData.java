package com.example.recyclerviewdatabaseproject;

public class MyListData{
    private int infoImvId;
    private String eventTime, serialNum, empId, logger;
    private String eventAdditionalDesc, appId;
    private int editImvId;
    private int deleteImvId;
    private int id, location, route, day, eventNum, eventAdditionalNum;

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

    public int getId(){return this.id; }
    public String getEventTime() {
        return eventTime;
    }
    public String getSerialNum() {return serialNum; }
    public String getEmpId() {return empId; }
    public int getLocation() {return location; }
    public int getRoute() {return route; }
    public int getDay() {return day; }
    public String getLogger() {return logger; }
    public int getEventNum() {return eventNum; }
    public String getEventAdditionalDesc() {return eventAdditionalDesc; }
    public int getEventAdditionalNum() {return eventAdditionalNum; }
    public int getInfoImvId() {
        return infoImvId;
    }
    public int getEditImvId() {
        return editImvId;
    }
    public int getDeleteImvId() {
        return deleteImvId;
    }
}
