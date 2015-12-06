package com.abdoofathy.dancegame;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	boolean connectedToArduino;
	BluetoothAdapter btAdapter;
	final int REQUEST_ENABLE_BT = 1;
	ListView devicesList;
	ArrayAdapter<String> devicesListAdapter;
	BroadcastReceiver receiver;
	final static String MAC_ADDRESS = "MAC_ADDRESS";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		
		devicesList = (ListView) findViewById(R.id.devicesList);
		devicesListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		devicesList.setAdapter(devicesListAdapter);
		findDevices();
		devicesList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// connect to that device
				String deviceInfo = parent.getItemAtPosition(position).toString();
				String deviceName = deviceInfo.substring(0,deviceInfo.length()-17);
				String macAddress = deviceInfo.substring(deviceInfo.length()-17);
				 // check the device name
				if(deviceName != ""){ // arduino bluetooth name
					// cancel discovery 
					btAdapter.cancelDiscovery();
					// after connecting go to activity Menu
					Intent menuIntent = new Intent(MainActivity.this,MenuActivity.class);
					menuIntent.putExtra(MAC_ADDRESS, macAddress);
					startActivity(menuIntent);
					finish();
				} else
				{
					Toast.makeText(MainActivity.this, "Please Pick The Valid Arduino Bluetooth", Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
		
	}
	
	private void init(){
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if(btAdapter == null){
			Toast.makeText(this, "No Bluetooth Adapter Found , Can't Proceed", Toast.LENGTH_SHORT).show();
			finish();
		}else{
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			enableBtIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 100);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_CANCELED){
			Toast.makeText(this, "Bluetooth Must Be Enabled To Proceed", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	
	private void findDevices(){
		btAdapter.startDiscovery();
		receiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				 String action = intent.getAction();
				 if(BluetoothDevice.ACTION_FOUND.equals(action)){
					  BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					  devicesListAdapter.add(device.getName() + "\n" + device.getAddress());
				 }
				
			}
			
		};
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);	
		registerReceiver(receiver, filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);

	}
	
	@Override
	protected void onDestroy(){
	    super.onDestroy();
		if (btAdapter != null) {
		      btAdapter.cancelDiscovery();
		    }
		 unregisterReceiver(receiver);
	}
}
