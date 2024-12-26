public class Parcel {
    private String id;
    private double length;
    private double width;
    private double height;
    private double weight;
    private int daysInDepot;
    private String status; // New field for parcel status

    public Parcel(String id, double length, double width, double height, double weight, int daysInDepot) {
        this.id = id;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.daysInDepot = daysInDepot;
        this.status = "Waiting"; // Default status is "Waiting"
    }

    public String getId() {
        return id;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public int getDaysInDepot() {
        return daysInDepot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double calculateFee() {
        double volume = length * width * height;
        double fee = volume * 0.5 + weight * 0.2; // Base fee
        if (daysInDepot > 5) {
            fee += 10; // Additional fee for parcels stored for more than 5 days
        }
        return fee;
    }

    @Override
    public String toString() {
        return "Parcel [ID=" + id + ", Dimensions=" + length + "x" + width + "x" + height +
                ", Weight=" + weight + "kg, DaysInDepot=" + daysInDepot + "]";
    }
}
