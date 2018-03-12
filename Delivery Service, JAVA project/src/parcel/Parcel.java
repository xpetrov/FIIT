/**
 * Abstract class Parcel
 */

package parcel;

import execution.Data;
import execution.MyException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Parcel implements Serializable{

    private TypeOfParcel typeOfParcel;
    private int weight;
    private int length, width, height;
    private int codeOfDpt, codeOfDst, location;
    private int trackingNumber;
    private ArrayList<Integer> way;
    private Queue<Integer> residualWay;
    private String typeOfDelivery;
    private double price;
    private boolean isOnWay;
    private boolean isDelivered;
    private LinkedList<String> status;

    /**
     * Class constructor specifying all the fields:
     * @param typeOfParcel specifying type of created parcel
     *                     @see TypeOfParcel
     * @param weight specifying weight of created parcel
     * @param length specifying length of created parcel
     * @param width specifying width of created parcel
     * @param height specifying height of created parcel
     * @param dpt specifying code of departure city of created parcel
     * @param dst specifying code of destination city of created parcel
     * @param typeOfDelivery specifying delivery type of created parcel
     * @param way specifying way of created parcel consisting from codes of visited cities
     * @param trackingNumber specifying tracking number of created parcel
     * @see execution.MyException
     */
    public Parcel(TypeOfParcel typeOfParcel, int weight, int length, int width, int height, int dpt, int dst, String typeOfDelivery, ArrayList<Integer> way, int trackingNumber) throws MyException{

        if (weight > 150 || length > 300 || width > 300 || height > 300) throw new MyException("Inaccessible parameters of a parcel");
        else {
            this.typeOfParcel = typeOfParcel;
            this.weight = weight;
            this.length = length;
            this.width = width;
            this.height = height;
            this.codeOfDpt = dpt;
            this.codeOfDst = dst;
            this.location = dpt;
            this.typeOfDelivery = typeOfDelivery;
            this.way = way;
            residualWay = new LinkedList<>();
            for (Integer o : way) residualWay.add(o);
            this.trackingNumber = trackingNumber;
            this.location = dpt;
            this.isOnWay = false;
            this.isDelivered = false;
            status = new LinkedList<>();
            this.status.add(Data.getTime() + " Ready to be shipped");
        }
    }

    public TypeOfParcel getTypeOfParcel() {
        return this.typeOfParcel;
    }
    public int getWeight() {
        return weight;
    }
    public int getTrackingNumber() {
        return trackingNumber;
    }
    public ArrayList<Integer> getWay() {
        return way;
    }
    public int getCodeOfDpt() {
        return codeOfDpt;
    }
    public int getCodeOfDst() {
        return codeOfDst;
    }
    public String getTypeOfDelivery() {return typeOfDelivery;}
    protected void setPrice(double price) {this.price = price;}
    public double getPrice() {return price;}
    public int getLocation() {return location;}
    public void setLocation(int location) {this.location = location;}
    public boolean isOnWay() {return isOnWay;}
    public void setOnWay(boolean value) {this.isOnWay = value;}
    public void removeFirstElementOfResidualWay() {residualWay.remove();}
    public int getFirstElementOfResidualWay() {return residualWay.element();}
    public boolean isDelivered() {return isDelivered;}
    public void setDelivered(boolean value) { this.isDelivered = value;}
    public Queue<Integer> getResidualWay() {return residualWay;}
    public String getStatus() {return status.getLast();}
    public void setStatus(String status) {this.status.add(status);}
    public int getLength() {return length;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public LinkedList<String> getStatusList() {return status;}
}
