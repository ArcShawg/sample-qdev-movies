package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ahoy matey! Test class for the MovieService search functionality.
 * These tests ensure our treasure hunting methods work properly, arrr!
 */
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    @DisplayName("Arrr! Test searching movies by name - should find treasure")
    public void testSearchMoviesByName_ValidName_ShouldReturnResults() {
        // Test partial name search (case-insensitive)
        List<Movie> results = movieService.searchMoviesByName("prison");
        
        assertNotNull(results, "Search results should not be null, matey!");
        assertFalse(results.isEmpty(), "Should find at least one movie treasure!");
        
        // Verify the result contains "The Prison Escape"
        assertTrue(results.stream().anyMatch(movie -> 
            movie.getMovieName().toLowerCase().contains("prison")),
            "Should find movie with 'prison' in the name!");
    }

    @Test
    @DisplayName("Test searching movies by name - case insensitive treasure hunt")
    public void testSearchMoviesByName_CaseInsensitive_ShouldReturnResults() {
        List<Movie> lowerCaseResults = movieService.searchMoviesByName("family");
        List<Movie> upperCaseResults = movieService.searchMoviesByName("FAMILY");
        List<Movie> mixedCaseResults = movieService.searchMoviesByName("FaMiLy");
        
        assertNotNull(lowerCaseResults, "Lowercase search should work!");
        assertNotNull(upperCaseResults, "Uppercase search should work!");
        assertNotNull(mixedCaseResults, "Mixed case search should work!");
        
        assertEquals(lowerCaseResults.size(), upperCaseResults.size(), 
            "Case should not matter in search, arrr!");
        assertEquals(lowerCaseResults.size(), mixedCaseResults.size(), 
            "Case should not matter in search, arrr!");
    }

    @Test
    @DisplayName("Test searching movies by name - empty name should return empty chest")
    public void testSearchMoviesByName_EmptyName_ShouldReturnEmpty() {
        List<Movie> emptyResults = movieService.searchMoviesByName("");
        List<Movie> nullResults = movieService.searchMoviesByName(null);
        List<Movie> whitespaceResults = movieService.searchMoviesByName("   ");
        
        assertTrue(emptyResults.isEmpty(), "Empty name should return empty results!");
        assertTrue(nullResults.isEmpty(), "Null name should return empty results!");
        assertTrue(whitespaceResults.isEmpty(), "Whitespace name should return empty results!");
    }

    @Test
    @DisplayName("Test searching movies by genre - should find adventure treasures")
    public void testSearchMoviesByGenre_ValidGenre_ShouldReturnResults() {
        List<Movie> dramaResults = movieService.searchMoviesByGenre("Drama");
        
        assertNotNull(dramaResults, "Drama search should not be null!");
        assertFalse(dramaResults.isEmpty(), "Should find drama movies!");
        
        // Verify all results contain "Drama" in genre
        assertTrue(dramaResults.stream().allMatch(movie -> 
            movie.getGenre().toLowerCase().contains("drama")),
            "All results should contain 'drama' in genre!");
    }

    @Test
    @DisplayName("Test searching movies by genre - partial match should work")
    public void testSearchMoviesByGenre_PartialMatch_ShouldReturnResults() {
        List<Movie> actionResults = movieService.searchMoviesByGenre("Action");
        
        assertNotNull(actionResults, "Action search should not be null!");
        assertFalse(actionResults.isEmpty(), "Should find action movies!");
        
        // Should find movies with "Action/Crime" or "Action/Sci-Fi"
        assertTrue(actionResults.stream().allMatch(movie -> 
            movie.getGenre().toLowerCase().contains("action")),
            "All results should contain 'action' in genre!");
    }

    @Test
    @DisplayName("Test comprehensive search with multiple parameters")
    public void testSearchMovies_MultipleParameters_ShouldReturnFilteredResults() {
        // Search for drama movies with "family" in the name
        List<Movie> results = movieService.searchMovies("family", null, "drama");
        
        assertNotNull(results, "Multi-parameter search should not be null!");
        
        if (!results.isEmpty()) {
            // Verify results match both criteria
            assertTrue(results.stream().allMatch(movie -> 
                movie.getMovieName().toLowerCase().contains("family") &&
                movie.getGenre().toLowerCase().contains("drama")),
                "Results should match both name and genre criteria!");
        }
    }

    @Test
    @DisplayName("Test search by ID - should find specific treasure")
    public void testSearchMovies_ById_ShouldReturnSpecificMovie() {
        List<Movie> results = movieService.searchMovies(null, 1L, null);
        
        assertNotNull(results, "ID search should not be null!");
        assertEquals(1, results.size(), "Should find exactly one movie by ID!");
        assertEquals(1L, results.get(0).getId(), "Should return movie with correct ID!");
    }

    @Test
    @DisplayName("Test search by invalid ID - should return empty chest")
    public void testSearchMovies_InvalidId_ShouldReturnEmpty() {
        List<Movie> results = movieService.searchMovies(null, 999L, null);
        
        assertNotNull(results, "Invalid ID search should not be null!");
        assertTrue(results.isEmpty(), "Should return empty results for invalid ID!");
    }

    @Test
    @DisplayName("Test search validation - should validate parameters properly")
    public void testIsValidSearchRequest_VariousInputs_ShouldValidateCorrectly() {
        // Valid cases
        assertTrue(movieService.isValidSearchRequest("test", null, null), 
            "Valid name should be accepted!");
        assertTrue(movieService.isValidSearchRequest(null, 1L, null), 
            "Valid ID should be accepted!");
        assertTrue(movieService.isValidSearchRequest(null, null, "drama"), 
            "Valid genre should be accepted!");
        assertTrue(movieService.isValidSearchRequest("test", 1L, "drama"), 
            "All valid parameters should be accepted!");
        
        // Invalid cases
        assertFalse(movieService.isValidSearchRequest(null, null, null), 
            "All null parameters should be rejected!");
        assertFalse(movieService.isValidSearchRequest("", null, null), 
            "Empty name should be rejected!");
        assertFalse(movieService.isValidSearchRequest("   ", null, null), 
            "Whitespace name should be rejected!");
        assertFalse(movieService.isValidSearchRequest(null, 0L, null), 
            "Zero ID should be rejected!");
        assertFalse(movieService.isValidSearchRequest(null, -1L, null), 
            "Negative ID should be rejected!");
        assertFalse(movieService.isValidSearchRequest(null, null, ""), 
            "Empty genre should be rejected!");
        assertFalse(movieService.isValidSearchRequest(null, null, "   "), 
            "Whitespace genre should be rejected!");
    }

    @Test
    @DisplayName("Test search with no matching results - should return empty chest gracefully")
    public void testSearchMovies_NoMatches_ShouldReturnEmpty() {
        List<Movie> results = movieService.searchMovies("nonexistentmovie", null, null);
        
        assertNotNull(results, "No match search should not be null!");
        assertTrue(results.isEmpty(), "Should return empty results when no matches found!");
    }

    @Test
    @DisplayName("Test existing functionality still works - getAllMovies")
    public void testGetAllMovies_ShouldReturnAllTreasures() {
        List<Movie> allMovies = movieService.getAllMovies();
        
        assertNotNull(allMovies, "All movies should not be null!");
        assertFalse(allMovies.isEmpty(), "Should have movies in the treasure chest!");
        
        // Should have at least the movies from movies.json
        assertTrue(allMovies.size() >= 10, "Should have at least 10 movies!");
    }

    @Test
    @DisplayName("Test existing functionality still works - getMovieById")
    public void testGetMovieById_ShouldReturnCorrectTreasure() {
        Optional<Movie> movie = movieService.getMovieById(1L);
        
        assertTrue(movie.isPresent(), "Should find movie with ID 1!");
        assertEquals(1L, movie.get().getId(), "Should return correct movie!");
        
        Optional<Movie> nonExistent = movieService.getMovieById(999L);
        assertFalse(nonExistent.isPresent(), "Should not find non-existent movie!");
        
        Optional<Movie> nullId = movieService.getMovieById(null);
        assertFalse(nullId.isPresent(), "Should not find movie with null ID!");
    }

    @Test
    @DisplayName("Test search performance - should handle multiple searches efficiently")
    public void testSearchPerformance_MultipleCalls_ShouldBeEfficient() {
        long startTime = System.currentTimeMillis();
        
        // Perform multiple searches
        for (int i = 0; i < 100; i++) {
            movieService.searchMovies("test", null, null);
            movieService.searchMovies(null, 1L, null);
            movieService.searchMovies(null, null, "drama");
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Should complete within reasonable time (less than 1 second for 300 searches)
        assertTrue(duration < 1000, 
            "300 searches should complete within 1 second, took: " + duration + "ms");
    }
}