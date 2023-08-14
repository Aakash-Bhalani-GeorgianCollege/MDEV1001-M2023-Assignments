package com.assignment.mdev1001_m2023_assignment4

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.mdev1001_m2023_assignment4.firebase.FirebaseController
import com.assignment.mdev1001_m2023_assignment4.adapter.MovieAdapter
import com.assignment.mdev1001_m2023_assignment4.databinding.ActivityMainBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
                movieAdapter = MovieAdapter(
                    firebaseController = firebaseController,
                    db = db,
                    context = this@MainActivity,
                    movieList = movieList
                )
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