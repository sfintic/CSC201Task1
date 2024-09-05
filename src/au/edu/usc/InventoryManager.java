package au.edu.usc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InventoryManager {

    // Store each tool's rental history
    private static class Rental {
        int quantity;
        double rentFeePerDay;
        int rentDays;

        Rental(int quantity, double rentFeePerDay, int rentDays) {
            this.quantity = quantity;
            this.rentFeePerDay = rentFeePerDay;
            this.rentDays = rentDays;
        }
    }

    // Inventory map stores available tools
    private HashMap<String, LinkedList<Double>> inventory = new HashMap<>();

    // Rental history stores the rent details for each tool
    private HashMap<String, LinkedList<Rental>> rentalHistory = new HashMap<>();

    // Add tools to inventory
    public void addTool(String toolName, int quantity, double acquisitionCost) {
        inventory.putIfAbsent(toolName, new LinkedList<>());
        for (int i = 0; i < quantity; i++) {
            inventory.get(toolName).add(acquisitionCost);
        }
    }

    // Rent tools from inventory and record rental details
    public double rentTool(String toolName, int quantity, double rentFeePerDay, int rentDays) {
        LinkedList<Double> toolList = inventory.get(toolName);
        if (toolList == null || toolList.size() < quantity) {
            return -1;  // Not enough tools to rent
        }

        rentalHistory.putIfAbsent(toolName, new LinkedList<>());
        LinkedList<Rental> rentals = rentalHistory.get(toolName);

        double totalIncome = 0;
        for (int i = 0; i < quantity; i++) {
            toolList.remove(0);  // Remove rented tools from inventory
            rentals.add(new Rental(1, rentFeePerDay, rentDays));  // Store rental details
            totalIncome += rentFeePerDay * rentDays;
        }
        return totalIncome;  // Income from renting
    }

    // Return tools to inventory and calculate surcharges
    public double returnTool(String toolName, int quantity, int daysRented) {
        LinkedList<Double> toolList = inventory.get(toolName);
        LinkedList<Rental> rentals = rentalHistory.get(toolName);
        if (toolList == null || rentals == null || rentals.size() < quantity) {
            return -1;  // Invalid return request
        }

        double totalSurcharge = 0;
        for (int i = 0; i < quantity; i++) {
            Rental rental = rentals.removeFirst();  // Get the earliest rented tool
            double surcharge = 0;

            // Calculate surcharge based on early or late return
            if (daysRented < rental.rentDays) {
                surcharge = 0.30 * rental.rentFeePerDay * (rental.rentDays - daysRented);  // Early return
            } else if (daysRented > rental.rentDays) {
                surcharge = 0.50 * rental.rentFeePerDay * (daysRented - rental.rentDays);  // Late return
            }

            // Return the tool to inventory
            toolList.add(50.00);  // Add returned tools back to inventory (dummy acquisition cost)

            totalSurcharge += surcharge;
        }

        return totalSurcharge;  // Return total surcharge for early/late returns
    }

    // Return damaged tools and charge replacement cost
    public double returnDamagedTool(String toolName, int quantity, double replacementCost) {
        LinkedList<Double> toolList = inventory.get(toolName);
        if (toolList == null || toolList.size() < quantity) {
            return -1;
        }

        for (int i = 0; i < quantity; i++) {
            toolList.remove(0);  // Remove damaged tools
        }

        double totalReplacementCost = replacementCost * quantity;
        return totalReplacementCost;  // Loss due to damaged tools
    }

    // Discard tools from inventory and calculate acquisition cost loss
    public double discardTool(String toolName, int quantity) {
        LinkedList<Double> toolList = inventory.get(toolName);
        if (toolList == null || toolList.size() < quantity) {
            return -1;
        }

        double totalDiscardCost = 0;
        for (int i = 0; i < quantity; i++) {
            totalDiscardCost += toolList.remove(0);  // Add acquisition cost to total loss
        }

        return totalDiscardCost;  // Return acquisition cost of discarded tools
    }

    // Display the current inventory
    public void checkInventory() {
        inventory.forEach((toolName, toolList) -> System.out.println(toolName + ": " + toolList.size()));
    }

    // Calculate and print the profit/loss
    public void calculateProfit(double totalIncome, double totalExpense) {
        double profit = totalIncome - totalExpense;
        System.out.printf("Profit/Loss: $%.2f\n", profit);
    }
}
