/*
This application is a post-pre calculator.
AUTHOR: Samuel Alston
DATE MODIFIED: DD/MM/YYYY 24/1/2018
CS 480 SP18
OVERVIEW: This application is to complete the CS 480 Mobile Apps project 1 requirement:
        "I expect this application to be in the realm of complexity of a basic postfix
         calculator – one or more buttons that are tied to some sort of interaction with
         one or more application views."
I will therefore create a calculator that may be switched to pre or postfix computing.
It will have 10 numeric keys, a button_plus, button_minus, button_multiply, button_divide,
button_period for decimal notation, button_equals, button_clear to reset the calculator,
and button_deliminator to indicate the end of a numeric input.

CURRENT STATUS: Basic UI is in place with buttons hooked up to stub functions.
 */

package sam.post_pre_calc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String bufferString="0";
    private String finalDisplayString= "";

    //operators call buttonOpOnClick
    public void buttonOpOnClick(View v) {
        TextView msgTextView = findViewById(R.id.buffer_screen);
        Button clicked = findViewById(v.getId());
        msgTextView.setText(clicked.getText());
    }

    private void moveBufferToDisplay(){
        finalDisplayString+=bufferString+" ";
        TextView finalDisplayView = findViewById(R.id.final_display_screen);
        finalDisplayView.setText(finalDisplayString);
    }

    //Numerics call buttonNumericOnClick
    public void buttonNumericOnClick(View v) {
        TextView msgTextView = findViewById(R.id.buffer_screen);
        Button clicked = findViewById(v.getId());

        if( bufferString=="0"){
            bufferString = (String)clicked.getText();
        }else{
            bufferString+=clicked.getText();
        }
        msgTextView.setText(bufferString);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
