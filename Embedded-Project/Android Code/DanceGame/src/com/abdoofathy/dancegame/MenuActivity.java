package com.abdoofathy.dancegame;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity{
	Button startGameBtn,exitGameBtn;
	TextView text;
	final int START_GAME = 1 , STOP_GAME = 0;
	private BluetoothSocket btSocket;
	InputStream inputStream;
	private OutputStream outputStream;
	byte[] readBuffer;
	int readBufferPosition;
	Thread workerThread;
	boolean workerRunning, sending;
	final String LEFT = "1" , RIGHT = "2", FORWARD = "3", BACKWARD = "4"; 
	final String PAUSE = "5" , START = "6";
	int rateOfMotion; // not used yet
	final String [] dataToBeSent = {LEFT,RIGHT,FORWARD,BACKWARD};
	Random rand = new Random();
	
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		connectToDevice();
		startGameBtn = (Button) findViewById(R.id.startGame);
		exitGameBtn = (Button) findViewById(R.id.exitGame);
		text = (TextView) findViewById(R.id.textView);
		startGameBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// send signal to arduino to start sending (arrows pressed)
				//workerThread.write(1);
				// start gameActivity
				//write();
				Toast.makeText(MenuActivity.this, "GAME STARTED!!!", Toast.LENGTH_SHORT).show();
				write(START);
				sendEvery(2000);
			}
		});
		
		
		
		exitGameBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// send signal to arduino to stop sending (arrows pressed)
				finish();
			}
		});
	}
	
	private void connectToDevice(){
		String macAddress = getIntent().getStringExtra(MainActivity.MAC_ADDRESS);
		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothDevice device = btAdapter.getRemoteDevice(macAddress);
		try {
			btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
			btSocket.connect();
			inputStream = btSocket.getInputStream();
			outputStream = btSocket.getOutputStream();
			Toast.makeText(MenuActivity.this, "Connected.", Toast.LENGTH_SHORT).show();
			beginListenForData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MenuActivity.this, "Couldn't Connect.", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			btSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		workerRunning = false;
		sending = false;
	}
	
	public void write(String string){
		try {
			outputStream.write(string.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void beginListenForData()
	{
		  final Handler handler = new Handler(); 
		    final byte delimiter = 126; //This is the ASCII code for end character (currently ~)
		    workerRunning = true;
		    readBufferPosition = 0;
		    readBuffer = new byte[1024];
		    workerThread = new Thread(new Runnable()
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

		                                handler.post(new Runnable()
		                                {
		                                    public void run()
		                                    {
		                                        text.setText(data);
		                                    }
		                                });
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
		    });

		    workerThread.start();
	}
	
	String pickString(){
		int pickedIndex = rand.nextInt(4);
		return dataToBeSent[pickedIndex];
	}
	
	void sendEvery(final long milSec){
		sending = true;
		Thread senderThread = new Thread(){
			public void run(){
				while(sending) {
					try {
						sleep(milSec);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					write(pickString());
				}
			}
		};
		senderThread.start();
	}

}
