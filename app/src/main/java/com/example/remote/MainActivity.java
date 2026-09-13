package com.example.remote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private UsbManager usbManager;
    private TextView text;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            scan();
        }
    };

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        IntentFilter f = new IntentFilter();
        f.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        f.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(receiver, f, Context.RECEIVER_EXPORTED);
        } else {
            registerReceiver(receiver, f);
        }

        scan();
    }

    private void scan() {
        StringBuilder log = new StringBuilder();
        log.append("USB Host: ").append(usbManager != null ? "есть" : "НЕТ").append("\n");
        if (usbManager == null) { text.setText(log); return; }

        HashMap<String, UsbDevice> list = usbManager.getDeviceList();
        log.append("Устройств найдено: ").append(list.size()).append("\n");

        for (UsbDevice d : list.values()) {
            log.append("\n=== ").append(d.getDeviceName()).append(" ===\n");
            log.append("VID: 0x").append(String.format("%04X", d.getVendorId())).append("\n");
            log.append("PID: 0x").append(String.format("%04X", d.getProductId())).append("\n");

            for (int i = 0; i < d.getInterfaceCount(); i++) {
                UsbInterface iface = d.getInterface(i);
                log.append("iface ").append(i)
                    .append(" class=").append(iface.getInterfaceClass())
                    .append(" ep=").append(iface.getEndpointCount()).append("\n");

                for (int j = 0; j < iface.getEndpointCount(); j++) {
                    UsbEndpoint ep = iface.getEndpoint(j);
                    log.append("  ep").append(j)
                        .append(" dir=").append(ep.getDirection() == UsbConstants.USB_DIR_OUT ? "OUT" : "IN")
                        .append(" type=").append(ep.getType())
                        .append(" maxPkt=").append(ep.getMaxPacketSize()).append("\n");
                }
            }
        }

        text.setText(log.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
