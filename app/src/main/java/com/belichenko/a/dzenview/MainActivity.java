package com.belichenko.a.dzenview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements DzenView.DzenViewListener{

    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Message", Toast.LENGTH_SHORT).show();
            }
        });

        textView2 = (TextView) findViewById(R.id.text_view2);
        DzenView dzenView = (DzenView) findViewById(R.id.dzen_view);
        dzenView.setListener(this);

    }

    @Override
    public void onAngelChanged(double angel) {
        textView2.setText(String.valueOf(angel));
    }
}
