package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VendingMachine {
    //This is output the log.txt
    String outputPath = "C:\\Users\\Student\\workspace\\capstone-1-team-4\\capstone\\src\\main\\java\\com\\techelevator\\log.txt";
    File receipt = new File(outputPath);
    private List<Item> items = new ArrayList<>();
    public Map<String, Item> itemMap = new HashMap<>();
    private Item currentItemSelected;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    private double balance = 0;

    public VendingMachine() {
        List<Item> Item = new ArrayList<>();
        String filePath = "vendingmachine.csv";
        File file = new File(filePath);
        try (Scanner fileScan = new Scanner(file)) {
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String[] lineParts = line.split("\\|");
                double price = Double.parseDouble(lineParts[2]);
                Item item = new Item(lineParts[1], price, lineParts[0], lineParts[3], lineParts[4]);
                items.add(item);
                itemMap.put(lineParts[0], item);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't Find The File");
        }
    }

    public void display() {
        for (Item item : items) {
            item.printThisItem();
        }
    }

    public Item getCurrentItemSelected() {
        return currentItemSelected;
    }

    public void setCurrentItemSelected(Item currentItemSelected) {
        this.currentItemSelected = currentItemSelected;
    }

    public void reset() {
        this.currentItemSelected = null;
    }

    public void purchase(String usersChoice) {
        setCurrentItemSelected(itemMap.get(usersChoice.toUpperCase()));
        if (getCurrentItemSelected().getPrice() > getBalance()) {
            System.out.println("You are BROKE BROOO!!!");
            return;
        }
        if (getCurrentItemSelected().getQuantity() > 0) {
            System.out.println(getCurrentItemSelected().getName() + " " + getCurrentItemSelected().getPrice());
            double price = getCurrentItemSelected().getPrice();
            getCurrentItemSelected().deduct();
            System.out.println("Enjoy your snack! " + getCurrentItemSelected().getFinalThought());
            setBalance(getBalance() - price);
            System.out.println("Your new balance is: " + roundToTwoSigs(getBalance()));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(receipt, true))) {
                writer.println(dtf.format(now) + " " + getCurrentItemSelected().getName() + " " + "$" + price + " " + "$" + roundToTwoSigs(getBalance()));
            } catch (FileNotFoundException e) {
                System.out.println("***");
            }
            setCurrentItemSelected(null);
        } else {
            System.out.println("Sorry! This item is SOLD OUT");
        }
    }

    public String roundToTwoSigs(double numberToRound) {
        DecimalFormat dFormat = new DecimalFormat("0.00");
        String formatted = dFormat.format(numberToRound);
        return formatted;
    }
}
