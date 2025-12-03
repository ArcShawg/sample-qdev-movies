package com.amazonaws.samples.qdevmovies.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private static final Logger logger = LogManager.getLogger(MovieService.class);
    private final List<Movie> movies;
    private final Map<Long, Movie> movieMap;

    public MovieService() {
        this.movies = loadMoviesFromJson();
        this.movieMap = new HashMap<>();
        for (Movie movie : movies) {
            movieMap.put(movie.getId(), movie);
        }
    }

    private List<Movie> loadMoviesFromJson() {
        List<Movie> movieList = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("movies.json");
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
                String jsonContent = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                JSONArray moviesArray = new JSONArray(jsonContent);
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObj = moviesArray.getJSONObject(i);
                    movieList.add(new Movie(
                        movieObj.getLong("id"),
                        movieObj.getString("movieName"),
                        movieObj.getString("director"),
                        movieObj.getInt("year"),
                        movieObj.getString("genre"),
                        movieObj.getString("description"),
                        movieObj.getInt("duration"),
                        movieObj.getDouble("imdbRating")
                    ));
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load movies from JSON: {}", e.getMessage());
        }
        return movieList;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(movieMap.get(id));
    }

    /**
     * Ahoy matey! Search for treasure (movies) in our vast collection using various criteria.
     * This method be the main search crew member that coordinates all search operations.
     * 
     * @param name The movie name to search for (partial matches allowed, case-insensitive)
     * @param id The specific movie ID to find
     * @param genre The genre to filter by (partial matches allowed, case-insensitive)
     * @return List of movies matching the search criteria, or empty list if no treasure found
     */
    public List<Movie> searchMovies(String name, Long id, String genre) {
        logger.info("Arrr! Starting treasure hunt with name='{}', id='{}', genre='{}'", name, id, genre);
        
        List<Movie> searchResults = new ArrayList<>(movies);
        
        // Filter by ID first, as it be the most specific search, matey!
        if (id != null && id > 0) {
            logger.debug("Searching for specific treasure with ID: {}", id);
            Optional<Movie> movieById = getMovieById(id);
            searchResults = movieById.map(List::of).orElse(new ArrayList<>());
        }
        
        // Filter by movie name if provided, ye scallywag!
        if (name != null && !name.trim().isEmpty()) {
            String searchName = name.trim().toLowerCase();
            logger.debug("Filtering treasure by name containing: '{}'", searchName);
            searchResults = searchResults.stream()
                .filter(movie -> movie.getMovieName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
        }
        
        // Filter by genre if provided, arrr!
        if (genre != null && !genre.trim().isEmpty()) {
            String searchGenre = genre.trim().toLowerCase();
            logger.debug("Filtering treasure by genre containing: '{}'", searchGenre);
            searchResults = searchResults.stream()
                .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
                .collect(Collectors.toList());
        }
        
        logger.info("Treasure hunt complete! Found {} movies matching yer criteria", searchResults.size());
        return searchResults;
    }

    /**
     * Search movies by name only, for when ye know what treasure ye be looking for!
     * 
     * @param name The movie name to search for (partial matches allowed, case-insensitive)
     * @return List of movies with names containing the search term
     */
    public List<Movie> searchMoviesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            logger.warn("Arrr! Empty name provided for search, returning empty treasure chest");
            return new ArrayList<>();
        }
        
        return searchMovies(name, null, null);
    }

    /**
     * Search movies by genre only, for when ye want to explore a specific type of adventure!
     * 
     * @param genre The genre to filter by (partial matches allowed, case-insensitive)
     * @return List of movies in the specified genre
     */
    public List<Movie> searchMoviesByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            logger.warn("Arrr! Empty genre provided for search, returning empty treasure chest");
            return new ArrayList<>();
        }
        
        return searchMovies(null, null, genre);
    }

    /**
     * Validate search parameters to ensure they be seaworthy, matey!
     * 
     * @param name The movie name parameter
     * @param id The movie ID parameter
     * @param genre The genre parameter
     * @return true if at least one valid search parameter is provided
     */
    public boolean isValidSearchRequest(String name, Long id, String genre) {
        boolean hasValidName = name != null && !name.trim().isEmpty();
        boolean hasValidId = id != null && id > 0;
        boolean hasValidGenre = genre != null && !genre.trim().isEmpty();
        
        boolean isValid = hasValidName || hasValidId || hasValidGenre;
        
        if (!isValid) {
            logger.warn("Arrr! No valid search parameters provided - all parameters be empty or invalid!");
        }
        
        return isValid;
    }
}
