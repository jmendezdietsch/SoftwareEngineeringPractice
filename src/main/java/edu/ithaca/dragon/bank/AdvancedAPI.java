package edu.ithaca.dragon.bank;

//API to be used by Teller systems
public interface AdvancedAPI extends BasicAPI {

    public void createAccount(String acctId, String password, double startingBalance, boolean savings) throws AccountAlreadyExistsException, IllegalArgumentException;

    public void closeAccount(String acctId) throws AccountDoesNotExistException, BalanceRemainingException;
}
