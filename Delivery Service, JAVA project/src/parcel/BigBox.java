package parcel;

import execution.MyException;

import java.io.Serializable;
import java.util.ArrayList;

public class BigBox extends Box implements Serializable, Price{

    // values in centimeters
    private int maxWeight = 50;
    private int maxLength = 100, maxWidth = 100, maxHeight = 100;

    public BigBox(int weight, int length, int width, int height, int codeOfDpt, int codeOfDst, String type, ArrayList<Integer> way, int trackingNumber) throws MyException {
        super(TypeOfParcel.BIG_BOX, weight, length, width, height, codeOfDpt, codeOfDst, type, way, trackingNumber);
        setPrice(calculatePrice());
    }

    @Override
    public double calculatePrice() {
        double price = 2.5 * getWeight();
        price *= 1 + (getWay().size() - 1) * 0.1;
        if (getTypeOfDelivery() == "by Plane") price *= 2;
        return Math.rint(100.0 * price) / 100;
    }
}
