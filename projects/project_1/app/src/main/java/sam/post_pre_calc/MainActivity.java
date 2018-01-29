/*
This application is a post-pre calculator.
AUTHOR: Samuel Alston
DATE MODIFIED: DD/MM/YYYY 29/1/2018
CS 480 SP18
OVERVIEW: This application is to complete the CS 480 Mobile Apps project 1 requirement:
        "I expect this application to be in the realm of complexity of a basic postfix
         calculator – one or more buttons that are tied to some sort of interaction with
         one or more application views."
I will therefore create a calculator that may be switched to pre or postfix computing.
It will have 10 numeric keys, a button_plus, button_minus, button_multiply, button_divide,
button_period for decimal notation, button_equals, button_clear to reset the calculator,
and button_deliminator to indicate the end of a numeric input.
 */

package sam.post_pre_calc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {

    //bufferString is the container for the object the user has direct control over
    private String bufferString="0";
    //when the user inputs an object it moves to the finalDisplayString
    private String finalDisplayString= "";
    //If the decimal is set during input, no more decimals can be added
    private boolean decimalSetTrue = FALSE;
    private boolean mustSetDecimal = FALSE;

    //operators call buttonOpOnClick()
    //if there is something in the buffer, move it to the finalDisplayString with
    //the operator clicked, setting the buffer to 0
    public void buttonOpOnClick(View v) {
        if (!bufferString.equals("0")){
            TextView bufferScreenView = findViewById(R.id.buffer_screen);
            Button clicked = findViewById(v.getId());
            String sendValue = (String)clicked.getText();
            moveBufferToDisplay(sendValue+" ");
            bufferScreenView.setText(clicked.getText());
            bufferString = "0";
        }
    }

    public void buttonClearClick(View v) {
        bufferString = "0";
        TextView bufferScreenView = findViewById(R.id.buffer_screen);
        bufferScreenView.setText(bufferString);
    }

    public void buttonClearAllLongClick(View v) {
        bufferString = "0";
        finalDisplayString = "0";
        TextView bufferScreenView = findViewById(R.id.buffer_screen);
        TextView finalDisplayView = findViewById(R.id.final_display_screen);
        bufferScreenView.setText(bufferString);
        finalDisplayView.setText(finalDisplayString);
    }


    public void buttonPeriodOnClick(View v) {
        if (!decimalSetTrue) {
            decimalSetTrue = TRUE;
            mustSetDecimal = TRUE;
            //bufferString += ".";
            TextView bufferScreenDisplay = findViewById(R.id.buffer_screen);
            //display the decimal in the buffer, don't put it in the string if not necessary
            bufferScreenDisplay.setText(bufferString+".");
        }
    }

    //move object in buffer to finalDisplayString and redraw finalDisplayView
    private void moveBufferToDisplay(String operator){
        //decimals will always be done with the press of an operator
        decimalSetTrue = FALSE;
        mustSetDecimal = FALSE;
        finalDisplayString+=bufferString+" "+operator;
        TextView finalDisplayView = findViewById(R.id.final_display_screen);
        finalDisplayView.setText(finalDisplayString);
    }

    //Numeric keys call buttonNumericOnClick
    //adds values to the buffer
    public void buttonNumericOnClick(View v) {
        TextView msgTextView = findViewById(R.id.buffer_screen);
        Button clicked = findViewById(v.getId());
        //If the buffer only contains 0 the first input must overwrite it
        if( bufferString.equals("0")){
            //If the user presses 0 with a buffer equal to 0 the string won't grow
            bufferString = (String)clicked.getText();
        }else{
            String toAdd = (String)clicked.getText();
            if(mustSetDecimal){
                toAdd = "." + toAdd;
                mustSetDecimal=FALSE;
            }
            bufferString+=toAdd;
        }
        msgTextView.setText(bufferString);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}