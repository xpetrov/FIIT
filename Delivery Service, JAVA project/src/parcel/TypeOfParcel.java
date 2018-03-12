/**
 * Final class defining types of parcels
 */
package parcel;

import java.io.Serializable;

public final class TypeOfParcel implements Serializable {
    public static final TypeOfParcel ENVELOPE = new TypeOfParcel("Envelope");
    public static final TypeOfParcel WRAPPER = new TypeOfParcel("Wrapper");
    public static final TypeOfParcel SMALL_BOX = new TypeOfParcel("Small Box");
    public static final TypeOfParcel MEDIUM_BOX = new TypeOfParcel("Medium Box");
    public static final TypeOfParcel BIG_BOX = new TypeOfParcel("Big Box");
    public static final TypeOfParcel SPECIAL = new TypeOfParcel("Special");

    public static TypeOfParcel[] value = {ENVELOPE, WRAPPER, SMALL_BOX, MEDIUM_BOX, BIG_BOX, SPECIAL};
    private String name;

    /**
     * Class constructor specifying name of created city
     * @param name - type of transport in String
     */
    TypeOfParcel(String name) {
        this.name = name;
    }

    /**
     *
     * @return type of parcel in String
     */
    public String getName() {
        return this.name;
    }
}
