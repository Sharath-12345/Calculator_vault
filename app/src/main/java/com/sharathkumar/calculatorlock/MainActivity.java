package com.sharathkumar.calculatorlock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


public class MainActivity extends AppCompatActivity {
    private TextView inputtextview, enterpasswordtextview;
    int heightofinputtextview, widthofinputtextview;
    GridLayout container1;

    String secondpassword;



    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnplus, btnminus, btnmul, btndiv, btndot, btnequal, btnback, btnac;
    String input = "0";
    int flag = 0;
    String password;
    String firstpassword;

    Boolean check;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences preferences=getSharedPreferences("firsttimessss",MODE_PRIVATE);
        check=preferences.getBoolean("flag",false);


        findingallids();


        MobileAds.initialize(this);
        AdRequest request=new AdRequest.Builder().build();
        AdView adView=findViewById(R.id.Adview);
        adView.loadAd(request);

        gettingdisplaymetrics();

        settingheightandwidth();

        display();


        if (check) {
            enterpasswordtextview.setText(null);

        }

    }



    private void evaluating() {
        if(check)
        {
            SharedPreferences sharedPreferences=getSharedPreferences("firsttimessss",MODE_PRIVATE);
            String pass=sharedPreferences.getString("sflag","sha");
            if(input.equals(pass))
            {
                input="0";
                inputtextview.setText(input);
                gotomenu();
            } else if (input.equals("1234567890")) {

                    input = "0";
                    inputtextview.setText(input);
                    Intent intent = new Intent(MainActivity.this, forgetpassword.class);
                    startActivity(intent);

            } else
            {
                try {

                    Context rhino = Context.enter();
                    rhino.setOptimizationLevel(-1);
                    Scriptable scriptable = rhino.initSafeStandardObjects();
                    Object evaluresult = rhino.evaluateString(scriptable, input, "Calculator", 1, null);
                    result = Context.toString(evaluresult);
                    input = result;
                    enterpasswordtextview.setText(null);
                    inputtextview.setText(input);
                } catch (Exception e) {
                    e.printStackTrace();
                    enterpasswordtextview.setText("Invalid input");
                    input="0";
                    inputtextview.setText(input);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enterpasswordtextview.setText("");
                        }
                    },3000);
                }
            }
        }

        if(!check)
        {
            if(input.length()>4)
            {
                Toast.makeText(this, "Password should be 4 digits only", Toast.LENGTH_SHORT).show();
            }
            else   if (input.length() == 4 && TextUtils.isDigitsOnly(input) && flag == 0) {
                firstpassword = input;
                enterpasswordtextview.setText("RE-ENTER THE PASSWORD");
                input = "0";
                inputtextview.setText(input);
                flag++;
            } else if (flag == 1) {
                secondpassword = input;
                if (firstpassword.equals(secondpassword)) {
                    input = "0";

                    Dialog dialog=new Dialog(this);
                    dialog.setContentView(R.layout.customdialogpasssucess);
                    dialog.setCancelable(false);
                    dialog.show();
                    Button btnokay=dialog.findViewById(R.id.btnokay);
                    btnokay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            SharedPreferences preferences=getSharedPreferences("firsttimessss",MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putBoolean("flag",true);
                            editor.putString("sflag",secondpassword);
                            editor.apply();
                            input="0";
                            enterpasswordtextview.setText(null);
                            inputtextview.setText(input);
                            dialog.dismiss();
                            gotosecquestion();

                        }
                    });


                } else {
                    Dialog dialog=new Dialog(this);
                    dialog.setContentView(R.layout.customdialogpasssucess);
                    dialog.setCancelable(false);
                    dialog.show();
                    TextView textView=dialog.findViewById(R.id.txtpasssucess);
                    textView.setText("Passwords dont match ");
                    Button okay=dialog.findViewById(R.id.btnokay);
                    okay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            input = "0";
                            inputtextview.setText(input);
                            enterpasswordtextview.setText("CREATE A 4 DIGIT PASSWORD AND PRESS ' = ''");
                            flag--;
                            dialog.dismiss();
                        }
                    });

                }

            }

        }

    }

    private void gotomenu() {
        Intent intent=new Intent(MainActivity.this,menu.class);
        startActivity(intent);
    }

    private void gotosecquestion() {
        Intent intent=new Intent(MainActivity.this,secquestion.class);
        startActivity(intent);
    }


    private void display() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="1";
                }
                else
                {
                    input=input+"1";
                }
                inputtextview.setText(input);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="2";
                }
                else
                {
                    input=input+"2";
                }
                inputtextview.setText(input);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="3";
                }
                else
                {
                    input=input+"3";
                }
                inputtextview.setText(input);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="4";
                }
                else
                {
                    input=input+"4";
                }
                inputtextview.setText(input);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="5";
                }
                else
                {
                    input=input+"5";
                }
                inputtextview.setText(input);
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="6";
                }
                else
                {
                    input=input+"6";
                }
                inputtextview.setText(input);
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="7";
                }
                else
                {
                    input=input+"7";
                }
                inputtextview.setText(input);
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="8";
                }
                else
                {
                    input=input+"8";
                }
                inputtextview.setText(input);
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="9";
                }
                else
                {
                    input=input+"9";
                }
                inputtextview.setText(input);
            }
        });

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="0";
                }
                else
                {
                    input=input+"0";
                }
                inputtextview.setText(input);
            }
        });

        btndot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input==null)
                {
                    input=".";
                }
                else
                {
                    input=input+".";
                }
                inputtextview.setText(input);
            }
        });
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="+";
                }

                else
                {
                    input=input+"+";
                }
                inputtextview.setText(input);
            }
        });

        btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="-";
                }

                else
                {
                    input=input+"-";
                }
                inputtextview.setText(input);
            }
        });

        btnmul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="*";
                }

                else
                {
                    input=input+"*";
                }
                inputtextview.setText(input);
            }
        });

        btndiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input=="0")
                {
                    input="/";
                }

                else
                {
                    input=input+"/";
                }


                inputtextview.setText(input);
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input!=null){
                    input = input.substring(0, input.length() - 1);
                    if(input.equals(""))
                    {
                        input="0";
                    }
                    inputtextview.setText(input);
                }
            }
        });

        btnac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input="0";
                inputtextview.setText(input);
            }
        });
        btnequal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluating();
            }
        });




    }


    private void settingheightandwidth() {
        btn7.setHeight(heightofinputtextview/10);
        btn7.setWidth(widthofinputtextview/4);
        btn8.setHeight(heightofinputtextview/10);
        btn8.setWidth(widthofinputtextview/4);
        btn9.setHeight(heightofinputtextview/10);
        btn9.setWidth(widthofinputtextview/4);
        btnplus.setHeight(heightofinputtextview/10);
        btnplus.setWidth(widthofinputtextview/4);

        btn4.setHeight(heightofinputtextview/10);
        btn4.setWidth(widthofinputtextview/4);
        btn5.setHeight(heightofinputtextview/10);
        btn5.setWidth(widthofinputtextview/4);
        btn6.setHeight(heightofinputtextview/10);
        btn6.setWidth(widthofinputtextview/4);
        btnminus.setHeight(heightofinputtextview/10);
        btnminus.setWidth(widthofinputtextview/4);

        btn1.setHeight(heightofinputtextview/10);
        btn1.setWidth(widthofinputtextview/4);
        btn2.setHeight(heightofinputtextview/10);
        btn2.setWidth(widthofinputtextview/4);
        btn3.setHeight(heightofinputtextview/10);
        btn3.setWidth(widthofinputtextview/4);
        btnmul.setHeight(heightofinputtextview/10);
        btnmul.setWidth(widthofinputtextview/4);

        btndot.setHeight(heightofinputtextview/10);
        btndot.setWidth(widthofinputtextview/4);
        btn0.setHeight(heightofinputtextview/10);
        btn0.setWidth(widthofinputtextview/4);
        btnback.setHeight(heightofinputtextview/10);
        btnback.setWidth(widthofinputtextview/4);
        btndiv.setHeight(heightofinputtextview/10);
        btndiv.setWidth(widthofinputtextview/4);

        btnac.setHeight(heightofinputtextview/10);
        btnac.setWidth(widthofinputtextview/2);
        btnequal.setHeight(heightofinputtextview/10);
        btnequal.setWidth(widthofinputtextview/2);
    }

    private void gettingdisplaymetrics() {

        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightofinputtextview=displayMetrics.heightPixels;
        widthofinputtextview=displayMetrics.widthPixels;
    }

    private void findingallids() {

        inputtextview=findViewById(R.id.inputtextview);
        container1=findViewById(R.id.container1);
        enterpasswordtextview=findViewById(R.id.enterpassordtextview);


        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        btn3=findViewById(R.id.btn3);
        btnmul=findViewById(R.id.btnmul);
        btn4=findViewById(R.id.btn4);
        btn5=findViewById(R.id.btn5);
        btn6=findViewById(R.id.btn6);
        btnminus=findViewById(R.id.btnmius);
        btn7=findViewById(R.id.btn7);
        btn8=findViewById(R.id.btn8);
        btn9=findViewById(R.id.btn9);
        btnplus=findViewById(R.id.btnplus);
        btndot=findViewById(R.id.btndot);
        btn0=findViewById(R.id.btn0);
        btnback=findViewById(R.id.btnback);
        btndiv=findViewById(R.id.btndiv);
        btnac=findViewById(R.id.btnac);
        btnequal=findViewById(R.id.btnequal);

    }
}