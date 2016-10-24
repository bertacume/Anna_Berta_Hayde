package edu.upc.abh.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity {

    String total_num = "";
    double value1, value2;
    String sign = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
    }

    public void OnClickNumber(View v){ //Función para concatenar el primer valor y mostrarlo en la pantalla (screen)
        Button btn = (Button)v;
        String str = btn.getText().toString();
        total_num += str;
        TextView screen = (TextView) findViewById(R.id.screen);
        screen.setText(total_num + "");
    }

    public void OnClickSign(View v){ //Función que almacena el primer valor en value1 y guarda el signo de la operación
       value1 = Double.parseDouble(total_num);
        total_num = "";
        Button btn = (Button)v;
        String str = btn.getText().toString();
        sign = str;
        TextView screen = (TextView) findViewById(R.id.screen);
        screen.setText("");
    }

    public void OnClickEqual(View v){
        TextView screen = (TextView) findViewById(R.id.screen);
        String str = screen.getText().toString();
        value2 = Double.parseDouble(str);
        double result = 0.;
        switch (sign){
            case "+" : result = value1 + value2;
                break;
            case "-" : result = value1 - value2;
                break;
            case "x" : result = value1 * value2;
                break;
            case "/" : result = value1 / value2;
                break;
        }
        screen.setText(result + "");
        //TODO: quan no poses un decimal que no et dongui un decimal
    }





}
