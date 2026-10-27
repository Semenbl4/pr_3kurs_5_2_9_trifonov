package com.example.pr_3kurs_5_2_9_trifonov

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LibraryActivity : AppCompatActivity() {

    private lateinit var rvMovies: RecyclerView
    private lateinit var btnDeleteSelected: Button
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var db: AppDatabase

    private var selectedMovie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        rvMovies = findViewById(R.id.rvMovies)
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected)

        rvMovies.layoutManager = LinearLayoutManager(this)
        db = AppDatabase.getDatabase(this)

        movieAdapter = MovieAdapter(emptyList()) { movie ->
            selectedMovie = movie
            if (movie != null) {
                btnDeleteSelected.visibility = View.VISIBLE
            } else {
                btnDeleteSelected.visibility = View.GONE
            }
        }
        rvMovies.adapter = movieAdapter

        btnDeleteSelected.setOnClickListener {
            selectedMovie?.let { movie ->
                CoroutineScope(Dispatchers.IO).launch {
                    db.movieDao().deleteMovie(movie)
                    loadMovies()

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LibraryActivity, "Фильм \"${movie.title}\" успешно удален", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        loadMovies()
    }

    private fun loadMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            val moviesList = db.movieDao().getAllMovies()
            withContext(Dispatchers.Main) {
                movieAdapter.updateList(moviesList)
            }
        }
    }
}