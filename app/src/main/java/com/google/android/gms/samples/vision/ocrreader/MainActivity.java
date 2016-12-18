/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.samples.vision.ocrreader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.ocrreader.Databases.ServiceDB;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.Calendar;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * recognizes text.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    // Use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView textValue;
    private EditText editText, customerCode;
    private Button send, edit, ok;
    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";
    public static int ocrDetect;
    public static String ocrFinal;
    private static String SOAP_ACTION1 = "http://tempuri.org/InsertReading";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "InsertReading";
    private static String METHOD_NAME2 = "GetDataResponse";
    private static String URL = "http://62.68.240.219/webserv/webservice2.asmx";
    public static LocationManager Coordinates;
    ServiceDB serviceDB;
    private String custCode;
    private  double lat,lon;
    String text1;
    public static Location location;
    boolean GPSEnabled, NetworkEnabled;
CheckBox meter1,meter2;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceDB = new ServiceDB(this);
        ocrDetect = 0;
        ocrFinal = "";
        statusMessage = (TextView) findViewById(R.id.status_message);
        textValue = (TextView) findViewById(R.id.text_value);
        editText = (EditText) findViewById(R.id.editText);
        send = (Button) findViewById(R.id.button2);
        ok = (Button) findViewById(R.id.button3);
        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);
        meter1 = (CheckBox) findViewById(R.id.checkBox);
        meter2 = (CheckBox) findViewById(R.id.checkBox2);
        meter1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(meter1.isChecked()) {
                   meter2.setChecked(false);
               }else{
                   meter2.setChecked(true);
               }
            }
        });
        meter2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(meter2.isChecked()) {
                    meter1.setChecked(false);
                }else{
                    meter1.setChecked(true);
                }
            }
        });
        findViewById(R.id.read_text).setOnClickListener(this);
        customerCode = (EditText) findViewById(R.id.editText2);
        _getLocation();

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_text) {
            // launch Ocr capture activity.
            ocrDetect = 1;
            Intent intent = new Intent(this, OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());
            intent.putExtra(OcrCaptureActivity.Meter1,meter1.isChecked());
            intent.putExtra(OcrCaptureActivity.Meter2,meter2.isChecked());
            if (customerCode.getText().toString().equals("")) {
                Toast.makeText(v.getContext(), "بالرجاء إدخال كود المشترك", Toast.LENGTH_LONG).show();
            } else {
                custCode = customerCode.getText().toString();
                startActivityForResult(intent, RC_OCR_CAPTURE);

            }
        }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ocrDetect = 0;
        if (requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    final String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    text1 = text;
                    statusMessage.setText(R.string.ocr_success);
                    editText.setVisibility(View.VISIBLE);
                    editText.setText(text);
                    ok.setVisibility(View.VISIBLE);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            text1 = editText.getText().toString();
                            textValue.setText(editText.getText());
                        }
                    });
                send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);

                            try {
                                c = Calendar.getInstance();
                                _getLocation();
                                request.addProperty("CustomerCode", custCode);
                                request.addProperty("MeterReading", text1);
                                request.addProperty("ReadingTime", c.getTime().getDay() + "/" +
                                        (c.getTime().getMonth() + 1) + "/" + (c.getTime().getYear() + 1900) + " " + c.getTime().getHours() + ":" + c.getTime().getMinutes());
                                request.addProperty("X", lat+"");
                                request.addProperty("Y", lon+"");
                                //Declare the version of the SOAP request
                                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                                envelope.setOutputSoapObject(request);
                                envelope.dotNet = true;
                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                    StrictMode.setThreadPolicy(policy);

                                try {
                                    AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);

                                    //this is the actual part that will call the webservice
                                    androidHttpTransport.call(SOAP_ACTION1, envelope);

                                    // Get the SoapResult from the envelope body.
                                    SoapPrimitive result = (SoapPrimitive) envelope.getResponse();

                                    if (result.toString().equals("1")) {
                                        Toast.makeText(getApplicationContext(), "تم ارسال القراءة بنجاح", Toast.LENGTH_LONG).show();

                                    } else if (result.toString().equals("0")) {
                                        Toast.makeText(getApplicationContext(), "هذا المستخدم غير موجود بقاعدة البيانات بالرجاء المحاولة مرة أخري ", Toast.LENGTH_LONG).show();

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                     Log.v("hihello", e.toString());
                                    Toast.makeText(getApplicationContext(), " لا يوجد اتصال بالشبكة، سيتم ارسال الطلب فور توافر اتصال بالانترنت", Toast.LENGTH_LONG).show();
                                    Log.v("hihello", serviceDB.addOrder("R"+text1, "C"+custCode, (c.getTime().getDay() + "/" +
                                            (c.getTime().getMonth() + 1) + "/" + (c.getTime().getYear() + 1900)+ " " + c.getTime().getHours() + ":" + c.getTime().getMinutes() + ""),lat,lon)+"");

                                }
                            } catch (Exception exception)
                            {
                                Toast.makeText(getApplicationContext(), "بالرجاء إعادة التصوير مرة أخري ", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Log.d(TAG, "Text read: " + text);
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void _getLocation() {
        // Get the location manager

        Coordinates = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET},
                    PackageManager.PERMISSION_GRANTED);
        }

        GPSEnabled = true;
        NetworkEnabled = true;

        try {
            GPSEnabled = Coordinates.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            NetworkEnabled = Coordinates.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if (!GPSEnabled){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("GPS is Required but Disabled : Enable GPS , Restart App.");

            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    finish();
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        }

        if (!NetworkEnabled){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Network is Required but Disabled : Enable Network , Restart App.");

            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    finish();
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        if(GPSEnabled && NetworkEnabled){
            Coordinates.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
            try {
                location = Coordinates.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                lat = location.getLatitude();
                lon = location.getLongitude();
            }catch(Exception e){

            }
        }
    }
}
