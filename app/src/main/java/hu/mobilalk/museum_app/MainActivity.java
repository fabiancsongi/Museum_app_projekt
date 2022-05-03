package hu.mobilalk.museum_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = MainActivity.class.getName();
    private static final String pref_key = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY=14;
    private static final int RC_SIGN_IN=16;


    EditText emailET;
    EditText passwordET;
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);

        preferences = getSharedPreferences(pref_key,MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("530150105397-vgnqt4l359f6334mife0thjagg4dvu7e.apps.googleusercontent.com").requestEmail().build();

        getSupportLoaderManager().restartLoader(0,null,this);
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.i(TAG, "firebaseAuthWithGoogle: " + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e){
                Log.w(TAG, "Google sign in failed: " + e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Sign in with credential successful");
                    goToHome();
                } else {
                    Log.d(TAG, "User login failed");
                    Toast.makeText(MainActivity.this, "Sign in with credential failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void login(View view){
        String emailstr=emailET.getText().toString();
        String passwordStr = passwordET.getText().toString();

        if(emailstr.length()!=0 && emailstr != null && passwordStr.length()!=0 && passwordStr != null ) {

            mAuth.signInWithEmailAndPassword(emailstr, passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "User signed in successfully");
                        goToHome();
                    } else {
                        Log.d(TAG, "User login failed");
                        Toast.makeText(MainActivity.this, "User login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            Toast.makeText(MainActivity.this, "Üres mező(k)!", Toast.LENGTH_LONG).show();
        }

    }

    public void loginAsGuest(View view){
        mAuth.signInAnonymously().addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "User signed in anonymusly");
                    goToHome();
                } else {
                    Log.d(TAG, "User login failed");
                    Toast.makeText(MainActivity.this, "Anonym login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void goToHome(){
            Intent intent = new Intent(this, Home.class);
            intent.putExtra("SECRET_KEY", SECRET_KEY);
            startActivity(intent);
        }


    public void register(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra("secret_key", 14);
        //TODO.
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

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", emailET.getText().toString());
        editor.putString("password", passwordET.getText().toString());
        editor.apply();

        Log.i(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new OneAsyncLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        Button button = findViewById(R.id.loginAsGuest);
        button.setText(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) { }
}