package banking;
import org.sqlite.SQLiteDataSource;
import java.sql.*;

import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;


public class Main {

    static Scanner userInput = new Scanner(System.in);
    static int balance = 0;
    static boolean exit = false;
    static ArrayList<Account> accounts = new ArrayList<Account>();
    static int numOfAccounts = 0;

    public static void main(String[] args) {

        AccountDB ops = new AccountDB(args[1]);

        menu(ops);
    }

    //This didn't used to have an AccountDB argument
    static void menu(AccountDB ops) {
        String menu = "1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit\n";
        Account user = null;

        while (!exit) {

            System.out.println(menu);

            switch(userInput.nextInt()) {
                case 1:

                    //start account creation

                    user = new Account();
                    if (!ops.containsRecord(user.getCardNumber(), user.getPinNumber())) {
                        ops.insertData(user.getCardNumber(), user.getPinNumber());
                        System.out.println("Your card has been created");
                        user.accountInformation();
                    } else {
                        user = new Account();
                        ops.insertData(user.getCardNumber(), user.getPinNumber());
                        System.out.println("Your card has been created");
                        user.accountInformation();
                    }

                    // Helps me make sure my database table is updating
//                    ops.displayTable();

                    //end account creation

                    /* //Previous method of creating a new account
                    accounts.add(new Account());
                    numOfAccounts++;
                    System.out.println("Your card has been created");
                    System.out.println("Your card number:");
                    accounts.get(numOfAccounts - 1).setAccountNumber();
                    System.out.println(accounts.get(numOfAccounts - 1).cardNumber);

                    System.out.println("Your card PIN:");
                    accounts.get(numOfAccounts - 1).setPin();
                    System.out.println(accounts.get(numOfAccounts - 1).pin);
                    System.out.println();
                    */

                    break;

                case 2:
                    System.out.println("Enter your card number:");
                    String inputNum = userInput.next();

                    System.out.println("Enter your PIN:");
                    String inputPin = userInput.next();

                    if (ops.containsRecord(inputNum, inputPin)) {

                        System.out.println("You have successfully logged in!");

                        loggedInMenu(inputNum, inputPin, ops);

                    } else {
                        System.out.println("Wrong card number or PIN!");
                    }
                    //end login dialog
                    /* //Previous method of checking card and pin number
                    boolean rightAccount = false;

                    Account inputAccount = new Account(inputNum, inputPin);
                    for (int i = 0; i < accounts.size(); i++) {
                        if (inputAccount.equals(accounts.get(i))) {
                            rightAccount = true;
                        }
                    }
                    if (rightAccount) {
                        System.out.println("You have successfully logged in!");
                        System.out.println();
                        loggedInMenu();
                    } else {
                        System.out.println("Wrong card number or PIN!");
                    }

                     */
                    break;

                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }
        userInput.close();

        System.out.println("Bye!");
    }

    static void loggedInMenu(String inputNum, String inputPin, AccountDB ops) {
        String menu = "1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit\n";

        boolean loggedOut = false;

        while (!loggedOut) {
            System.out.println(menu);
            switch (userInput.nextInt()) {
                case 1:
//                    System.out.println("Balance: " + balance + "\n");
                    ops.printBalance(inputNum, inputPin);
                    System.out.println(menu);
                    break;
                case 2:
                    System.out.println("Enter income:");
                    int deposit = userInput.nextInt();
                    ops.depositMoney(inputNum, inputPin, deposit);
                    System.out.println("Income was added!");
                    break;
                case 3:
                    System.out.println("Enter card number:");
                    int cardNumber = userInput.nextInt();
                    if (cardNumber == Integer.parseInt(inputNum)) {
                        System.out.println("You can't transfer money to the same account!");
                    } else if (!luhnCheck(cardNumber)) {
                        System.out.println("Probably you made mistake " +
                                "in the card number.\n" +
                                "Please try again!");
                    } else if (!ops.containsRecord(inputNum, inputPin)) {
                        System.out.println("Such a card does not exist");
                    } else {
                        System.out.println("Enter how much money you want to transfer:");
                        int transferAmount = userInput.nextInt();
                        if (ops.checkMoney(inputNum, inputPin, transferAmount)) {
                            ops.transferMoney(inputNum, inputPin, transferAmount, Integer.toString(cardNumber));
                            System.out.println("Success!");
                        } else {
                            System.out.println("Not enough money!");
                        }
                    }
                    break;
                case 4:
                    ops.closeAccount(inputNum);
                    break;
                case 5:
                    loggedOut = true;
                    System.out.println("You have successfully logged out!");
                    break;
                case 0:
                    exit = true;
                    loggedOut = true;
                    break;
                default:
                    System.out.println("Invalid Input");
                    break;
            }
        }
    }

    static boolean luhnCheck(int cardNumber) {
        String number = String.valueOf(cardNumber);
        int[] luhnArray = new int[number.length()];
        int sum = 0;

        for(int i = 0; i < number.length(); i++) {
            luhnArray[i] = Character.digit(number.charAt(i), 10);
        }

        for (int i = 0; i < number.length(); i++) {
            if (i % 2 == 0) {
                luhnArray[i] = Character.getNumericValue(number.charAt(i)) * 2;
            } else {
                luhnArray[i] = Character.getNumericValue(number.charAt(i));
            }
            if (luhnArray[i] > 9) {
                luhnArray[i] -= 9;
            }
            sum += luhnArray[i];
        }
        sum += luhnArray[luhnArray.length - 1];

        if (sum % 10 == 0) {
            return true;
        }
        return false;

    }
}
