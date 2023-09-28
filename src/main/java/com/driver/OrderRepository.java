package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String,Order> orderList;

    HashMap<String,List<String>> partnerOrders;
    HashMap<String,DeliveryPartner> partnerList;



    OrderRepository(){
        orderList = new HashMap<>();
        partnerList = new HashMap<>();
       partnerOrders = new HashMap<>();
    }
    public void addOrder(Order order) {
        orderList.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerList.put(partnerId,partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderList.containsKey(orderId) && partnerList.containsKey(partnerId)){
            if(partnerOrders.containsKey(partnerId)){
                List<String> orders = partnerOrders.get(partnerId);
                boolean found = false;
                for(List<String> myOrders : partnerOrders.values()) {
                    for (String order : myOrders) {
                        if (order.equals(orderId)) {
                            found = true;
                            break;
                        }
                    }
                }
                if(found==false){
                    orders.add(orderId);
                    partnerOrders.put(partnerId,orders);
                    DeliveryPartner partner = partnerList.get(partnerId);
                    int no_of_orders = partner.getNumberOfOrders();
                    partner.setNumberOfOrders(no_of_orders=no_of_orders+1);
                }
            }
            else{
                List<String> orders = new ArrayList<>();
                orders.add(orderId);
                partnerOrders.put(partnerId,orders);
                DeliveryPartner partner = partnerList.get(partnerId);
                partner.setNumberOfOrders(1);
            }
            }

    }

    public Order getOrderById(String orderId) {
        if(orderList.containsKey(orderId)) {
            return orderList.get(orderId);
        }
        return new Order();
    }

    public DeliveryPartner getPartnerId(String partnerId) {
        if(partnerList.containsKey(partnerId)) {
            return partnerList.get(partnerId);
        }
        return new DeliveryPartner();
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        if(partnerOrders.containsKey(partnerId)){
            List<String> list = partnerOrders.get(partnerId);
            return list.size();
        }
        else {
            return 0;

        }
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        if(partnerOrders.containsKey(partnerId)){
            return partnerOrders.get(partnerId);
        }
        return new ArrayList<>();
    }

    public List<String> getAllOrders() {
        List<String> list = new ArrayList<>();
        for(String key : orderList.keySet()){
            list.add(key);
        }
        return list;
    }

    public Integer getCountOfUnassignedOrders() {
        Integer count = 0;
        for(String orderKey : orderList.keySet()){
            boolean found=false;

            for(List<String> orders : partnerOrders.values()){
                for(String order : orders){
                    if(orderKey.equals(order)){
                        found=true;
                        break;
                    }
                }
                if(found==true) break;
            }
            if(found==false){
                count++;
            }
        }
        return count;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        String hours = time.charAt(0)+time.charAt(1)+"";
        String minutes = time.charAt(3)+time.charAt(4)+"";
        int partnerTime = Integer.parseInt(hours)*60+Integer.parseInt(minutes);
        Integer count = 0;
        if(partnerOrders.containsKey(partnerId)){
           List<String> orders = partnerOrders.get(partnerId);
           for(String order : orders){
               for(String orderKey : orderList.keySet()){
                   if(order.equals(orderKey)){
                       Order myorder = orderList.get(orderKey);
                       int deliveryTime = myorder.getDeliveryTime();
                       if(partnerTime>deliveryTime){
                           count++;
                       }
                       break;
                   }
               }
           }

        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {

        int lastDeliveryTime = Integer.MIN_VALUE;
        if(partnerOrders.containsKey(partnerId)){
        List<String> orders  = partnerOrders.get(partnerId);
        for(String order : orders){
            for(String orderKey : orderList.keySet()){
                if(order.equals(orderKey)){
                    Order myOrder = orderList.get(orderKey);
                    int deliveryTime = myOrder.getDeliveryTime();
                    if(deliveryTime>lastDeliveryTime){
                        lastDeliveryTime=deliveryTime;
                    }
                    break;
                }
            }
        }

        }

        int hours = lastDeliveryTime/60;
        int minutes = lastDeliveryTime%60;
        String hourFormat = ""+hours;
        String minutesFormat = ""+minutes;
        if(hours<10){
            hourFormat = "0"+hours;
        }
        if(minutes<10){
            minutesFormat = "0"+minutes;
        }
        String time = hourFormat+":"+minutesFormat;
        return time;
    }

    public void deletePartnerById(String partnerId) {
        partnerOrders.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        for(List<String> orders : partnerOrders.values()){
            for(String orderKey : orders){
                if(orderKey.equals(orderId)){
                    orders.remove(orderKey);
                }
            }
        }
        orderList.remove(orderId);
    }
}
