package edu.ithaca.dragon.bank;

import java.util.*;

/**
 * extends Central Bank and has accounts Map as public
 * accounts Map to be public would be better as it can check for already made accounts before ATM
 * need to add ATM transactions to history through addHistory
 */
public class AtmUI extends CentralBank{
    boolean logout = false;

    private BasicAPI atm;

    public AtmUI(BasicAPI atm) {
        this.atm = atm;
    }

    //all the ui methods!!!!!!

    public boolean ATMCredentials(String acctId, String password) {
        /**
         *if(!accounts.containsKey(acctId)) {
         *             while(!accounts.containsKey(acctId)){
         *                 System.out.println("Account does not exist, enter a correct account");
         *                 Have a new account input
         *             }
         *         }
         *         else {
         *             BankAccount account = accounts.get(acctId);
         *             if(!account.getPassword().equals(password)) {
         *                 while(!account.getPassword().equals(password)){
         *                     System.out.println("Password is invalid, enter a correct password");
         *                      Have a new password input
         *                 }
         *             }
         *             if(account.isFrozen()) {
         *                  System.out.println("Account can not be used as it is frozen);
         *                  return false;
         *             }
         *             return true;
         *         }
         */
        return false;
    }
    public double ATMBalance(String acctId) throws AccountDoesNotExistException {
        /**
         * if (!accounts.containsKey(acctId))
         *             throw new AccountDoesNotExistException("Account with this id does not exists");
         *         BankAccount account = accounts.get(acctId);
         *         return account.getBalance();
         */
    }

    public void ATMWithdraw(String acctId, double amount) {
        /**
         * if (!accounts.containsKey(acctId))
         *             throw new AccountDoesNotExistException("Account with this id does not exists");
         *         BankAccount account = accounts.get(acctId);
         *         account.withdraw(amount);
         */
    }

    public void ATMDeposit(String acctId, double amount) {
        /**
         * if (!accounts.containsKey(acctId))
         *             throw new AccountDoesNotExistException("Account with this id does not exists");
         *         BankAccount account = accounts.get(acctId);
         *         account.deposit(amount);
         */
    }

    public void ATMTransfer(String acctIdToWithdrawFrom, String acctIdToDepositTo, double amount) {
        /**
         * if (!accounts.containsKey(acctIdToWithdrawFrom))
         *             throw new AccountDoesNotExistException("Account with this id does not exists");
         * if (!accounts.containsKey(acctIdToWithdrawFrom))
         *             throw new AccountDoesNotExistException("Account with this id does not exists");
         *         BankAccount account = accounts.get(acctId);
         *         account.deposit(amount);
         */
    }

    //check and ask after user has finished a transaction and would like to log out
    public boolean logout(String choice) {
        if(choice.equals("Y")) {
            logout = true;
            return true;
        }
        else if(!choice.equals("Y") && !choice.equals("N")){
            System.out.println("Response not valid");
            return false;
        }
        else {
            return  false;
        }
    }

    //run the ui
    public void run() {

        //It would be in the while loop and would print each time after logout
        System.out.println("Welcome to the ATM");

        /**
         * //isOn would handle and have the ATM always be on with multiple logins
         * boolean isOn = true;
         * String account = " ";
         * String password = " ";
         *
         *         while (isOn){
         *             System.out.println("Welcome to the ATM");
         *
         *             //with use login
         *             System.out.println("Enter your account login Id");
         *             if(ATMCredentials(account, password).equals(true) && logout.equals(false){
         *                  System.out.println("What would you like to do(check balance, deposit, withdraw, transfer");
         *
         *                  if(input.equals("check balance")){
         *                      ATMBalance(account);
         *                  }
         *                  else if(input.equals("deposit")){
         *                      System.out.println("How much would you like to deposit: ");
         *                      amount = " ";
         *                      ATMDeposit(account, amount);
         *                  }
         *                  else if(input.equals("withdraw")){
         *                      System.out.println("How much would you like to withdraw: ");
         *                      amount = " ";
         *                      ATMWithdraw(account, amount);
         *                  }
         *                  else if(input.equals("transfer")){
         *                      System.out.println("Would you like to transfer from or to: ");
         *                      String transferChoice = " ";
         *                      boolean transfer = true;
         *                      while(transfer.equals(true)) {
         *                          if(input.equals("from")) {
         *                              System.out.println("What account would you like to withraw: ");
         *                              transferAct = " ";
         *                              //Check if transferAct is valid
         *                              System.out.println("How much would you like to transfer: ");
         *                              amount = " ";
         *                              ATMTransfer(transferAct, account, amount);
         *                              transfer = false;
         *                          }
         *                          else if(input.equals("to")) {
         *                              System.out.println("What account would you like to withraw: ");
         *                              transferAct = " ";
         *                              //Check if transferAct is valid
         *                              System.out.println("How much would you like to transfer: ");
         *                              amount = " ";
         *                              ATMTransfer(transferAct, account, amount);
         *                              transfer = false;
         *                          }
         *                          else{
         *                              while(!input.equals("to") && !input.equals("from")) {
         *                                  System.out.println("Wrong input. Would you like to transfer from or to: ");
         *                                  String transferChoice = " ";
         *                              }
         *                          }
         *                       }
         *                       System.out.println("Would you like to logout: ");
         *                       String logoutIn = " ";
         *                       logout(logoutIn);
         *                       }
         *                  }
         *
         *              }
         *         }
         *
         *   }
        */
    }

}
