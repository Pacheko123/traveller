package com.pacheko.traveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class signUp extends AppCompatActivity {
    public TextView toLogin;
    public EditText mail,password;
    public Button signup;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        auth = FirebaseAuth.getInstance();
        toLogin = findViewById(R.id.tologinLink);

        progressBar = findViewById(R.id.pBar);

        mail = findViewById(R.id.editMail);
        password = findViewById(R.id.editPass);
        signup = findViewById(R.id.signupButton);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLogin =  new Intent(signUp.this, MainActivity.class);
                startActivityForResult(toLogin,0);
              overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String mailReg = mail.getText().toString();
                String passReg = password.getText().toString();
                if (mailReg.isEmpty() || passReg.isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(signUp.this,"KIndly fill all fields",Toast.LENGTH_SHORT).show();
                }else{

                    //create a new user
                    auth.createUserWithEmailAndPassword(mailReg,passReg)
                            .addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(signUp.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                    /* If sign in fails, display a message to the user. If sign in succeeds
                                     the auth state listener will be notified and logic to handle the Signup*/
                                    if (!task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(signUp.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(signUp.this, chatRoom.class));
                                        finish();
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
