import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoviesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState = _uiState.asStateFlow()

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    fun updateMovies() {
        viewModelScope.launch {
            val movies = fetchMovies()
            _uiState.update { it.copy(movies = movies) }
        }
    }

    private suspend fun fetchMovies() = httpClient
        .get("https://my-json-server.typicode.com/necatisozer/movieapi/popular")
        .body<List<Movie>>()

    override fun onCleared() {
        httpClient.close()
        super.onCleared()
    }
}

data class MoviesUiState(val movies: List<Movie> = emptyList())