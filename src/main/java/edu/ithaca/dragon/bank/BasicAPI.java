package edu.ithaca.dragon.bank;

//API to be used by ATMs
public interface BasicAPI {

    boolean confirmCredentials(String acctId, String password);

    double checkBalance(String acctId);

    void withdraw(String acctId, double amount) throws InsufficientFundsException, AccountDoesNotExistException;

    void deposit(String acctId, double amount) throws AccountDoesNotExistException;

    void transfer(String acctIdToWithdrawFrom, String acctIdToDepositTo, double amount) throws InsufficientFundsException, AccountDoesNotExistException;

    String transactionHistory(String acctId);

}
