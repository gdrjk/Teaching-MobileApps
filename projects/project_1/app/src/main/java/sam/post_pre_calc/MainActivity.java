/*
This application is a post-pre calculator.

AUTHOR: Samuel Alston
DATE: DD/MM/YYYY 22/1/2018
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
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public void buttonOneOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("1");
        //msgTextView.append("1");
    }

    public void buttonTwoOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("2");
        //msgTextView.append("1");
    }
    public void buttonThreeOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("3");
        //msgTextView.append("1");
    }
    public void buttonFourOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("4");
        //msgTextView.append("1");
    }
    public void buttonFiveOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("5");
        //msgTextView.append("1");
    }
    public void buttonSixOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("6");
        //msgTextView.append("1");
    }
    public void buttonSevenOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("7");
        //msgTextView.append("1");
    }
    public void buttonEightOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("8");
        //msgTextView.append("1");
    }
    public void buttonNineOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("9");
        //msgTextView.append("1");
    }
    public void buttonZeroOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("0");
        //msgTextView.append("1");
    }
    public void buttonPlusOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("+");
        //msgTextView.append("1");
    }
    public void buttonMinusOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("-");
        //msgTextView.append("1");
    }
    public void buttonMultiplyOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("*");
        //msgTextView.append("1");
    }
    public void buttonDivideOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("/");
        //msgTextView.append("1");
    }
    public void buttonPeriodOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText(".");
        //msgTextView.append("1");
    }
    public void buttonEqualsOnClick(View v){
        TextView msgTextView = findViewById(R.id.display_screen);
        msgTextView.setText("=");
        //msgTextView.append("1");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
