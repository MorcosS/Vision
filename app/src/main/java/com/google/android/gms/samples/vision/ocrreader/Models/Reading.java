package com.google.android.gms.samples.vision.ocrreader.Models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MorcosS on 12/19/16.
 */
public class Reading {
    String rdg_value, Cst_ParCode;
    String dateTime;

    public Reading(String Cst_ParCode, String rdg_value) {
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateTime = dateFormatLocal.format(new Date());
        this.rdg_value = rdg_value;
        this.Cst_ParCode = Cst_ParCode;
    }

    public String getRdg_value() {
        return rdg_value;
    }

    public void setRdg_value(String rdg_value) {
        this.rdg_value = rdg_value;
    }

    public String getCst_ParCode() {
        return Cst_ParCode;
    }

    public void setCst_ParCode(String cst_ParCode) {
        Cst_ParCode = cst_ParCode;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
