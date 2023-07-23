package com.assignment3.mdev1001_m2023_assignment3.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.assignment3.mdev1001_m2023_assignment3.MovieDetailActivity
import com.assignment3.mdev1001_m2023_assignment3.R
import com.assignment3.mdev1001_m2023_assignment3.model.Movie
import com.bumptech.glide.Glide
import com.google.gson.Gson


open class MovieAdapter(
    var context: Context,
    var movieList: ArrayList<Movie>,
) : RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {


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
        holder.titleTextView.text = movie.Title
      /*  if (movie.imdbRating.toInt() > 7) {
            holder.ratingTextView.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
        } else if (movie.imdbRating.toInt() > 5) {
            holder.ratingTextView.setTextColor(ContextCompat.getColor(context, R.color.colorYellow))
        } else {
            holder.ratingTextView.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
        }*/
        holder.ratingTextView.text = movie.imdbRating.toString()
        holder.yearTextView.text = movie.Year.toString()



        Glide.with(context).load(movie.Poster).into(holder.imgThumbnail)


        holder.itemView.setOnClickListener {
            val gson = Gson()
            val movieObj = gson.toJson(movie)
            context.startActivity(
                Intent(
                    context, MovieDetailActivity::class.java
                ).putExtra("MOVIEOBJ", movieObj)
            )
        }
        holder.imgRightArrow.setOnClickListener {
            val gson = Gson()
            val userObj = gson.toJson(movie)
            context.startActivity(
                Intent(
                    context, MovieDetailActivity::class.java
                ).putExtra("MOVIEOBJ", userObj)
            )
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ratingTextView: TextView = itemView.findViewById(R.id.txtCritRating)
        val titleTextView: TextView = itemView.findViewById(R.id.txtMovieTitleVal)
        val yearTextView: TextView = itemView.findViewById(R.id.txtYear)
        val imgThumbnail: ImageView = itemView.findViewById(R.id.imgThumbnail)
        val imgRightArrow: ImageView = itemView.findViewById(R.id.imgRightArrow)
    }
}