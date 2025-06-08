// File: src/main/java/com/library/lms/model/Patron.java
package com.library.lms.model;

import com.library.lms.Config;
import com.library.lms.exception.ValidationException;
import java.math.BigDecimal;

/**
 * Represents a library patron with a String ID, name, address, and overdue fine.
 */
public class Patron {
    private final String id;
    private final String name;
    private final String address;
    private BigDecimal fine;

    /**
     * Constructs and validates a new Patron.
     *
     * @param id      seven-character numeric string (may include leading zeros)
     * @param name    non-empty full name
     * @param address non-empty address
     * @param fine    0.00–250.00 inclusive
     */
    public Patron(String id, String name, String address, BigDecimal fine) {
        if (id == null || !id.matches("\\d{" + Config.ID_LENGTH + "}")) {
            throw new ValidationException("ID must be exactly " + Config.ID_LENGTH + " digits");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Name cannot be blank");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new ValidationException("Address cannot be blank");
        }
        if (fine.scale() > 2) {
            throw new ValidationException("Fine must have at most two decimal places");
        }
        BigDecimal min = new BigDecimal("0.00"), max = new BigDecimal("250.00");
        if (fine.compareTo(min) < 0 || fine.compareTo(max) > 0) {
            throw new ValidationException("Fine must be between 0.00 and 250.00");
        }
        this.id = id;
        this.name = name.trim();
        this.address = address.trim();
        this.fine = fine.setScale(2);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public BigDecimal getFine() { return fine; }

    public void setFine(BigDecimal fine) {
        // re-validate via temporary constructor
        new Patron(id, name, address, fine);
        this.fine = fine.setScale(2);
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s", id, name, address, fine);
    }
}
