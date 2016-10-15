package com.mdmedia.stmt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by arief on 29/09/2015.
 */
public class HomeActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homelayout);

        Button jadwal = (Button)findViewById(R.id.jadwal);
        Button pesan = (Button)findViewById(R.id.form);
        Button jadwalujian = (Button)findViewById(R.id.jadwalujian);

        jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent= new Intent(HomeActivity.this,MainActivity.class);
                Intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(Intent);
            }
        });

        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(HomeActivity.this, FormEmail.class);
                Intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(Intent);
            }
        });
        jadwalujian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(HomeActivity.this, UjianActivity.class);
                Intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(Intent);
            }
        });

    }

}
