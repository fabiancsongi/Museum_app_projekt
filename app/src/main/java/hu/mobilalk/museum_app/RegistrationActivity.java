package hu.mobilalk.museum_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = RegistrationActivity.class.getName();
    private static final String pref_key = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY= 14;

    EditText familyNameET;
    EditText firstNameET;
    EditText yearOfBirthET;
    EditText monthOfBirthET;
    EditText dayOfBirthET;
    EditText userNameET;
    EditText emailET;
    EditText passwordFirstET;
    EditText passwordSecondET;
    EditText phoneNumberET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        int secret_key = getIntent().getIntExtra("secret_key", 0);

        if(secret_key != 14){
            finish();
        }
        Log.i(TAG, "onCreate");

        familyNameET = findViewById(R.id.editTextFamilyName);
        firstNameET = findViewById(R.id.editTextFirstName);
        yearOfBirthET = findViewById(R.id.editTextYearOfBirth);
        monthOfBirthET = findViewById(R.id.editTextMonthOfBirth);
        dayOfBirthET = findViewById(R.id.editTextDayOfBirth);
        userNameET = findViewById(R.id.editTextUserNameRegistration);
        emailET = findViewById(R.id.editTextEmail);
        passwordFirstET = findViewById(R.id.editTextpasswordFirst);
        passwordSecondET = findViewById(R.id.editTextpasswordSecond);
        phoneNumberET = findViewById(R.id.editTextPhoneNumber);

        preferences = getSharedPreferences(pref_key,MODE_PRIVATE);
        String email = preferences.getString("email","");
        String password = preferences.getString("password", "");

        emailET.setText(email);
        passwordFirstET.setText(password);

        mAuth = FirebaseAuth.getInstance();
    }
    public void registrationSend(View view){

        String familyNameStr=familyNameET.getText().toString();
        String firstNameStr=firstNameET.getText().toString();
        String yearOfBirthStr=yearOfBirthET.getText().toString();
        String monthOfBirthStr=monthOfBirthET.getText().toString();
        String dayOfBirthStr=dayOfBirthET.getText().toString();
        String userNameStr=userNameET.getText().toString();
        String emailStr=emailET.getText().toString();
        String passwordFirstStr=passwordFirstET.getText().toString();
        String passwordSecondStr=passwordSecondET.getText().toString();

        if(!passwordFirstStr.equals(passwordSecondStr)){
            Log.i(TAG, "A két jelszó nem egyezik");
            return;
        }

        Log.i(TAG, "Adatok: " + familyNameStr +
                "; " + firstNameStr + "; " + yearOfBirthStr +
                "; " + monthOfBirthStr + "; " + dayOfBirthStr +
                "; " + userNameStr + "; " + emailStr +
                "; " + passwordFirstStr + "; " + passwordSecondStr);


        if(familyNameStr.length() != 0 && familyNameStr != null &&
                firstNameStr.length() != 0 && firstNameStr != null &&
                yearOfBirthStr.length() != 0 && yearOfBirthStr != null &&
                monthOfBirthStr.length() != 0 && monthOfBirthStr != null &&
                dayOfBirthStr.length() != 0 && dayOfBirthStr != null &&
                userNameStr.length() != 0 && userNameStr != null &&
                emailStr.length() != 0 && emailStr != null &&
                passwordFirstStr.length() != 0 && passwordFirstStr != null &&
                passwordSecondStr.length() != 0 && passwordSecondStr != null) {
            mAuth.createUserWithEmailAndPassword(emailStr, passwordFirstStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "User created successfully");
                        goToHome();
                    } else {
                        Log.d(TAG, "User not created");
                        Toast.makeText(RegistrationActivity.this, "User not created: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            Toast.makeText(RegistrationActivity.this, "Üres mező(k)!",Toast.LENGTH_LONG).show();
        }
    }

    private void goToHome(/*user data*/){
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }
}