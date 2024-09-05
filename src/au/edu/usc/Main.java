package au.edu.usc;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        String[] inputFiles = {
                "T1_test_cases/input_01.txt",
                "T1_test_cases/input_02.txt",
                "T1_test_cases/input_03.txt",
                "T1_test_cases/input_04.txt",
                "T1_test_cases/input_05.txt",
                "T1_test_cases/input_06.txt"
        };

        for (String inputFile : inputFiles) {
            // Print the file name
            System.out.println(inputFile.replace("T1_test_cases/", ""));
            System.out.println("~~~~~~~~~~~~~");
            processInputFile(inputFile);
            System.out.println();  // Separate output for each file
        }
    }

    public static void processInputFile(String inputFile) {
        InventoryManager inventoryManager = new InventoryManager();
        double totalIncome = 0;
        double totalExpenses = 0;
        boolean errorOccurred = false;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] command = line.split(" ");
                switch (command[0]) {
                    case "ADD":
                        if (command.length != 4) {
                            logger.log(Level.SEVERE, "Invalid ADD command: {0}", line);
                            errorOccurred = true;
                        } else {
                            inventoryManager.addTool(command[1], Integer.parseInt(command[2]), Double.parseDouble(command[3]));
                        }
                        break;
                    case "RENT":
                        if (command.length != 5) {
                            logger.log(Level.SEVERE, "Invalid RENT command: {0}", line);
                            errorOccurred = true;
                        } else {
                            double income = inventoryManager.rentTool(command[1], Integer.parseInt(command[2]), Double.parseDouble(command[3]), Integer.parseInt(command[4]));
                            if (income == -1) {
                                System.out.println("Profit/Loss: NA");
                                errorOccurred = true;
                            } else {
                                totalIncome += income;
                            }
                        }
                        break;
                    case "RETURN":
                        if (command.length != 4) {
                            logger.log(Level.SEVERE, "Invalid RETURN command: {0}", line);
                            errorOccurred = true;
                        } else {
                            double expense = inventoryManager.returnTool(command[1], Integer.parseInt(command[2]), Integer.parseInt(command[3]));
                            if (expense == -1) {
                                System.out.println("Profit/Loss: NA");
                                errorOccurred = true;
                            } else {
                                totalExpenses += expense;
                            }
                        }
                        break;
                    case "RETURN_DAMAGED":
                        if (command.length != 4) {
                            logger.log(Level.SEVERE, "Invalid RETURN_DAMAGED command: {0}", line);
                            errorOccurred = true;
                        } else {
                            totalExpenses += inventoryManager.returnDamagedTool(command[1], Integer.parseInt(command[2]), Double.parseDouble(command[3]));
                        }
                        break;
                    case "DISCARD":
                        if (command.length != 3) {
                            logger.log(Level.SEVERE, "Invalid DISCARD command: {0}. Expected 3 arguments but found {1}", new Object[]{line, command.length});
                            errorOccurred = true;
                        } else {
                            double discardCost = inventoryManager.discardTool(command[1], Integer.parseInt(command[2]));
                            if (discardCost == -1) {
                                System.out.println("Profit/Loss: NA");
                                errorOccurred = true;
                            } else {
                                totalExpenses += discardCost;
                            }
                        }
                        break;
                    case "CHECK":
                        inventoryManager.checkInventory();
                        break;
                    case "PROFIT":
                        if (!errorOccurred) {
                            inventoryManager.calculateProfit(totalIncome, totalExpenses);
                        } else {
                            System.out.println("Profit/Loss: NA");
                        }
                        break;
                    default:
                        logger.log(Level.SEVERE, "Invalid command: {0}", line);
                        errorOccurred = true;
                        break;
                }

                // Stop processing further commands if an error occurred
                if (errorOccurred) break;
            }
            reader.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error processing input file: {0}", e.toString());
        }
    }
}
