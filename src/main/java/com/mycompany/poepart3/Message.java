/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poepart3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilterWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents my QuickChat message and manages the Part 3 message collections
 */
public class Message {
    private String messageId;
    private int messageNumber;
    private String recipientCell;
    private String messageText;
    private String messageHash;
    private String status;
    private String sender;
    
    private static int totalMessagesSent;
    private static File storageFile = new File("stored_messages.json");
    
    static final ArrayList<Message> sentMessagesList = new ArrayList<>();
    static final ArrayList<Message> disregardedMessagesList = new ArrayList<>();
    static final ArrayList<Message> storedMessagesList = new ArrayList<>();
    static final ArrayList<String> messageHashesList = new ArrayList<>();
    static final ArrayList<String> messageIdsList = new ArrayList<>();

    public Message(){
        this(generateMessageId(), 0, " "," "," ","QuickChat User");
    }
    
   public Message(int messageNumber, String recipientCell, String messageText){
       this(generateMessageId(), messageNumber, recipientCell, messageText," ", "QuickChat User");
   }
   
   public Message(String messageId, int messageNumber, String recipientCell
                  String messageText, String status, String sender){
       this.messageId = messageId == null ? " " : messageId;
       this.messageNumber = messageNumber;
       this.recipientCell = recipientCell == null ? " " : recipientCell;
       this.messageText = messageText == null ? " " : messageText;
       this.status = status == null ? " " : status; 
       this.sender = sender == null ? "QuickChat User" : sender; 
       this.messageHash = createMessageHash();
   }
   
   //Generates a ten-digit message ID using string manipulation.
   public static String generateMessageId(){
       Random random = new Random();
       StringBuilder id = new StringBuilder();
       for (int i=0; i <10; i++){
           id.append(random.nextInt(10));
       }
       return id.toString();
   }
   
   public Boolean checkMessageId(){
       return checkMessageId(messageId);
   }
   
   public Boolean checkMessageId(String id){
       return id ! = null && id.matches("\\d{10}");
   }
   
   public String getMessageIdNotification(){
       return "Message ID generated: "+ messageId;
   }
   
   public String checkRecipientCell(){
       return checkRecipientCell(recipientCell);
   }
   
