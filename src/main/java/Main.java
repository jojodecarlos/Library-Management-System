// File: src/main/java/com/library/lms/Main.java
package com.library.lms;

import com.library.lms.repository.PatronRepository;
import com.library.lms.ui.ConsoleUI;

/**
 * Entry point for the Library Management System (LMS) application.
 */
public class Main {
    /**
     * Launches the LMS console interface.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        PatronRepository repository = new PatronRepository();
        ConsoleUI console = new ConsoleUI(repository);
        console.start();
    }
}
