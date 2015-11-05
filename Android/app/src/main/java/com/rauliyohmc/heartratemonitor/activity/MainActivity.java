package com.rauliyohmc.heartratemonitor.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rauliyohmc.heartratemonitor.R;

import java.util.ArrayList;

/**
 * Created by rauliyohmc on 05/03/15.
 */


public class MainActivity extends ActionBarActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private ListView listView;
    private ProgressBar progressBar;
    private BluetoothAdapter bluetoothAdapter;
    private DeviceListAdapter deviceListAdapter;
    private Boolean scanning = false;
    private Handler handler; // to handle a timeout for the scanning process

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        handler = new Handler(); // handler object created in mainActivity, hence it's stuck in the message queue of the main Thread
        listView = (ListView) findViewById(R.id.devicesListView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "ERROR: Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (!scanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                deviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
        }
        return true;
    }

    /**
     * Handling screen rotation
     * We save the previously found devices in the bundle before destroying the activity
     * OnRestoreInstanceState() will allow to pull that data into the devices listView
     */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (deviceListAdapter.getCount() > 0) {
            //ArrayList<BluetoothDevice> test = new ArrayList<BluetoothDevice>(deviceListAdapter.getDevicesList());
            outState.putParcelableArrayList("DevicesFound", deviceListAdapter.getDevicesList());
            Log.d("Checking", "We save the device found");
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getParcelableArrayList("DevicesFound") != null) {
            // Retrieving the previous devices found
            ArrayList<BluetoothDevice> devicesList = savedInstanceState.getParcelableArrayList("DevicesFound");
            deviceListAdapter = new DeviceListAdapter(devicesList);
            listView.setAdapter(deviceListAdapter);
            Log.d("Checking", "check state");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("OnResume", "welcome Back");
        final Intent intent = new Intent(this, DeviceActivity.class);

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!bluetoothAdapter.isEnabled()) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        if (deviceListAdapter == null) {
            deviceListAdapter = new DeviceListAdapter();
            listView.setAdapter(deviceListAdapter);
        }

        // Sets the item click listener in the listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BluetoothDevice bluetoothDevice = deviceListAdapter.getDevice(position);
                if (bluetoothDevice == null) return;
                intent.putExtra(DeviceActivity.EXTRAS_DEVICE_NAME, bluetoothDevice.getName());
                intent.putExtra(DeviceActivity.EXTRAS_DEVICE_ADDRESS, bluetoothDevice.getAddress());
                if (scanning) {
                    bluetoothAdapter.stopLeScan(callback);
                    scanning = false;
                }
                startActivity(intent);
            }
        });

    }

    // In case we decide to not enable bluetooth, we finish the activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        //deviceListAdapter.clear();
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() { // Causes the Runnable r to be added to the message queue, to be run after the specified amount of time elapses.
                @Override
                public void run() {
                    scanning = false;
                    bluetoothAdapter.stopLeScan(callback);
                    // Declare that the options menu has changed, so should be recreated
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            scanning = true;
            bluetoothAdapter.startLeScan(callback);

        } else {
            scanning = false;
            bluetoothAdapter.stopLeScan(callback);
        }
        invalidateOptionsMenu();
    }


    // Adapter for holding devices found through scanning.
    private class DeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> devicesList;
        private LayoutInflater inflater;

        public DeviceListAdapter() {
            super();
            devicesList = new ArrayList<BluetoothDevice>();
            inflater = MainActivity.this.getLayoutInflater();
        }

        public DeviceListAdapter(ArrayList<BluetoothDevice> list) {
            super();
            devicesList = new ArrayList<BluetoothDevice>(list);
            inflater = MainActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!devicesList.contains(device)) {
                devicesList.add(device);
            }
        }

        public ArrayList getDevicesList() {
            return devicesList;
        }

        public BluetoothDevice getDevice(int position) {
            return devicesList.get(position);
        }

        public void clear() {
            devicesList.clear();
        }

        @Override
        public int getCount() {
            return devicesList.size();
        }

        @Override
        public Object getItem(int i) {
            return devicesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = inflater.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress  = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = devicesList.get(i);
            final String deviceName = device.getName();
            final String deviceAddress = device.getAddress();

            if (deviceName != null && deviceName.length() > 0){
                viewHolder.deviceName.setText(deviceName);
                viewHolder.deviceAddress.setText(deviceAddress);
            } else{
                viewHolder.deviceName.setText(R.string.unknown_device);
                viewHolder.deviceAddress.setText(device.getAddress());
            }

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback callback =
            new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    deviceListAdapter.addDevice(device);
                    deviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }


}
