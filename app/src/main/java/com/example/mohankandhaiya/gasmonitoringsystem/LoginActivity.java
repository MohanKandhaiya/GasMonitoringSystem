package com.example.mohankandhaiya.gasmonitoringsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog dialog;
    private EditText Username, Password;
    private Button LoginButton;
    private FirebaseAuth firebaseAuth;

    static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        LoginButton = (Button) findViewById(R.id.login);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginDetails();

            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
// dont call **super**, if u want disable back button in current screen.
    }

    private void LoginDetails() {
//        dialog=new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
//        dialog.show();
//        dialog.setMessage("Logging in");
        String emailaddress = Username.getText().toString();
        String password = Password.getText().toString();

        if (emailaddress.isEmpty() && password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Username and Password Fields are Empty", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        } else if (emailaddress.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Username Field is Empty",  Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        } else if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Password Field is Empty",  Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        } else {
            Validate(emailaddress, password);
        }
    }

    private void Validate(final String userName, String passWord){

        firebaseAuth.signInWithEmailAndPassword(userName,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                else{

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_LONG).show();

                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Incorrect email address", Toast.LENGTH_LONG).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

