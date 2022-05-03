package hu.mobilalk.museum_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private static final String TAG = Home.class.getName();

    private static final int SECRET_KEY=14;
    private FirebaseUser user;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.i(TAG, "Authenticated user");
        } else{
            Log.d(TAG, "Not authenticated user");
            finish();
        }
    }

    public void tickets(View view){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.i(TAG, "Authenticated user");
            goToTickets();
        }
    }

    private void goToTickets(){
        Intent intent = new Intent(this, TicketsListActivity.class);
        startActivity(intent);
    }
}