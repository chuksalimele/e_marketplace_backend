// Role.java
package com.marketplace.emarketplacebackend.model;

import jakarta.persistence.*; // Essential JPA annotations
import lombok.Data;          // Lombok: Generates boilerplate code (getters/setters/etc.)
import lombok.NoArgsConstructor; // Lombok: Generates no-argument constructor
import lombok.AllArgsConstructor; // Lombok: Generates constructor with all fields

@Entity // Marks this class as a JPA entity, meaning it maps to a database table
@Table(name = "roles") // Specifies the actual table name in the database as "roles"
                       // (Using "roles" instead of default "role" to avoid potential SQL keyword conflicts)
@Data // From Lombok: Automatically creates getters, setters, toString(), equals(), and hashCode() methods
@NoArgsConstructor // From Lombok: Creates an empty constructor, required by JPA
@AllArgsConstructor // From Lombok: Creates a constructor with all fields, useful for creation
public class Role {

    @Id // Marks this field as the primary key of the table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tells the database to auto-increment this ID
    private Long id; // Unique identifier for each role

    @Enumerated(EnumType.STRING) // Specifies that the `name` enum value should be stored as its String representation
                                 // (e.g., "ROLE_USER" instead of an integer 0, 1, etc.)
    @Column(length = 20, unique = true) // Defines column properties: max length 20, and ensures each role name is unique
    private ERole name; // The specific name of the role, chosen from our predefined enum

    // An Enum (short for enumeration) defines a fixed set of constants.
    // This ensures we only have specific, valid roles like USER, SELLER, ADMIN.
    public enum ERole {
        ROLE_USER,   // Standard user role (e.g., a buyer)
        ROLE_SELLER, // User with seller permissions
        ROLE_ADMIN   // Administrator role with full access
    }

    // A convenience constructor for creating a Role object with just its name
    public Role(ERole name) {
        this.name = name;
    }
}