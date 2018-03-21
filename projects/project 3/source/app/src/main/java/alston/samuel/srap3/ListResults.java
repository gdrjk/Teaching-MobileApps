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
    private TextView workingLabels;
    private TextView header;
    private String stringOfLabels;
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
        btnNo = (Button) findViewById(R.id.button_no);
        btnYes = (Button) findViewById(R.id.button_yes);

        btnYes.setOnClickListener(new btnYesListener());
        btnNo.setOnClickListener(new btnNoListener());

        resultTextView = (TextView) findViewById(R.id.textViewResult);
        workingLabels = (TextView) findViewById(R.id.workingLabels);
        header = (TextView) findViewById(R.id.header);

        //initialize string to empty so we can += strings
        stringOfLabels = "";

        thisIntent = getIntent();
        Bundle b = thisIntent.getExtras();

        labels =b.getStringArray("labels");
        certainty = b.getFloatArray("certainty");

        i = 0;

        try {
            resultTextView.setText(labels[i] + "? \n(" + certainty[i] + " probability of accuracy.)");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"No labels were returned. Try another image.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    class btnYesListener implements  Button.OnClickListener {
        //exit activity when yes is clicked, prompting user to do another image detection
        @Override
        public void onClick(View view) {
            if(i < labels.length) {
                stringOfLabels+="("+ labels[i] + ") ";
                iterateLabel();
                workingLabels.setText(stringOfLabels);
            } else {
                endOfLabels();
            }
        }
    }

    class btnNoListener implements Button.OnClickListener {
        //Change textView to next possible answer
        @Override
        public void onClick(View view) {
            if(i < labels.length) {
                iterateLabel();
            } else {
                endOfLabels();
            }
        }
    }

    class btnExitListener implements  Button.OnClickListener {
        //Exit activity
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    private void iterateLabel(){
        //go to next label, set text view
        i++;
        if(i < labels.length ) {
            resultTextView.setText(labels[i] + "? \n(" + certainty[i] + " probability of accuracy.)");
        } else {
            endOfLabels();
        }
    }

    private void endOfLabels(){
        //when list of labels runs out, set screen to display the labels image did contain
        //disable yes button, turn no button into exit button
        header.setText("Image Contained:");
        resultTextView.setText(stringOfLabels);
        stringOfLabels="";
        workingLabels.setText(stringOfLabels);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnNo.setText("Exit");
                btnNo.setOnClickListener(new btnExitListener());
                btnYes.setEnabled(false);
                header.setTextSize(24);
                resultTextView.setTextSize(18);
            }
        });
    }
}
