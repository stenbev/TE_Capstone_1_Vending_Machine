package com.techelevator;

import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class VendingMachineCLI {
    private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
    private static final String PURCHASE_MENU_OPTION_ADD_MONEY = "Feed Money";
    private static final String PURCHASE_MENU_BUY_PRODUCT = "Select Product";
    private static final String PURCHASE_MENU_BACK_TO_MAIN = "Back to Main Menu";
    private static final String PURCHASE_MENU_FINISH_CASH_OUT = "Finish transaction and get change";
    private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
    private static final String MAIN_MENU_OPTION_EXIT = "Exit";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT};
    private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_OPTION_ADD_MONEY, PURCHASE_MENU_BUY_PRODUCT, PURCHASE_MENU_BACK_TO_MAIN, PURCHASE_MENU_FINISH_CASH_OUT};
    private Menu menu;
    private VendingMachine vendingMachine = new VendingMachine();
    String outputPath = "C:\\Users\\Student\\workspace\\capstone-1-team-4\\capstone\\src\\main\\java\\com\\techelevator\\log.txt";
    File receipt = new File(outputPath);

    public VendingMachineCLI(Menu menu) {
        this.menu = menu;
    }

    public void run() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
                vendingMachine.display();
            } else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
                while (true) {
                    String purchaseMenu = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
                    if (purchaseMenu.equals(PURCHASE_MENU_BACK_TO_MAIN)) {
                        break;
                    } else if (purchaseMenu.equals(PURCHASE_MENU_BUY_PRODUCT)) {
                        System.out.println("Please enter slot number:");
                        String usersChoice = scan.nextLine();
                        if (vendingMachine.itemMap.containsKey(usersChoice.toUpperCase())) {
                            vendingMachine.purchase(usersChoice);
                        } else {
                            System.out.println("We are sorry! You entered an invalid number. Please try again.");
                        }
                    } else if (purchaseMenu.equals(PURCHASE_MENU_OPTION_ADD_MONEY)) {
                        System.out.println("Add money for item in whole dollar amounts");
                        try {
                            int addMoney = Integer.parseInt(scan.nextLine());
                            int amount = Integer.valueOf(addMoney);
                            if (amount >= 1) {
                                vendingMachine.setBalance(vendingMachine.getBalance() + amount);
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
                                LocalDateTime now = LocalDateTime.now();
                                try (PrintWriter writer = new PrintWriter(new FileOutputStream(receipt, true))) {
                                    writer.println(dtf.format(now) + " FEED MONEY: " + "$" + addMoney + " " + "$" + vendingMachine.getBalance());
                                } catch (FileNotFoundException e) {
                                    System.out.println("*");
                                }
                            }
                            while (addMoney < 0) {
                                System.out.println("You have entered a negative number, try again");
                                break;
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("ERROR! Please enter a valid dollar amount value in whole dollars.");
                        }
                    } else if (purchaseMenu.equals(PURCHASE_MENU_FINISH_CASH_OUT)) {
                        int quarters = 0;
                        int nickles = 0;
                        int dimes = 0;
                        double endingBalance = vendingMachine.getBalance();
                        while (vendingMachine.getBalance() >= .05) {
                            if (vendingMachine.getBalance() >= 0.25) {
                                quarters++;
                                vendingMachine.setBalance(vendingMachine.getBalance() - 0.25);
                            } else if (vendingMachine.getBalance() >= 0.10) {
                                dimes++;
                                vendingMachine.setBalance(vendingMachine.getBalance() - 0.10);
                            } else if (vendingMachine.getBalance() >= 0.05) {
                                nickles++;
                                vendingMachine.setBalance(vendingMachine.getBalance() - 0.05);
                            }
                        }
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        try (PrintWriter writer = new PrintWriter(new FileOutputStream(receipt, true))) {
                            writer.println(dtf.format(now) + " GIVE CHANGE: " + "$" + vendingMachine.roundToTwoSigs(endingBalance) + " " + "$" + vendingMachine.roundToTwoSigs(vendingMachine.getBalance()));
                        } catch (FileNotFoundException e) {
                            System.out.println("**");
                        }
                        System.out.println("Your total change is: " + vendingMachine.roundToTwoSigs(endingBalance));
                        System.out.println("Thank you! Your change is: " + "Quarters: " + quarters + " Dimes: " + dimes + " Nickles: " + nickles);
                        System.out.println("You are now being transferred back to Main Menu");
                        break;
                    }
                }
            } else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        Menu menu = new Menu(System.in, System.out);
        VendingMachineCLI cli = new VendingMachineCLI(menu);
        cli.run();
    }
}