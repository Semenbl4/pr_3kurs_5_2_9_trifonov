package com.example.pr_3kurs_5_2_9_trifonov

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class SearchActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etYear: EditText
    private lateinit var btnSearch: Button
    private lateinit var tvMovieInfo: TextView
    private lateinit var btnAddToLibrary: Button
    private lateinit var btnOpenLibrary: Button

    private var currentMovie: Movie? = null
    private val apiKey = "f7abfaea"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        etTitle = findViewById(R.id.etTitle)
        etYear = findViewById(R.id.etYear)
        btnSearch = findViewById(R.id.btnSearch)
        tvMovieInfo = findViewById(R.id.tvMovieInfo)
        btnAddToLibrary = findViewById(R.id.btnAddToLibrary)
        btnOpenLibrary = findViewById(R.id.btnOpenLibrary)

        btnSearch.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val year = etYear.text.toString().trim()

            if (title.isNotEmpty()) {
                searchMovie(title, year)
            } else {
                Toast.makeText(this, "Введите название фильма", Toast.LENGTH_SHORT).show()
            }
        }

        btnAddToLibrary.setOnClickListener {
            currentMovie?.let { movie ->
                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatabase.getDatabase(this@SearchActivity)
                    db.movieDao().insertMovie(movie)

                    launch(Dispatchers.Main) {
                        Toast.makeText(this@SearchActivity, "${movie.title} добавлен в медиатеку!", Toast.LENGTH_SHORT).show()
                    }
                }
            } ?: Toast.makeText(this, "Сначала найдите фильм", Toast.LENGTH_SHORT).show()
        }

        btnOpenLibrary.setOnClickListener {
            val intent = Intent(this, LibraryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun searchMovie(title: String, year: String) {
        val queue = Volley.newRequestQueue(this)

        var url = "http://omdbapi.com?" + "apikey=" + apiKey + "&t=" + title

        if (year.isNotEmpty()) {
            url += "&y=" + year
        }

        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getString("Response") == "True") {
                        currentMovie = Gson().fromJson(response, Movie::class.java)

                        tvMovieInfo.text = "Название: ${currentMovie?.title}\nГод: ${currentMovie?.year}\nЖанр: ${currentMovie?.genre}\nРейтинг: ${currentMovie?.imdbRating}"
                        btnAddToLibrary.visibility = View.VISIBLE
                    } else {
                        tvMovieInfo.text = "Фильм не найден!"
                        btnAddToLibrary.visibility = View.GONE
                        currentMovie = null
                    }
                } catch (e: Exception) {
                    tvMovieInfo.text = "Ошибка обработки данных"
                    btnAddToLibrary.visibility = View.GONE
                }
            },
            {
                Toast.makeText(this, "Ошибка сети", Toast.LENGTH_SHORT).show()
            })

        queue.add(stringRequest)
    }
}