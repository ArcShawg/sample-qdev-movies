# 🏴‍☠️ Movie Search API Documentation

Ahoy matey! This document provides comprehensive documentation for the Movie Search and Filtering API with pirate language theme.

## 🔍 Search Endpoints

### 1. REST API Search (JSON Response)

**Endpoint:** `GET /movies/search`

**Description:** Search for movie treasures using various criteria and receive JSON response.

**Query Parameters:**
- `name` (optional, string): Movie name to search for (partial matches, case-insensitive)
- `id` (optional, long): Specific movie ID to find (must be positive integer)
- `genre` (optional, string): Genre to filter by (partial matches, case-insensitive)

**Requirements:**
- At least one parameter must be provided
- All parameters are optional but cannot all be empty/null

**Response Format:**
```json
{
  "success": boolean,
  "message": "string (pirate-themed message)",
  "movies": [Movie array],
  "totalResults": number (only present on successful searches)
}
```

**HTTP Status Codes:**
- `200 OK`: Successful search (even if no results)
- `400 Bad Request`: No valid search parameters provided
- `500 Internal Server Error`: Unexpected server error

### 2. HTML Form Search

**Endpoint:** `GET /movies/search-form`

**Description:** Search for movies using HTML form and receive movies page with results.

**Query Parameters:** Same as REST API

**Response:** Returns `movies.html` template with:
- Search results displayed in movie grid
- Search form pre-filled with submitted values
- Appropriate success/error messages
- Pirate-themed UI elements

## 📊 Movie Data Model

```json
{
  "id": 1,
  "movieName": "The Prison Escape",
  "director": "John Director",
  "year": 1994,
  "genre": "Drama",
  "description": "Two imprisoned men bond over a number of years...",
  "duration": 142,
  "imdbRating": 5.0,
  "icon": "🎬"
}
```

## 🎯 Search Examples

### By Movie Name
```bash
# Find movies with "prison" in the name
curl "http://localhost:8080/movies/search?name=prison"

# Case-insensitive search
curl "http://localhost:8080/movies/search?name=FAMILY"
```

### By Genre
```bash
# Find all drama movies
curl "http://localhost:8080/movies/search?genre=drama"

# Find action movies (matches "Action/Crime", "Action/Sci-Fi")
curl "http://localhost:8080/movies/search?genre=action"
```

### By ID
```bash
# Get specific movie by ID
curl "http://localhost:8080/movies/search?id=1"
```

### Combined Search
```bash
# Find drama movies with "family" in name
curl "http://localhost:8080/movies/search?name=family&genre=drama"

# Multiple criteria
curl "http://localhost:8080/movies/search?name=the&genre=drama&id=1"
```

## 🛡️ Error Handling

### Invalid Parameters
```json
{
  "success": false,
  "message": "Arrr! Ye need to provide at least one search parameter, matey! Use 'name', 'id', or 'genre'.",
  "movies": []
}
```

### No Results Found
```json
{
  "success": true,
  "message": "Arrr! No movie treasures found matching yer search criteria, matey! Try different search terms.",
  "movies": []
}
```

### Server Error
```json
{
  "success": false,
  "message": "Arrr! Something went wrong during the treasure hunt, matey! Try again later.",
  "movies": []
}
```

## 🎨 HTML Form Integration

The search form is integrated into the main movies page (`/movies`) with:

**Form Fields:**
- Movie Name: Text input for partial name matching
- Movie ID: Number input for specific movie lookup
- Genre: Text input for genre filtering

**Buttons:**
- "🔍 Hunt for Treasure!": Submit search
- "🗺️ Show All Treasures": Reset to show all movies

**Features:**
- Form persistence: Search values maintained after submission
- Responsive design: Works on mobile and desktop
- Pirate-themed styling: Consistent with application theme
- JavaScript enhancements: Loading states, enter key support

## 🧪 Testing

### Unit Tests
- `MovieServiceTest.java`: Comprehensive service layer testing
- `MoviesControllerTest.java`: Controller endpoint testing
- Edge cases: Empty parameters, invalid inputs, no results

### Integration Testing
```bash
# Run all tests
mvn test

# Run specific search tests
mvn test -Dtest=MovieServiceTest
mvn test -Dtest=MoviesControllerTest
```

## 🚀 Performance Considerations

- **In-Memory Search**: Fast performance for current dataset size
- **Case-Insensitive Matching**: Efficient string operations
- **Partial Matching**: Uses `contains()` for flexible searching
- **Input Validation**: Early parameter validation prevents unnecessary processing

## 🔧 Configuration

No additional configuration required. The search functionality uses:
- Existing movie data from `movies.json`
- Spring Boot auto-configuration
- Default logging configuration

## 🎭 Pirate Language Features

All API responses include authentic pirate language:
- Success messages: "Ahoy! Found X movie treasures..."
- Error messages: "Arrr! No valid search parameters..."
- Logging: Pirate-themed log messages throughout
- UI Elements: Pirate emojis and terminology

## 📱 Client Integration

### JavaScript/AJAX
```javascript
async function searchMovies(params) {
    const url = new URL('/movies/search', window.location.origin);
    Object.keys(params).forEach(key => {
        if (params[key]) url.searchParams.append(key, params[key]);
    });
    
    const response = await fetch(url);
    return await response.json();
}
```

### Form Submission
```html
<form action="/movies/search-form" method="get">
    <input type="text" name="name" placeholder="Movie name">
    <input type="number" name="id" placeholder="Movie ID">
    <input type="text" name="genre" placeholder="Genre">
    <button type="submit">Search</button>
</form>
```

## 🔍 Available Movies

The system includes 12 movies with the following genres:
- Drama
- Crime/Drama  
- Action/Crime
- Action/Sci-Fi
- Drama/Romance
- Adventure/Fantasy
- Adventure/Sci-Fi
- Drama/History
- Drama/Thriller
- Drama/Family

## 🎯 Best Practices

1. **Always validate parameters** before making API calls
2. **Handle both success and error responses** appropriately
3. **Use case-insensitive searches** for better user experience
4. **Implement proper error handling** for network failures
5. **Cache results** when appropriate for better performance

---

**Arrr! This documentation should help ye navigate the treacherous waters of movie searching, matey! 🏴‍☠️**