package com.google.android.gms.samples.vision.ocrreader.Models;

/**
 * Created by MorcosS on 12/18/16.
 */
public class Customers {
    String Cst_ParCode,Cst_Name,Cst_X,Cst_Y;

    public Customers(String cst_Name) {
        Cst_Name = cst_Name;
    }

    public String getCst_ParCode() {
        return Cst_ParCode;
    }

    public void setCst_ParCode(String cst_ParCode) {
        Cst_ParCode = cst_ParCode;
    }

    public String getCst_Name() {
        return Cst_Name;
    }

    public void setCst_Name(String cst_Name) {
        Cst_Name = cst_Name;
    }

    public String getCst_X() {
        return Cst_X;
    }

    public void setCst_X(String cst_X) {
        Cst_X = cst_X;
    }

    public String getCst_Y() {
        return Cst_Y;
    }

    public void setCst_Y(String cst_Y) {
        Cst_Y = cst_Y;
    }
}
