package com.assignment.mdev1001_m2023_assignment4

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.assignment.mdev1001_m2023_assignment4.adapter.MovieAdapter
import com.assignment.mdev1001_m2023_assignment4.databinding.ActivityMainBinding
import com.assignment.mdev1001_m2023_assignment4.entity.Movie
import com.assignment.mdev1001_m2023_assignment4.firebase.FirebaseController
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityMainBinding
    var movieList = arrayListOf<DocumentSnapshot>()
    private lateinit var movieAdapter: MovieAdapter
    val db = Firebase.firestore
    var firebaseController = FirebaseController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        mBinding.btnAddMovie.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddEditMovieActivity::class.java))
        }
        /* mBinding.btnLogout.setOnClickListener {
             SharedPreferenceManager.removeAllData()
             finish()
         }*/
    }

    private fun getMovieList() {
        firebaseController.getAllMovies(db, "movies") { documentList, boolean ->
            if (boolean) {
                movieList.clear()
                movieList.addAll(documentList!!)
                movieAdapter = object : MovieAdapter(
                    firebaseController = firebaseController,
                    db = db,
                    context = this@MainActivity,
                    movieList = movieList
                ) {
                    override fun onClick(position: Int, movie: Movie, documentId: String) {
                        super.onClick(position, movie, documentId)
                        val gson = Gson()

                        val movieObj = gson.toJson(movie)
                        startActivity(
                            Intent(this@MainActivity, AddEditMovieActivity::class.java).putExtra(
                                "TYPE",
                                "UPDATE"
                            ).putExtra("MOVIEOBJ", movieObj)
                                .putExtra("DOCUMENTID", documentId)
                        )
                    }
                }
                mBinding.rvMovie.adapter = movieAdapter
            } else {
                Toast.makeText(this@MainActivity, "No Movies", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getMovieList()
    }
}