package com.abdoofathy.progame;

public interface IBluetooth {
	public void enableBluetooth();
	public void discoverDevices();
    public void connectToDevice(String macAddress);
    public boolean isConnected();
    public boolean isDiscovering();
    public void sendMessage(String message);
    public void stopSending();
	public void resumeSending();
    public void listenForInput();
    public void clearRecvData();
    public String getInput();
    public void stop();
    public void sendEvery(final long milSec);
    public String getGuessedNumber();
    public String getPrevNumber();
	public final String LEFT = "1" , RIGHT = "2", UP = "3", DOWN = "4"; 
	public final String PAUSE = "5" , START = "6", QUIT = "7";
	public final long sendingRate = 1000;
}
