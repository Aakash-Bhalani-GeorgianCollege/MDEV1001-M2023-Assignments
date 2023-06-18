package com.aakash.mdev1001_m2023_assignment2.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aakash.mdev1001_m2023_assignment2.AddEditMovieActivity
import com.aakash.mdev1001_m2023_assignment2.R
import com.aakash.mdev1001_m2023_assignment2.db.MovieDatabase
import com.aakash.mdev1001_m2023_assignment2.entity.Movie
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class MovieAdapter(
    var context: Context,
    var movieList: ArrayList<Movie>,
    var database: MovieDatabase
) :
    RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie_adapter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = movieList[position]
        holder.titleTextView.text = movie.title
        if (movie.criticsRating > 7) {
            holder.ratingTextView.setTextColor(ContextCompat.getColor(context, R.color.green))
        } else if (movie.criticsRating > 5) {
            holder.ratingTextView.setTextColor(ContextCompat.getColor(context, R.color.yellow))
        } else {
            holder.ratingTextView.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        holder.ratingTextView.text = movie.criticsRating.toString()

        holder.imgDelete.setOnClickListener {
            movieList.removeAt(position)

            GlobalScope.launch(Dispatchers.IO) {
                Log.i("MovieLog", "DELETE")
                database.movieDao().deleteMovies(movie)
                withContext(Dispatchers.Main){
                    notifyDataSetChanged()
                }
            }


        }

        Log.i("MovieThumbmnail", movie.movieThumbnail)

        Glide.with(context).load(movie.movieThumbnail).into(holder.imgThumbnail)


        holder.itemView.setOnClickListener {
            val gson = Gson()
            val userObj = gson.toJson(movie)
            context.startActivity(
                Intent(context, AddEditMovieActivity::class.java).putExtra(
                    "TYPE",
                    "UPDATE"
                ).putExtra("MOVIEOBJ", userObj)
            )
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ratingTextView: TextView = itemView.findViewById(R.id.txtCritRating)
        val titleTextView: TextView = itemView.findViewById(R.id.txtMovieTitleVal)
        val imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)
        val imgThumbnail: ImageView = itemView.findViewById(R.id.imgThumbnail)
    }
}