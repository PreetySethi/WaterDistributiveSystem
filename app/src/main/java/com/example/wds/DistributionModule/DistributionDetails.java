package com.example.wds.DistributionModule;

public class DistributionDetails {
     public DistributionDetails()
     {

     }

    public DistributionDetails(String name, String id, String number, boolean permission) {
        this.name = name;
        this.id = id;
        this.number = number;
        this.permission = permission;
    }

    private String name;
    private String id;
    private String number;
    private boolean permission;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean getPermission() {
        return permission;
    }

    public void setPermission(boolean permission)
    {
        this.permission = permission;
    }


}