   //Accepts a South African +27 number, +27-prefixed number, or local 0-prefixed number.
    public String checkRecipientCell(String cellNumber) {
        if (cellNumber != null && cellNumber.trim().matches("(?:\\+27|27)\\d{9}|0\\d{9}")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String checkMessageLength() {
        return checkMessageLength(messageText);
    }

    public String checkMessageLength(String text) {
        if (text == null || text.length() <= 250) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (text.length() - 250) + "; please reduce the size.";
    }
    
    //Hash Format: first 2 ID digits, message number, then first and last words. 
     public String createMessageHash() {
        String firstTwoDigits = messageId != null && messageId.length() >= 2
                ? messageId.substring(0, 2) : "00";
        String cleanText = messageText == null
                ? "" : messageText.replaceAll("[^a-zA-Z0-9\\s]", " ").trim();
        String[] words = cleanText.isEmpty() ? new String[0] : cleanText.split("\\s+");
        String firstWord = words.length == 0 ? "" : words[0].toUpperCase();
        String lastWord = words.length == 0 ? "" : words[words.length - 1].toUpperCase();
        return firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord;
    }
     
     public String createMessageHash(String id, int number, String text){
         String oldId = messageId;
         int oldNumber = messageNumber;
         String oldText = messageText;
         messageId = id == null ? " " : id;
         messageNumber = number;
         messageText = text == null ? " ": text;
         String hash = createMessageHash();
         messageId = oldId;
         messageNumber = oldNumber;
         messageText = oldText;
         return hash;
     }
     
     //Records a message as sent, disregarded, or stored.
     public String SentMessage(int choice) {
        if (choice == 1) {
            status = "Sent";
            totalMessagesSent++;
            addMessageToArrays(this);
            return "Message successfully sent.";
        }
        if (choice == 2) {
            status = "Disregarded";
            addMessageToArrays(this);
            return "Message disregarded.";
        }
        if (choice == 3) {
            status = "Stored";
            addMessageToArrays(this);
            saveAllStoredMessagesToJson();
            return "Message successfully stored.";
        }
        return "Invalid option selected.";
    }

    public String sentMessage(int choice) {
        return SentMessage(choice);
    }

    public String printMessages() {
        return "Message ID: " + messageId + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipientCell + "\n"
                + "Message: " + messageText;
    }
    
    public int returnTotalMessages(){ return totalMessagesSent;}
    public static int getTotalMessagesSent() {return totalMessagesSent; }

    //Part 3 collections and array accessors
    public static void addMessageToArrays(Message message) {
        if (message == null) return;
        addUnique(messageIdsList, message.messageId);
        addUnique(messageHashesList, message.messageHash);
        if ("Sent".equalsIgnoreCase(message.status)) addUniqueMessage(sentMessagesList, message);
        if ("Disregarded".equalsIgnoreCase(message.status)) addUniqueMessage(disregardedMessagesList, message);
        if ("Stored".equalsIgnoreCase(message.status)) addUniqueMessage(storedMessagesList, message);
    }

    private static void addUnique(ArrayList<String> list, String value) {
        if (!list.contains(value)) list.add(value);
    }

    private static void addUniqueMessage(ArrayList<Message> list, Message message) {
        for (Message item : list) {
            if (item.messageId.equals(message.messageId)) return;
        }
        list.add(message);
    }
    
    public static void clearAllArrays(){
        sentMessagesList.clear();
        disregardMessagesList.clear();
        storedMessagesList.clear();
        messagesHashesList.clear();
        messagesIdsList.clear();
        int totalMessageSent = 0; 
    }

    public static String[] getSentMessages() { return texts(sentMessagesList); }
    public static String[] getDisregardedMessages() { return texts(disregardedMessagesList); }
    public static String[] getStoredMessages() { return texts(storedMessagesList); }
    public static String[] getMessageHashes() { return messageHashesList.toArray(new String[0]); }
    public static String[] getMessageIds() { return messageIdsList.toArray(new String[0]); }

    private static String[] texts(ArrayList<Message> messages) {
        String[] result = new String[messages.size()];
        for (int i = 0; i < messages.size(); i++) result[i] = messages.get(i).messageText;
        return result;
    }
    
    public static String displaySenderAndRecipient() {
        if (storedMessagesList.isEmpty()) return "No stored messages found.";
        StringBuilder result = new StringBuilder("=== STORED MESSAGES: SENDER & RECIPIENT ===\n");
        for (Message message : storedMessagesList) {
            result.append("Sender: ").append(message.sender).append("\n")
                  .append("Recipient: ").append(message.recipientCell).append("\n\n");
        }
        return result.toString().trim();
    }
    
    public static String getLongestStoredMessage() {
        if (storedMessagesList.isEmpty()) return "No stored messages available.";
        Message longest = storedMessagesList.get(0);
        for (Message message : storedMessagesList) {
            if (message.messageText.length() > longest.messageText.length()) longest = message;
        }
        return longest.messageText;
    }
    
     public static String searchByMessageId(String id) {
        if (id == null || id.trim().isEmpty()) return "Invalid message ID.";
        for (Message message : allMessages()) {
            if (message.messageId.equals(id.trim())) {
                return "Recipient: " + message.recipientCell + "\nMessage: " + message.messageText;
            }
        }
        return "Message ID not found.";
    }
     
     //Returns the text of every stored message for the requested recipient.
     public static String searchByRecipient(String recipient) {
        if (recipient == null || recipient.trim().isEmpty()) return "Invalid recipient number.";
        String query = normalizePhone(recipient);
        StringBuilder result = new StringBuilder();
        ArrayList<Message> searchable = new ArrayList<>();
        searchable.addAll(sentMessagesList);
        searchable.addAll(storedMessagesList);
        for (Message message : searchable) {
            if (normalizePhone(message.recipientCell).equals(query)) {
                if (result.length() > 0) result.append("\n");
                result.append(message.messageText);
            }
        }
        return result.length() == 0 ? "No messages found for recipient: " + recipient : result.toString();
    }

    private static String normalizePhone(String phone) {
        String digits = phone.replaceAll("\\D", "");
        if (digits.startsWith("27") && digits.length() == 11) return "0" + digits.substring(2);
        return digits;
    }
    
    public static String deleteMessageByHash(String hash) {
        if (hash == null || hash.trim().isEmpty()) return "Invalid message hash.";
        for (int i = 0; i < storedMessagesList.size(); i++) {
            Message message = storedMessagesList.get(i);
            if (message.messageHash.equals(hash.trim())) {
                storedMessagesList.remove(i);
                rebuildIndexLists();
                saveAllStoredMessagesToJson();
                return "Message: \"" + message.messageText + "\" successfully deleted.";
            }
        }
        return "Message with hash " + hash + " not found.";
    }
    
    public static String displayStoredMessagesReport() {
        if (storedMessagesList.isEmpty()) return "No stored messages to display in report.";
        StringBuilder result = new StringBuilder("=== STORED MESSAGES REPORT ===\n");
        for (Message message : storedMessagesList) {
            result.append("Message ID: ").append(message.messageId).append("\n")
                  .append("Message Hash: ").append(message.messageHash).append("\n")
                  .append("Sender: ").append(message.sender).append("\n")
                  .append("Recipient: ").append(message.recipientCell).append("\n")
                  .append("Message: ").append(message.messageText).append("\n")
                  .append("Status: ").append(message.status).append("\n")
                  .append("------------------------------\n");
        }
        return result.toString().trim();
    }

    private static ArrayList<Message> allMessages() {
        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessagesList);
        all.addAll(disregardedMessagesList);
        all.addAll(storedMessagesList);
        return all;
    }

    private static void rebuildIndexLists() {
        messageIdsList.clear();
        messageHashesList.clear();
        for (Message message : allMessages()) {
            addUnique(messageIdsList, message.messageId);
            addUnique(messageHashesList, message.messageHash);
        }
    }
    
    //Determining Part 3 rubric data for unit tests.
    public static void populateTestData() {
        clearAllArrays();
        Message one = new Message("1000000001", 0, "27834557896", "Did you get the cake?", "Sent", "Ofentse Mole");
        Message two = new Message("2000000002", 1, "27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored", "Ofentse Mole");
        Message three = new Message("3000000003", 2, "27834284567", "Yohoooo, I am at your gate.", "Disregarded", "Ofentse Mole");
        Message four = new Message("4000000004", 3, "0838884567", "It is dinner time!", "Sent", "Ofentse Mole");
        Message five = new Message("5000000005", 4, "27838884567", "OK, I am leaving without you", "Stored", "Ofentse Mole");
        addMessageToArrays(one);
        addMessageToArrays(two);
        addMessageToArrays(three);
        addMessageToArrays(four);
        addMessageToArrays(five);
        totalMessagesSent = 2;
    }
    
    //JSON array persistence
    public void storeMessage(){
        status = "Stored";
        addMessageToArrays(this);
        saveAllStoredMessagesToJson();
    }
    
     public static void saveAllStoredMessagesToJson() {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessagesList.size(); i++) {
            Message m = storedMessagesList.get(i);
            json.append("  {\n")
                .append("    \"messageId\": \"").append(escapeJson(m.messageId)).append("\",\n")
                .append("    \"messageNumber\": ").append(m.messageNumber).append(",\n")
                .append("    \"recipientCell\": \"").append(escapeJson(m.recipientCell)).append("\",\n")
                .append("    \"messageText\": \"").append(escapeJson(m.messageText)).append("\",\n")
                .append("    \"messageHash\": \"").append(escapeJson(m.messageHash)).append("\",\n")
                .append("    \"status\": \"Stored\",\n")
                .append("    \"sender\": \"").append(escapeJson(m.sender)).append("\"\n")
                .append("  }");
            if (i < storedMessagesList.size() - 1) json.append(',');
            json.append('\n');
        }
        json.append("]\n");
        try (FileWriter writer = new FileWriter(storageFile, false)) {
            writer.write(json.toString());
        } catch (IOException e) {
            System.err.println("Error storing messages to JSON: " + e.getMessage());
        }
    }
     
