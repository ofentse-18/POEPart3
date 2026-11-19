/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poepart3;

import java.util.Scanner;

/**
 * Console application combining Parts 1, 2 and 3.
 */
public class POEPart3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login user = new Login();
        
        System.out.println("=== USER REGISTRATION ===");
        System.out.print("Enter first name: ");
        user.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter last name: ");
        user.setLastName(scanner.nextLine().trim());

        while (true) {
            System.out.print("Enter username (underscore, no more than 5 characters): ");
            user.setUsername(scanner.nextLine().trim());
            if (user.checkUserName()) break;
            System.out.println("Username is not correctly formatted.");
        }
        while (true) {
            System.out.print("Enter password (8+ characters, uppercase, number, special character): ");
            user.setPassword(scanner.nextLine());
            if (user.checkPasswordComplexity()) break;
            System.out.println("Password does not meet the complexity requirements.");
        }
        while (true) {
            System.out.print("Enter cell number (+27 followed by 9 digits): ");
            user.setCellPhoneNumber(scanner.nextLine().trim());
            if (user.checkCellPhoneNumber()) break;
            System.out.println("Cell phone number is incorrectly formatted.");
        }
        System.out.println(user.registerUser());

        System.out.println("\n=== USER LOGIN ===");
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            loggedIn = user.loginUser(username, password);
            System.out.println(user.returnLoginStatus(loggedIn));
        }

        String sender = (user.getFirstName() + " " + user.getLastName()).trim();
        Message.loadStoredMessagesFromJson();
        System.out.println("\nWelcome to QuickChat.");
        boolean running = true;
        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option (1-4): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    sendMessages(scanner, sender);
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    System.out.println("Exiting QuickChat. Goodbye!");
                    running = false;
                    break;
                case "4":
                    runStoredMessagesMenu(scanner);
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1, 2, 3, or 4.");
            }
        }
        scanner.close();
    }

    private static void sendMessages(Scanner scanner, String sender) {
        System.out.print("How many messages would you like to enter? ");
        int count;
        try {
            count = Integer.parseInt(scanner.nextLine().trim());
            if (count < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid non-negative whole number.");
            return;
        }

        for (int i = 0; i < count; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + count + " ---");
            Message message = new Message();
            message.setSender(sender);
            message.setMessageNumber(Message.getMessageIds().length + 1);

            while (true) {
                System.out.print("Recipient cell number: ");
                String recipient = scanner.nextLine().trim();
                String validation = message.checkRecipientCell(recipient);
                System.out.println(validation);
                if (validation.startsWith("Cell phone number successfully")) {
                    message.setRecipientCell(recipient);
                    break;
                }
            }
            while (true) {
                System.out.print("Message (maximum 250 characters): ");
                String text = scanner.nextLine();
                String validation = message.checkMessageLength(text);
                System.out.println(validation);
                if (validation.equals("Message ready to send.")) {
                    message.setMessageText(text);
                    break;
                }
            }

            int action;
            while (true) {
                System.out.println("1) Send Message");
                System.out.println("2) Disregard Message");
                System.out.println("3) Store Message");
                System.out.print("Choose action (1-3): ");
                try {
                    action = Integer.parseInt(scanner.nextLine().trim());
                    if (action >= 1 && action <= 3) break;
                } catch (NumberFormatException ignored) { }
                System.out.println("Choose 1, 2, or 3.");
            }
            System.out.println(message.SentMessage(action));
            if (action == 1) {
                System.out.println("\n--- Message Details ---");
                System.out.println(message.printMessages());
            } else if (action == 2) {
                System.out.println("Message disregarded and added to the disregarded messages list.");
            }
        }
        System.out.println("Total messages sent: " + Message.getTotalMessagesSent());
    }
      
    //Package visible so JUnit tests can exercise the actual console submenu.
    static void runStoredMessagesMenu(Scanner scanner){
        boolean inMenu = true;
        while (inMenu){
            System.out.println("\n=== STORED MESSAGES ===");
            System.out.println("1) Display sender and recipient of stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search by message ID");
            System.out.println("4) Search stored messages by recipient");
            System.out.println("5) Delete a stored message by hash");
            System.out.println("6) Display full stored messages report");
            System.out.println("7) Return to main menu");
            System.out.print("Choose an option (1-7): ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.println(Message.displaySenderAndRecipient());
                    break;
                case "2":
                    System.out.println("Longest stored message: " + Message.getLongestStoredMessage());
                    break;
                case "3":
                    System.out.print("Enter message ID: ");
                    System.out.println(Message.searchByMessageId(scanner.nextLine().trim()));
                    break;
                case "4":
                    System.out.print("Enter recipient number: ");
                    System.out.println(Message.searchByRecipient(scanner.nextLine().trim()));
                    break;
                case "5":
                    System.out.print("Enter message hash: ");
                    System.out.println(Message.deleteMessageByHash(scanner.nextLine().trim()));
                    break;
                case "6":
                    System.out.println(Message.displayStoredMessagesReport());
                    break;
                case "7":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose 1 to 7.");
            }
        }
    }
}
        
    

