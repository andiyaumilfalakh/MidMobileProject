package com.example.ayfalakh.midmobileproject;

import android.content.Intent;
import android.os.UserHandle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.exceptions.RealmMigrationNeededException;

public class MenuActivity extends AppCompatActivity {
    Button show, edit, delete, logout;
    String phone;
    Realm realm;
    UserModel user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        show = findViewById(R.id.show);
        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);
        logout = findViewById(R.id.logout);

        try {
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException r) {
            Realm.deleteRealm(realm.getDefaultConfiguration());
            realm = Realm.getDefaultInstance();
        }

        Intent i = getIntent();
        phone = i.getStringExtra("phone");

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, ShowActivity.class);
                i.putExtra("phone", phone);
                startActivity(i);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, EditActivity.class);
                i.putExtra("phone", phone);
                startActivity(i);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = realm.where(UserModel.class).equalTo("phone", phone).findFirst();
                if(user!=null){
                    realm.beginTransaction();
                    user.deleteFromRealm();
                    realm.commitTransaction();
                    Intent i = new Intent(MenuActivity.this, MainActivity.class);
                    Toast.makeText(MenuActivity.this, phone+" Has been erased", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                Toast.makeText(MenuActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
    }
}
