//package com.google.android.gms.samples.vision.ocrreader;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.samples.vision.ocrreader.Databases.ServiceDB;
//import com.google.android.gms.samples.vision.ocrreader.Models.Customers;
//import com.google.gson.Gson;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.ksoap2.SoapEnvelope;
//import org.ksoap2.serialization.SoapObject;
//import org.ksoap2.serialization.SoapPrimitive;
//import org.ksoap2.serialization.SoapSerializationEnvelope;
//import org.ksoap2.transport.AndroidHttpTransport;
//
//public class LoginActivity extends Activity {
//    private String Col_code;
//    private EditText Col_Code;
//    private static String SOAP_ACTION1 = "http://tempuri.org/GetCustomersByCOD";
//    private static String NAMESPACE = "http://tempuri.org/";
//    private static String METHOD_NAME1 = "InsertReading";
//    private static String METHOD_NAME2 = "GetCustomersByCOD";
//    private static String METHOD_NAME3 = "ValidateUser";
//    private static String URL = "http://62.68.240.219/webserv/webservice2.asmx";
//    private SoapObject request;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        sharedPref = getApplicationContext().getSharedPreferences("SharedPreference", Activity.MODE_PRIVATE);
//        editor = sharedPref.edit();
//        request = new SoapObject(NAMESPACE, METHOD_NAME2);
//
//        Col_Code = (EditText) findViewById(R.id.col_code);
//        if (!sharedPref.getString("Col_code", "").equals("")) {
//            Col_Code.setText(sharedPref.getString("Col_code", ""));
//        }
//    }
//
//    public void RecieveCstList(View view) {
//        Col_code = Col_Code.getText().toString();
//        if (!sharedPref.getString("Col_code", "").equals(Col_code)) {
//
////            try {
////                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
////
////                envelope.setOutputSoapObject(request);
////                envelope.dotNet = true;
////                if (android.os.Build.VERSION.SDK_INT > 9) {
////                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
////                    StrictMode.setThreadPolicy(policy);
////                }
////                AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
////
////                //this is the actual part that will call the webservice
////                androidHttpTransport.call(SOAP_ACTION1, envelope);
////
////                // Get the SoapResult from the envelope body.
////                SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
////
////                if (result.toString().length() > 2) {
////
////                    getCstDataFromJson(result.toString());
////
////                } else {
////
////                }
////
////            } catch (Exception e) {
////                Toast.makeText(view.getContext(),"خطأ في الاتصال حاول لاحقاً",Toast.LENGTH_LONG).show();
////
////            }
////            finally {
////                Intent intent = new Intent(this,Picker_Activity.class);
////                startActivity(intent);
////            }
//        }
//        Intent intent = new Intent(this,Picker_Activity.class);
//        startActivity(intent);
//    }
//
//    private void getCstDataFromJson(String result)
//            throws JSONException {
//
//        JSONArray array = new JSONArray(result);
//        ServiceDB serviceDB = new ServiceDB(this);
//        serviceDB.deleteAllCSt();
//        for (int i = 0; i < array.length(); i++) {
//            // Get the JSON object representing the day
//            Customers customer = new Gson().fromJson(String.valueOf(array.getJSONObject(i)), Customers.class);
//            serviceDB.GPSEntryInsert(customer);
//
//        }
//        Toast.makeText(this,"تم ادخال عدد "+ array.length() + "مشترك", Toast.LENGTH_LONG).show();
//    }
//}
