package edu.ithaca.dragon.bank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CentralBankTest {


    public static BasicAPI createTestBank() throws AccountAlreadyExistsException, AccountDoesNotExistException {
        CentralBank test = new CentralBank();
        test.createAccount("first@bank.com", "password1", 100, false);
        test.createAccount("second@bank.com", "password2", 200, true);
        test.createAccount("third@bank.com", "password3", 300, false);
        test.freezeAccount("third@bank.com");

        return test;
    }


    @Test
    void confirmCredentialsTest() throws AccountDoesNotExistException, AccountAlreadyExistsException {
        CentralBank bank = new CentralBank();
        bank.createAccount("a@b.com", "mysupersecurepassword", 100, false);

        //incorrect password
        assertFalse(bank.confirmCredentials("a@b.com", "")); //border - nothing
        assertFalse(bank.confirmCredentials("a@b.com", "iambreakingin")); //middle - just something else
        assertFalse(bank.confirmCredentials("a@b.com", "ysupersecurepassword")); //border - close to password

        //correct password
        assertTrue(bank.confirmCredentials("a@b.com", "mysupersecurepassword"));


        //and with savings
        bank.createAccount("b@c.com", "mysupersecurepassword", 100, true);
        //incorrect password
        assertFalse(bank.confirmCredentials("b@c.com", "")); //border - nothing
        assertFalse(bank.confirmCredentials("b@c.com", "iambreakingin")); //middle - just something else
        assertFalse(bank.confirmCredentials("b@c.com", "ysupersecurepassword")); //border - close to password
        //correct password
        assertTrue(bank.confirmCredentials("b@c.com", "mysupersecurepassword"));
    }

    @Test
    void withdrawTest() throws InsufficientFundsException, IllegalArgumentException, ExceedsMaxWithdrawalException,
            AccountAlreadyExistsException, AccountDoesNotExistException, AccountFrozenException {
        CentralBank newAccount = new CentralBank();
        String newAccountID = "email@test.com";
        newAccount.createAccount(newAccountID, "password", 200, false);

        assertThrows(AccountDoesNotExistException.class, () -> newAccount.withdraw("notemail@test.com", 100));

        assertThrows(InsufficientFundsException.class, () -> newAccount.withdraw("email@test.com", 201)); //border case
        assertThrows(InsufficientFundsException.class, () -> newAccount.withdraw("email@test.com", 350));
        assertThrows(InsufficientFundsException.class, () -> newAccount.withdraw("email@test.com", 1000)); //border case

        newAccount.createAccount("try@savings.com", "password", 100, true);
        assertThrows(ExceedsMaxWithdrawalException.class, () -> newAccount.withdraw("try@savings.com", 501)); //border case
        assertThrows(ExceedsMaxWithdrawalException.class, () -> newAccount.withdraw("try@savings.com", 750));
        assertThrows(ExceedsMaxWithdrawalException.class, () -> newAccount.withdraw("try@savings.com", 1000)); //border case

        //Withdraw equals or less than is in account
        newAccount.withdraw("email@test.com", 1);
        assertEquals(199, newAccount.checkBalance("email@test.com")); //border case
        newAccount.withdraw("email@test.com", 100);
        assertEquals(99, newAccount.checkBalance("email@test.com"));
        newAccount.withdraw("email@test.com", 99);
        assertEquals(0, newAccount.checkBalance("email@test.com")); //border case

        // Negative, One to Two Decimals
        assertThrows(IllegalArgumentException.class, () -> newAccount.withdraw("email@test.com", -1.01)); // border case
        assertThrows(IllegalArgumentException.class, () -> newAccount.withdraw("email@test.com", -53.83));
        assertThrows(IllegalArgumentException.class, () -> newAccount.withdraw("email@test.com", -9999999.9)); // border case

        // Negative, Multiple Decimals
        assertThrows(IllegalArgumentException.class, () -> newAccount.withdraw("email@test.com", -1.0000001)); // border case
        assertThrows(IllegalArgumentException.class, () -> newAccount.withdraw("email@test.com", -7.48));
        assertThrows(IllegalArgumentException.class, () -> newAccount.withdraw("email@test.com", -9999999.9999999)); // border case

        // Positive, Multiple Decimals
        assertThrows(IllegalArgumentException.class, () -> newAccount.withdraw("email@test.com", 0.000001)); // border case
        assertThrows(IllegalArgumentException.class, () -> newAccount.withdraw("email@test.com", 92.498865));
        assertThrows(IllegalArgumentException.class, () -> newAccount.withdraw("email@test.com", 9999999.999999)); //border case

        // Tests Frozen Exception
        newAccount.deposit(newAccountID, 5);
        newAccount.freezeAccount(newAccountID);
        assertThrows(AccountFrozenException.class, () -> newAccount.withdraw(newAccountID, 5));
    }

    @Test
    void depositTest() throws AccountAlreadyExistsException, AccountDoesNotExistException, AccountFrozenException {
        CentralBank newAccount = new CentralBank();
        String newAccountID = "email@test.com";
        newAccount.createAccount(newAccountID, "password", 200, false);

        // Negative, One to Two Decimals
        assertThrows(IllegalArgumentException.class, () -> newAccount.deposit("email@test.com", -1.01)); // border case
        assertThrows(IllegalArgumentException.class, () -> newAccount.deposit("email@test.com", -53.83));
        assertThrows(IllegalArgumentException.class, () -> newAccount.deposit("email@test.com", -9999999.9)); // border case

        // Positive, One to Two Decimals
        newAccount.deposit("email@test.com", 0);
        assertEquals(200, newAccount.checkBalance("email@test.com")); //border case
        newAccount.deposit("email@test.com", 100);
        assertEquals(300, newAccount.checkBalance("email@test.com"));
        newAccount.deposit("email@test.com", 999);
        assertEquals(1299, newAccount.checkBalance("email@test.com")); //border case

        // Negative, Multiple Decimals
        assertThrows(IllegalArgumentException.class, () -> newAccount.deposit("email@test.com", -1.0000001)); // border case
        assertThrows(IllegalArgumentException.class, () -> newAccount.deposit("email@test.com", -7.48));
        assertThrows(IllegalArgumentException.class, () -> newAccount.deposit("email@test.com", -9999999.9999999)); // border case

        // Positive, Multiple Decimals
        assertThrows(IllegalArgumentException.class, () -> newAccount.deposit("email@test.com", 0.000001)); // border case
        assertThrows(IllegalArgumentException.class, () -> newAccount.deposit("email@test.com", 92.498865));
        assertThrows(IllegalArgumentException.class, () -> newAccount.deposit("email@test.com", 9999999.999999)); //border case

        // Tests Frozen Exception
        newAccount.freezeAccount(newAccountID);
        assertThrows(AccountFrozenException.class, () -> newAccount.deposit(newAccountID, 5));
    }

    @Test
    void transferTest() throws AccountAlreadyExistsException, InsufficientFundsException, AccountDoesNotExistException,
            ExceedsMaxWithdrawalException, AccountFrozenException {
        CentralBank account = new CentralBank();
        String accountAID = "a@test.com";
        account.createAccount(accountAID, "password", 200, false);

        String accountBID = "b@test.com";
        account.createAccount(accountBID, "password", 400, true);

        // Negative, One to Two Decimals
        assertThrows(IllegalArgumentException.class, () -> account.transfer(accountAID, accountBID, -1.01)); // border case
        assertThrows(IllegalArgumentException.class, () -> account.transfer(accountAID, accountBID, -53.83));
        assertThrows(IllegalArgumentException.class, () -> account.transfer(accountAID, accountBID, -9999999.9)); // border case

        // Positive, One to Two Decimals
        account.transfer(accountAID, accountBID, 0);
        assertEquals(200, account.checkBalance(accountAID)); //border case
        assertEquals(400, account.checkBalance(accountBID));
        account.transfer(accountAID, accountBID, 20);
        assertEquals(180, account.checkBalance(accountAID));
        assertEquals(420, account.checkBalance(accountBID));
        account.transfer(accountAID, accountBID, 180);
        assertEquals(0, account.checkBalance(accountAID)); //border case
        assertEquals(600, account.checkBalance(accountBID));
        assertThrows(InsufficientFundsException.class, () -> account.transfer(accountAID, accountBID, 900));

        // Negative, Multiple Decimals
        assertThrows(IllegalArgumentException.class, () -> account.transfer(accountAID, accountBID, -1.0000001)); // border case
        assertThrows(IllegalArgumentException.class, () -> account.transfer(accountAID, accountBID, -7.48));
        assertThrows(IllegalArgumentException.class, () -> account.transfer(accountAID, accountBID, -9999999.9999999)); // border case

        // Positive, Multiple Decimals
        assertThrows(IllegalArgumentException.class, () -> account.transfer(accountAID, accountBID, 0.000001)); // border case
        assertThrows(IllegalArgumentException.class, () -> account.transfer(accountAID, accountBID, 92.498865));
        assertThrows(IllegalArgumentException.class, () -> account.transfer(accountAID, accountBID, 9999999.999999)); //border case

        // Tests Frozen Exception
        account.freezeAccount(accountAID);
        assertThrows(AccountFrozenException.class, () -> account.transfer(accountAID, accountBID, 5));
        account.freezeAccount(accountBID);
        assertThrows(AccountFrozenException.class, () -> account.transfer(accountBID, accountAID, 5));
    }

    @Test
    void checkBalanceTest() throws AccountDoesNotExistException, AccountAlreadyExistsException {

        // No decimals
        CentralBank accountA = new CentralBank();
        String accountAID = "a@test.com";
        accountA.createAccount(accountAID, "password", 0, false);
        assertEquals(0, accountA.checkBalance("a@test.com")); //border case
        CentralBank accountB = new CentralBank();
        String accountBID = "b@test.com";
        accountB.createAccount(accountBID, "password", 200, true);
        assertEquals(200, accountB.checkBalance("b@test.com"));
        CentralBank accountC = new CentralBank();
        String accountCID = "c@test.com";
        accountC.createAccount(accountCID, "password", 9999, false);
        assertEquals(9999, accountC.checkBalance("c@test.com")); //border case

        // One to two decimals
        CentralBank accountD = new CentralBank();
        String accountDID = "d@test.com";
        accountD.createAccount(accountDID, "password", 0.01, false);
        assertEquals(0.01, accountD.checkBalance("d@test.com")); //border case
        CentralBank accountE = new CentralBank();
        String accountEID = "e@test.com";
        accountE.createAccount(accountEID, "password", 200.4, true);
        assertEquals(200.4, accountE.checkBalance("e@test.com"));
        CentralBank accountF = new CentralBank();
        String accountFID = "f@test.com";
        accountF.createAccount(accountFID, "password", 9999.99, false);
        assertEquals(9999.99, accountF.checkBalance("f@test.com")); //border case
    }

    @Test
    void accountExistsTest() throws AccountAlreadyExistsException, IllegalArgumentException {
        CentralBank bank = new CentralBank();
        bank.createAccount("yes@yes.com", "password", 0, false);
        assertTrue(bank.accountExists("yes@yes.com"));
        assertFalse(bank.accountExists("nope@nope.com"));
    }

    @Test
    void createAccountTest() throws AccountAlreadyExistsException, IllegalArgumentException, AccountDoesNotExistException {

        CentralBank bank = new CentralBank();

        //equivalence classes

        //account created
        //good id and start balance
        String id1 = "a@b.com";
        bank.createAccount(id1, "password", 0, false);
        assertTrue(bank.accountExists(id1));
        assertEquals(0, bank.checkBalance(id1));

        String id2 = "b@c.com";
        bank.createAccount(id2, "password", 100.5, true);
        assertTrue(bank.accountExists(id2));
        assertEquals(100.5, bank.checkBalance(id2));

        //this project does not allow dashes in the domain
        //String id3 = "c.long.email@d-long-email.com";
        String id3 = "test.test.test@email.com";
        bank.createAccount(id3, "password", 100000.86, false);
        assertTrue(bank.accountExists(id3));
        assertEquals(100000.86, bank.checkBalance(id3));


        //account not created
        //invalid id/email
        assertThrows(IllegalArgumentException.class, () -> bank.createAccount("#bad", "password", 100, false));
        assertThrows(IllegalArgumentException.class, () -> bank.createAccount("bad..email@bad-.com", "password", 100, false));
        assertThrows(IllegalArgumentException.class, () -> bank.createAccount("bad@bad.c", "password", 100, false));

        //id already exists
        assertThrows(AccountAlreadyExistsException.class, () -> bank.createAccount(id1, "password", 100, false));
        assertThrows(AccountAlreadyExistsException.class, () -> bank.createAccount(id2, "password", 100, false));
        assertThrows(AccountAlreadyExistsException.class, () -> bank.createAccount(id3, "password", 100, false));

        //invalid start balance
        assertThrows(IllegalArgumentException.class, () -> bank.createAccount("c@d.com", "password", -0.01, false));
        assertThrows(IllegalArgumentException.class, () -> bank.createAccount("d@e.com", "password", 100.999, false));
        assertThrows(IllegalArgumentException.class, () -> bank.createAccount("e@f.com", "password", -5.055, false));

        //invalid id and start balance
        assertThrows(AccountAlreadyExistsException.class, () -> bank.createAccount(id1, "password", -0.01, false));
        assertThrows(AccountAlreadyExistsException.class, () -> bank.createAccount(id2, "password", 100.999, false));
        assertThrows(AccountAlreadyExistsException.class, () -> bank.createAccount(id3, "password", -5.055, false));

    }

    @Test
    void closeAccountTest() throws AccountAlreadyExistsException,
            AccountDoesNotExistException, InsufficientFundsException, BalanceRemainingException,
            ExceedsMaxWithdrawalException, AccountFrozenException {

        CentralBank bank = new CentralBank();
        bank.createAccount("a@b.com", "password", 100, false);
        bank.createAccount("b@c.com", "password", 100, false);
        bank.createAccount("c@d.com", "password", 0.01, false);
        bank.createAccount("d@e.com", "password", 0, false);

        //class - account does not exist
        assertThrows(AccountDoesNotExistException.class, () -> bank.closeAccount("e@f.com"));
        assertThrows(AccountDoesNotExistException.class, () -> bank.closeAccount("acctId"));
        assertThrows(AccountDoesNotExistException.class, () -> bank.closeAccount(""));

        //class - account exists and has money
        assertThrows(BalanceRemainingException.class, () -> bank.closeAccount("a@b.com"));
        assertThrows(BalanceRemainingException.class, () -> bank.closeAccount("c@d.com"));

        //class - account exists and does not have money
        bank.withdraw("a@b.com", bank.checkBalance("a@b.com"));
        assertEquals(0, bank.checkBalance("a@b.com"));
        bank.closeAccount("a@b.com");
        assertFalse(bank.accountExists("a@b.com"));
        assertTrue(bank.accountExists("b@c.com"));
        assertTrue(bank.accountExists("c@d.com"));
        assertTrue(bank.accountExists("d@e.com"));

        //class - removed account (does not exist)
        assertThrows(AccountDoesNotExistException.class, () -> bank.closeAccount("a@b.com"));

        //just another test
        bank.withdraw("c@d.com", bank.checkBalance("c@d.com"));
        assertEquals(0, bank.checkBalance("c@d.com"));
        bank.closeAccount("c@d.com");
        assertFalse(bank.accountExists("a@b.com"));
        assertTrue(bank.accountExists("b@c.com"));
        assertFalse(bank.accountExists("c@d.com"));
        assertTrue(bank.accountExists("d@e.com"));
        assertThrows(AccountDoesNotExistException.class, () -> bank.closeAccount("c@d.com"));

    }

    @Test
    void calcTotalAssetsTest() throws AccountAlreadyExistsException, IllegalArgumentException, AccountDoesNotExistException {
        CentralBank bank = new CentralBank();

        //equivalence class - bank has accounts
        //border
        bank.createAccount("a@b.com", "password", 0, false);
        bank.createAccount("b@c.com", "password", 0, true);
        bank.createAccount("c@d.com", "password", 0, false);
        bank.createAccount("d@e.com", "password", 0, true);

        assertEquals(0, bank.calcTotalAssets());

        bank = new CentralBank();
        bank.createAccount("a@b.com", "password", 100.50, true);
        bank.createAccount("b@c.com", "password", 150.05, false);
        bank.createAccount("c@d.com", "password", 200.50, true);
        bank.createAccount("d@e.com", "password", 250.05, false);

        assertEquals(701.10, bank.calcTotalAssets());

        //border
        bank = new CentralBank();
        bank.createAccount("a@b.com", "password", 100000, true);
        bank.createAccount("b@c.com", "password", 500000, false);
        bank.createAccount("c@d.com", "password", 1000000, true);
        bank.createAccount("d@e.com", "password", 5000000, false);

        assertEquals(6600000, bank.calcTotalAssets());


        //equivalence class - bank has no accounts
        CentralBank bank2 = new CentralBank();
        assertThrows(AccountDoesNotExistException.class, () -> bank2.calcTotalAssets());

    }

    @Test
    void veraIntegrationTest() throws AccountAlreadyExistsException, AccountDoesNotExistException, BalanceRemainingException, InsufficientFundsException, ExceedsMaxWithdrawalException, AccountFrozenException {
        CentralBank test = new CentralBank();
        String testID = "test@email.com";
        test.createAccount(testID, "password", 200, false);
        assertTrue(test.confirmCredentials("test@email.com", "password"));

        test.deposit(testID, 45);
        assertEquals(245, test.checkBalance(testID));
        test.withdraw(testID, 25);
        assertEquals(220, test.checkBalance(testID));

        String test2ID = "test2@email.com";
        test.createAccount(test2ID, "password2", 350, false);
        assertTrue(test.confirmCredentials(test2ID, "password2"));

        test.transfer(testID, test2ID, 100);
        assertEquals(120, test.checkBalance(testID));
        assertEquals(450, test.checkBalance(test2ID));

        test.withdraw("test@email.com", 120);
        assertEquals(0, test.checkBalance(testID));
        test.withdraw("test2@email.com", 450);
        assertEquals(0, test.checkBalance(test2ID));

        test.closeAccount(testID);
        test.closeAccount(test2ID);
    }


    @Test
    void veraDepositSystemTest() throws AccountAlreadyExistsException, AccountDoesNotExistException, InsufficientFundsException, ExceedsMaxWithdrawalException, AccountFrozenException {
        CentralBank test = new CentralBank();
        String testID = "test@email.com";
        test.createAccount(testID, "password", 200, false);
        String test2ID = "test2@email.com";
        test.createAccount(test2ID, "password2", 400, true);
        String test3ID = "test3@email.com";
        test.createAccount(test3ID, "password3", 600, false);

        test.deposit(testID, 45);
        assertEquals(245, test.checkBalance(testID));
        test.deposit(test2ID, 45);
        assertEquals(445, test.checkBalance(test2ID));
        test.deposit(test3ID, 45);
        assertEquals(645, test.checkBalance(test3ID));

        test.withdraw(testID, 25);
        assertEquals(220, test.checkBalance(testID));
        test.withdraw(test2ID, 50);
        assertEquals(395, test.checkBalance(test2ID));
        test.withdraw(test3ID, 75);
        assertEquals(570, test.checkBalance(test3ID));

        test.transfer(testID, test2ID, 100);
        assertEquals(120, test.checkBalance(testID));
        assertEquals(495, test.checkBalance(test2ID));
        test.transfer(test3ID, test2ID, 200);
        assertEquals(370, test.checkBalance(test3ID));
        assertEquals(695, test.checkBalance(test2ID));

        test.deposit(testID, 50);
        assertEquals(170, test.checkBalance(testID));
        test.deposit(test2ID, 50);
        assertEquals(745, test.checkBalance(test2ID));
        test.deposit(test3ID, 50);
        assertEquals(420, test.checkBalance(test3ID));
    }
}