package com.google.android.gms.samples.vision.ocrreader.Databases;

import android.provider.BaseColumns;

/**
 * Created by MorcosS on 12/12/16.
 */
public class ServiceDBContract {
    public static final class GPSFetching implements BaseColumns {

        public static final String TABLE_NAME = "com_cst";

        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_cst_parcode = "cst_ParCode";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_cst_name= "cst_name";

        // Short description and long description of the weather, as provided by API.
        // e.g "clear" vs "sky is clear".
        public static final String COLUMN_GPS_X = "cst_gps_x";

        // Min and max temperatures for the day (stored as floats)
        public static final String COLUMN_GPS_Y = "cst_gps_y";

    }

    public static final class GPSEntry implements BaseColumns {

        public static final String TABLE_NAME = "com_send_gps";


        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_cst_parcode = "cst_ParCode";
        // Weather id as returned by API, to identify the icon to be used

        public static final String COLUMN_GPS_X = "cst_gps_x";

        // Min and max temperatures for the day (stored as floats)
        public static final String COLUMN_GPS_Y = "cst_gps_y";

    }

    public static final class ReadingsEntry implements BaseColumns {

        public static final String TABLE_NAME = "com_rdg";

        // Date, stored as long in milliseconds since the epoch
            public static final String COLUMN_CST_PARCODE = "cst_ParCode";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_CST_RDG= "cst_rdg";

        // Short description and long description of the weather, as provided by API.
        // e.g "clear" vs "sky is clear".
        public static final String COLUMN_DATE = "com_rdg_date";


    }

}



