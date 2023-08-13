package com.example.calculatorlock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class secquestion extends AppCompatActivity {
    Spinner spinner;
    Toolbar toolbar;
    Button skipbtn,savebtn;
    static String edtvalue;
    EditText edtsecquestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secquestion);

        findingids();

        settingspinner();



        SharedPreferences sharedPreferences = getSharedPreferences("secquestion", MODE_PRIVATE);
        String answer=sharedPreferences.getString("flags","");
        edtsecquestion.setText(answer);

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(secquestion.this,menu.class);
                startActivity(intent);
                finish();
            }
        });




        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtvalue=edtsecquestion.getText().toString();
                if(edtvalue.length()>2) {
                    SharedPreferences sharedPreferences = getSharedPreferences("secquestion", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("flags", edtvalue.trim());
                    editor.apply();

                    Dialog dialog=new Dialog(secquestion.this);
                    dialog.setContentView(R.layout.customdialogpasssucess);
                    dialog.setCancelable(false);
                    dialog.show();
                    TextView textView=dialog.findViewById(R.id.txtpasssucess);
                    textView.setText("Security Question Updated Sucessfully");
                    Button button=dialog.findViewById(R.id.btnokay);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(secquestion.this, menu.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }
                    });




                }
            }
        });






    }



    private void settingspinner() {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,new String[]{"What is your mother name ",
                "What is your father name",
                "What is your school name ",
                "What is your best friend name"
        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Security Question");
    }

    private void findingids() {
        spinner=findViewById(R.id.spinner);
        toolbar=findViewById(R.id.toolbar);
        savebtn=findViewById(R.id.btnsave);
        skipbtn=findViewById(R.id.skipbtn);
        edtsecquestion=findViewById(R.id.edtsecquestion);
    }

    @Override
    public void onBackPressed() {

    }
}