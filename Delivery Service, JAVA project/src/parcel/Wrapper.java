/**
 * Wrapper class
 */
package parcel;

import execution.MyException;

import java.io.Serializable;
import java.util.ArrayList;

public class Wrapper extends Parcel implements Serializable, Price{

    /**
     *
     * @param weight specifying weight of created parcel
     * @param length specifying length of created parcel
     * @param width specifying width of created parcel
     * @param height specifying height of created parcel
     * @param codeOfDpt specifying code of departure city of created parcel
     * @param codeOfDst specifying code of destination city of created parcel
     * @param type specifying delivery type of created parcel
     * @param way specifying way of created parcel consisting from codes of visited cities
     * @param trackingNumber specifying tracking number of created parcel
     * @see execution.MyException
     */
    public Wrapper(int weight, int length, int width, int height, int codeOfDpt, int codeOfDst, String type, ArrayList<Integer> way, int trackingNumber) throws MyException {
        super(TypeOfParcel.WRAPPER, weight, length, width, height, codeOfDpt, codeOfDst, type, way, trackingNumber);
        setPrice(calculatePrice());
    }

    /**
     * This method calculate price for delivery
     * @return value of price in Double
     */
    @Override
    public double calculatePrice() {
        double price = 1 * getWeight();
        if (getTypeOfDelivery() == "by Plane") price *= 2;
        return Math.rint(100.0 * price) / 100;
    }
}
