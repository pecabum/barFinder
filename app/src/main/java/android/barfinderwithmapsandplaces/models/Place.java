package android.barfinderwithmapsandplaces.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;


public class Place implements Parcelable{

    private double lat;
    private double lng;
    private String id;
    private String name;
    private String placeId;
    private String reference;
    private String rating;
    private int distance;

    public Place(double lat, double lng, String id, String name, String placeId, String reference, String rating, int distance) {
        this.lat = lat;
        this.lng = lng;
        this.id = id;
        this.name = name;
        this.placeId = placeId;
        this.reference = reference;
        this.rating = rating;
        this.distance = distance;
    }

    public Place(Parcel in) {
        name = in.readString();
        id = in.readString();
        placeId = in.readString();
        reference = in.readString();
        rating = in.readString();
        id = in.readString();
        lat = in.readDouble();
        lng =in.readDouble();
        distance = in.readInt();
    }


    public double getLat() {
        return lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLng() {
        return lng;
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
        placeLocation.setLatitude(lat);
        placeLocation.setLongitude(lng);
        return placeLocation;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return name + ": " + name;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(id);
        out.writeString(placeId);
        out.writeString(reference);
        out.writeString(rating);
        out.writeString(rating);
        out.writeDouble(lat);
        out.writeDouble(lng);
        out.writeInt(distance);
    }

    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
