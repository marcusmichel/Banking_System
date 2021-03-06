package banking;

import java.util.Random;

public class Account {

    //After integrating a database, this class is no longer used in main.
    // I'm keeping it, for now, as I could potentially integrate it into the database.
    Random r = new Random();
    String iin = "400000";
    String cardNumber;
    String pin;
    int balance;

    public Account(String cardNum, String pinNum) {
        cardNumber = cardNum;
        pin = pinNum;
    }
    public Account() {
        setAccountNumber();
        setPin();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPinNumber() {
        return pin;
    }

    public void accountInformation() {
        System.out.println("Your card number:");
        System.out.println(getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(getPinNumber());
    }

    void setAccountNumber() {
        String accNumber = "";
        for (int i = 0; i < 9; i++) {
            accNumber = accNumber + String.valueOf(r.nextInt(10));
        }

        cardNumber = iin + accNumber;

        int[] luhnArray = new int[cardNumber.length()];
        int sum = 0;

        for (int i = 0; i < cardNumber.length(); i++) {
            if (i % 2 == 0) {
                luhnArray[i] = Character.getNumericValue(cardNumber.charAt(i)) * 2;
            } else {
                luhnArray[i] = Character.getNumericValue(cardNumber.charAt(i));
            }
            if (luhnArray[i] > 9) {
                luhnArray[i] -= 9;
            }
            sum += luhnArray[i];
        }

        int checkSum = 0;
        for (checkSum = 0; checkSum <= 9; checkSum++) {
            if ((sum + checkSum) % 10 == 0) {
                System.out.println(sum + checkSum);
                break;
            }
        }
        
        cardNumber = cardNumber + Integer.toString(checkSum);
    }
    void setPin() {

        this.pin = String.valueOf(r.nextInt(10000));
        int length = pin.length();

        if (length < 4) {
            for (int i = 0; i < 4 - length; i++) {
                pin = "0" + pin;
            }
        }
    }

    void deposit(int deposit) {
        this.balance = balance + deposit;
    }

    boolean equals(Account account) {
        if (this.cardNumber.equals(account.cardNumber)) {
            if (pin.equals(account.pin)) {
               return true;
            }
        }
        return false;
    }

}
