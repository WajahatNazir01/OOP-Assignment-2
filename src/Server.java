import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server implements Runnable {
    private ServerSocket serverSocket;
    private PrintWriter out;
    private List<Contact> contacts = new ArrayList<>();
    private List<Sms> messages=new ArrayList<>();


    public static void main(String[] args) {
        new Server(12346);

    }


    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            advanceContacts();
            new Thread(this).start(); // Start server thread
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void advanceContacts() {
        contacts.add(new Contact("1", "Ahmad", "03039812367"));
        contacts.add(new Contact("2", "Wajahat", "0317080150"));
    }

    private void MainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Contacts");
        System.out.println("2. Send Message");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");
    }

    private void ContactsMenu() {
        System.out.println("\n--- Contacts ---");
        System.out.println("1. Add Contact");
        System.out.println("2. Delete Contact");
        System.out.println("3. View Contacts");
        System.out.println("4. Go Back");
        System.out.print("Enter choice: ");
    }



    private void SendMessage() {
        System.out.println("\n--- Send Message ---");
        System.out.println("1. Select Contact");
        System.out.println("2. Start New Chat");
        System.out.println("3. Go Back");
        System.out.print("Enter choice: ");
    }

    private void MenuChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1 -> manageContacts(scanner);
            case 2 -> sendMessageMenu(scanner);
            case 3 -> System.exit(0);
            default -> System.out.println("Invalid choice. Try again.");
        }
    }

    private void manageContacts(Scanner scanner) {
        while (true) {
            ContactsMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> addContact(scanner);
                case 2 -> deleteContact(scanner);
                case 3 -> viewAllContacts();
                case 4 -> { return; }

                // Go to Main Menu
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
    private void sendMessageMenu(Scanner scanner) {
        while (true) {
            SendMessage();
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 : {
                    System.out.println("""
                            1.Press 1 to send message:
                            2.Press 2 to delete message by content:
                            3.Press 3 to display all messages for specific contact
                            """);
                    int choice2 = Integer.parseInt(scanner.nextLine());
                    switch (choice2) {
                        case 1:
                            selectContactToSendMessage(scanner);
                            break;
                        case 2:
                            deleteMessageByContent(scanner);
                            break;
                        case 3:
                            displayMessagesForContact(scanner);
                            break;
                    }
                    break;
                }
                case 2 :{ startNewChat(scanner);
                    break;}
                case 3 : { return; } // Go to Main Menu
                default :{ System.out.println("Invalid choice. Try again.");}
            }
        }
    }


//    private void sendMessageMenu(Scanner scanner) {
//        while (true) {
//            SendMessage();
//            int choice = Integer.parseInt(scanner.nextLine());
//            switch (choice) {
//                case 1 -> selectContactToSendMessage(scanner);
//                case 2 -> startNewChat(scanner);
//                case 3 -> { return; } // Go to Main Menu
//                default -> System.out.println("Invalid choice. Try again.");
//            }
//        }
//    }

    private void addContact(Scanner scanner) {
        System.out.print("Enter Contact ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Contact Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Contact Number: ");
        String number = scanner.nextLine();
        contacts.add(new Contact(id, name, number));
        System.out.println("Contact added successfully!");
    }

    private void deleteContact(Scanner scanner) {
        System.out.print("Enter Contact ID to delete: ");
        String id = scanner.nextLine();
        Contact contact = findContactById(id);
        if (contact != null) {
            contacts.remove(contact);
            System.out.println("Contact deleted successfully!");
        } else {
            System.out.println("Contact not found.");
        }
    }


    private Contact findContactById(String id) {
        for (Contact contact : contacts) {
            if (contact.getId().equals(id)) {
                return contact;
            }
        }


        return null;
    }




    private void viewAllContacts() {
        System.out.println("Contacts:");
        if (contacts.isEmpty()) {
            System.out.println("No contacts to display.");
        } else {

            for (Contact contact : contacts) {
                System.out.println(contact);
            }
        }
    }


    private void selectContactToSendMessage(Scanner scanner) {
        viewAllContacts();
        System.out.print("Enter Contact ID to send message: ");
        String id = scanner.nextLine();
        Contact contact = findContactById(id);
        if (contact != null) {
            enterMessagingMode(scanner, contact.getName());
        }
        else
        {
            System.out.println("Contact not found.");
        }
    }
    private Sms findMessageByContent(String content) {
        for (Sms message : messages) {
            if (message.getContent().equals(content)) {
                return message;
            }
        }
        return null;
    }

    private void deleteMessageByContent(Scanner scanner) {
        System.out.print("Enter message content to delete: ");
        String messageContent = scanner.nextLine();

        Sms message = findMessageByContent(messageContent);
        if (message != null) {
            messages.remove(message);
            System.out.println("Message deleted successfully!");
        } else {
            System.out.println("Message not found.");
        }
    }


    private void displayMessagesForContact(Scanner scanner) {
        System.out.print("Enter Contact ID to view messages: ");
        String contactId = scanner.nextLine();


        List<Sms> contactMessages = new ArrayList<>();
        Contact contact= findContactById(contactId);
        for (Sms message : messages) {
            if (contact.getId().equals(contactId)) {
                contactMessages.add(message);
            }
        }

        if (contactMessages.isEmpty()) {
            System.out.println("No messages found for this contact.");
        } else {
            System.out.println("\nMessages for contact ID " + contactId + ":");
            for (Sms message : contactMessages) {
                System.out.println(message);
            }
        }
    }

    private void startNewChat(Scanner scanner) {
        System.out.print("Enter New Contact Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter New Contact Number: ");
        String number = scanner.nextLine();
        String id = String.valueOf(contacts.size() + 1);
        Contact contact = new Contact(id, name, number);
        contacts.add(contact);
        System.out.println("New contact added successfully. Starting chat...");
        enterMessagingMode(scanner, contact.getName());
    }

    private void enterMessagingMode(Scanner scanner, String contactName) {
        System.out.println("Enter messages to " + contactName + " (enter '0' to exit messaging mode):");
        while (true) {

            String message = scanner.nextLine();
            if (message.equals("0")) {
                System.out.println("Exiting messaging mode...");
                break;
            }

            sendMessage(message);
        }
    }

    public void run() {

        try {
            System.out.println("Waiting for client connection...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");


            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);


            new Thread(() -> {
                try {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(contacts.getFirst().getName() +": " + inputLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


            while (true) {
                MainMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                MenuChoice(choice, scanner);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }



    }



    private void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
        messages.add(new Sms(message));
    }


}
