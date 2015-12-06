package com.abdoofathy.progame.android;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import com.abdoofathy.progame.IBluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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

public class BTManager extends Activity implements IBluetooth{

	private BluetoothAdapter btAdapter;
	private final int REQUEST_ENABLE_BT = 1;
	private ListView devicesList;
	private ArrayList<String> mDevicesList = new ArrayList<String>();
	private ArrayAdapter<String> devicesListAdapter;
	private BroadcastReceiver receiver;
	private boolean isConnected , isDiscovering;
	private BluetoothSocket btSocket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private byte[] readBuffer;
	private int readBufferPosition;
	private Thread workerThread;
	private volatile boolean workerRunning = true, sending = true;
	private final String LEFT = "1" , RIGHT = "2", UP = "3", DOWN = "4"; 
	public final String PAUSE = "5" , START = "6";
	private final String [] dataToBeSent = {LEFT,RIGHT,UP,DOWN};
	private Random rand = new Random();
	private String recvData;
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	public final static String MAC_ADDRESS = "MAC_ADDRESS";
	private String macAddress;
	private String pickedString, prevString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.btmanager);
		enableBluetooth();
		devicesList = (ListView) findViewById(R.id.devicesList);
		devicesListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		devicesList.setAdapter(devicesListAdapter);
		devicesList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// connect to that device
				String deviceInfo = parent.getItemAtPosition(position).toString();
				String macAddress = deviceInfo.substring(deviceInfo.length()-17);
				BTManager.this.macAddress = macAddress;
				startGame();
			}
			
		});
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_ENABLE_BT)
			if(resultCode == RESULT_CANCELED){
				Toast.makeText(this, "Bluetooth Must Be Enabled To Proceed", Toast.LENGTH_SHORT).show();
				finish();
			}else
			{
				discoverDevices();
			}
	}
	
	
	@Override
	protected void onDestroy(){
	    super.onDestroy();
	    btAdapter.cancelDiscovery();
	    if(receiver != null)
	    	unregisterReceiver(receiver);
		 stop();
	}

	@Override
	public void enableBluetooth() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if(btAdapter == null){
			Toast.makeText(BTManager.this, "No Bluetooth Adapter Found , Can't Proceed", Toast.LENGTH_SHORT).show();
			finish();
		}else{
			if(!btAdapter.isEnabled()){
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}else
			{
				discoverDevices();
			}
		}
	}

	@Override
	public void discoverDevices() {
		if(btAdapter.isDiscovering()){
			btAdapter.cancelDiscovery();
		}
		btAdapter.startDiscovery();
		isDiscovering = true;
		receiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				 String action = intent.getAction();
				 if(BluetoothDevice.ACTION_FOUND.equals(action)){
					  BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					  mDevicesList.add(device.getName() + "\n" + device.getAddress());
					  devicesList.setAdapter(new ArrayAdapter<String>(context,
		                        android.R.layout.simple_list_item_1, mDevicesList));
				 }
			}
			
		};
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);	
		registerReceiver(receiver, filter);
	}

	@Override
	public void connectToDevice(String macAddress) {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		btAdapter.cancelDiscovery();
		BluetoothDevice device = btAdapter.getRemoteDevice(macAddress);
		
		try {
			btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
			btSocket.connect();
			inputStream = btSocket.getInputStream();
			outputStream = btSocket.getOutputStream();
			isConnected = true;
		} catch (IOException e) {

		}
	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}

	@Override
	public boolean isDiscovering() {
		return isDiscovering;
	}

	@Override
	public void sendMessage(String message) {
		try {
			outputStream.write(message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listenForInput() {
		    final byte delimiter = 126; //This is the ASCII code for end character (currently ~)
		    readBufferPosition = 0;
		    readBuffer = new byte[1024];
		    workerThread = new Thread()
		    {
		        public void run()
		        {                
		           while(!Thread.currentThread().isInterrupted() && workerRunning)
		           {
		                try 
		                {
		                    int bytesAvailable = inputStream.available();                        
		                    if(bytesAvailable > 0)
		                    {
		                        byte[] packetBytes = new byte[bytesAvailable];
		                        inputStream.read(packetBytes);
		                        for(int i=0;i<bytesAvailable;i++)
		                        {
		                            byte b = packetBytes[i];
		                            if(b == delimiter)
		                            {
								     byte[] encodedBytes = new byte[readBufferPosition];
								     System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
								     final String data = new String(encodedBytes);
								     readBufferPosition = 0;
								     recvData = data;
		                            }
		                            else
		                            {
		                                readBuffer[readBufferPosition++] = b;
		                            }
		                        }
		                    }
		                } 
		                catch (IOException ex) 
		                {
		                	try {
								inputStream.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                	break;
		                }
		           }
		        }
		    };

		    workerThread.start();
	}
	
	public String getInput(){
	    return recvData;
	}

	@Override
	public void stop() {
		if(btSocket != null){
			try {
				inputStream.close();
				outputStream.close();
				btSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		workerRunning = false;
		sending = false;
	}
	
	private String pickString(){
		prevString = pickedString;
		int pickedIndex = rand.nextInt(4);
		return dataToBeSent[pickedIndex];
	}
	
	public void sendEvery(final long milSec){
		Thread senderThread = new Thread(){
			public void run(){
				while(sending) {
					try {
						sleep(milSec);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					pickedString = pickString();
					sendMessage(pickedString);
				}
			}
		};
		senderThread.start();
	}
	
	public void stopSending(){
		sending = false;
	}
	
	public void clearRecvData(){
		recvData = null;
	}
	
	public void resumeSending(){
		sending = true;
	}
	
	public void resumeListeningForInput(){
		workerRunning = true;
	}
	
	private void startGame(){
		Intent startGame = new Intent(BTManager.this,AndroidLauncher.class);
		startGame.putExtra(MAC_ADDRESS, macAddress);
		startActivity(startGame);
		finish();
	}
	
	public String getGuessedNumber(){
		return pickedString;
	}
	
    public String getPrevNumber(){
    	return prevString;
    }
	
}
