package com.example.pr_3kurs_5_2_9_trifonov

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(
    private var movies: List<Movie>,
    private val onMovieSelected: (Movie?) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardMovie: CardView = view.findViewById(R.id.cardMovie)
        val tvTitle: TextView = view.findViewById(R.id.itemTitle)
        val tvYear: TextView = view.findViewById(R.id.itemYear)
        val tvGenre: TextView = view.findViewById(R.id.itemGenre)
        val tvPlot: TextView = view.findViewById(R.id.itemPlot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.tvTitle.text = movie.title
        holder.tvYear.text = movie.year
        holder.tvGenre.text = movie.genre
        holder.tvPlot.text = movie.plot

        if (selectedPosition == position) {
            holder.cardMovie.setCardBackgroundColor(Color.parseColor("#2C3246"))
        } else {
            holder.cardMovie.setCardBackgroundColor(Color.parseColor("#1E2230"))
        }

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition

            if (selectedPosition == position) {
                selectedPosition = RecyclerView.NO_POSITION
                onMovieSelected(null)
            } else {
                selectedPosition = position
                onMovieSelected(movie)
            }

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int = movies.size

    fun updateList(newMovies: List<Movie>) {
        movies = newMovies
        selectedPosition = RecyclerView.NO_POSITION
        onMovieSelected(null)
        notifyDataSetChanged()
    }
}