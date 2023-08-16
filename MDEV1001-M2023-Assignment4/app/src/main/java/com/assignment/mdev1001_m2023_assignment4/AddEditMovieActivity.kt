package com.assignment.mdev1001_m2023_assignment4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.assignment.mdev1001_m2023_assignment4.databinding.ActivityAddEditMovieBinding
import com.assignment.mdev1001_m2023_assignment4.entity.Movie
import com.assignment.mdev1001_m2023_assignment4.firebase.FirebaseController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson

class AddEditMovieActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityAddEditMovieBinding
    var firebaseController = FirebaseController()
    var db = Firebase.firestore
    var imagePath = ""
    val storageRef = Firebase.storage.reference
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_movie)

        mBinding.btnCancel.setOnClickListener {
            finish()
        }

        mBinding.imgEdit.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .start(101)
        }


        val movieType = intent.getStringExtra("TYPE")


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
            Glide.with(this).load(movieObj.movieThumbnail).into(mBinding.imgMovieThumb)
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
                mBinding.progressBar.visibility = View.VISIBLE
                firebaseController.uploadImage(storageRef, imagePath) { url, isUploaded ->
                    if (isUploaded) {
                        movieObj.movieThumbnail = url!!
                        firebaseController.updateMovie(db, "movies", documentId, movieObj) {
                            if (it) {
                                mBinding.progressBar.visibility = View.GONE
                                Log.i("DataStatus", "Updated")
                                Toast.makeText(
                                    this@AddEditMovieActivity,
                                    "Updated Successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                mBinding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@AddEditMovieActivity,
                                    "Update Failed!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        mBinding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Can't Upate File", Toast.LENGTH_SHORT).show()
                    }

                }
                //firebase update document

            }
        } else {
            mBinding.txtAddEditMovie.text = "Add Movie"
            mBinding.btnAddOrUpdate.setText("Add")
            mBinding.btnAddOrUpdate.setOnClickListener {
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
                mBinding.progressBar.visibility = View.VISIBLE
                firebaseController.uploadImage(storageRef, imagePath) { url, isUploaded ->

                    if (isUploaded) {

                        movieObj.movieThumbnail = url!!
                        firebaseController.addMovie(db, "movies", movieObj) {
                            if (it) {
                                mBinding.progressBar.visibility = View.GONE
                                Log.i("DataStatus", "Added")
                                Toast.makeText(
                                    this@AddEditMovieActivity,
                                    "Added Successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                mBinding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@AddEditMovieActivity,
                                    "Add Failed!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        mBinding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@AddEditMovieActivity,
                            "Can't Upate File",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == RESULT_OK) {
            mBinding.imgMovieThumb.setImageURI(data?.data)
            imagePath = data?.data.toString()
        }

    }
}