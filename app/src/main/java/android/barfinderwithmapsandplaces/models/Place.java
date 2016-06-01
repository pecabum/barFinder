package android.barfinderwithmapsandplaces.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;


public class Place implements Parcelable {

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    private Geometry geometry;
    private String id;
    private String name;
    private String place_id;
    private String rating;

    private int distance;

    protected Place(Parcel in) {
        geometry = in.readParcelable(Geometry.class.getClassLoader());
        id = in.readString();
        name = in.readString();
        place_id = in.readString();
        rating = in.readString();
        distance = in.readInt();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };


    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDistance() {
        return distance;
    }

    public Location getLocation() {
        Location placeLocation = new Location(name);
        placeLocation.setLatitude(geometry.getLocation().getLat());
        placeLocation.setLongitude(geometry.getLocation().getLng());
        return placeLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(geometry, flags);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(place_id);
        dest.writeString(rating);
        dest.writeInt(distance);
    }
}
