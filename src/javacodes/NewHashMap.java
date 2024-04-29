/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes;

/**
 *
 * @author malin
 */
public class NewHashMap {

    int capacity = 5;
    public Object[] table;
    public int size;

    public NewHashMap() {
        table = new Object[capacity];
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int hash(Object obj) {
        int index = Math.abs(obj.hashCode() % capacity);
        return index;
    }

    public static class KeyValue {

        Object key;
        Object value;
        KeyValue next;

        public KeyValue(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    public void put(Object key, Object value) {
        int index = hash(key);
        if (table[index] == null) {
            table[index] = new KeyValue(key, value);
            size++;
        } else {
            KeyValue current = (KeyValue) table[index];
            while (current != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            current.next = new KeyValue(key, value);
            size++;
        }
    }
      public  Object get(Object key) {
        int index = hash(key);
        KeyValue current = (KeyValue) table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null; 
    }

//get, actual value jaba samma vatdaina tyo bela samma get inside get 
    public void printAllElements() {
        for (Object entry : table) {
            if (entry != null) {
                if (entry instanceof KeyValue) {
                    KeyValue current = (KeyValue) entry;
                    while (current != null) {
                        System.out.println("Key: " + current.key + ", Value: " + current.value);
                        current = current.next;
                    }
                } else {
                    NewHashMap set = (NewHashMap) entry;
                    set.printAllElements();
                }
            }
        }
    }

    public static void main(String[] args) {
        NewHashMap map = new NewHashMap();
        System.out.println("Is Empty? " + map.isEmpty());
        map.put("Hello", 1);
        map.put("World", 2);
        map.put(456, "Hi");
        map.put(123, "Namaste");
        map.put(456, "Hiii");
        map.put(56, "iii");
        System.out.println(map.size());
        map.put(46, "Heyyyi");
        map.put(146, "Heyyyiss");

        System.out.println("Size of the map: " + map.size());
        System.out.println("Check purpose");
        map.printAllElements();
        System.out.println("Is Empty? " + map.isEmpty());
         System.out.println("Value for key 'Hello': " + map.get("Hello"));
        System.out.println("Value for key '123': " + map.get(123));
        System.out.println("Value for non existing key: " + map.get("No Value"));
    }
}
