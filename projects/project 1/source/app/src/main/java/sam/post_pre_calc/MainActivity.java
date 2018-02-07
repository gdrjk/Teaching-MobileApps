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
button_period for decimal notation, button_equals, button_clear and button_clear_all to
clear the calculator, and button_deliminator to move number in the buffer to the finalDisplay.
 */

package alston.samuel.post_pre_calc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

class Node {
    private String nodeValue;
    private Node next;
    private Node previous;
    private Boolean isOperator;

    Node(String newVal, Node previousNode) {
        setNext(null);
        setPrevious(previousNode);
        setNodeValue(newVal);

        switch (newVal.trim()) {
            case "+":
            case "-":
            case "*":
            case "/":
                setIsOperator(TRUE);
                break;
            default:
                setIsOperator(FALSE);
        }
    }

    public Node getNext() {
        return this.next;
    }

    public void setNext(Node current) {
        this.next = current;
    }

    public Node getPrevious() {
        return this.previous;
    }

    public void setPrevious(Node current) {
        this.previous = current;
    }

    public Boolean getIsOperator() {
        return isOperator;
    }

    public void setIsOperator(Boolean value) {
        isOperator = value;
    }

    public String getValue() {
        return nodeValue;
    }

    public void setNodeValue(String newVal) {
        nodeValue = newVal;
    }
}

public class MainActivity extends AppCompatActivity {
    //bufferString is the container for the object the user has direct control over
    private String bufferString = "0";
    //when the user inputs an object it moves to the finalDisplayString
    private String finalDisplayString = "";
    //If the decimal is set during input, no more decimals can be added
    private boolean decimalSetTrue = FALSE;
    //To prevent strings with needless trailing decimals, they are not added to the buffer until another number is pressed, although they appear in the buffer view
    private boolean mustSetDecimal = FALSE;
    //point to a node in a linked list, defaults to last node when built
    private Node head;

    public void redrawBuffer() {
        TextView bufferScreenView = findViewById(R.id.buffer_screen);
        bufferScreenView.setText(bufferString);
    }

    public void redrawFinalDisplay() {
        TextView bufferScreenView = findViewById(R.id.final_display_screen);
        bufferScreenView.setText(finalDisplayString);
    }

    private void moveBufferToDisplay() {
        //decimals will always be done with the press of an operator
        decimalSetTrue = FALSE;
        mustSetDecimal = FALSE;
        finalDisplayString += bufferString;
        redrawFinalDisplay();
    }

    public void buttonOpOnClick(View v) {
        //operators call buttonOpOnClick()
        //if there is something in the buffer, move it to the finalDisplayString with
        //the operator clicked, setting the buffer to 0
        Button clicked = findViewById(v.getId());
        if(!bufferString.equals("0")){
            moveBufferToDisplay();
            bufferString = "0";
        }
        finalDisplayString+= " "+clicked.getText()+" ";
        redrawFinalDisplay();
        redrawBuffer();
    }

    public void buttonClearClick(View v) {
        //sets buffer to "0" and redraws, if CE was clicked, does the same for the finalDisplay
        bufferString = "0";
        redrawBuffer();
        Button clicked = findViewById(v.getId());
        if(clicked.getText().equals("CE")){
            finalDisplayString = "";
            redrawFinalDisplay();
        }
    }

    public void buttonPeriodOnClick(View v) {
        //When decimal is pressed, it appears in the buffer but won't be placed until a following numeric value is added in buttonNumericOnClick
        if (!decimalSetTrue) {
            decimalSetTrue = TRUE;
            mustSetDecimal = TRUE;
            TextView bufferScreenDisplay = findViewById(R.id.buffer_screen);
            //display the decimal in the buffer, don't put it in the string if not necessary
            bufferScreenDisplay.setText(bufferString + ".");
        }
    }

    public void buttonNumericOnClick(View v) {
        //Numeric keys call buttonNumericOnClick
        //adds values to the buffer
        Button clicked = findViewById(v.getId());
        //If the buffer only contains 0 the first input must overwrite it
        if (bufferString.equals("0")&& !decimalSetTrue) {
            //If the user presses 0 with a buffer equal to 0 the string won't grow
            bufferString = (String) clicked.getText();
        } else {
            String toAdd = (String) clicked.getText();
            if (mustSetDecimal) {
                toAdd = "." + toAdd;
                mustSetDecimal = FALSE;
            }
            bufferString += toAdd;
        }
        redrawBuffer();
    }

    public void buttonDeliminatorOnClick(View v) {
        bufferString+=" ";
        moveBufferToDisplay();
        bufferString="0";
        redrawBuffer();
    }

    public void buttonEqualsOnClick(View v) {
        //When the equals operator is pressed, the buffer goes to the final and the final splits the string and puts them into nodes
        //Computes based on the post_or_pre switch
        String arrayOfValues[] = finalDisplayString.split(" ");
        Node previous = null;
        Node current = null;
        for (String val : arrayOfValues) {
            current = new Node(val, previous);
            if(previous!=null)
                previous.setNext(current);
            previous = current;
        }
        head = current;


        //traverse();
        postFixCompute();
        /*
        Switch postOrPre = findViewById(R.id.switch_post_or_pre);
        String postOrPreString = (String) postOrPre.getText();

        if(postOrPreString.equals("post")){
            postFixCompute();
        } else {

        }
        */
    }

    public void postFixCompute() {
        //when the linked list is made, the head is pointing to the last node
        goToStartOfLL();
        Double left=0d, right=0d, answer=0d;
        Node previous=null;
        Node current = head;
        while(current != null) {
            if(current.getIsOperator()){
                try{
                    previous = current.getPrevious();
                    right = Double.parseDouble(previous.getValue());
                } catch (NumberFormatException e) {
                    right = 0d;
                }
                try {
                    previous = previous.getPrevious();
                    left = Double.parseDouble(previous.getValue());
                } catch(NumberFormatException e) {
                    left = 0d;
                }
                previous.setNodeValue(answer.toString());
                previous.setIsOperator(FALSE);
                previous.setNext(current.getNext());
                if(current.getNext()!=null)
                    current.getNext().setPrevious(previous);

                switch(current.getValue()){
                    case "+":
                        answer = left + right;
                        break;
                    case "-":
                        answer = left - right;
                        break;
                    case "*":
                        if(left==0 || right==0)
                            answer=0d;
                        else
                            answer = left * right;
                        break;
                    case "/":
                        try{
                            if(right==0 || left==0){
                                answer = 0d;
                            } else {
                                answer = left / right;
                            }
                        } catch(Exception e){
                            finalDisplayString = e.toString();
                        }
                }

            }
            current = current.getNext();
        }
        if(answer==0)
            finalDisplayString="0";
        else
            finalDisplayString = String.valueOf(answer);
        redrawFinalDisplay();
        finalDisplayString="";

    }

    public void preFixCompute() {

    }

    public void traverse() {
        goToStartOfLL();
        finalDisplayString = "";
        Node current = head;
        while(current != null){
            finalDisplayString+= current.getValue();
            current = current.getNext();
        }
        redrawFinalDisplay();
    }

    public void goToStartOfLL() {
        //this will point head to the first node
        while(head.getPrevious() != null)
            head = head.getPrevious();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
