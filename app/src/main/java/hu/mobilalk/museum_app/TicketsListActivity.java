package hu.mobilalk.museum_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TicketsListActivity extends AppCompatActivity {

    private static final String TAG = TicketsListActivity.class.getName();
    private static final int SECRET_KEY=14;
    private FirebaseUser user;

    private RecyclerView mRecycleview;
    private ArrayList<TicketsItem> mItemList;
    private TicketsItemAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    private CollectionReference mMyItems;

    private FrameLayout redCircle;
    private TextView contentTextView;

    private NotificationHandler mNotificationHandler;

    private int cartItems=0;
    private int gridNumber=1;
    private int queryLimit=10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.i(TAG, "Authenticated user");
        } else {
            Log.d(TAG, "Not authenticated user");
            finish();
        }

        mRecycleview = findViewById(R.id.recyclerView);
        mRecycleview.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        mAdapter = new TicketsItemAdapter(this, mItemList);

        mRecycleview.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");
        mMyItems = mFirestore.collection("MyItems");

        mNotificationHandler = new NotificationHandler(this);

        queryData();
    }

    private void queryData(){

        mItemList.clear();

        mItems.orderBy("title").limit(queryLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                TicketsItem item = document.toObject(TicketsItem.class);
                mItemList.add(item);
            }
            if(mItemList.size()==0){
                initializeData();
                queryData();
            }
            mAdapter.notifyDataSetChanged();
        });
    }


    private void initializeData(){
        String[] itemsList = getResources().getStringArray(R.array.tickets_titles);
        String[] itemsDescription = getResources().getStringArray(R.array.tickets_descriptions);
        String[] itemsPrice = getResources().getStringArray(R.array.tickets_prices);

        for(int i = 0; i<itemsList.length; i++){
            mItems.add(new TicketsItem(itemsList[i], itemsDescription[i],itemsPrice[i]));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.tickets_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                Log.d(TAG, "Logout clicked");
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("SECRET_KEY", SECRET_KEY);
                startActivity(intent);
                return true;
            case R.id.settings:
                Log.d(TAG, "settings clicked");
                return true;
            case R.id.go_to_cart:
                Intent intentCart = new Intent(this, MyCartActivity.class);
                intentCart.putExtra("SECRET_KEY", SECRET_KEY);
                startActivity(intentCart);
                Log.d(TAG, "go to cart clicked");
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        final MenuItem alertMennuItem = menu.findItem(R.id.go_to_cart);
        FrameLayout rootView = (FrameLayout) alertMennuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.red_circle);
        contentTextView = (TextView) rootView.findViewById(R.id.red_circle_count);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(alertMennuItem);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    public void updateCircleCount(TicketsItem item){
        redCircle.setVisibility(View.GONE);
        mMyItems.add(new MyTicketsItem(item.getTitle(),item.getDescription(),item.getPrice()));
        mNotificationHandler.send(item.getTitle() + " jegy a kosárba helyezve.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}