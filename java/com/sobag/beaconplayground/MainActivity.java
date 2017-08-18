package com.sobag.beaconplayground;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class MainActivity extends Activity {

    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------
    private static final String url = "jdbc:mysql://inoicbeacon.mysql.database.azure.com:3306/inoicbeacon";
    private static final String user = "inoic@inoicbeacon";
    private static final String pass = "test@123";

    private static final String LOG_TAG = "MainActivity";

    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private Handler scanHandler = new Handler();
    private int scan_interval_ms = 2000;
    private boolean isScanning = false;
    private String distance;
    private Handler mHandler;
    private static int major;
    private static int minor;
    private static int Rssi;
    private static String uuid;
    private static int txpower;
    private static double accuracy;
    private static String link;

    // ------------------------------------------------------------------------
    // default stuff...
    // ------------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init BLE
        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

        mHandler = new Handler();

        scanHandler.post(scanRunnable);
        mHandler.post(mRunnable);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mRunnable);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(LOG_TAG, "OUTPUT := UUID: " + uuid + "\\nmajor: " + major + "\\nminor" + minor + "\\rssi" + Rssi + "\\Distance " + accuracy);

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                Log.i(LOG_TAG, "Connection Success ");

                Statement myStmt = con.createStatement();
                String sql = "SELECT * FROM `beacondata` WHERE `beaconUUID` = '" + uuid + "' " ;
                ResultSet rs = myStmt.executeQuery(sql);

                if(accuracy < 3) {
                    while (rs.next()) {
                        link = rs.getString(7);
                        WebView web = (WebView) findViewById(R.id.webView);
                        web.loadUrl(link);

                        TextView TextViewData = (TextView) findViewById(R.id.TextViewData);
                        TextViewData.setText("URL of the image is:  " + link);
                    }
                }
                else{
                    WebView web = (WebView) findViewById(R.id.webView);
                    web.loadUrl("https://psych0omantis.files.wordpress.com/2012/04/no-ads-2go6ve2.png");

                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error !!!",
                        Toast.LENGTH_LONG).show();
            }



            mHandler.post(this);

            /*TextView TextViewData = (TextView) findViewById(R.id.TextViewData);
            TextViewData.setText("value is " + uuid);

            TextView textViewMajor = (TextView) findViewById(R.id.textViewMajor);
            textViewMajor.setText("value is " + major);

            TextView textViewRssi = (TextView) findViewById(R.id.textViewRssi);
            textViewRssi.setText("RSSI is " + Rssi);

            TextView textViewAccuracy = (TextView) findViewById(R.id.textViewAccuracy);
            textViewAccuracy.setText("Accuracy is " + accuracy);

            TextView textViewDistance = (TextView) findViewById(R.id.textViewDistance);
            textViewDistance.setText("Distance is " + distance);

            TextView textViewTxpower = (TextView) findViewById(R.id.textViewTxpower);
            textViewTxpower.setText("TX power is " + txpower);


            mHandler.post(this);*/
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ------------------------------------------------------------------------
    // public usage
    // ------------------------------------------------------------------------

    private Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {

            if (isScanning) {
                if (btAdapter != null) {
                    btAdapter.stopLeScan(leScanCallback);
                }
            } else {
                if (btAdapter != null) {
                    btAdapter.startLeScan(leScanCallback);
                }
            }

            isScanning = !isScanning;

            scanHandler.postDelayed(this, scan_interval_ms);
        }
    };

    // ------------------------------------------------------------------------
    // Inner classes
    // ------------------------------------------------------------------------

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            int startByte = 2;
            boolean patternFound = false;
            while (startByte <= 5) {
                if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                    patternFound = true;
                    break;
                }
                startByte++;
            }

            Log.i("INFO :- ScanRecord", scanRecord.toString());

            if (patternFound) {
                //Convert to hex String
                byte[] uuidBytes = new byte[16];
                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                String hexString = bytesToHex(uuidBytes);

                //UUID detection
                uuid = hexString.substring(0, 8) + "-" +
                        hexString.substring(8, 12) + "-" +
                        hexString.substring(12, 16) + "-" +
                        hexString.substring(16, 20) + "-" +
                        hexString.substring(20, 32);

                // major
                major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);
                // minor
                minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);

                Rssi = rssi;//(scanRecord[startByte + 24]);

                txpower = (scanRecord[startByte + 24]);

                double x;
                if (Rssi == 0) {
                    x = -1.0; // if we cannot determine accuracy, return -1.
                }
                double ratio = Rssi * 1.0 / txpower;
                if (ratio < 1.0) {
                    double y = Math.pow(ratio, 10);
                } else {
                    accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
                }


                if (accuracy == -1.0) {
                    distance = "Unknown";
                } else if (accuracy < 1 ) {
                    distance = "Immediate";
                } else if (accuracy < 3) {
                    distance = "Near";
                } else {
                    distance = "Far";
                }
            }
        }
    };

    /*public  double calculateAccuracy() {

        if (Rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }
        double ratio = Rssi*1.0/txpower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
            return accuracy;
        }
    }*/

  /*  public void getDistance(double accuracy) {
        if (accuracy == -1.0) {
            distance =  "Unknown";
        } else if (accuracy < 1 && accuracy < 3) {
            distance =  "Immediate";
        } else if (accuracy < 3) {
            distance = "Near";
        } else {
            distance = "Far";
        }

    }*/

    /**
     * bytesToHex method
     */
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}

