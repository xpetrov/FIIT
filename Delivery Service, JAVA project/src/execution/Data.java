package execution;

import cities.*;
import parcel.Parcel;
import transport.Plane;
import transport.Transport;
import transport.Truck;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class Data implements Serializable {
    private LinkedList<Parcel> allParcels;
    private LinkedList<Transport> transports;
    transient private ArrayList<City> cities = new ArrayList<>();
    transient ArrayList<Integer>[] trucksAdjacency = new ArrayList[17];
    transient ArrayList<Integer>[] trucksDistance = new ArrayList[17];
    transient ArrayList<Integer>[] planesAdjacency = new ArrayList[17];
    transient ArrayList<Integer>[] planesDistance = new ArrayList[17];

    transient private int maxWeightOfWrapper = 5, maxSizeOfWrapper = 15;
    transient private int maxWeightOfSmallBox = 5, maxSizeOfSmallBox = 40;
    transient private int maxWeightOfMediumBox = 25, maxSizeOfMediumBox = 70;
    transient private int maxWeightOfBigBox = 50, maxSizeOfBigBox = 100;

    public int getMaxWeightOfWrapper() {return maxWeightOfWrapper;}
    public int getMaxSizeOfWrapper() {return maxSizeOfWrapper;}
    public int getMaxWeightOfSmallBox() {return maxWeightOfSmallBox;}
    public int getMaxSizeOfSmallBox() {return maxSizeOfSmallBox;}
    public int getMaxWeightOfMediumBox() {return maxWeightOfMediumBox;}
    public int getMaxSizeOfMediumBox() {return maxSizeOfMediumBox;}
    public int getMaxWeightOfBigBox() {return maxWeightOfBigBox;}
    public int getMaxSizeOfBigBox() {return maxSizeOfBigBox;}
    public ArrayList<City> getCities() {return cities;}
    public List<Parcel> getAllParcels() {return allParcels;}
    public LinkedList<Transport> getTransports() {return transports;}

    public Data() throws FileNotFoundException, IOException, ClassNotFoundException, ParseException {
        City.createCities(cities);
        readData("TruckDistance.txt", trucksAdjacency, trucksDistance, 44);
        readData("PlaneDistance.txt", planesAdjacency, planesDistance, 32);

        try {
            FileInputStream parcelsInput = new FileInputStream("Parcels");
            ObjectInputStream objectInputStream = new ObjectInputStream(parcelsInput);
            allParcels = (LinkedList<Parcel>) objectInputStream.readObject();

            FileInputStream transportInput = new FileInputStream("Transport");
            ObjectInputStream objectInputStream1 = new ObjectInputStream(transportInput);
            transports = (LinkedList<Transport>) objectInputStream1.readObject();

            for (Transport transport : transports) {
                Iterator<Parcel> contentIterator = transport.getContents().iterator();
                while (contentIterator.hasNext()) {
                    Parcel parcel = contentIterator.next();
                    for (Parcel current : allParcels) {
                        if (current.getTrackingNumber() == parcel.getTrackingNumber()) {
                            int index = allParcels.indexOf(current);
                            allParcels.set(index, parcel);
                            break;
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            allParcels = new LinkedList<>();
            createTransport();
        }

        arrival();
    }

    private void createTransport() {
        transports = new LinkedList<>();
        for (int i = 0; i < trucksAdjacency.length; i++) {
            Iterator<Integer> iterator = trucksAdjacency[i].iterator();
            int y;
            while (iterator.hasNext()) {
                y = iterator.next();
                transports.add(new Truck(i + 1, y + 1, cities.get(i).getCity().getName() + "/" + cities.get(y).getCity().getName()));
            }

            /*
            for (int y = 0; y < trucksAdjacency[i].size(); y++)
                transports.add(new Truck(i + 1, trucksAdjacency[i].get(y), cities.get(i).getCity().getName() + "/" + cities.get(trucksAdjacency[i].get(y)).getCity().getName()));
            */
        }
        for (int i = 0; i < planesAdjacency.length; i++) {
            if (i != 15) {
                for (int y = 0; y < planesAdjacency[i].size(); y++)
                    transports.add(new Plane(i + 1, planesAdjacency[i].get(y) + 1, cities.get(i).getCity().getName() + "/" + cities.get(planesAdjacency[i].get(y)).getCity().getName()));
            }
        }

    }

    public void departure(LinkedList<Transport> transports) throws IOException, ParseException{

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int departureYear = calendar.get(Calendar.YEAR);

        int departureTime = month * 1000000 + day * 10000 + hour * 100 + minute;

        Iterator<Transport> iterator = transports.iterator();
        Transport tempTransport;
        while (iterator.hasNext()) {
            calendar.setTime(date);
            tempTransport = iterator.next();

            if (tempTransport.isOnWay() == false) {
                tempTransport.fill(allParcels, cities);

                int distance;
                if (tempTransport instanceof Truck) {
                    int index = 0;
                    Iterator<Integer> adjacencyIterator = trucksAdjacency[tempTransport.getCodeOfHometown() - 1].iterator();
                    while (adjacencyIterator.hasNext()) {
                        if ((tempTransport.getCodeOfDsttown() - 1) == adjacencyIterator.next()) break;
                        index++;
                    }
                    distance = trucksDistance[tempTransport.getCodeOfHometown() - 1].get(index);
                } else {
                    int index = 0;
                    Iterator<Integer> adjacencyIterator = planesAdjacency[tempTransport.getCodeOfHometown() - 1].iterator();
                    while (adjacencyIterator.hasNext()) {
                        if ((tempTransport.getCodeOfDsttown() - 1) == adjacencyIterator.next()) break;
                        index++;
                    }
                    distance = planesDistance[tempTransport.getCodeOfHometown() - 1].get(index);
                }

                int transitHourTime = distance / tempTransport.getSpeed();
                int transitMinuteTime = (int) (((double) (distance % tempTransport.getSpeed()) / tempTransport.getSpeed()) * 60);

                calendar.add(Calendar.HOUR_OF_DAY, transitHourTime);
                calendar.add(Calendar.MINUTE, transitMinuteTime);

                int arrivalMonth = calendar.get(Calendar.MONTH) + 1;
                int arrivalDay = calendar.get(Calendar.DAY_OF_MONTH);
                int arrivalHour = calendar.get(Calendar.HOUR_OF_DAY);
                int arrivalMinute = calendar.get(Calendar.MINUTE);

                int arrivalYear = calendar.get(Calendar.YEAR);

                int arrivalTime = arrivalMonth * 1000000 + arrivalDay * 10000 + arrivalHour * 100 + arrivalMinute;
                tempTransport.setDepartureTime(departureTime);
                tempTransport.setArrivalTime(arrivalTime);
                tempTransport.setArrivalYear(arrivalYear);
                tempTransport.setArrivalDepartureTime(parseIntToTime(departureTime, departureYear) + " - " + parseIntToTime(arrivalTime, arrivalYear));

                tempTransport.setOnWay(true);

                //System.out.print(tempTransport.getName() + " " + tempTransport.getDepartureTime() + " " + tempTransport.getArrivalTime());
                //System.out.println();
            }
        }

        saveData();
    }

    public boolean isArrived(Transport transport) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        int actualTime = month * 1000000 + day * 10000 + hour * 100 + minute;

        //System.out.print(actualTime + " " + transport.getDepartureTime() + " " + transport.getArrivalTime() + " " + transport.getName());
        //System.out.println();

        return (actualTime >= transport.getArrivalTime()) ? true : false;
    }

    public void arrival() throws IOException, ParseException{
        Iterator<Transport> iterator = transports.iterator();
        Transport tempTransport;
        while (iterator.hasNext()) {
            tempTransport = iterator.next();
            if (tempTransport.isOnWay() && isArrived(tempTransport)) {
                tempTransport.setOnWay(false);
                if (tempTransport.getLocation() == tempTransport.getCodeOfHometown()) tempTransport.setLocation(tempTransport.getCodeOfDsttown());
                else tempTransport.setLocation(tempTransport.getCodeOfHometown());
                tempTransport.unload(cities);
                //System.out.println(tempTransport.getName() + " is unloaded");
            }
        }

        saveData();
    }

    private void readData(String pathname, ArrayList<Integer>[] adjacency, ArrayList<Integer>[] distance, int countOfDirections) throws FileNotFoundException {
        // n - количество вершин графа
        // m - количество рёбер графа * 2
        Scanner file;
        file = new Scanner(new File(pathname));

        int n = cities.size();
        int m = countOfDirections;
        //инициализируем списка смежности графа размерности n
        for (int i = 0; i < n; ++i) {
            adjacency[i] = new ArrayList<>();
        }

        //инициализация списка, в котором хранятся веса ребер
        for (int i = 0; i < n; ++i) {
            distance[i] = new ArrayList<>();
        }

        //считываем граф, заданный списком ребер
        for (int i = 0; i < m; ++i) {
            int u = file.nextInt() - 1;
            int v = file.nextInt() - 1;
            int w = file.nextInt();
            adjacency[u].add(v);
            distance[u].add(w);
        }
        file.close();
    }


    public void addParcel(Parcel parcel) throws IOException {
        allParcels.add(parcel);
        saveData();
    }

    public ArrayList<Integer> createWay(int start, int finish, String typeOfDelivery) {
        start--;
        finish--;

        ArrayList<Integer> adj[] = new ArrayList[cities.size()];
        ArrayList<Integer> weight[] = new ArrayList[cities.size()];
        if (typeOfDelivery.equals("by Truck")) {
            adj = trucksAdjacency;
            weight = trucksDistance;
        } else if (typeOfDelivery.equals("by Plane")) {
            adj = planesAdjacency;
            weight = planesDistance;
        }

        int INF = Integer.MAX_VALUE / 2;
        int n = cities.size();
        boolean used[] = new boolean[n];
        Arrays.fill(used, false);
        int prev[] = new int[n];
        Arrays.fill(prev, -1);
        int dist[] = new int[n];
        Arrays.fill(dist, INF);

        dist[start] = 0; //кратчайшее расстояние до стартовой вершины равно 0
        for (int iter = 0; iter < n; ++iter) {
            int v = -1;
            int distV = INF;
            //выбираем вершину, кратчайшее расстояние до которого еще не найдено
            for (int i = 0; i < n; ++i) {
                if (used[i]) {
                    continue;
                }
                if (distV < dist[i]) {
                    continue;
                }
                v = i;
                distV = dist[i];
            }
            //рассматриваем все дуги, исходящие из найденной вершины
            for (int i = 0; i < adj[v].size(); ++i) {
                int u = adj[v].get(i);
                int weightU = weight[v].get(i);
                //релаксация вершины
                if (dist[v] + weightU < dist[u]) {
                    dist[u] = dist[v] + weightU;
                    prev[u] = v;
                }
            }
            //помечаем вершину v просмотренной, до нее найдено кратчайшее расстояние
            used[v] = true;
        }

        ArrayList<Integer> way = new ArrayList<>();
        printWay(finish, way, prev);
        return way;
    }

    private void printWay(int v, ArrayList<Integer> way, int prev[]) {
        if (v == -1) {
            return;
        }
        printWay(prev[v], way, prev);
        way.add(v);
    }

    public Parcel findParcel(int trackingNumber) {
        Iterator<Parcel> iterator = allParcels.iterator();
        Parcel current;
        while (iterator.hasNext()) {
            current = iterator.next();
            if (current.getTrackingNumber() == trackingNumber) return current;
        }
        return null;
    }

    public int getCountOfTransportOnWay() {
        int countOfTransportOnWay = 0;
        Iterator<Transport> iterator = transports.iterator();
        while (iterator.hasNext()) if (iterator.next().isOnWay()) countOfTransportOnWay++;

        return countOfTransportOnWay;
    }
    public int getCountOfTransport() {return transports.size();}
    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d.M.y H:mm");
        String string = simpleDateFormat.format(date);
        return string;
    }

    public static String parseIntToTime(int arrivalTime, int arrivalYear) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d.M.y H:mm");
        String temp = (arrivalTime / 10000) % 100 + "." + arrivalTime / 1000000 + "." + arrivalYear + " " + (arrivalTime % 10000) / 100 + ":" + arrivalTime % 100;
        Date date = simpleDateFormat.parse(temp);
        String string = simpleDateFormat.format(date);
        return string;
    }

    public void saveData() throws IOException{
        FileOutputStream transportsOutput = new FileOutputStream("Transport");
        ObjectOutputStream transportsOutputStream = new ObjectOutputStream(transportsOutput);
        transportsOutputStream.writeObject(transports);

        FileOutputStream allParcelsOutput = new FileOutputStream("Parcels");
        ObjectOutputStream allParcelsOutputStream = new ObjectOutputStream(allParcelsOutput);
        allParcelsOutputStream.writeObject(allParcels);
    }

}