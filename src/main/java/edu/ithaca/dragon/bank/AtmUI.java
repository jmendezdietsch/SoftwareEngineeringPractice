package edu.ithaca.dragon.bank;

import java.util.*;

public class AtmUI extends CentralBank{

    private BasicAPI atm;

    public AtmUI(BasicAPI atm) {
        this.atm = atm;
    }

    //all the ui methods!!!!!!

    public boolean ATMCredentials(String acctId, String password) throws AccountDoesNotExistException {
        return confirmCredentials(acctId, password);
    }

    public double ATMBalance(String acctId) throws AccountDoesNotExistException {
        return checkBalance(acctId);
    }

    public void ATMWithdraw(String acctId, double amount) throws InsufficientFundsException, AccountDoesNotExistException, AccountFrozenException, ExceedsMaxWithdrawalException {
        withdraw(acctId, amount);
    }

    public void ATMDeposit(String acctId, double amount) throws AccountDoesNotExistException, AccountFrozenException {
        deposit(acctId, amount);
    }

    public void ATMTransfer(String acctIdToWithdrawFrom, String acctIdToDepositTo, double amount) throws InsufficientFundsException, AccountDoesNotExistException, AccountFrozenException, ExceedsMaxWithdrawalException {
        transfer(acctIdToWithdrawFrom, acctIdToDepositTo, amount);
    }

    //run the ui
    public void run() {

        System.out.println("Welcome to the ATM");

        Scanner checkId = new Scanner(System.in);
        System.out.println("Enter account id: ");

    }

}
