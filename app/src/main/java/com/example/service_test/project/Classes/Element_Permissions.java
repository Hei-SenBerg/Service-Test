package com.example.service_test.project.Classes;

public class Element_Permissions  {

    private String sPermission;
    private boolean bObligatoire;

    public Element_Permissions(){}

    public Element_Permissions(String sPermission, boolean bObligatoire) {
        this.sPermission = sPermission;
        this.bObligatoire = bObligatoire;
    }

    public String getsPermission() {
        return sPermission;
    }

    public void setsPermission(String sPermission) {
        this.sPermission = sPermission;
    }

    public boolean isbObligatoire() {
        return bObligatoire;
    }

    public void setbObligatoire(boolean bObligatoire) {
        this.bObligatoire = bObligatoire;
    }

}
