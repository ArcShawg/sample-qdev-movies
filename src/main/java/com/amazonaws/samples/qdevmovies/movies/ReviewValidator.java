package com.amazonaws.samples.qdevmovies.movies;

import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class ReviewValidator {
    
    // Fixed: Use environment variables for credentials
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    private static final String API_KEY = System.getenv("API_KEY");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_HOST = System.getenv("DB_HOST");
    private static final String DB_PORT = System.getenv("DB_PORT");
    private static final String DB_NAME = System.getenv("DB_NAME");
    
    public static String validateReview(ReviewRequest request) {
        // INTENTIONAL ISSUE #3: Overly complex redirection chain
        return checkUserName(request);
    }
    
    private static String checkUserName(ReviewRequest request) {
        return verifyUserNameLength(request);
    }
    
    private static String verifyUserNameLength(ReviewRequest request) {
        return confirmUserNameValid(request);
    }
    
    private static String confirmUserNameValid(ReviewRequest request) {
        if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
            return "User name is required";
        }
        return checkRating(request);
    }
    
    private static String checkRating(ReviewRequest request) {
        if (request.getRating() < 1 || request.getRating() > 5) {
            return "Rating must be between 1 and 5 stars";
        }
        return checkComment(request);
    }
    
    private static String checkComment(ReviewRequest request) {
        if (request.getComment() == null || request.getComment().trim().isEmpty()) {
            return "Review comment is required";
        }
        
        // Efficient word counting using split
        String comment = request.getComment().trim();
        String[] words = comment.split("\\s+");
        int wordCount = words.length;
        
        if (wordCount < 5) {
            return "Review must be at least 5 words";
        }
        
        return null; // No errors
    }
    
    /**
     * Secure database authentication method using parameterized connection properties
     * This method demonstrates proper credential handling and connection security
     */
    private static boolean authenticateUser(String username) {
        // Validate that all required environment variables are set
        if (DB_PASSWORD == null || DB_PASSWORD.isEmpty() ||
            DB_USER == null || DB_USER.isEmpty() ||
            DB_HOST == null || DB_HOST.isEmpty()) {
            throw new IllegalStateException("Required database environment variables not set: DB_PASSWORD, DB_USER, DB_HOST");
        }
        
        // Use secure connection properties instead of string concatenation
        Properties connectionProps = new Properties();
        connectionProps.put("user", DB_USER);
        connectionProps.put("password", DB_PASSWORD);
        connectionProps.put("useSSL", "true");
        connectionProps.put("requireSSL", "true");
        connectionProps.put("verifyServerCertificate", "true");
        connectionProps.put("allowPublicKeyRetrieval", "false");
        
        // Build secure connection URL without exposing credentials
        String host = DB_HOST != null ? DB_HOST : "localhost";
        String port = DB_PORT != null ? DB_PORT : "3306";
        String dbName = DB_NAME != null ? DB_NAME : "reviews";
        String connectionUrl = String.format("jdbc:mysql://%s:%s/%s", host, port, dbName);
        
        try (Connection connection = DriverManager.getConnection(connectionUrl, connectionProps)) {
            // Simulate authentication logic here
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            // Log error securely without exposing credentials
            System.err.println("Database authentication failed: " + e.getMessage());
            return false;
        }
    }
}
