package execution;

import cities.City;
import transport.Transport;
import transport.Truck;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by IVAN-PC on 08.05.2017.
 */
public class TransportTableStuff {
    private String typeOfTransport;
    private String workingArea;
    private String location;
    private double workload;

    public TransportTableStuff(Transport transport, ArrayList<City> cities) {
        this.typeOfTransport = (transport instanceof Truck) ? "Truck" : "Plane";
        this.workingArea = transport.getName();
        this.location = (transport.isOnWay()) ? ("On way from " + cities.get(transport.getLocation() - 1).getCity().getName() + " to " + cities.get(transport.getDestination() - 1).getCity().getName()) : ("In " + cities.get(transport.getLocation() - 1).getCity().getName());
        this.workload = Math.rint(100.0 * (double)transport.getCurrentWeight() / transport.getAvailableWeight()) / 100;
    }

    public String getTypeOfTransport() {return typeOfTransport;}
    public String getWorkingArea() {return workingArea;}
    public String getLocation() {return location;}
    public double getWorkload() {return workload;}
}
