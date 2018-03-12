package execution;

//import com.sun.xml.internal.ws.api.ha.StickyFeature;

/**
 * Created by IVAN-PC on 01.05.2017.
 */
public class ParcelTableStuff {
    int trackingNumber;
    String typeOfParcel;
    String typeOfDelivery;
    String dpt, dst;
    String statusOfParcel;
    int weight;
    double price;

    public ParcelTableStuff(int trackingNumber, String typeOfParcel, String typeOfDelivery, String dpt, String dst, String statusOfParcel, int weight, double price) {
        this.trackingNumber = trackingNumber;
        this.typeOfParcel = typeOfParcel;
        this.typeOfDelivery = typeOfDelivery;
        this.dpt = dpt;
        this.dst = dst;
        this.statusOfParcel = statusOfParcel;
        this.weight = weight;
        this.price = price;
    }

    public int getTrackingNumber() {return trackingNumber;}
    public String getTypeOfParcel() {return typeOfParcel;}
    public String getTypeOfDelivery() {return typeOfDelivery;}
    public String getDpt() {return dpt;}
    public String getDst() {return dst;}
    public String getStatusOfParcel() {return statusOfParcel;}
    public int getWeight() {return weight;}
    public double getPrice() {return price;}

}
