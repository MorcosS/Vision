package com.google.android.gms.samples.vision.ocrreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Picker_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
    }

    public void InsertReading(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void InsertGPS(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
