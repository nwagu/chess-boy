package com.nwagu.android.chessboy.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.widget.Toast;

import com.nwagu.android.chessboy.Data.Constants;
import com.nwagu.android.chessboy.DeviceListActivity;
import com.nwagu.android.chessboy.PlayActivity;

public class BluetoothManager {
    private PlayActivity activity;
    private String connectedDeviceName;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothChatService chatService;

    public BluetoothManager(PlayActivity activity) {
        this.activity = activity;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void init() {
        chatService = new BluetoothChatService(activity, activity.bluetoothHandler);
        chatService.start();
    }

    public void startBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(activity, "Bluetooth is not supported on your device.", Toast.LENGTH_LONG).show();
        } else {

            if (!bluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
            } else {
                if (chatService == null) {
                    chatService = new BluetoothChatService(activity, activity.bluetoothHandler);
                    chatService.start();
                }
                Intent serverIntent = new Intent(activity, DeviceListActivity.class);
                activity.startActivityForResult(serverIntent, Constants.REQUEST_CONNECT_DEVICE_INSECURE);
            }
            activity.iAmBluetoothWhite = false;
        }
    }

    public void connectDevice(Intent data, boolean secure) {
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS); // Get the device MAC address
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address); // Get the BluetoothDevice object
        activity.iAmBluetoothWhite = true; // The initiator of the connection is white
        chatService.connect(device, secure); // Attempt to connect to the device
    }

    /*
    Does the actual sending of data via Bluetooth
    //TODO Return a boolean to ensure message has been received
     */
    public void sendMessage(String message) {
        // Check that we're connected before sending
        if (chatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(activity, "You are not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            chatService.write(send);

        }
    }

    public void startChatService() {
        if (chatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (chatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                chatService.start();
            }
        }
    }

    public void stopChatService() {
        if (chatService != null) {
            chatService.stop();
        }
    }

    public void closeBluetooth() {
        if(bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }

    public String getConnectedDeviceName() {
        return connectedDeviceName;
    }

    public void setConnectedDeviceName(String connectedDeviceName) {
        this.connectedDeviceName = connectedDeviceName;
    }
}
