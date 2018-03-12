package execution;

import cities.City;
import javafx.scene.control.Alert;
import parcel.*;
import transport.Transport;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


public class Shipping {
    Data data;
    Parcel parcel;
    private int codeOfDpt, codeOfDst;

    public Shipping(String city1, String city2, String type, Data data) throws IOException, MyException{
        this.data = data;
        this.codeOfDpt = defineCity(city1).getCode();
        this.codeOfDst = defineCity(city2).getCode();

            parcel = new Envelope(codeOfDpt, codeOfDst, type, data.createWay(codeOfDpt, codeOfDst, type), createTrackingNumber());
            data.addParcel(parcel);

    }

    public Shipping(String city1, String city2, String type, int weight, int length, int width, int height, Data data) throws IOException, MyException{
        this.data = data;
        this.codeOfDpt = defineCity(city1).getCode();
        this.codeOfDst = defineCity(city2).getCode();


            if (weight <= data.getMaxWeightOfWrapper() && length <= data.getMaxSizeOfWrapper() && width <= data.getMaxSizeOfWrapper() && height <= data.getMaxSizeOfWrapper()) {
                parcel = new Wrapper(weight, length, width, height, codeOfDpt, codeOfDst, type, data.createWay(codeOfDpt, codeOfDst, type), createTrackingNumber());
            } else if (weight <= data.getMaxWeightOfSmallBox() && length <= data.getMaxSizeOfSmallBox() && width <= data.getMaxSizeOfSmallBox() && height <= data.getMaxSizeOfSmallBox()) {
                parcel = new SmallBox(weight, length, width, height, codeOfDpt, codeOfDst, type, data.createWay(codeOfDpt, codeOfDst, type), createTrackingNumber());
            } else if (weight <= data.getMaxWeightOfMediumBox() && length <= data.getMaxSizeOfMediumBox() && width <= data.getMaxSizeOfMediumBox() && height <= data.getMaxSizeOfMediumBox()) {
                parcel = new MediumBox(weight, length, width, height, codeOfDpt, codeOfDst, type, data.createWay(codeOfDpt, codeOfDst, type), createTrackingNumber());
            } else if (weight <= data.getMaxWeightOfBigBox() && length <= data.getMaxSizeOfBigBox() && width <= data.getMaxSizeOfBigBox() && height <= data.getMaxSizeOfBigBox()) {
                parcel = new BigBox(weight, length, width, height, codeOfDpt, codeOfDst, type, data.createWay(codeOfDpt, codeOfDst, type), createTrackingNumber());
            } else
                parcel = new Special(weight, length, width, height, codeOfDpt, codeOfDst, type, data.createWay(codeOfDpt, codeOfDst, type), createTrackingNumber());

            data.addParcel(parcel);

    }

    private City defineCity(String city) {
        City current;
        Iterator<City> iterator = data.getCities().iterator();
        while (iterator.hasNext()) {
            current = iterator.next();
            if (current.getCity().getName().equals(city)) return current;
        }
        return null;
    }

    private int createTrackingNumber() {
        int currentNumber;
        int countOfParcels = data.getAllParcels().size();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR) % 100;
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int lastNumber;
        int today = 10000 * year + 100 * month + day;
        currentNumber = today * 1000;

        if (countOfParcels == 0) {}
        else {
            Iterator<Parcel> iterator = data.getAllParcels().iterator();
            Parcel  lastParcel = null;
            for (int i = 0; i < countOfParcels; i++) {
                lastParcel = iterator.next();
            }
            lastNumber = lastParcel.getTrackingNumber();
            if (lastNumber / 1000 == today) {
                int rest = lastNumber % 1000;
                currentNumber += rest + 1;
            }
        }
        return currentNumber;
    }
}
