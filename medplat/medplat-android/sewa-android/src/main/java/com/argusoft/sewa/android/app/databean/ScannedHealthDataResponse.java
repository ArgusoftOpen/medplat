package com.argusoft.sewa.android.app.databean;


public class ScannedHealthDataResponse {
    private String Response;
    private String Device_ID;
    private String User_ID;
    private String Scan_ID;
    private String Error;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getDevice_ID() {
        return Device_ID;
    }

    public void setDevice_ID(String device_ID) {
        Device_ID = device_ID;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getScan_ID() {
        return Scan_ID;
    }

    public void setScan_ID(String scan_ID) {
        Scan_ID = scan_ID;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }
}