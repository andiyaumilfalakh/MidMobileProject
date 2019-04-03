package com.example.ayfalakh.midmobileproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import io.realm.Realm;
import io.realm.exceptions.RealmMigrationNeededException;

public class ShowActivity extends AppCompatActivity {
    Button back;
    TextView phone, name, birthdate, gender, address;
    String getphone, getname, getbirthdate, getgender, getaddress;
    boolean isMale;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_activity);
        back = findViewById(R.id.backbtn);

        phone = findViewById(R.id.phoneNumber);
        name = findViewById(R.id.name);
        birthdate = findViewById(R.id.birthdate);
        gender = findViewById(R.id.gender);
        address = findViewById(R.id.address);

        Intent i = getIntent();
        getphone = i.getStringExtra("phone");

        try {
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException r) {
            Realm.deleteRealm(realm.getDefaultConfiguration());
            realm = Realm.getDefaultInstance();
        }

        UserModel user = realm.where(UserModel.class).equalTo("phone", getphone).findFirst();
        if (user != null) {
            getaddress = user.getAddress();
            getname = user.getName();
            getbirthdate = user.getBirthdate();
            getgender = isMale ? "Male" : "Female";

            phone.setText(getphone);
            name.setText(getname);
            birthdate.setText(getbirthdate);
            address.setText(getaddress);
            gender.setText(getgender);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowActivity.this, MenuActivity.class);
                i.putExtra("phone", getphone);
                startActivity(i);
            }
        });
    }
}
