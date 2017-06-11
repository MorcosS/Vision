package com.google.android.gms.samples.vision.ocrreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.samples.vision.ocrreader.Databases.ServiceDB;
import com.google.android.gms.samples.vision.ocrreader.Models.Customers;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

public class Picker_Activity extends Activity {
    public static String SOAP_ACTION1 = "http://tempuri.org/GetCustomersByCOD";
    public static String NAMESPACE = "http://tempuri.org/";
    public static String METHOD_NAME1 = "InsertReading";
    public static String METHOD_NAME2 = "GetCustomersByCOD";
    public static String METHOD_NAME3 = "ValidateUser";
    public static String URL = "http://62.68.240.219/webserv/webservice2.asmx";
    private SoapObject request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        request = new SoapObject(NAMESPACE, METHOD_NAME2);
        Button insertRgd = (Button) findViewById(R.id.button4);
        Button insertGps = (Button) findViewById(R.id.button5);
        Button updateDate = (Button) findViewById(R.id.button7);
       int Col_Type =  SignInActivity.sharedPref.getInt("Col_Type",-1);
        if(Col_Type == 1){
            insertGps.setVisibility(View.GONE);
        }else if(Col_Type==2){
            insertRgd.setVisibility(View.GONE);
            updateDate.setVisibility(View.GONE);
        }
    }

    public void InsertReading(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void InsertGPS(View view){
        Intent intent = new Intent(this,GPSSending.class);
        startActivity(intent);
    }

    public void RecieveList(View view) {

            try {
                request.addProperty("cod_id", SignInActivity.sharedPref.getString("Col_code",""));
                request.addProperty("meter_type", "");

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);

                //this is the actual part that will call the webservice
                androidHttpTransport.call(SOAP_ACTION1, envelope);

                // Get the SoapResult from the envelope body.
                SoapPrimitive result = (SoapPrimitive) envelope.getResponse();

                if (result.toString().length() > 2) {
                    getCstDataFromJson(result.toString());

                } else {

                }

            } catch (Exception e) {
                Toast.makeText(view.getContext(),"خطأ في الاتصال حاول لاحقاً",Toast.LENGTH_LONG).show();

            }


    }

    private void getCstDataFromJson(String result)
            throws JSONException {


        JSONArray array = new JSONArray(result);
        ServiceDB serviceDB = new ServiceDB(this);
        serviceDB.deleteAllCSt();
        for (int i = 0; i < array.length(); i++) {
            // Get the JSON object representing the day
            Customers customer = new Gson().fromJson(String.valueOf(array.getJSONObject(i)), Customers.class);
            serviceDB.GPSEntryInsert(customer);

        }
        Toast.makeText(this,"تم ادخال عدد "+ array.length() + "مشترك", Toast.LENGTH_LONG).show();
    }

}
