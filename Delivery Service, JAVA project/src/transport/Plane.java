/**
 * Plane class
 */
package transport;

import java.io.Serializable;

public class Plane extends Transport implements Serializable{
    /**
     * Class constructor
     * @param codeOfHometown specifying the code of Hometown
     * @param codeOfDestinationTown specifying code of DestinationTown
     * @param name specifying name of created transport. Includes names of working area cities, e.g. "Athens/Istanbul"
     */
    public Plane(int codeOfHometown, int codeOfDestinationTown, String name) {
        super(TypeOfTransport.PLANE, 40000, 10000, codeOfHometown, codeOfDestinationTown, name);
    }
}
