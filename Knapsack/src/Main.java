import java.io.*;
import java.util.*;

class Main {
    static class Item{
        public Item(int id, int value, int weight, float density){
            this.id = id;
            this.value = value;
            this.weight = weight;
            this.density = density;
        }
        int id;
        int value;
        int weight;
        float density;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String text = reader.readLine();
        String[] parts = text.split(" ");
        int n = Integer.parseInt(parts[0]);
        int W = Integer.parseInt(parts[1]);
        ArrayList<Item> items = new ArrayList<Item>();
        for (int i = 0; i < n; i++){
            text = reader.readLine();
            parts = text.split(" ");
            int value = Integer.parseInt(parts[0]);
            int weight = Integer.parseInt(parts[1]);
            float density = (float)value / weight;
            Item item = new Item(i, value, weight, density);
            items.add(item);
        }
        reader.close();
        Comparator<Item> densityComparator = new Comparator<Item>(){
            public int compare(Item item1, Item item2){
                int result = Float.compare(item2.density, item1.density);
                if (result != 0){
                    return result;
                }
                return Integer.compare(item2.value, item1.value);
            }
        };
        Collections.sort(items, densityComparator);
        /*
        for (int i = 0; i < n; i++){
            System.out.println(items.get(i).id + " " + items.get(i).value + " " + items.get(i).weight + " " + items.get(i).density);
        }
         */
        int firstWeight = 0;
        int firstValue = 0;
        int index = 0;
        ArrayList<Integer> pickedFirst = new ArrayList<Integer>(Collections.nCopies(n, 0));
        while (index < n){
            Item current = items.get(index);
            int id = current.id;
            if (firstWeight + current.weight <= W) {
                //System.out.println("id = " + id);
                pickedFirst.set(id, 1);
                firstWeight += current.weight;
                firstValue += current.value;
                //System.out.println("firstValue = " + firstValue);
            }
            else{
                if (current.value > firstValue){
                    firstValue = current.value;
                    firstWeight = current.weight;
                    pickedFirst = new ArrayList<Integer>(Collections.nCopies(n, 0));
                    pickedFirst.set(id, 1);
                }
            }
            index++;
        }
        Comparator<Item> valueComparator = new Comparator<Item>(){
            public int compare(Item item1, Item item2){
                int result = Integer.compare(item2.value, item1.value);
                if (result != 0){
                    return result;
                }
                return Integer.compare(item1.weight, item2.weight);
            }
        };
        /*
        Collections.sort(items, valueComparator);
        for (int i = 0; i < n; i++){
            System.out.println(items.get(i).id + " " + items.get(i).value + " " + items.get(i).weight + " " + items.get(i).density);
        }
        */
        int secondWeight = 0;
        int secondValue = 0;
        index = 0;
        ArrayList<Integer> pickedSecond = new ArrayList<Integer>(Collections.nCopies(n, 0));
        while (index < n){
            Item current = items.get(index);
            int id = current.id;
            if (secondWeight + current.weight <= W) {
                //System.out.println("id = " + id);
                pickedSecond.set(id, 1);
                secondWeight += current.weight;
                secondValue += current.value;
                //System.out.println("secondValue = " + secondValue);
            }
            else{
                if (current.value > secondValue){
                    secondValue = current.value;
                    secondWeight = current.weight;
                    pickedSecond = new ArrayList<Integer>(Collections.nCopies(n, 0));
                    pickedSecond.set(id, 1);
                }
            }
            index++;
        }
        if (firstValue >= secondValue){
            System.out.println(firstValue);
            for (int i = 0; i < n; i++){
                System.out.println(pickedFirst.get(i));
            }
        }
        else{
            System.out.println(secondValue);
            for (int i = 0; i < n; i++){
                System.out.println(pickedSecond.get(i));
            }
        }
    }
}
