package com.google.android.gms.samples.vision.ocrreader.Models;

import android.content.Context;

import com.google.android.gms.samples.vision.ocrreader.R;

/**
 * Created by MorcosS on 3/4/17.
 */

public class Meter {
   public String [] meterStatusLabels,meterStatusValues,meterTypeLabels,meterTypeValues;

    public Meter(Context context) {
        meterStatusLabels = context.getResources().getStringArray(R.array.meter_status_array);
        meterStatusValues = context.getResources().getStringArray(R.array.meter_status_values_array);
        meterTypeLabels = context.getResources().getStringArray(R.array.meter_type_array);
        meterTypeValues = context.getResources().getStringArray(R.array.meter_type_values_array);
    }


    public String getMeterTypeFromInt(int value) {
        for (int i=0; i<meterTypeValues.length;i++) {
            if(Integer.parseInt(meterTypeValues[i])==value){
                return meterTypeLabels[i];
            }
        }
        return "";
    }

    public String getMeterStatusFromInt(int value) {
        for (int i=0; i<meterStatusValues.length;i++) {
            if(Integer.parseInt(meterStatusValues[i])==value){
                return meterStatusLabels[i];
            }
        }
        return "";
    }

    public boolean isEditableStatus(int value){
        switch (value){
            case 6:
            case 5:
            case 101:
                return false;
            default: return true;
        }
    }

    public int isMeterOfType(int i){
        switch (i){
            case 1:
            case 5:
            case 3:
            case 4:
                return 1;
            case 2: return 2;
            case 6: return 3;
            case 11: return 0;
            default: return -1;
        }
    }
}
