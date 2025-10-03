package com.vaadin.starter.bakery.backend.data;

import java.util.LinkedHashMap;
import java.util.List;

import com.vaadin.starter.bakery.backend.data.entity.Product;

/**
 * DashboardData holds aggregated dashboard statistics for the Bakery application.
 * This class acts as a data transfer object between the backend and the dashboard UI.
 */
public class DashboardData {

    /**
     * Overall delivery statistics, such as totals or averages.
     */
    private DeliveryStats deliveryStats;

    /**
     * List of deliveries for the current month, one entry per day or other period.
     */
    private List<Number> deliveriesThisMonth;

    /**
     * List of deliveries for the current year, one entry per month.
     */
    private List<Number> deliveriesThisYear;

    /**
     * Sales data per month, typically organized as [month][statistic].
     */
    private Number[][] salesPerMonth;

    /**
     * Map of products to the number of times each product was delivered.
     * Maintains insertion order.
     */
    private LinkedHashMap<Product, Integer> productDeliveries;

    /**
     * Gets the overall delivery statistics.
     * @return DeliveryStats object
     */
    public DeliveryStats getDeliveryStats() {
        return deliveryStats;
    }

    /**
     * Sets the overall delivery statistics.
     * @param deliveryStats DeliveryStats object
     */
    public void setDeliveryStats(DeliveryStats deliveryStats) {
        this.deliveryStats = deliveryStats;
    }

    /**
     * Gets deliveries for the current month.
     * @return List of delivery counts for the month
     */
    public List<Number> getDeliveriesThisMonth() {
        return deliveriesThisMonth;
    }

    /**
     * Sets deliveries for the current month.
     * @param deliveriesThisMonth List of delivery counts for the month
     */
    public void setDeliveriesThisMonth(List<Number> deliveriesThisMonth) {
        this.deliveriesThisMonth = deliveriesThisMonth;
    }

    /**
     * Gets deliveries for the current year.
     * @return List of delivery counts for the year
     */
    public List<Number> getDeliveriesThisYear() {
        return deliveriesThisYear;
    }

    /**
     * Sets deliveries for the current year.
     * @param deliveriesThisYear List of delivery counts for the year
     */
    public void setDeliveriesThisYear(List<Number> deliveriesThisYear) {
        this.deliveriesThisYear = deliveriesThisYear;
    }

    /**
     * Sets sales data per month.
     * @param salesPerMonth 2D array of sales data
     */
    public void setSalesPerMonth(Number[][] salesPerMonth) {
        this.salesPerMonth = salesPerMonth;
    }

    /**
     * Gets sales data for a particular month.
     * @param i Month index (0-based)
     * @return Array of sales statistics for the month
     */
    public Number[] getSalesPerMonth(int i) {
        return salesPerMonth[i];
    }

    /**
     * Gets the deliveries count per product.
     * @return Map of Product to delivery count
     */
    public LinkedHashMap<Product, Integer> getProductDeliveries() {
        return productDeliveries;
    }

    /**
     * Sets the deliveries count per product.
     * @param productDeliveries Map of Product to delivery count
     */
    public void setProductDeliveries(LinkedHashMap<Product, Integer> productDeliveries) {
        this.productDeliveries = productDeliveries;
    }

}
