package android.barfinderwithmapsandplaces.fragments;

import android.barfinderwithmapsandplaces.models.Place;
import android.barfinderwithmapsandplaces.adapters.PlacesAdapter;
import android.barfinderwithmapsandplaces.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class PlacesListFragment extends Fragment {
    private static final String KEY_LIST = "list";

    private RecyclerView rvPlaces;
    private ArrayList<Place> placesList;
    private PlacesAdapter rcAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_LIST)) {
            placesList = new ArrayList<>();
        }
        else {
            placesList = savedInstanceState.getParcelableArrayList(KEY_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_places_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPlaces = (RecyclerView) view.findViewById(R.id.rv_places);
        configList();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_LIST, placesList);
    }

    private void configList() {
        LinearLayoutManager lLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        if (rvPlaces != null) {
            rvPlaces.setHasFixedSize(true);
            rvPlaces.setLayoutManager(lLayout);
        }
        rcAdapter = new PlacesAdapter(getActivity(),placesList);
        if (rvPlaces != null) {
            rvPlaces.setAdapter(rcAdapter);
        }
    }

    public void loadData(ArrayList<Place> list) {
        this.placesList = list;
        if(rcAdapter!=null) {
            rcAdapter.setNewPlaces(placesList);
        }
    }
}
