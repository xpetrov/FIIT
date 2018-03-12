package parcel;

import execution.MyException;

import java.io.Serializable;
import java.util.ArrayList;

public class Box extends Parcel implements Serializable{

    protected Box(TypeOfParcel value, int weight, int length, int width, int height, int codeOfDpt, int codeOfDst, String type, ArrayList<Integer> way, int trackingNumber) throws MyException {
        super(value, weight, length, width, height, codeOfDpt, codeOfDst, type, way, trackingNumber);
    }

}
