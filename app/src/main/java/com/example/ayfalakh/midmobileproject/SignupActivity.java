package com.example.ayfalakh.midmobileproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

public class SignupActivity extends AppCompatActivity {
    Realm realm;

    RealmResults<UserModel> user;

    Button signupbtn,cancel;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText birthdate;
    EditText phone,pass,name,address;
    RadioGroup gender;
    RadioButton selectedGender;
    String getphone, getpass, getname, getaddress, getbirthdate, getgender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        try {
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException r) {
            Realm.deleteRealm(realm.getDefaultConfiguration());
            realm = Realm.getDefaultInstance();
        }

        phone = findViewById(R.id.phoneNumber);
        pass = findViewById(R.id.password);
        name = findViewById(R.id.username);
        address = findViewById(R.id.address);
        gender = findViewById(R.id.gender);

        signupbtn = findViewById(R.id.signupbtn);
        cancel = findViewById(R.id.cancelbtn);
        birthdate = findViewById(R.id.birthdate);

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SignupActivity.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = gender.getCheckedRadioButtonId();
                selectedGender = findViewById(selectedId);

                getbirthdate = birthdate.getText().toString();
                getphone = phone.getText().toString();
                getname = name.getText().toString();
                getpass = pass.getText().toString();
                getaddress = address.getText().toString();
                getgender = selectedGender.getText().toString();

                boolean isMale = getgender.equals("Male");

                UserModel model = new UserModel(getphone, getname, getpass, isMale, getaddress, getbirthdate);
                model.setMale(isMale);

                if(getphone.isEmpty() || getname.isEmpty() || getpass.isEmpty() || getaddress.isEmpty() || getbirthdate.isEmpty() ){
                    Toast.makeText(SignupActivity.this, "Fill The Blank", Toast.LENGTH_SHORT).show();
                }else{
                    UserModel user = realm.where(UserModel.class).equalTo("phone", getphone).findFirst();
                    if (user == null) {
                        realm.beginTransaction();
                        realm.copyToRealm(model);
                        realm.commitTransaction();
                        Toast.makeText(SignupActivity.this, getphone+" registered", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(SignupActivity.this, getphone+" already exists, try other phone number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
    private void updateLabel() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        birthdate.setText(df.format(myCalendar.getTime()));
    }
}