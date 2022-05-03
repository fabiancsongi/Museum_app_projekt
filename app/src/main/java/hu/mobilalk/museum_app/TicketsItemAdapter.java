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
import java.util.Locale;

public class TicketsItemAdapter extends RecyclerView.Adapter<TicketsItemAdapter.ViewHolder> implements Filterable {
    private static final String TAG = Home.class.getName();

    private ArrayList<TicketsItem> mTicketsItemsData;
    private ArrayList<TicketsItem> mTicketsItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;


    TicketsItemAdapter(Context context, ArrayList<TicketsItem> itemsData){
        this.mTicketsItemsData=itemsData;
        this.mTicketsItemsDataAll=itemsData;
        this.mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsItemAdapter.ViewHolder holder, int position) {
        TicketsItem currentItem = mTicketsItemsData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mTicketsItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return ticketsFilter;
    }
    private Filter ticketsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<TicketsItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length()==0){
                results.count = mTicketsItemsDataAll.size();
                results.values = mTicketsItemsDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase(Locale.ROOT).trim();

                for(TicketsItem item : mTicketsItemsDataAll){
                    if(item.getTitle().toLowerCase(Locale.ROOT).contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mTicketsItemsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitle;
        private TextView mDescription;
        private TextView mPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.itemTitle);
            mDescription = itemView.findViewById(R.id.itemDetails);
            mPrice = itemView.findViewById(R.id.priceOfTicket);
        }

        public void bindTo(TicketsItem currentItem) {
            mTitle.setText(currentItem.getTitle());
            mDescription.setText(currentItem.getDescription());
            mPrice.setText(currentItem.getPrice());

            itemView.findViewById(R.id.addTicketToCart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Add to cart clicked.");
                    ((TicketsListActivity)mContext).updateCircleCount(currentItem);
                }

            });
        }
    }
}
