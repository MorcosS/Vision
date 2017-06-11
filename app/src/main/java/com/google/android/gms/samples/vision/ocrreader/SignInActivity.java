package com.google.android.gms.samples.vision.ocrreader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends Activity  {

    public static SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    public static String SOAP_ACTION2 = "http://tempuri.org/ValidateUser";
    public static String SOAP_ACTION1 = "http://tempuri.org/GetCustomersByCOD";
    public static String NAMESPACE = "http://tempuri.org/";
    public static String METHOD_NAME1 = "InsertReading";
    public static String METHOD_NAME2 = "GetCustomersByCOD";
    public static String METHOD_NAME3 = "ValidateUser";
    public static String URL = "http://62.68.240.219/webserv/webservice2.asmx";
    private SoapObject request;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    String Col_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        sharedPref = getApplicationContext().getSharedPreferences("SharedPreference", Activity.MODE_PRIVATE);
        editor = sharedPref.edit();
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {

                    return true;
                }
                return false;
            }
        });
        request = new SoapObject(NAMESPACE,METHOD_NAME3);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    public void Signin(View view) {
            Col_code = mEmailView.getText().toString();
        try {
            request.addProperty("UserName", Col_code);
            request.addProperty("Password", mPasswordView.getText().toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);

            //this is the actual part that will call the webservice
            androidHttpTransport.call(SOAP_ACTION2, envelope);

            // Get the SoapResult from the envelope body.
            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            editor.putInt("Col_Type", Integer.parseInt(result.toString()));
            editor.putString("Col_code", Col_code);
            editor.commit();


        } catch (Exception e) {
            Toast.makeText(view.getContext(),"خطأ في الاتصال حاول لاحقاً",Toast.LENGTH_LONG).show();

        }
        Intent intent = new Intent(this,Picker_Activity.class);
        startActivity(intent);

    }

}

