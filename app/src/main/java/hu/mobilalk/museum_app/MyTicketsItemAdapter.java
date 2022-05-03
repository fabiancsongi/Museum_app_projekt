package hu.mobilalk.museum_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyTicketsItemAdapter extends RecyclerView.Adapter<MyTicketsItemAdapter.ViewHolderCart> implements Filterable {
    private static final String TAG = Home.class.getName();

    private ArrayList<MyTicketsItem> mMyTicketsItemsData;
    private ArrayList<MyTicketsItem> mMyTicketsItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;


    MyTicketsItemAdapter(Context context, ArrayList<MyTicketsItem> itemsData){
        this.mMyTicketsItemsData=itemsData;
        this.mMyTicketsItemsDataAll=itemsData;
        this.mContext=context;
    }

    @NonNull
    @Override
    public ViewHolderCart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyTicketsItemAdapter.ViewHolderCart(LayoutInflater.from(mContext).inflate(R.layout.list_item_cart, parent, false));
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyTicketsItemAdapter.ViewHolderCart holder, int position) {
        MyTicketsItem currentItem = mMyTicketsItemsData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in_rows);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    private Filter ticketsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<TicketsItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            results.count = mMyTicketsItemsDataAll.size();
            results.values = mMyTicketsItemsDataAll;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mMyTicketsItemsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

            @Override
    public int getItemCount() {
        return mMyTicketsItemsData.size();
    }
    class ViewHolderCart extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDescription;
        private TextView mPrice;

        public ViewHolderCart(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.itemTitleCart);
            mDescription = itemView.findViewById(R.id.itemDetailsCart);
            mPrice = itemView.findViewById(R.id.priceOfTicketCart);


        }

        public void bindTo(MyTicketsItem currentItem) {
            mTitle.setText(currentItem.getTitle());
            mDescription.setText(currentItem.getDescription());
            mPrice.setText(currentItem.getPrice());

            itemView.findViewById(R.id.removeTicketFromCart).setOnClickListener(view -> ((MyCartActivity)mContext).deleteItem(currentItem));

        }
    }
}