     public static void loadStoredMessagesFromJson() {
        File file = storageFile;
        if (!file.isFile()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) content.append(line).append('\n');
            for (String object : extractJsonObjects(content.toString())) {
                String id = jsonString(object, "messageId");
                if (id.isEmpty()) continue;
                int number = 0;
                try { number = Integer.parseInt(jsonNumber(object, "messageNumber")); }
                catch (NumberFormatException ignored) { }
                String recipient = jsonString(object, "recipientCell");
                String text = jsonString(object, "messageText");
                String status = jsonString(object, "status");
                String sender = jsonString(object, "sender");
                Message message = new Message(id, number, recipient, text,
                        status.isEmpty() ? "Stored" : status,
                        sender.isEmpty() ? "QuickChat User" : sender);
                String hash = jsonString(object, "messageHash");
                if (!hash.isEmpty()) message.messageHash = hash;
                addMessageToArrays(message);
            }
        } catch (IOException e) {
            System.err.println("Error loading stored messages from JSON: " + e.getMessage());
        }
    }

     private static ArrayList<String> extractJsonObjects(String json) {
        ArrayList<String> objects = new ArrayList<>();
        boolean inString = false;
        boolean escaped = false;
        int depth = 0;
        int start = -1;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (inString) {
                if (escaped) escaped = false;
                else if (c == '\\') escaped = true;
                else if (c == '"') inString = false;
            } else if (c == '"') {
                inString = true;
            } else if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}' && depth > 0) {
                depth--;
                if (depth == 0 && start >= 0) objects.add(json.substring(start, i + 1));
            }
        }
        return objects;
    }
     
     private static String jsonString(String object, String key) {
        Pattern pattern = Pattern.compile("\\\"" + Pattern.quote(key)
                + "\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"\\\\])*)\\\"");
        Matcher matcher = pattern.matcher(object);
        return matcher.find() ? unescapeJson(matcher.group(1)) : "";
    }

      private static String jsonNumber(String object, String key) {
        Pattern pattern = Pattern.compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*(-?\\d+)");
        Matcher matcher = pattern.matcher(object);
        return matcher.find() ? matcher.group(1) : "0";
    }
      
      private static String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

      private static String unescapeJson(String value) {
        StringBuilder result = new StringBuilder();
        boolean escaped = false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (escaped) {
                if (c == 'n') result.append('\n');
                else if (c == 'r') result.append('\r');
                else if (c == 't') result.append('\t');
                else result.append(c);
                escaped = false;
            } else if (c == '\\') escaped = true;
            else result.append(c);
        }
        if (escaped) result.append('\\');
        return result.toString();
    }
      
       public static void setStorageFile(File file) {
        if (file == null) throw new IllegalArgumentException("Storage file cannot be null.");
        storageFile = file;
    }
       
        public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; this.messageHash = createMessageHash(); }
    public int getMessageNumber() { return messageNumber; }
    public void setMessageNumber(int messageNumber) { this.messageNumber = messageNumber; this.messageHash = createMessageHash(); }
    public String getRecipientCell() { return recipientCell; }
    public void setRecipientCell(String recipientCell) { this.recipientCell = recipientCell == null ? "" : recipientCell; }
    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText == null ? "" : messageText; this.messageHash = createMessageHash(); }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status == null ? "" : status; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender == null ? "QuickChat User" : sender; }
}
