package banking;
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
        menu();
        System.out.println("Bye!");
    }

    static void menu() {
        String menu = "1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit\n";

        while (!exit) {

            System.out.println(menu);

            switch(userInput.nextInt()) {
                case 1:
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

                    break;
                case 2:
                    System.out.println("Enter your card number:");
                    String inputNum = userInput.next();

                    System.out.println("Enter your PIN:");
                    String inputPin = userInput.next();

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
                    break;

                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }
    }
    static void loggedInMenu() {
        String menu = "1. Balance\n" +
                "2. Log out\n" +
                "0. Exit\n";

        System.out.println(menu);
        boolean loggedOut = false;

        while (!loggedOut) {
            switch (userInput.nextInt()) {
                case 1:
                    System.out.println("Balance: " + balance + "\n");
                    System.out.println(menu);
                    break;
                case 2:
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
}
