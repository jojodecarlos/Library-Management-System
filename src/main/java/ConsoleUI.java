// File: src/main/java/com/library/lms/ui/ConsoleUI.java
package com.library.lms.ui;

import com.library.lms.Config;
import com.library.lms.exception.ValidationException;
import com.library.lms.model.Patron;
import com.library.lms.repository.PatronRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Console UI for interacting with the LMS, with ID treated as String.
 */
public class ConsoleUI {
    private final PatronRepository repo;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(PatronRepository repo) {
        this.repo = repo;
    }

    /** Main loop displaying menu and handling choices. */
    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1:
                    loadFile();
                    break;
                case 2:
                    addPatron();
                    break;
                case 3:
                    removePatron();
                    break;
                case 4:
                    listPatrons();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        System.out.println("Exiting LMS. Goodbye.");
    }

    private void displayMenu() {
        System.out.println("\n1. Load patrons from file");
        System.out.println("2. Add new patron");
        System.out.println("3. Remove patron");
        System.out.println("4. List all patrons");
        System.out.println("5. Exit");
    }

    private void loadFile() {
        System.out.print("File path: ");
        String path = scanner.nextLine();
        try {
            repo.loadFromFile(path);
            listPatrons();
        } catch (IOException e) {
            System.err.println("Load failed: " + e.getMessage());
        }
    }

    private void addPatron() {
        try {
            String id = readId("Enter ID (" + Config.ID_LENGTH + " digits): ");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Address: ");
            String address = scanner.nextLine();
            BigDecimal fine = readBigDecimal("Fine 0.00-250.00: ");
            repo.add(new Patron(id, name, address, fine));
            listPatrons();
        } catch (ValidationException e) {
            System.err.println(e.getMessage());
        }
    }

    private void removePatron() {
        String id = readId("Enter ID to remove: ");
        boolean removed = repo.removeById(id);
        System.out.println(removed ? "Removed." : "Not found.");
        listPatrons();
    }

    private void listPatrons() {
        List<Patron> all = repo.getAll();
        if (all.isEmpty()) {
            System.out.println("No patrons.");
            return;
        }
        System.out.println("ID | Name | Address | Fine");
        all.forEach(System.out::println);
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid int.");
            }
        }
    }

    private BigDecimal readBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return new BigDecimal(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid decimal.");
            }
        }
    }

    private String readId(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches("\\d{" + Config.ID_LENGTH + "}")) {
                return input;
            }
            System.out.println("Invalid ID. Must be exactly " + Config.ID_LENGTH + " digits.");
        }
    }
}
