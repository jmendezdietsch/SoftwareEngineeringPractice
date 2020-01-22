package edu.ithaca.dragon.bank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void getBalanceTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals(200, bankAccount.getBalance());
    }

    @Test
    void withdrawTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        bankAccount.withdraw(100);

        assertEquals(100, bankAccount.getBalance());
    }

    @Test
    void isEmailValidTest(){
        //assertTrue(BankAccount.isEmailValid( "a@b.com"));
        //assertFalse( BankAccount.isEmailValid(""));

        //Josue's invalid email prefix tests
        assertFalse((BankAccount.isEmailValid("TomHanksIsCool-@b.com"))); //equivalence case is - next to the @ edge case
        assertFalse((BankAccount.isEmailValid("Maud..Vile@b.com"))); //equivalence case is .. in the prefix edge case
        assertFalse((BankAccount.isEmailValid("Yung#Muney@b.com"))); //equivalence case is # in the prefix edge case
        assertFalse((BankAccount.isEmailValid(".TableScraps@b.com")));  //equivalence case is prefix starting with . edge case

        //Josue's invalid email domain tests
        assertFalse((BankAccount.isEmailValid("TomHanksIsCold@b#c.com"))); //equivalence case # in domain edge case
        assertFalse((BankAccount.isEmailValid("KevinLevin@b"))); //equivalence case no extension to the domain edge case
        assertFalse((BankAccount.isEmailValid("JetSetRadio@b..com"))); //equivalence case .. in the domain edge case
        assertFalse((BankAccount.isEmailValid("Jajaja@b.SpanishLaugh.com"))); //equivalence case two . in the domain edge case
        //there is no equivalence case for there only being one letter in the extension. edge case
        //there is no equivalence case for any valid emails.
    }

    @Test
    void constructorTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance());
        //check for exception thrown correctly
        assertThrows(IllegalArgumentException.class, ()-> new BankAccount("", 100));
    }

}