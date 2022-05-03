package hu.mobilalk.museum_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MyCartActivity extends AppCompatActivity {

    private static final String TAG = MyCartActivity.class.getName();
    private static final int SECRET_KEY=14;
    private FirebaseUser user;

    private RecyclerView mRecycleview;
    private ArrayList<MyTicketsItem> mMyItemList;
    private MyTicketsItemAdapter mAdapter;

    private TextView mCircleCount;

    private FirebaseFirestore mFirestore;
    private CollectionReference mMyItems;

    private int gridNumber=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.i(TAG, "Authenticated user");
        } else {
            Log.d(TAG, "Not authenticated user");
            finish();
        }

        mRecycleview = findViewById(R.id.recyclerViewMyCart);
        mRecycleview.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mMyItemList = new ArrayList<>();

        mCircleCount = findViewById(R.id.red_circle_count);

        mAdapter = new MyTicketsItemAdapter(this, mMyItemList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleview.setLayoutManager(llm);
        mRecycleview.setAdapter( mAdapter );

        mFirestore = FirebaseFirestore.getInstance();
        mMyItems = mFirestore.collection("MyItems");


        queryData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private int queryData(){

        mMyItemList.clear();

        mMyItems.orderBy("title").limit(20).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                MyTicketsItem item = document.toObject(MyTicketsItem.class);
                item.setId(document.getId());
                mMyItemList.add(item);
            }
            if(mMyItemList.size()==0){
                queryData();
            }
            mAdapter.notifyDataSetChanged();
        });
        return mMyItemList.size();
    }

    public void deleteItem(MyTicketsItem item){
        DocumentReference ref = mMyItems.document(item._getId());

        ref.delete().addOnSuccessListener(success ->{
            Log.d(TAG, "Item deleted "+ item._getId() + "::::" + item.getTitle());
            if(mMyItemList.size()==0){
                Log.d(TAG, "Cart empty, going back.");
                finish();
            }
        })
        .addOnFailureListener(failure ->{
            Log.d(TAG, "No item deleted ");
        });
        Log.d(TAG, ""+mMyItemList.size());

        queryData();
    }
}