package alston.samuel.srap3;
/*
    Author: Samuel Alston
    Last Modified: 2/27/2018
    This activity is for displaying the results from the image detection sent from MainActivity

 */


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ListResults extends AppCompatActivity {
    private Intent thisIntent;
    private TextView resultTextView;
    private Button btnYes;
    private Button btnNo;
    private String[] labels;
    private float[] certainty;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_results);

        initializeVars();
    }

    private void initializeVars() {
        //run first time set up for activity
        //find views and set textView to first response.
        //initialize i to 1 so it will iterate through arrays after first element
        btnNo = (Button) findViewById(R.id.button_no);
        btnYes = (Button) findViewById(R.id.button_yes);

        btnYes.setOnClickListener(new btnYesListener());
        btnNo.setOnClickListener(new btnNoListener());

        resultTextView = (TextView) findViewById(R.id.textViewResult);
        thisIntent = getIntent();
        Bundle b = thisIntent.getExtras();

        labels =b.getStringArray("labels");
        certainty = b.getFloatArray("certainty");

        i = 1;

        try {
            resultTextView.setText(labels[0] + "? \n(" + certainty[0] + "% certainty)");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"No labels were returned. Try another image.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    class btnYesListener implements  Button.OnClickListener {
        //exit activity when yes is clicked, prompting user to do another image detection
        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext(),"That's what I thought! Do another.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    class btnNoListener implements Button.OnClickListener {
        //Change textView to next possible answer
        @Override
        public void onClick(View view) {
            if(i < labels.length) {
                resultTextView.setText(labels[i] + "? \n(" + certainty[i] + "% certainty)");
                i++;
            } else {
                Toast.makeText(getApplicationContext(),"I'm out of guesses! Do another.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
