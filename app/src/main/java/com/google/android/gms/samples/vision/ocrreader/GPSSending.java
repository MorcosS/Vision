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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.samples.vision.ocrreader.Databases.ServiceDB;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

public class GPSSending extends Activity {
    EditText cst_ParCode;
    private static String SOAP_ACTION1 = "http://tempuri.org/SetCustomerGPS";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "SetCustomerGPS";
    private static String URL = "http://62.68.240.219/webserv/webservice2.asmx";
    public static Location location;
    boolean GPSEnabled, NetworkEnabled;
    public static LocationManager Coordinates;
    ServiceDB serviceDB;
    private  double lat,lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpssending);
        cst_ParCode = (EditText) findViewById(R.id.editText3);
        Button send = (Button) findViewById(R.id.button6);
        serviceDB = new ServiceDB(this);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);

                try {

                        _getLocation();
                    if (lat == 0 || lon == 0) {
                        Toast.makeText(getApplicationContext()," بالرجاء تفعيل خدمات الموقع والمحاولة مرة أخري", Toast.LENGTH_LONG).show();
                    } else {
                        request.addProperty("Cst_ParCode", cst_ParCode.getText().toString());
                        request.addProperty("x", lat + "");
                        request.addProperty("y", lon + "");
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
                                Toast.makeText(getApplicationContext(), "تم ارسال الموقع بنجاح", Toast.LENGTH_LONG).show();

                            } else if (result.toString().equals("0")) {
                                Toast.makeText(getApplicationContext(), "هذا المستخدم غير موجود بقاعدة البيانات بالرجاء المحاولة مرة أخري ", Toast.LENGTH_LONG).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), " لا يوجد اتصال بالشبكة، سيتم ارسال الطلب فور توافر اتصال بالانترنت", Toast.LENGTH_LONG).show();
                            serviceDB.InsertGPS(cst_ParCode.getText().toString(), lat + "", lon + "");

                        }
                    }
                    }catch(Exception exception){
                        Toast.makeText(getApplicationContext(), "بالرجاء إعادة التصوير مرة أخري ", Toast.LENGTH_LONG).show();
                    }

            }
        });
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
        } catch (Exception ex) {
        }

        try {
            NetworkEnabled = Coordinates.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!GPSEnabled) {
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

        if (!NetworkEnabled) {
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

        if (GPSEnabled && NetworkEnabled) {
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
            } catch (Exception e) {

            }
        }
    }
}
