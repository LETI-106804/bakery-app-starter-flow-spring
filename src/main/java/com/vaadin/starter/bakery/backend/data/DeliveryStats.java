package com.vaadin.starter.bakery.backend.data;

/**
 * DeliveryStats is a simple data container class that holds statistics about deliveries and orders
 * for the bakery application. The statistics include counts for orders delivered today, orders due today,
 * orders due tomorrow, orders not available for delivery today, and new orders.
 *
 * This class provides getter and setter methods for each statistic field.
 */
public class DeliveryStats {

    /** Number of orders delivered today */
    private int deliveredToday;

    /** Number of orders due today */
    private int dueToday;

    /** Number of orders due tomorrow */
    private int dueTomorrow;

    /** Number of orders not available for delivery today */
    private int notAvailableToday;

    /** Number of new orders */
    private int newOrders;

    /**
     * Gets the number of orders delivered today.
     * @return the deliveredToday count
     */
    public int getDeliveredToday() {
        return deliveredToday;
    }

    /**
     * Sets the number of orders delivered today.
     * @param deliveredToday the deliveredToday count to set
     */
    public void setDeliveredToday(int deliveredToday) {
        this.deliveredToday = deliveredToday;
    }

    /**
     * Gets the number of orders due today.
     * @return the dueToday count
     */
    public int getDueToday() {
        return dueToday;
    }

    /**
     * Sets the number of orders due today.
     * @param dueToday the dueToday count to set
     */
    public void setDueToday(int dueToday) {
        this.dueToday = dueToday;
    }

    /**
     * Gets the number of orders due tomorrow.
     * @return the dueTomorrow count
     */
    public int getDueTomorrow() {
        return dueTomorrow;
    }

    /**
     * Sets the number of orders due tomorrow.
     * @param dueTomorrow the dueTomorrow count to set
     */
    public void setDueTomorrow(int dueTomorrow) {
        this.dueTomorrow = dueTomorrow;
    }

    /**
     * Gets the number of orders not available for delivery today.
     * @return the notAvailableToday count
     */
    public int getNotAvailableToday() {
        return notAvailableToday;
    }

    /**
     * Sets the number of orders not available for delivery today.
     * @param notAvailableToday the notAvailableToday count to set
     */
    public void setNotAvailableToday(int notAvailableToday) {
        this.notAvailableToday = notAvailableToday;
    }

    /**
     * Gets the number of new orders.
     * @return the newOrders count
     */
    public int getNewOrders() {
        return newOrders;
    }

    /**
     * Sets the number of new orders.
     * @param newOrders the newOrders count to set
     */
    public void setNewOrders(int newOrders) {
        this.newOrders = newOrders;
    }

}
