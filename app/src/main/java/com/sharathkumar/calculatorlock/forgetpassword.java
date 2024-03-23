package com.sharathkumar.calculatorlock;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sharathkumar.calculatorlock.R;

public class forgetpassword extends AppCompatActivity {
    TextView txt;
    Button submit;
    EditText edtanswer;
    String edtanswerofuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        txt=findViewById(R.id.sectxt);
        submit=findViewById(R.id.submit);
        edtanswer=findViewById(R.id.edtanswer);

        SharedPreferences sharedPreferences = getSharedPreferences("secquestion", MODE_PRIVATE);
        String answer=sharedPreferences.getString("flags","");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtanswerofuser=edtanswer.getText().toString();

                if(answer.equals(edtanswerofuser))
                {
                    Intent intent=new Intent(forgetpassword.this,changepasswordactivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(forgetpassword.this, "Wrong answer", Toast.LENGTH_SHORT).show();
                }

            }

        });


    }
}