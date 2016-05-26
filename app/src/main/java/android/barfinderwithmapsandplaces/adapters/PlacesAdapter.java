package android.barfinderwithmapsandplaces.adapters;

import android.barfinderwithmapsandplaces.R;
import android.barfinderwithmapsandplaces.models.Place;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private ArrayList<Place> itemList;
    private Context context;

    public PlacesAdapter(Context context, ArrayList<Place> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_place_item, null);
        RecyclerViewHolder rcv = new RecyclerViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        holder.placeName.setText(itemList.get(position).getName());
        holder.placeDistance.setText(String.format(context.getString(R.string.metric), Integer.toString(itemList.get(position).getDistance())));
        holder.mainView.setTag(itemList.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public void setNewPlaces(ArrayList<Place> newPlaces) {
        this.itemList = newPlaces;
        notifyDataSetChanged();
    }
}
