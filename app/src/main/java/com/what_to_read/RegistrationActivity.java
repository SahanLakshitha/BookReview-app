package com.what_to_read;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.info_tech.R;

public class RegistrationActivity extends AppCompatActivity {
    EditText name, pass, email;
    Button register;
    DatabaseFactory databaseFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.regName);
        pass = findViewById(R.id.regPassword);
        email = findViewById(R.id.regMail);
        register = findViewById(R.id.register);
        databaseFactory = new DatabaseFactory(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nm = name.getText().toString().trim();
                String ps = pass.getText().toString().trim();
                String em = email.getText().toString().trim();
                Boolean inserting = databaseFactory.save_user_data(nm, ps, em);
                if(inserting == true){
                    Toast.makeText(RegistrationActivity.this, "Registration successfully", Toast.LENGTH_SHORT).show();
//                    databaseFactory.save_Current_user(nm, em, null);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                else {
                    Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}