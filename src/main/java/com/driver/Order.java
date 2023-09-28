package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order() {
    }

    public Order(String id, String deliveryTime) {
         this.id=id;
         String hours = ""+deliveryTime.charAt(0)+deliveryTime.charAt(1);
         String minutes = ""+deliveryTime.charAt(3)+deliveryTime.charAt(4);
         this.deliveryTime = Integer.parseInt(hours)*60+Integer.parseInt(minutes);
        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
