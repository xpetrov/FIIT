/**
 * Abstract class Transport
 */

package transport;

import cities.City;
import execution.Data;
import parcel.*;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

public abstract class Transport implements Serializable{

    private String name;
    private TypeOfTransport typeOfTransport;
    private int availableWeight; // [m^3]
    private int currentWeight;
    private int speed;// [km/h]
    protected boolean isFull, isOnWay;
    protected LinkedList<Parcel> contents;
    private int codeOfHometown, codeOfDsttown, location;
    private int departureTime;
    private int arrivalTime;
    private int arrivalYear;
    private String arrivalDepartureTime;

    /**
     * Class constructor specifying all the fields:
     * @param typeOfTransport specifying type of created transport
     *                        @see TypeOfTransport
     * @param availableWeight specifying maximal value of load-carrying ability. Measured in kgs
     * @param speed specifying speed of created transport. Measured in km/h
     * @param codeOfHometown specifying the code of Hometown
     * @param codeOfDsttown specifying code of DestinationTown
     * @param name specifying name of created transport. Includes names of working area cities, e.g. "Athens/Istanbul"
     */
    public Transport(TypeOfTransport typeOfTransport, int availableWeight, int speed, int codeOfHometown, int codeOfDsttown, String name) {
        this.name = name;
        this.typeOfTransport = typeOfTransport;
        this.availableWeight = availableWeight;
        this.speed = speed;
        this.codeOfHometown = codeOfHometown;
        this.codeOfDsttown = codeOfDsttown;
        this.currentWeight = 0;
        this.isFull = false;
        this.isOnWay = false;
        this.contents = new LinkedList<>();
        this.location = codeOfHometown;
    }

    public TypeOfTransport getTypeOfTransport() {
        return this.typeOfTransport;
    }
    public int getAvailableWeight() {
        return availableWeight;
    }
    public int getCurrentWeight() {return currentWeight;}
    public int getSpeed() {
        return speed;
    }
    public int getCodeOfHometown() {
        return codeOfHometown;
    }
    public int getCodeOfDsttown() {
        return codeOfDsttown;
    }
    public int getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(int value) {this.departureTime = value;}
    public int getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(int value) {this.arrivalTime = value;}
    public boolean isOnWay() {return isOnWay;}
    public void setOnWay(boolean value) {this.isOnWay = value;}
    public int getLocation() {return location;}
    public void setLocation(int codeOfCity) {this.location = codeOfCity;}
    public String getName() {return name;}
    public LinkedList<Parcel> getContents() {return contents;}
    public void setArrivalYear(int arrivalYear) {this.arrivalYear = arrivalYear;}
    public int getArrivalYear() {return arrivalYear;}
    public void setArrivalDepartureTime(String arrivalDepartureTime) {this.arrivalDepartureTime = arrivalDepartureTime;}
    public String getArrivalDepartureTime() {return arrivalDepartureTime;}

    /**
     * This method filing this transport by parcels being in the same city in
     * accordance with type of delivery of parcel and type of this transport
     *
     * @param allParcels list of all created parcels
     *                   @see parcel
     * @param cities list of all available cities
     *               @see cities.City
     */
    public void fill(List<Parcel> allParcels, ArrayList<City> cities) {
        Iterator<Parcel> iterator = allParcels.iterator();
        Parcel tempParcel;
        while (iterator.hasNext()) {
            tempParcel = iterator.next();
            int parcelsNextPoint;
            if (tempParcel.getLocation() == this.location && sameType(tempParcel) && tempParcel.isOnWay() == false && this.isOnWay() == false && tempParcel.isDelivered() == false) {

                Iterator<Integer> iterator1 = tempParcel.getResidualWay().iterator();
                iterator1.next();
                parcelsNextPoint = iterator1.next() + 1;

                int dst = getDestination();
                if (freeSpace(tempParcel) && (parcelsNextPoint == dst))
                {
                    contents.add(tempParcel);

                    tempParcel.setStatus(Data.getTime() + " On way from " + cities.get(this.location - 1).getCity().getName() + " to " + cities.get(dst - 1).getCity().getName());
                    tempParcel.setOnWay(true);
                    currentWeight += tempParcel.getWeight();
                }
            }

        }
        isFull = true;
    }

    /**
     * This method define whether delivery type of parcel corresponds to type of current transport
     *
     * @param parcel current parcel
     * @return true if delivery type of parcel corresponds to type of transport
     */
    public boolean sameType(Parcel parcel) {
        if (parcel.getTypeOfDelivery().equals("by Truck") && this instanceof Truck) return true;
        if (parcel.getTypeOfDelivery().equals("by Plane") && this instanceof Plane) return true;
        return false;
    }

    /**
     * This method unloading current transport
     * @param cities list of all available cities
     */
    public void unload(ArrayList<City> cities) throws ParseException{
        Iterator<Parcel> iterator = this.contents.iterator();

        while (iterator.hasNext()) {
            Parcel tempParcel;
            tempParcel = iterator.next();
            tempParcel.setOnWay(false);
            tempParcel.removeFirstElementOfResidualWay();
            tempParcel.setLocation(tempParcel.getFirstElementOfResidualWay() + 1);
            if (tempParcel.getLocation() == tempParcel.getCodeOfDst()) {
                tempParcel.setDelivered(true);
                tempParcel.setStatus(Data.parseIntToTime(arrivalTime, arrivalYear) + " Is delivered");
            } else
                tempParcel.setStatus(Data.parseIntToTime(arrivalTime, arrivalYear) + " Transhipment in " + cities.get(this.location - 1).getCity().getName());
        }
        this.contents.clear();
        currentWeight = 0;
    }

    /**
     * This method define whether this transport have enough free place to load current parcel
     * @param parcel current parcel
     * @return true if current parcel can be loaded to this transport
     */
    private boolean freeSpace(Parcel parcel) {return (currentWeight + parcel.getWeight() > availableWeight) ? false : true;}

    /**
     *
     * @return code of city in which this transport are
     */
    public int getDestination() {return (this.location == codeOfHometown) ? codeOfDsttown : codeOfHometown;}

}
