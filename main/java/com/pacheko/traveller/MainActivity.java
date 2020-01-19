package com.pacheko.traveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity {
    public Button loginButton;
    public EditText textEmailAddress,textPassword;
    public TextView signup;
    public TextView passReset;
    private FirebaseAuth authLogin;
    private ProgressBar toNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.loginButton);
        textEmailAddress = findViewById(R.id.editText);
        textPassword = findViewById(R.id.editText2);
        signup = findViewById(R.id.signupLink);
        passReset = findViewById(R.id.passForgot);

        toNext = findViewById(R.id.pBar);

        authLogin = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNext.setVisibility(View.VISIBLE);
                final String email = textEmailAddress.getText().toString();
                final String password = textPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    toNext.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Please fill in all the fields",Toast.LENGTH_SHORT).show();
                }else {
                    authLogin.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    /* If sign in fails, display a message to the user. If sign in succeeds
                                     the auth state listener will be notified and logic to handle the
                                     signed in user can be handled in the listener.*/

                                    if (!task.isSuccessful()) {

                                        if (password.length() < 6) {
                                            textPassword.setError(getString(R.string.minimum_password));
                                            toNext.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(getApplicationContext(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                            toNext.setVisibility(View.GONE);
                                        }
                                    } else {
                                        toNext.setVisibility(View.VISIBLE);
                                        Intent login = new Intent(MainActivity.this, chatRoom.class);
                                        startActivityForResult(login, 0);
//                                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                        ///finish();
                                    }
                                }
                            });
                }
            }
        });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent join = new Intent(MainActivity.this,signUp.class);
                    startActivityForResult(join,0);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });

            passReset.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    toNext.setVisibility(View.VISIBLE);
                    final String email = textEmailAddress.getText().toString();

                    if (email.isEmpty()){
                        toNext.setVisibility(View.GONE);
                        textEmailAddress.setError("Enter email Address");
                        textEmailAddress.requestFocus();
                    }else{
                        authLogin.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                toNext.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "An email with the reset link has been sent", Toast.LENGTH_SHORT).show();
                            } else {
                                toNext.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this,"Connect to internet or check your email address" ,Toast.LENGTH_LONG).show();
                            }
                         }
                    });

                    }
                }
            });

        }

    @Override
    public void onBackPressed(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
    }

}
