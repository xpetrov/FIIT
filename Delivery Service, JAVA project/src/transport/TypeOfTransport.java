/**
 * Final class defining types of transport
 */
package transport;

import java.io.Serializable;

final class TypeOfTransport implements Serializable{
    public static final TypeOfTransport TRUCK = new TypeOfTransport("Truck");
    public static final TypeOfTransport PLANE = new TypeOfTransport("Plane");

    public static TypeOfTransport[] value = {TRUCK, PLANE};
    private String name;

    /**
     * Class constructor specifying name of created city
     * @param name - type of transport in String
     */
    TypeOfTransport(String name) {
        this.name = name;
    }

    /**
     *
     * @return type of transport in String
     */
    public String getName() {
        return this.name;
    }
}
