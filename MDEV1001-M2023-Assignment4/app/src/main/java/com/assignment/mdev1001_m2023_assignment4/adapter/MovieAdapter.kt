package com.assignment.mdev1001_m2023_assignment4.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.assignment.mdev1001_m2023_assignment4.AddEditMovieActivity
import com.assignment.mdev1001_m2023_assignment4.firebase.FirebaseController
import com.assignment.mdev1001_m2023_assignment4.R
import com.assignment.mdev1001_m2023_assignment4.entity.Movie
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson


open class MovieAdapter(
    var firebaseController: FirebaseController,
    var db: FirebaseFirestore,
    var context: Context,
    var movieList: ArrayList<DocumentSnapshot>
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
        val documentSnapshot = movieList[position]
        val movie: Movie = documentSnapshot.toObject(Movie::class.java)!!
        Log.i("CRITICRATING",movie.criticsRating.toString())
        holder.ratingTextView.text = movie.criticsRating.toString()

        holder.titleTextView.text = movie.title
        holder.txtStudio.text = movie.studio

        holder.imgDelete.setOnClickListener {
            firebaseController.deleteMovie(db, "movies", documentSnapshot.id) {
                if (it) {
                    Log.i("DELETESTATUS", "Delete")
                    movieList.removeAt(position)
                    notifyItemRemoved(position)
                    
                } else {
                    Log.i("DELETESTATUS", "Failed")
                }
            }
        }
        Glide.with(context).load(movie.movieThumbnail).into(holder.imgThumbnail)

        holder.itemView.setOnClickListener {
            onClick(position,movie,documentSnapshot.id)

        }

    }

    open fun onClick(position: Int,movie: Movie,documentId:String){

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ratingTextView: TextView = itemView.findViewById(R.id.txtCritRating)
        val titleTextView: TextView = itemView.findViewById(R.id.txtMovieTitleVal)
        val txtStudio: TextView = itemView.findViewById(R.id.txtStudio)
        val imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)
        val imgThumbnail: ImageView = itemView.findViewById(R.id.imgThumbnail)
    }
}