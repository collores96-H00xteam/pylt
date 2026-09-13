package com.example.remote;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ConsumerIrManager ir;
    private TextView status;
    private Spinner spinner;
    private HashMap<String, int[][]> db = new HashMap<>();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        status = findViewById(R.id.statusText);
        spinner = findViewById(R.id.brandSpinner);
        Button btnOn = findViewById(R.id.btnOn);
        Button btnOff = findViewById(R.id.btnOff);

        ir = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
        if (ir == null || !ir.hasIrEmitter()) {
            status.setText("Нет ИК-порта");
            btnOn.setEnabled(false);
            btnOff.setEnabled(false);
            return;
        }
        status.setText("ИК-порт есть. Выбери бренд.");
        fill();

        ArrayAdapter<String> a = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                db.keySet().toArray(new String[0]));
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(a);

        btnOn.setOnClickListener(v -> send(true));
        btnOff.setOnClickListener(v -> send(false));
    }

    private void send(boolean on) {
        String brand = spinner.getSelectedItem().toString();
        int[] p = db.get(brand)[on ? 0 : 1];
        try {
            int freq = p[0];
            int[] pulses = new int[p.length - 1];
            System.arraycopy(p, 1, pulses, 0, pulses.length);
            ir.transmit(freq, pulses);
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void fill() {
        db.put("Samsung", new int[][]{
            {38000,69,9000,4500,560,560,560,560,560,560,560,560,560,1690,560,1690,560,560,560,560,560,560,560,1690,560,560,560,560,560,560,560,1690,560,1690,560,1690,560,2000},
            {38000,69,9000,4500,560,560,560,560,560,560,560,560,560,1690,560,1690,560,560,560,560,560,1690,560,560,560,560,560,560,560,1690,560,560,560,560,560,560,560,2000}
        });
        db.put("LG", new int[][]{
            {38000,69,9000,4500,560,560,560,560,560,560,560,560,560,1690,560,1690,560,560,560,560,560,560,560,1690,560,1690,560,560,560,560,560,1690,560,560,560,1690,560,2000},
            {38000,69,9000,4500,560,560,560,560,560,560,560,560,560,1690,560,1690,560,560,560,560,560,1690,560,1690,560,1690,560,560,560,560,560,1690,560,560,560,1690,560,2000}
        });
        db.put("Sony", new int[][]{
            {40000,25,2400,600,1200,600,600,600,1200,600,1200,600,600,600,600,600,1200,600,600,600,1200,600,600,2000},
            {40000,25,2400,600,1200,600,600,600,1200,600,1200,600,600,600,1200,600,600,600,600,600,1200,600,600,2000}
        });
        db.put("Philips", new int[][]{
            {38000,69,9000,4500,560,1690,560,1690,560,560,560,560,560,560,560,1690,560,1690,560,560,560,1690,560,1690,560,1690,560,560,560,1690,560,560,560,1690,560,1690,560,2000},
            {38000,69,9000,4500,560,1690,560,1690,560,560,560,560,560,560,560,1690,560,1690,560,560,560,560,560,560,560,1690,560,1690,560,1690,560,560,560,1690,560,1690,560,2000}
        });
        db.put("Panasonic", new int[][]{
            {37000,69,3600,1800,560,560,560,560,560,560,560,560,560,1690,560,1690,560,560,560,1690,560,560,560,1690,560,560,560,1690,560,1690,560,560,560,1690,560,560,560,2000},
            {37000,69,3600,1800,560,560,560,560,560,560,560,560,560,1690,560,1690,560,560,560,1690,560,1690,560,1690,560,560,560,560,560,1690,560,560,560,1690,560,560,560,2000}
        });
        db.put("Hisense", new int[][]{
            {38000,69,9000,4500,560,560,560,560,560,1690,560,1690,560,1690,560,1690,560,560,560,560,560,1690,560,1690,560,1690,560,1690,560,560,560,1690,560,560,560,1690,560,2000},
            {38000,69,9000,4500,560,560,560,560,560,1690,560,1690,560,1690,560,1690,560,560,560,560,560,560,560,1690,560,1690,560,1690,560,560,560,1690,560,560,560,1690,560,2000}
        });
    }
}
