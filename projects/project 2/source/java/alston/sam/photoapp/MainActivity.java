package alston.samuel.emotojunior;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    GridLayout mainGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /*   Button next = (Button) findViewById(R.id.button_next);

 next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });*/
        mainGrid = (GridLayout)findViewById(R.id.main);

        setSingleEvent(mainGrid);

    }

    private void setSingleEvent(GridLayout mainGrid){
        //loop all child items of main grid
        for(int i=0; i<mainGrid.getChildCount();i++)
        {

            CardView cardView = (CardView)mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this,"Clicked: "+finalI,Toast.LENGTH_LONG);

                }
            });
        }
    }
}
