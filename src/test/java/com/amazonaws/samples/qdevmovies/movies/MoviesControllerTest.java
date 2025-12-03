package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ahoy matey! Test class for the MoviesController including the new search functionality.
 * These tests ensure our treasure hunting endpoints work properly, arrr!
 */
public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MovieService mockMovieService;
    private ReviewService mockReviewService;

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create enhanced mock services with search functionality
        mockMovieService = new MovieService() {
            @Override
            public List<Movie> getAllMovies() {
                return Arrays.asList(
                    new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5),
                    new Movie(2L, "Action Movie", "Action Director", 2022, "Action", "Action description", 110, 4.0),
                    new Movie(3L, "Family Drama", "Family Director", 2021, "Drama/Family", "Family description", 130, 4.8)
                );
            }
            
            @Override
            public Optional<Movie> getMovieById(Long id) {
                if (id == 1L) {
                    return Optional.of(new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5));
                } else if (id == 2L) {
                    return Optional.of(new Movie(2L, "Action Movie", "Action Director", 2022, "Action", "Action description", 110, 4.0));
                }
                return Optional.empty();
            }
            
            @Override
            public List<Movie> searchMovies(String name, Long id, String genre) {
                List<Movie> allMovies = getAllMovies();
                List<Movie> results = new ArrayList<>();
                
                for (Movie movie : allMovies) {
                    boolean matches = true;
                    
                    if (id != null && id > 0) {
                        matches = movie.getId() == id;
                    }
                    
                    if (matches && name != null && !name.trim().isEmpty()) {
                        matches = movie.getMovieName().toLowerCase().contains(name.toLowerCase());
                    }
                    
                    if (matches && genre != null && !genre.trim().isEmpty()) {
                        matches = movie.getGenre().toLowerCase().contains(genre.toLowerCase());
                    }
                    
                    if (matches) {
                        results.add(movie);
                    }
                }
                
                return results;
            }
            
            @Override
            public List<Movie> searchMoviesByName(String name) {
                return searchMovies(name, null, null);
            }
            
            @Override
            public List<Movie> searchMoviesByGenre(String genre) {
                return searchMovies(null, null, genre);
            }
            
            @Override
            public boolean isValidSearchRequest(String name, Long id, String genre) {
                boolean hasValidName = name != null && !name.trim().isEmpty();
                boolean hasValidId = id != null && id > 0;
                boolean hasValidGenre = genre != null && !genre.trim().isEmpty();
                return hasValidName || hasValidId || hasValidGenre;
            }
        };
        
        mockReviewService = new ReviewService() {
            @Override
            public List<Review> getReviewsForMovie(long movieId) {
                return new ArrayList<>();
            }
        };
        
        // Inject mocks using reflection
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    // Original tests
    @Test
    @DisplayName("Test getting all movies - should return movies template")
    public void testGetMovies() {
        String result = moviesController.getMovies(model);
        assertNotNull(result);
        assertEquals("movies", result);
    }

    @Test
    @DisplayName("Test getting movie details - should return movie-details template")
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        assertNotNull(result);
        assertEquals("movie-details", result);
    }

    @Test
    @DisplayName("Test getting movie details for non-existent movie - should return error template")
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        assertNotNull(result);
        assertEquals("error", result);
    }

    @Test
    @DisplayName("Test movie service integration - should work with mock service")
    public void testMovieServiceIntegration() {
        List<Movie> movies = mockMovieService.getAllMovies();
        assertEquals(3, movies.size());
        assertEquals("Test Movie", movies.get(0).getMovieName());
    }

    // New REST API search tests
    @Test
    @DisplayName("Arrr! Test REST search by name - should return JSON response")
    public void testSearchMoviesRest_ByName_ShouldReturnJsonResponse() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMovies("Test", null, null);
        
        assertNotNull(response, "Response should not be null, matey!");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return OK status!");
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body, "Response body should not be null!");
        assertTrue((Boolean) body.get("success"), "Search should be successful!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertNotNull(movies, "Movies list should not be null!");
        assertFalse(movies.isEmpty(), "Should find at least one movie!");
    }

    @Test
    @DisplayName("Test REST search by ID - should return specific movie")
    public void testSearchMoviesRest_ById_ShouldReturnSpecificMovie() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMovies(null, 1L, null);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        Map<String, Object> body = response.getBody();
        assertTrue((Boolean) body.get("success"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertEquals(1, movies.size(), "Should return exactly one movie!");
        assertEquals(1L, movies.get(0).getId(), "Should return movie with correct ID!");
    }

    @Test
    @DisplayName("Test REST search with no parameters - should return bad request")
    public void testSearchMoviesRest_NoParameters_ShouldReturnBadRequest() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMovies(null, null, null);
        
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        Map<String, Object> body = response.getBody();
        assertFalse((Boolean) body.get("success"));
        assertNotNull(body.get("message"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertTrue(movies.isEmpty(), "Should return empty movie list!");
    }

    @Test
    @DisplayName("Test HTML form search by name - should return movies template with results")
    public void testSearchMoviesForm_ByName_ShouldReturnMoviesTemplate() {
        String result = moviesController.searchMoviesForm("Test", null, null, model);
        
        assertNotNull(result);
        assertEquals("movies", result);
        
        // Check that search results are added to model
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("searchMessage"));
        assertTrue(model.containsAttribute("searchName"));
    }
}
