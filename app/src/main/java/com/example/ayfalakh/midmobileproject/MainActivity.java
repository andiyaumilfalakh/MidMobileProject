package com.example.ayfalakh.midmobileproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.exceptions.RealmMigrationNeededException;

public class MainActivity extends AppCompatActivity {
    Realm realm;
    Button signup, login;
    EditText number, pass;
    String getnumber, getpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Realm.init(this);

        try {
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException r) {
            Realm.deleteRealm(realm.getDefaultConfiguration());
            realm = Realm.getDefaultInstance();
        }

        signup = findViewById(R.id.signupbtn);
        login = findViewById(R.id.loginBtn);
        number = findViewById(R.id.phoneNumber);
        pass = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getnumber = number.getText().toString();
                getpass = pass.getText().toString();

                if(getnumber.isEmpty() || getpass.isEmpty()){
                    Toast.makeText(MainActivity.this, "Fill The Blank", Toast.LENGTH_SHORT).show();
                }else{
                    UserModel user = realm.where(UserModel.class).equalTo("phone", getnumber).findFirst();
                    if (user != null) {
                        String user_password = user.getPass();
                        if (user_password.equals(getpass)) {
                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            intent.putExtra("phone", getnumber);
                            Toast.makeText(MainActivity.this, getnumber+" Login Successfull", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Phone number not registered", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

        LinearLayout touchInterceptor = findViewById(R.id.mainlinear);
        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(number.isFocused() || pass.isFocused()){
                        Rect outRect = new Rect();
                        number.getGlobalVisibleRect(outRect);
                        pass.getGlobalVisibleRect(outRect);
                        if(!outRect.contains((int)event.getRawX(),(int)event.getRawY())){
                            number.clearFocus();
                            pass.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are You Sure Want To Exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
