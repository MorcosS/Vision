package com.google.android.gms.samples.vision.ocrreader.Databases;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.samples.vision.ocrreader.Databases.ServiceDBContract.GPSEntry;
import com.google.android.gms.samples.vision.ocrreader.Databases.ServiceDBContract.GPSFetching;
import com.google.android.gms.samples.vision.ocrreader.Databases.ServiceDBContract.ReadingsEntry;
import com.google.android.gms.samples.vision.ocrreader.Models.Customers;
import com.google.android.gms.samples.vision.ocrreader.Models.Reading;

/**
 * Created by MorcosS on 9/25/16.
 */
public class ServiceDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String CREATE_TABLE_Reading = "CREATE TABLE "
            + "Reading_Item" + "(" + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "Reading_No"
            + " STRING," + "Person_ID String," + "Reading_Time String," + " X double, Y double)";

    public ServiceDB(Context context) {
        super(context, "Order_Items", null, DATABASE_VERSION);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ServiceDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        final String CREATE_TABLE_Reading = "CREATE TABLE "
//                + "Reading_Item" + "(" + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "Reading_No"
//                + " STRING," + "Person_ID String," + "Reading_Time String," + " X double, Y double)";

        final String SQL_CREATE_GPS_TABLE = "CREATE TABLE " + GPSEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                GPSEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                GPSEntry.COLUMN_cst_parcode + " STRING NOT NULL, " +
                GPSEntry.COLUMN_GPS_X + " STRING NOT NULL, " +
                GPSEntry.COLUMN_GPS_Y + " STRING STRING NULL," +
                GPSEntry.COLUMN_METER_TYPE + " INTEGER NULL" +
                ");";
        db.execSQL(SQL_CREATE_GPS_TABLE);

        final String SQL_CREATE_RDG_TABLE = "CREATE TABLE " + ReadingsEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ReadingsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                ReadingsEntry.COLUMN_CST_PARCODE + " STRING NOT NULL, " +
                ReadingsEntry.COLUMN_DATE + " STRING NOT NULL, " +
                ReadingsEntry.COLUMN_CST_RDG + " TEXT STRING NULL," +
                ReadingsEntry.COLUMN_METER_STATUS +"INTEGER NULL," +
                ReadingsEntry.COLUMN_DESCRIPTION + "TEXT STRING NULL " +
                "); " ;
        db.execSQL(SQL_CREATE_RDG_TABLE);


        final String SQL_CREATE_GPS_ENTRY = "CREATE TABLE " + GPSFetching.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                GPSFetching._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                // the ID of the location entry associated with this weather data
                GPSFetching.COLUMN_cst_parcode + " STRING NOT NULL, " +
                GPSFetching.COLUMN_cst_name + " STRING NOT NULL, "+
                GPSFetching.COLUMN_GPS_X + " DECIMAL, " +
                GPSFetching.COLUMN_GPS_Y + " DECIMAL, " +
                GPSFetching.COLUMN_CST_METER_TYPE + " INTEGER, " +
                GPSFetching.COLUMN_CST_METER_STATUS + " INTEGER " + "); " ;
        db.execSQL(SQL_CREATE_GPS_ENTRY);
    }


//    public boolean addOrder(String Reading_NO, String Person_ID, String Reading_Time, double X, double Y) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("Reading_Time", Reading_Time);
//        values.put("Person_ID", Person_ID);
//        values.put("Reading_No", Reading_NO);
//        values.put("X", X);
//        values.put("Y", Y);
//        long movie_row = db.insert("Reading_Item", null, values);
//        db.close(); // Closing database connection
//        if (movie_row == -1) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public Cursor getOrder() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String selectQuery = "SELECT  * " + " FROM Reading_Item";
//        Cursor c = db.rawQuery(selectQuery, null);
//        if (c == null || !c.moveToFirst()) return null;
//        return c;
//    }
//
//    public void deleteOrder(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        db.delete("Reading_Item", "ID = " + id, null);
//    }

    //Still To Review for the new UPDATE

    public Cursor FetchGPS(double Lat,double Lon, double radius) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+ GPSFetching.TABLE_NAME +" WHERE " +
            GPSFetching.COLUMN_GPS_X  + " <= "+(Lat+(radius/110000.0)) + " AND "
                +  GPSFetching.COLUMN_GPS_X  + " >= "+(Lat-(radius/110000.0)) + " AND "
                +  GPSFetching.COLUMN_GPS_Y  + " <= "+(Lon+(radius/110000.0)) + " AND "
                +  GPSFetching.COLUMN_GPS_Y  + " >= "+(Lon-(radius/110000.0));
        Cursor c = db.rawQuery(selectQuery, null);
         if (c == null || !c.moveToFirst())
            return null;
        db.close();
        return c;
    }

    public boolean GPSEntryInsert(Customers cst){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GPSFetching.COLUMN_cst_parcode, cst.getCst_ParCode());
        values.put(GPSFetching.COLUMN_cst_name, cst.getCst_Name());
        values.put(GPSFetching.COLUMN_GPS_X, cst.getCst_X());
        values.put(GPSFetching.COLUMN_GPS_Y, cst.getCst_Y());
        values.put(GPSFetching.COLUMN_CST_METER_TYPE, cst.getMeter_type());
        values.put(GPSFetching.COLUMN_CST_METER_STATUS,cst.getMeter_status());
        long row= db.insert(GPSFetching.TABLE_NAME, null, values);
        db.close(); // Closing database connection
        if (row== -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteAllCSt() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GPSFetching.TABLE_NAME,null, null);
    }

    public boolean InsertReading(Reading rdg){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReadingsEntry.COLUMN_CST_PARCODE, rdg.getCst_ParCode());
        values.put(ReadingsEntry.COLUMN_CST_RDG, rdg.getRdg_value());
        values.put(ReadingsEntry.COLUMN_DATE, rdg.getDateTime());
        values.put(ReadingsEntry.COLUMN_METER_STATUS,rdg.getMeter_status());
        values.put(ReadingsEntry.COLUMN_DESCRIPTION,rdg.getDescription());
        long row= db.insert(ReadingsEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection
        if (row== -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getReading() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * " + " FROM " + ReadingsEntry.TABLE_NAME;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c == null || !c.moveToFirst()) return null;
        return c;
    }

    public void deleteReading(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(ReadingsEntry.TABLE_NAME, ReadingsEntry._ID + " = " + id, null);
    }

    public boolean InsertGPS(String Cst_Parcode,String X, String Y,int meterType){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GPSEntry.COLUMN_cst_parcode, Cst_Parcode);
        values.put(GPSEntry.COLUMN_GPS_X, X);
        values.put(GPSEntry.COLUMN_GPS_Y, Y);
        values.put(GPSEntry.COLUMN_METER_TYPE,meterType);
        long row= db.insert(GPSEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection
        if (row== -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getGPS() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * " + " FROM " + GPSEntry.TABLE_NAME;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c == null || !c.moveToFirst()) return null;
        return c;
    }

    public void deleteGPS(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(GPSEntry.TABLE_NAME, GPSEntry._ID + " = " + id, null);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReadingsEntry.TABLE_NAME );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GPSEntry.TABLE_NAME );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GPSFetching.TABLE_NAME );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "Reading_Item" );
        onCreate(sqLiteDatabase);
    }


}