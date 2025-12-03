# 🏴‍☠️ Movie Service - Pirate's Treasure Chest of Films 🏴‍☠️

Ahoy matey! Welcome to the most swashbuckling movie catalog web application on the seven seas! Built with Spring Boot and featuring a pirate-themed search functionality that'll help ye find yer favorite movie treasures.

## ⚓ Features

- **🎬 Movie Treasure Chest**: Browse 12 classic movies with detailed information
- **🔍 Advanced Treasure Hunt**: Search movies by name, ID, or genre with our new pirate-themed search functionality
- **📋 Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **⭐ Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **📱 Responsive Design**: Mobile-first design that works on all devices from ship to shore
- **🎨 Modern Pirate UI**: Dark theme with gradient backgrounds and smooth animations
- **🚀 REST API**: JSON endpoints for modern web applications and treasure hunting

## 🛠️ Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Log4j 2.20.0**
- **JUnit 5.8.2**
- **Thymeleaf** for HTML templating
- **JSON** for data storage and API responses

## 🚀 Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **🏴‍☠️ Movie Treasure Chest**: http://localhost:8080/movies
- **🔍 Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)

## 🔍 New Search & Filtering API

Ahoy! We've added powerful search functionality to help ye find yer favorite movie treasures!

### REST API Endpoints

#### Search Movies (JSON Response)
```
GET /movies/search
```

**Query Parameters:**
- `name` (optional): Movie name to search for (partial matches, case-insensitive)
- `id` (optional): Specific movie ID to find
- `genre` (optional): Genre to filter by (partial matches, case-insensitive)

**Examples:**
```bash
# Search by movie name
curl "http://localhost:8080/movies/search?name=prison"

# Search by genre
curl "http://localhost:8080/movies/search?genre=drama"

# Search by specific ID
curl "http://localhost:8080/movies/search?id=1"

# Combined search (name and genre)
curl "http://localhost:8080/movies/search?name=family&genre=drama"
```

**Response Format:**
```json
{
  "success": true,
  "message": "Ahoy! Found 2 movie treasures matching yer search!",
  "movies": [
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
  ],
  "totalResults": 1
}
```

#### HTML Form Search
```
GET /movies/search-form
```

Same parameters as the REST API, but returns the movies HTML page with search results and the search form pre-filled.

### 🎯 Search Features

- **🔤 Case-Insensitive**: Search works regardless of capitalization
- **🔍 Partial Matching**: Find movies with partial name or genre matches
- **🎯 Multiple Criteria**: Combine name, ID, and genre filters
- **⚡ Fast Performance**: Efficient in-memory searching
- **🛡️ Input Validation**: Proper error handling for invalid parameters
- **🏴‍☠️ Pirate-Themed Messages**: All responses include authentic pirate language!

### 🎨 HTML Search Form

The movies page now includes a beautiful pirate-themed search form with:
- **🎬 Movie Name Field**: Search by movie title
- **🆔 Movie ID Field**: Find specific movies by ID
- **🎭 Genre Field**: Filter by movie genre
- **🔍 Hunt for Treasure Button**: Submit search with pirate flair
- **🗺️ Show All Treasures Button**: Reset to view all movies

## 📚 API Documentation

### Error Handling

All endpoints include proper error handling with pirate-themed messages:

**No Search Parameters:**
```json
{
  "success": false,
  "message": "Arrr! Ye need to provide at least one search parameter, matey! Use 'name', 'id', or 'genre'.",
  "movies": []
}
```

**No Results Found:**
```json
{
  "success": true,
  "message": "Arrr! No movie treasures found matching yer search criteria, matey! Try different search terms.",
  "movies": []
}
```

**Server Error:**
```json
{
  "success": false,
  "message": "Arrr! Something went wrong during the treasure hunt, matey! Try again later.",
  "movies": []
}
```

### HTTP Status Codes

- `200 OK`: Successful search (even if no results found)
- `400 Bad Request`: Invalid or missing search parameters
- `500 Internal Server Error`: Unexpected server error

## 🏗️ Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── movies/
│   │       │   ├── MoviesApplication.java    # Main Spring Boot application
│   │       │   ├── MoviesController.java     # REST controller with search endpoints
│   │       │   ├── MovieService.java         # Service layer with search logic
│   │       │   ├── Movie.java                # Movie data model
│   │       │   ├── Review.java               # Review data model
│   │       │   └── ReviewService.java        # Review service
│   │       └── utils/
│   │           ├── MovieIconUtils.java       # Movie icon utilities
│   │           └── MovieUtils.java           # Movie validation utilities
│   └── resources/
│       ├── application.yml                   # Application configuration
│       ├── movies.json                       # Movie data (12 movies)
│       ├── mock-reviews.json                 # Mock review data
│       ├── log4j2.xml                        # Logging configuration
│       └── templates/
│           ├── movies.html                   # Enhanced with search form
│           └── movie-details.html            # Movie details page
└── test/                                     # Comprehensive unit tests
    └── java/
        └── com/amazonaws/samples/qdevmovies/movies/
            ├── MovieServiceTest.java         # Service layer tests
            ├── MoviesControllerTest.java     # Controller tests (updated)
            └── MovieTest.java                # Model tests
```

## 🧪 Testing

Run the comprehensive test suite:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MovieServiceTest

# Run with coverage
mvn test jacoco:report
```

### Test Coverage

- **MovieService**: Search functionality, validation, edge cases
- **MoviesController**: REST endpoints, HTML form handling, error scenarios
- **Integration Tests**: End-to-end search workflows
- **Performance Tests**: Search efficiency under load

## 🔧 Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

Check the logs for pirate-themed error messages:
```bash
tail -f logs/application.log
```

## 🎯 Usage Examples

### JavaScript/AJAX Integration

```javascript
// Search for movies using fetch API
async function searchMovies(name, genre) {
    const params = new URLSearchParams();
    if (name) params.append('name', name);
    if (genre) params.append('genre', genre);
    
    const response = await fetch(`/movies/search?${params}`);
    const data = await response.json();
    
    if (data.success) {
        console.log(`Ahoy! Found ${data.totalResults} movies:`, data.movies);
    } else {
        console.error('Search failed:', data.message);
    }
}

// Example usage
searchMovies('prison', 'drama');
```

### cURL Examples

```bash
# Find all drama movies
curl -X GET "http://localhost:8080/movies/search?genre=drama" \
     -H "Accept: application/json"

# Search for movies with "family" in the name
curl -X GET "http://localhost:8080/movies/search?name=family" \
     -H "Accept: application/json"

# Get movie by specific ID
curl -X GET "http://localhost:8080/movies/search?id=5" \
     -H "Accept: application/json"
```

## 🤝 Contributing

This project welcomes contributions from fellow pirates! Feel free to:
- 🎬 Add more movies to the treasure chest
- 🎨 Enhance the pirate-themed UI/UX
- 🔍 Improve search functionality
- 📱 Enhance responsive design
- 🧪 Add more comprehensive tests
- 📚 Improve documentation

## 📜 License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

**Arrr! May fair winds fill yer sails as ye explore this movie treasure chest! 🏴‍☠️**
