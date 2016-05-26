package android.barfinderwithmapsandplaces.adapters;

import android.barfinderwithmapsandplaces.R;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView placeName;
    public TextView placeDistance;
    public View mainView;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mainView = itemView;
        placeName = (TextView) itemView.findViewById(R.id.tv_place_name);
        placeDistance = (TextView) itemView.findViewById(R.id.tv_place_distance);
    }

    @Override
    public void onClick(View view) {

        Location location = (Location) view.getTag();
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + location.getLatitude() + "," + location.getLongitude());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
            view.getContext().startActivity(mapIntent);
        }
    }
}