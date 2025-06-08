// File: src/main/java/com/library/lms/repository/PatronRepository.java
package com.library.lms.repository;

import com.library.lms.model.Patron;
import com.library.lms.exception.ValidationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Manages in-memory storage of Patron objects.
 */
public class PatronRepository {
    private final List<Patron> patrons = new ArrayList<>();

    /**
     * Loads patrons from a text file; reports errors with line numbers.
     *
     * @param path filesystem path to the input file
     * @throws IOException if the file cannot be read
     */
    public void loadFromFile(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] parts = line.split("-", 4);
                if (parts.length != 4) {
                    System.err.printf(
                            "Line %d: malformed entry, expected 4 fields but got %d%n",
                            lineNumber, parts.length);
                    continue;
                }
                String rawId = parts[0].trim();
                String name = parts[1].trim();
                String address = parts[2].trim();
                String fineStr = parts[3].trim();
                try {
                    BigDecimal fine = new BigDecimal(fineStr);
                    add(new Patron(rawId, name, address, fine));
                } catch (NumberFormatException e) {
                    System.err.printf("Line %d: invalid number format → %s%n",
                            lineNumber, e.getMessage());
                } catch (ValidationException e) {
                    System.err.printf("Line %d: validation error → %s%n",
                            lineNumber, e.getMessage());
                }
            }
        }
    }

    /**
     * Adds a new Patron after ensuring ID uniqueness.
     *
     * @param p Patron to add
     * @throws ValidationException if ID already exists
     */
    public void add(Patron p) {
        for (Patron ex : patrons) {
            if (ex.getId().equals(p.getId())) {
                throw new ValidationException("ID already exists: " + p.getId());
            }
        }
        patrons.add(p);
    }

    /**
     * Removes a Patron by its string ID.
     *
     * @param id the ID to remove
     * @return true if removal occurred, false otherwise
     */
    public boolean removeById(String id) {
        return patrons.removeIf(p -> p.getId().equals(id));
    }

    /**
     * Returns all patrons sorted by ascending ID.
     *
     * @return unmodifiable list of patrons
     */
    public List<Patron> getAll() {
        List<Patron> sorted = new ArrayList<>(patrons);
        Collections.sort(sorted, Comparator.comparing(Patron::getId));
        return Collections.unmodifiableList(sorted);
    }
}
