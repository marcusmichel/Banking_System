package banking;
import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        String iin = "400000";
        Random
        boolean exit = false;

        while (!exit) {
            menu();
            switch(userInput.nextInt()) {
                case 1:
                    System.out.println("Your card has been created");
                    System.out.println("Your card number:")
                case 2:
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid input");
            }
            exit = true;
        }



    }

    static void menu() {
        System.out.println("1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit\n");
    }
}
