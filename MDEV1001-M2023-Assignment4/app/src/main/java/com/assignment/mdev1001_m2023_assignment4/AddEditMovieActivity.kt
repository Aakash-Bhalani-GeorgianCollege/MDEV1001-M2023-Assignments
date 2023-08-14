package com.assignment.mdev1001_m2023_assignment4

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.assignment.mdev1001_m2023_assignment4.firebase.FirebaseController
import com.assignment.mdev1001_m2023_assignment4.databinding.ActivityAddEditMovieBinding
import com.assignment.mdev1001_m2023_assignment4.entity.Movie
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEditMovieActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityAddEditMovieBinding
    var token = ""
    var firebaseController = FirebaseController()
    var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_movie)

        mBinding.btnCancel.setOnClickListener {
            finish()
        }

        var movieType = intent.getStringExtra("TYPE")


        if (movieType == "UPDATE") {
            val movieObj = Gson().fromJson(intent.getStringExtra("MOVIEOBJ"), Movie::class.java)
            val documentId = intent.getStringExtra("DOCUMENTID")

            Log.i("DOCUMENTID", documentId!!)


            val genres = StringBuffer()
            for (i in movieObj.genres) {
                genres.append(i)
            }

            val directors = StringBuffer()
            for (i in movieObj.directors) {
                directors.append(i)
            }

            val writers = StringBuffer()
            for (i in movieObj.writers) {
                writers.append(i)
            }

            val actors = StringBuffer()
            for (i in movieObj.actors) {
                actors.append(i)
            }

            mBinding.txtTitleVal.setText(movieObj.title)
            mBinding.txtStudioVal.setText(movieObj.studio)
            mBinding.txtGenresVal.setText(genres)
            mBinding.txtDirectorsVal.setText(directors)
            mBinding.txtWritersVal.setText(writers)
            mBinding.txtActorsal.setText(actors)
            mBinding.txtYearVal.setText(movieObj.year.toString())
            mBinding.txtLengthVal.setText(movieObj.length.toString())
            mBinding.txtShortDescriptionVal.setText(movieObj.shortDescription)
            mBinding.txtMPARatingVal.setText(movieObj.mpaRating)
            mBinding.txtCriticsRatingVal.setText(movieObj.criticsRating.toString())

            mBinding.btnAddOrUpdate.setOnClickListener {

                movieObj.title = mBinding.txtTitleVal.text.toString()
                movieObj.studio = mBinding.txtStudioVal.text.toString()
                movieObj.genres = mBinding.txtGenresVal.text.toString().split(",")
                movieObj.directors = mBinding.txtDirectorsVal.text.toString().split(",")
                movieObj.writers = mBinding.txtWritersVal.text.toString().split(",")
                movieObj.actors = mBinding.txtActorsal.text.toString().split(",")
                movieObj.year = mBinding.txtYearVal.text.toString().toInt()
                movieObj.length = mBinding.txtLengthVal.text.toString().toInt()
                movieObj.shortDescription = mBinding.txtShortDescriptionVal.text.toString()
                movieObj.mpaRating = mBinding.txtMPARatingVal.text.toString()
                movieObj.criticsRating = mBinding.txtCriticsRatingVal.text.toString().toDouble()


                //firebase update document
                firebaseController.updateMovie(db, "movies", documentId, movieObj) {
                    if (it) {
                        Log.i("DataStatus", "Updated")
                        Toast.makeText(
                            this@AddEditMovieActivity,
                            "Updated Successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@AddEditMovieActivity,
                            "Update Failed!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            mBinding.txtAddEditMovie.text = "Add Movie"
            mBinding.btnAddOrUpdate.setText("Add")
            mBinding.btnAddOrUpdate.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    val movieObj = Movie()

                    movieObj.title = mBinding.txtTitleVal.text.toString()
                    movieObj.studio = mBinding.txtStudioVal.text.toString()
                    movieObj.genres = arrayListOf(mBinding.txtGenresVal.text.toString())
                    movieObj.directors = arrayListOf(mBinding.txtDirectorsVal.text.toString())
                    movieObj.writers = arrayListOf(mBinding.txtWritersVal.text.toString())
                    movieObj.actors = arrayListOf(mBinding.txtActorsal.text.toString())
                    movieObj.year = mBinding.txtYearVal.text.toString().toInt()
                    movieObj.length = mBinding.txtLengthVal.text.toString().toInt()
                    movieObj.shortDescription = mBinding.txtShortDescriptionVal.text.toString()
                    movieObj.mpaRating = mBinding.txtMPARatingVal.text.toString()
                    movieObj.criticsRating = mBinding.txtCriticsRatingVal.text.toString().toDouble()

                    //firebase add document

                    firebaseController.addMovie(db, "movies", movieObj) {
                        if (it) {
                            Log.i("DataStatus", "Updated")
                            Toast.makeText(
                                this@AddEditMovieActivity,
                                "Added Successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@AddEditMovieActivity,
                                "Add Failed!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}