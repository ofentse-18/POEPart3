/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.poepart3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/** JUnit 5 tests for the Part 1 registration/login logic used in Part 3. */
class LoginTest {
    @Test
    void acceptsValidRegistrationValues() {
        Login login = new Login("kyl_1", "Ch&&sec99!", "+27821234567", "Kai", "Lee");
        assertTrue(login.checkUserName());
        assertTrue(login.checkPasswordComplexity());
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    void rejectsInvalidRegistrationValues() {
        Login login = new Login("kyle1", "password", "0821234567", "Kai", "Lee");
        assertFalse(login.checkUserName());
        assertFalse(login.checkPasswordComplexity());
        assertFalse(login.checkCellPhoneNumber());
    }

    @Test
    void loginSucceedsOnlyForMatchingCredentials() {
        Login login = new Login("kyl_1", "Ch&&sec99!", "+27821234567", "Kai", "Lee");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec99!"));
        assertFalse(login.loginUser("kyl_1", "wrong"));
    }
}

