package parcel;

import execution.MyException;

import java.io.Serializable;
import java.util.ArrayList;

public class SmallBox extends Box implements Serializable, Price{

    // values in centimeters
    private int maxWeight = 5;
    private int maxLength = 40, maxWidth = 40, maxHeight = 40;

    public SmallBox(int weight, int length, int width, int height, int codeOfDpt, int codeOfDst, String type, ArrayList<Integer> way, int trackingNumber) throws MyException {
        super(TypeOfParcel.SMALL_BOX, weight, length, width, height, codeOfDpt, codeOfDst, type, way, trackingNumber);
        setPrice(calculatePrice());
    }

    @Override
    public double calculatePrice() {
        double price = 1.5 * getWeight();
        price *= 1 + (getWay().size() - 1) * 0.2;
        if (getTypeOfDelivery() == "by Plane") price *= 2;
        return Math.rint(100.0 * price) / 100;
    }

}
