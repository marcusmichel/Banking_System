package banking;

import java.util.Random;

public class Account {

    Random r = new Random();
    String iin = "400000";
    String cardNumber;
    String pin;

    public Account(String cardNum, String pinNum) {
        cardNumber = cardNum;
        pin = pinNum;
    }
    public Account() {
        cardNumber = null;
        pin = null;
    }

    void setAccountNumber() {
        String accNumber = "";
        for (int i = 0; i < 10; i++) {
            accNumber = accNumber + String.valueOf(r.nextInt(10));
        }

        cardNumber = iin + accNumber;
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

    boolean equals(Account account) {
        if (this.cardNumber.equals(account.cardNumber)) {
            if (pin.equals(account.pin)) {
               return true;
            }
        }
        return false;
    }
}
