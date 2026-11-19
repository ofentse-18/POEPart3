/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.poepart3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** JUnit 5 tests for Part 2 message behavior and Part 3 collections/submenu. */
class MessageTest {
    private final PrintStream originalOut = System.out;
    @TempDir Path tempDir;

    @BeforeEach
    void setUp() {
        Message.clearAllArrays();
        Message.setStorageFile(tempDir.resolve("stored_messages.json").toFile());
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        Message.setStorageFile(new java.io.File("stored_messages.json"));
    }

    @Test
    void testSentMessagesArrayContainsRubricSentMessages() {
        Message.populateTestData();
        assertEquals(2, Message.sentMessagesList.size());
        assertEquals("Did you get the cake?", Message.sentMessagesList.get(0).getMessageText());
        assertEquals("It is dinner time!", Message.sentMessagesList.get(1).getMessageText());
        assertEquals(2, Message.getTotalMessagesSent());
    }

    @Test
    void testDisregardedAndStoredArraysArePopulated() {
        Message.populateTestData();
        assertEquals(1, Message.disregardedMessagesList.size());
        assertEquals("Yohoooo, I am at your gate.", Message.disregardedMessagesList.get(0).getMessageText());
        assertEquals(2, Message.storedMessagesList.size());
    }

    @Test
    void testLongestStoredMessage() {
        Message.populateTestData();
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                Message.getLongestStoredMessage());
    }

    @Test
    void testSearchByIdReturnsRecipientAndMessage() {
        Message.populateTestData();
        assertEquals("Recipient: 0838884567\nMessage: It is dinner time!",
                Message.searchByMessageId("4000000004"));
    }

    @Test
    void testSearchByUnknownId() {
        Message.populateTestData();
        assertEquals("Message ID not found.", Message.searchByMessageId("9999999999"));
    }

    @Test
    void testSearchStoredMessagesByRecipientReturnsBothMatches() {
        Message.populateTestData();
        assertEquals("Where are you? You are late! I have asked you to be on time.\n"
                        + "OK, I am leaving without you",
                Message.searchByRecipient("27838884567"));
    }

    @Test
    void testSearchRecipientAcceptsEquivalentLocalNumber() {
        Message.populateTestData();
        assertEquals("Where are you? You are late! I have asked you to be on time.\n"
                        + "OK, I am leaving without you",
                Message.searchByRecipient("+27838884567"));
    }

    @Test
    void testDeleteStoredMessageByHash() {
        Message.populateTestData();
        String hash = Message.storedMessagesList.get(0).getMessageHash();
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                Message.deleteMessageByHash(hash));
        assertEquals(1, Message.storedMessagesList.size());
        assertEquals("OK, I am leaving without you", Message.storedMessagesList.get(0).getMessageText());
        assertEquals(4, Message.getMessageHashes().length);
    }

    @Test
    void testDeleteUnknownHash() {
        Message.populateTestData();
        assertEquals("Message with hash missing not found.", Message.deleteMessageByHash("missing"));
    }

    @Test
    void testReportIncludesAllStoredDetails() {
        Message.populateTestData();
        String report = Message.displayStoredMessagesReport();
        assertTrue(report.contains("STORED MESSAGES REPORT"));
        assertTrue(report.contains("Message ID: 2000000002"));
        assertTrue(report.contains("Message Hash: " + Message.storedMessagesList.get(0).getMessageHash()));
        assertTrue(report.contains("Recipient: 27838884567"));
        assertTrue(report.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(report.contains("Status: Stored"));
        assertTrue(report.contains("OK, I am leaving without you"));
    }

    @Test
    void testActualStoredMessagesSubmenuIdSearch() {
        Message.populateTestData();
        String output = runSubmenu("3\n4000000004\n7\n");
        assertTrue(output.contains("Recipient: 0838884567"));
        assertTrue(output.contains("Message: It is dinner time!"));
    }

    @Test
    void testActualStoredMessagesSubmenuRecipientSearch() {
        Message.populateTestData();
        String output = runSubmenu("4\n27838884567\n7\n");
        assertTrue(output.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(output.contains("OK, I am leaving without you"));
    }

    @Test
    void testActualStoredMessagesSubmenuDeleteAndReport() {
        Message.populateTestData();
        String hash = Message.storedMessagesList.get(0).getMessageHash();
        String output = runSubmenu("5\n" + hash + "\n6\n7\n");
        assertTrue(output.contains("successfully deleted"));
        int reportStart = output.indexOf("=== STORED MESSAGES REPORT ===");
        assertTrue(reportStart >= 0);
        String report = output.substring(reportStart);
        assertFalse(report.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(report.contains("OK, I am leaving without you"));
        assertEquals(1, Message.storedMessagesList.size());
    }

    @Test
    void testHashGeneratedFromIdNumberAndMessageWords() {
        Message message = new Message("1234567890", 4, "27834557896", "Did you get the cake?", "Sent");
        assertEquals("12:4:DIDCAKE", message.getMessageHash());
    }

    @Test
    void testMessageIdMustBeTenDigits() {
        Message message = new Message();
        assertTrue(message.checkMessageId("0123456789"));
        assertFalse(message.checkMessageId("123"));
    }

    private String runSubmenu(String input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream capture = new PrintStream(output);
        System.setOut(capture);
        POEPart3.runStoredMessagesMenu(new Scanner(input));
        capture.flush();
        return new String(output.toByteArray(), StandardCharsets.UTF_8);
    }
}

