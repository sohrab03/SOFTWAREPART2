import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class ParcelMap {
    private Map<String, Parcel> parcels;

    public ParcelMap() {
        parcels = new HashMap<>();
    }

    // Adds a parcel ot the map
    public void addParcel(Parcel parcel) {
        parcels.put(parcel.getId(), parcel);
    }

    // retrieves a parcel by its iD
    public Parcel getParcel(String id) {
        return parcels.get(id);
    }

    // Removes a parcel by its iD
    public Parcel removeParcel(String id) {
        return parcels.remove(id);
    }

    // Print a list of all the parcels
    public void printParcels() {
        for (Parcel parcel : parcels.values()) {
            System.out.println(parcel);
        }
    }

    // Will get the number parcels
    public int getParcelCount() {
        return parcels.size();
    }

    // gets you all the parcels
    public Collection<Parcel> getAllParcels() {
        return parcels.values();
    }
}
