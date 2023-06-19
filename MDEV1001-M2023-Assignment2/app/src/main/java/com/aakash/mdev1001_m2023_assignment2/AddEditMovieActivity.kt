package com.aakash.mdev1001_m2023_assignment2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import com.aakash.mdev1001_m2023_assignment2.databinding.ActivityAddEditMovieBinding
import com.aakash.mdev1001_m2023_assignment2.db.MovieDatabase
import com.aakash.mdev1001_m2023_assignment2.entity.Movie
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddEditMovieActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityAddEditMovieBinding
    lateinit var database: MovieDatabase
    var imagePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_movie)

        mBinding.btnCancel.setOnClickListener {
            finish()
        }

        mBinding.imgEdit.setOnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(101)
        }

        database = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            "movie_database"
        ).build()


        var movieType = intent.getStringExtra("TYPE")

        if (movieType == "UPDATE") {
            var movieObj = Gson().fromJson(intent.getStringExtra("MOVIEOBJ"), Movie::class.java)
            mBinding.txtTitleVal.setText(movieObj.title)
            mBinding.txtStudioVal.setText(movieObj.studio)
            mBinding.txtGenresVal.setText(movieObj.genres)
            mBinding.txtDirectorsVal.setText(movieObj.directors)
            mBinding.txtWritersVal.setText(movieObj.writers)
            mBinding.txtActorsal.setText(movieObj.actors)
            mBinding.txtYearVal.setText(movieObj.year.toString())
            mBinding.txtLengthVal.setText(movieObj.length.toString())
            mBinding.txtShortDescriptionVal.setText(movieObj.shortDescription)
            mBinding.txtMPARatingVal.setText(movieObj.mpaRating)
            mBinding.txtCriticsRatingVal.setText(movieObj.criticsRating.toString())



            mBinding.btnAddOrUpdate.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    movieObj.title = mBinding.txtTitleVal.text.toString()
                    movieObj.studio = mBinding.txtStudioVal.text.toString()
                    movieObj.genres = mBinding.txtGenresVal.text.toString()
                    movieObj.directors = mBinding.txtDirectorsVal.text.toString()
                    movieObj.writers = mBinding.txtWritersVal.text.toString()
                    movieObj.actors = mBinding.txtActorsal.text.toString()
                    movieObj.year = mBinding.txtYearVal.text.toString().toInt()
                    movieObj.length = mBinding.txtLengthVal.text.toString().toInt()
                    movieObj.shortDescription = mBinding.txtShortDescriptionVal.text.toString()
                    movieObj.mpaRating = mBinding.txtMPARatingVal.text.toString()
                    movieObj.criticsRating = mBinding.txtCriticsRatingVal.text.toString().toDouble()
                    mBinding.imgMovieThumb.setImageURI(movieObj.movieThumbnail.toUri())
                    database.movieDao().updateMovies(movieObj)
                    Log.i("DBMESS", "Updated")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddEditMovieActivity, "Updated", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }

                }
            }
        } else {
            mBinding.txtAddEditMovie.text = "Add Movie"
            mBinding.btnAddOrUpdate.setText("Add")
            mBinding.btnAddOrUpdate.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    var movieObj = Movie(
                        title = mBinding.txtTitleVal.text.toString(),
                        studio = mBinding.txtStudioVal.text.toString(),
                        genres = mBinding.txtGenresVal.text.toString(),
                        directors = mBinding.txtDirectorsVal.text.toString(),
                        writers = mBinding.txtWritersVal.text.toString(),
                        actors = mBinding.txtActorsal.text.toString(),
                        year = mBinding.txtYearVal.text.toString().toInt(),
                        length = mBinding.txtLengthVal.text.toString().toInt(),
                        shortDescription = mBinding.txtShortDescriptionVal.text.toString(),
                        mpaRating = mBinding.txtMPARatingVal.text.toString(),
                        criticsRating = mBinding.txtCriticsRatingVal.text.toString().toDouble(),
                        movieThumbnail = imagePath
                    )
                    database.movieDao().insertMovie(movieObj)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddEditMovieActivity, "Added", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==101&&resultCode== RESULT_OK){
            mBinding.imgMovieThumb.setImageURI(data?.data)
            Log.i("ImagePath",data?.data.toString())
            imagePath = data?.data.toString()
        }

    }

}