/**
 * Envelope class
 */
package parcel;

import execution.MyException;

import java.io.Serializable;
import java.util.ArrayList;

public class Envelope extends Parcel implements Serializable, Price {

    /**
     * Class constructor
     * @param codeOfDpt specifying code of departure city of created parcel
     * @param codeOfDst specifying code of destination city of created parcel
     * @param type specifying delivery type of created parcel
     * @param way specifying way of created parcel consisting from codes of visited cities
     * @param trackingNumber specifying tracking number of created parcel
     * @see execution.MyException
     */
    public Envelope(int codeOfDpt, int codeOfDst, String type, ArrayList<Integer> way, int trackingNumber) throws MyException {
        super(TypeOfParcel.ENVELOPE, 0, 0, 0, 0, codeOfDpt, codeOfDst, type, way, trackingNumber);
        setPrice(calculatePrice());
    }

    /**
     * This method calculate price for delivery
     * @return value of price in Double
     */
    @Override
    public double calculatePrice() {
        double price = 1;
        if (getTypeOfDelivery() == "by Plane") price = 5;
        return Math.rint(100.0 * price) / 100;
    }
}
