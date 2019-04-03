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
import io.realm.exceptions.RealmMigrationNeededException;

public class EditActivity extends AppCompatActivity {
    Button save, cancel;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;
    EditText phone, name, birthdate, address, pass;
    boolean isMale;
    String getphone, getpass, getname, getbirthdate, getaddress;

    Realm realm;
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        save = findViewById(R.id.savebtn);
        cancel = findViewById(R.id.cancelbtn);
        phone = findViewById(R.id.phoneNumber);
        name = findViewById(R.id.name);
        pass = findViewById(R.id.password);
        birthdate = findViewById(R.id.birthdate);
        address = findViewById(R.id.address);
        pass = findViewById(R.id.password);

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
                new DatePickerDialog(EditActivity.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Intent i = getIntent();
        getphone = i.getStringExtra("phone");

        try {
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException r) {
            Realm.deleteRealm(realm.getDefaultConfiguration());
            realm = Realm.getDefaultInstance();
        }

        user = realm.where(UserModel.class).equalTo("phone", getphone).findFirst();
        if (user != null) {
            getname = user.getName();
            getpass = user.getPass();
            isMale = user.isMale();
            getaddress = user.getAddress();
            getbirthdate = user.getBirthdate();

            name.setText(getname);
            phone.setText(getphone);
            pass.setText(getpass);
            address.setText(getaddress);
            birthdate.setText(getbirthdate);
            RadioButton male, female;
            male = findViewById(R.id.male);
            female = findViewById(R.id.female);
            if (isMale) {
                male.setChecked(true);
            } else {
                female.setChecked(true);
            }
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = realm.where(UserModel.class).equalTo("phone", getphone).findFirst();
                if (user != null) {
                    getname = name.getText().toString();
                    getpass = pass.getText().toString();
                    getbirthdate = birthdate.getText().toString();
                    RadioGroup genderRadio = findViewById(R.id.gender);
                    RadioButton selectedgender = findViewById(genderRadio.getCheckedRadioButtonId());
                    isMale = selectedgender.getText().toString().equals("Male");
                    getaddress = address.getText().toString();

                    realm.beginTransaction();
                    user.setName(getname);
                    user.setPass(getpass);
                    user.setMale(isMale);
                    user.setAddress(getaddress);
                    user.setBirthdate(getbirthdate);
                    realm.commitTransaction();

                }
                Intent intent = new Intent(EditActivity.this, MenuActivity.class);
                Toast.makeText(EditActivity.this, getphone+" Data Updated", Toast.LENGTH_SHORT).show();
                intent.putExtra("phone", getphone);
                startActivity(intent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditActivity.this, MenuActivity.class);
                i.putExtra("phone", getphone);
                startActivity(i);
            }
        });
    }
    private void updateLabel() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        birthdate.setText(df.format(getbirthdate));
    }
}
