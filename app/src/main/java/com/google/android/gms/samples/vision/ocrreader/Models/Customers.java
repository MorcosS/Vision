package com.google.android.gms.samples.vision.ocrreader.Models;

/**
 * Created by MorcosS on 12/18/16.
 */
public class Customers {
    String cst_ParCode;
    String cst_name;
    String Cst_GPS_X;
    String Cst_GPS_Y;
    int mtr_type;
    int mtr_status;

    public int getMeter_status() {
        return mtr_status;
    }

    public void setMeter_status(int meter_status) {
        this.mtr_status = meter_status;
    }

    public int getMeter_type() {
        return mtr_type;
    }

    public void setMeter_type(int meter_type) {
        this.mtr_type = meter_type;
    }



    public Customers(String cst_Name) {
        cst_name = cst_Name;
    }

    public String getCst_ParCode() {
        return cst_ParCode;
    }

    public void setCst_ParCode(String cst_ParCode) {
        this.cst_ParCode = cst_ParCode;
    }

    public String getCst_Name() {
        return cst_name;
    }

    public void setCst_Name(String cst_Name) {
        cst_name = cst_Name;
    }

    public String getCst_X() {
        return Cst_GPS_X;
    }

    public void setCst_X(String cst_X) {
        Cst_GPS_X = cst_X;
    }

    public String getCst_Y() {
        return Cst_GPS_Y;
    }

    public void setCst_Y(String cst_Y) {
        Cst_GPS_Y = cst_Y;
    }
}
