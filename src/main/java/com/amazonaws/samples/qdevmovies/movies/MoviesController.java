package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model) {
        logger.info("Ahoy! Fetching all movies for the crew");
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies";
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Ahoy! Fetching details for movie treasure with ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Arrr! Movie treasure with ID {} not found in our chest", movieId);
            model.addAttribute("title", "Treasure Not Found");
            model.addAttribute("message", "Arrr! The movie treasure with ID " + movieId + " has sailed away and cannot be found, matey!");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }

    /**
     * Ahoy matey! REST endpoint for searching movie treasures.
     * This be the main search API that returns JSON responses for ye modern web applications.
     * 
     * @param name Movie name to search for (optional, partial matches allowed)
     * @param id Specific movie ID to find (optional)
     * @param genre Genre to filter by (optional, partial matches allowed)
     * @return ResponseEntity with search results or error message
     */
    @GetMapping("/movies/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchMovies(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre) {
        
        logger.info("Arrr! REST API treasure hunt initiated with name='{}', id='{}', genre='{}'", name, id, genre);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate that at least one search parameter be provided, ye scallywag!
            if (!movieService.isValidSearchRequest(name, id, genre)) {
                logger.warn("Arrr! Invalid search request - no valid parameters provided");
                response.put("success", false);
                response.put("message", "Arrr! Ye need to provide at least one search parameter, matey! Use 'name', 'id', or 'genre'.");
                response.put("movies", List.of());
                return ResponseEntity.badRequest().body(response);
            }
            
            // Perform the treasure hunt!
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            if (searchResults.isEmpty()) {
                logger.info("Arrr! No treasure found matching the search criteria");
                response.put("success", true);
                response.put("message", "Arrr! No movie treasures found matching yer search criteria, matey! Try different search terms.");
                response.put("movies", List.of());
                return ResponseEntity.ok(response);
            }
            
            logger.info("Treasure hunt successful! Found {} movies", searchResults.size());
            response.put("success", true);
            response.put("message", String.format("Ahoy! Found %d movie treasure%s matching yer search!", 
                searchResults.size(), searchResults.size() == 1 ? "" : "s"));
            response.put("movies", searchResults);
            response.put("totalResults", searchResults.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Arrr! Unexpected error during treasure hunt: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Arrr! Something went wrong during the treasure hunt, matey! Try again later.");
            response.put("movies", List.of());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * HTML form endpoint for movie search - returns the movies page with search results
     * This be for when ye want to search using the web form, arrr!
     * 
     * @param name Movie name to search for (optional)
     * @param id Specific movie ID to find (optional)
     * @param genre Genre to filter by (optional)
     * @param model Spring MVC model for the template
     * @return movies template with search results
     */
    @GetMapping("/movies/search-form")
    public String searchMoviesForm(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre,
            org.springframework.ui.Model model) {
        
        logger.info("Arrr! HTML form treasure hunt initiated with name='{}', id='{}', genre='{}'", name, id, genre);
        
        try {
            // If no search parameters provided, show all movies
            if (!movieService.isValidSearchRequest(name, id, genre)) {
                logger.info("No search parameters provided, showing all movie treasures");
                model.addAttribute("movies", movieService.getAllMovies());
                model.addAttribute("searchMessage", "Ahoy! Here be all our movie treasures. Use the search form above to find specific ones!");
                model.addAttribute("showingAll", true);
                return "movies";
            }
            
            // Perform the search
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            if (searchResults.isEmpty()) {
                logger.info("No treasures found matching search criteria");
                model.addAttribute("movies", List.of());
                model.addAttribute("searchMessage", "Arrr! No movie treasures found matching yer search criteria, matey! Try different search terms.");
                model.addAttribute("noResults", true);
            } else {
                logger.info("Found {} movie treasures matching search criteria", searchResults.size());
                model.addAttribute("movies", searchResults);
                model.addAttribute("searchMessage", String.format("Ahoy! Found %d movie treasure%s matching yer search!", 
                    searchResults.size(), searchResults.size() == 1 ? "" : "s"));
                model.addAttribute("hasResults", true);
            }
            
            // Add search parameters back to the model for form persistence
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
            
            return "movies";
            
        } catch (Exception e) {
            logger.error("Arrr! Unexpected error during HTML form treasure hunt: {}", e.getMessage(), e);
            model.addAttribute("movies", List.of());
            model.addAttribute("searchMessage", "Arrr! Something went wrong during the treasure hunt, matey! Try again later.");
            model.addAttribute("errorOccurred", true);
            return "movies";
        }
    }
}