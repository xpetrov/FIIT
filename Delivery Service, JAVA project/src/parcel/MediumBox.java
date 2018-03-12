package parcel;

import execution.MyException;

import java.io.Serializable;
import java.util.ArrayList;

public class MediumBox extends Box implements Serializable, Price{

    // values in centimeters
    private int maxWeight = 20;
    private int maxLength = 70, maxWidth = 70, maxHeight = 70;

    public MediumBox(int weight, int length, int width, int height, int codeOfDpt, int codeOfDst, String type, ArrayList<Integer> way, int trackingNumber) throws MyException {
        super(TypeOfParcel.MEDIUM_BOX, weight, length, width, height, codeOfDpt, codeOfDst, type, way, trackingNumber);
        setPrice(calculatePrice());
    }

    @Override
    public double calculatePrice() {
        double price = 2 * getWeight();
        price *= 1 + (getWay().size() - 1) * 0.15;
        if (getTypeOfDelivery() == "by Plane") price *= 2;
        return Math.rint(100.0 * price) / 100;
    }
}
