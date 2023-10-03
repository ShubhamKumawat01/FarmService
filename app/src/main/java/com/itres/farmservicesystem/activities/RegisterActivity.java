package com.itres.farmservicesystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.itres.farmservicesystem.R;

public class RegisterActivity extends AppCompatActivity {

    EditText name,email,password;
    private FirebaseAuth auth;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        //getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();//for firebase
        if(auth.getCurrentUser()!=null){//so the current user directly go to main activity second time open app
            startActivity( new Intent(RegisterActivity.this,MainActivity.class) );
            finish();
        }
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        sharedPreferences=getSharedPreferences( "onBoardingScreen",MODE_PRIVATE );
        boolean isFirstTime=sharedPreferences.getBoolean( "firstTime",true );
        //fist time onboard activity will perform
        if(isFirstTime){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean( "firstTime",false );
            editor.apply();

            Intent intent=new Intent(RegisterActivity.this,OnBoardingActivity.class);
            startActivity( intent );
            finish();
        }
    }

    public void signUp(View view) {
        String userName=name.getText().toString();
        String userEmail=email.getText().toString();
        String userPassword=password.getText().toString();

        if(TextUtils.isEmpty( userName )){
            Toast.makeText( this, "Enter Name!", Toast.LENGTH_SHORT ).show();
            return;
        }
        if(TextUtils.isEmpty( userEmail )){
            Toast.makeText( this, "Enter Email Address!", Toast.LENGTH_SHORT ).show();
            return;
        }
        if(TextUtils.isEmpty( userPassword )){
            Toast.makeText( this, "Enter Password!", Toast.LENGTH_SHORT ).show();
            return;
        }
        if(userPassword.length()<6){
            Toast.makeText( this, "Password is too short,Enter minimum 6 character!", Toast.LENGTH_SHORT ).show();
            return;
        }

        auth.createUserWithEmailAndPassword(userEmail,userPassword )
                .addOnCompleteListener( RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText( RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT ).show();
                            startActivity( new Intent(RegisterActivity.this,MainActivity.class) );//go to the main activity
                        }
                        else {
                            Toast.makeText( RegisterActivity.this, "Registration Failed!!"+task.getException(), Toast.LENGTH_SHORT ).show();//give the reason
                        }
                    }
                } );
    }

    public void signIn(View view) {
        startActivity( new Intent(RegisterActivity.this,LoginActivity.class) );
    }
}