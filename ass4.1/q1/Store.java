package q1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

class Store implements Serializable {

    private static Store instance = null;
    private HashMap<String, Integer> inventory;
    private int curOrderID;
    private ArrayList<Order> orders;

    private Store() {
        inventory = new HashMap<>();
        curOrderID = 1;
        orders = new ArrayList<>();
    }

    private Store(HashMap<String, Integer> inv) {
        inventory = inv;
        curOrderID = 1;
        orders = new ArrayList<>();
    }

    static Store getInstance() {
        if (instance == null) {
            synchronized (Store.class) {
                if (instance == null) {
                    instance = new Store();
                }
            }
        }
        return instance;
    }

    static Store getInstance(HashMap<String, Integer> inv) {
        if (instance == null) {
            synchronized (Store.class) {
                if (instance == null) {
                    instance = new Store(inv);
                }
            }
        }
        return instance;
    }

    static void setInstance(Store s) {
        instance = s;
    }

    private class Order implements Serializable {
        int id;
        String user;
        String product;
        int quantity;

        Order(String user, String product, int quantity, int id) {
            this.product = product;
            this.quantity = quantity;
            this.id = id;
            this.user = user;
        }

        int getID() {
            return id;
        }

        String getUser() {
            return user;
        }

        String getProduct() {
            return product;
        }

        int getQuantity() {
            return quantity;
        }

    }

    /**
     *
     * @param user The user to search the store for
     * @param product The product the user bought
     * @param quantity The amount the user bought, if successful
     * @return 0 if quantity too low, -1 if item does not exist or order ID if
     *         purchase is successful
     */
    synchronized int purchase(String user, String product,
                                     int quantity) {
        if (!inventory.keySet().contains(product)) {
            return -1;
        } else if ((int) inventory.get(product) >= quantity) {
            orders.add(new Order(user, product, quantity, curOrderID));
            int newID = curOrderID;
            curOrderID++;
            inventory.put(product, (int) inventory.get(product) - quantity);
            return newID;
        }
        return 0;
    }

    synchronized String cancel(int orderID) {
        String retStr = orderID + " not found, no such order";
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getID() == orderID) {
                int curInv = inventory.get(orders.get(i).getProduct());
                curInv += orders.get(i).getQuantity();
                inventory.put(orders.get(i).getProduct(), curInv);
                retStr = "Order " + orderID + " is canceled";
                orders.remove(i);
            }
        }
        return retStr;
    }

    synchronized ArrayList<String> search(String user) {
        ArrayList<Order> userOrders = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getUser().equals(user)) {
                userOrders.add(orders.get(i));
            }
        }
        ArrayList<String> output = new ArrayList<>();
        if (userOrders.isEmpty()) {
            output.add("No order found for " + user);
        } else {
            Iterator<Order> i = userOrders.iterator();
            while (i.hasNext()) {
                Order next = i.next();
                output.add(next.getID() + ", " + next.getProduct() + ", "
                        + next.getQuantity());
            }
        }
        return output;
    }

    String[] list() {
        String[] data = new String[inventory.size()];
        int i = 0;
        for (String key : inventory.keySet()) {
            data[i] = key + " " + inventory.get(key);
            i++;
        }
        return data;
    }
}