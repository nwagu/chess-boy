package com.nwagu.android.chessboy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PGNViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pgnview);

        // Set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED);


        Intent hero = getIntent();
        String hio = hero.getStringExtra("PGN");

        final EditText pgnEditView = (EditText) findViewById(R.id.pgn_view);
        pgnEditView.setText(hio, TextView.BufferType.EDITABLE);


        Button closeButton = (Button) findViewById(R.id.button_close_pgn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
		
        Button shareButton = (Button) findViewById(R.id.button_share_pgn);
        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent  i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_TEXT, pgnEditView.getText().toString());
                startActivity(Intent.createChooser(i, "Share PGN"));
            }
        });

        // Initialize array adapter for newly discovered devices

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
