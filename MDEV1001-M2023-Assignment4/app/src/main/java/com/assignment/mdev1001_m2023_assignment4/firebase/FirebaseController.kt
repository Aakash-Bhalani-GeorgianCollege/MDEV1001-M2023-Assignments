package com.assignment.mdev1001_m2023_assignment4.firebase

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.assignment.mdev1001_m2023_assignment4.entity.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class FirebaseController {

    fun getAllMovies(
        db: FirebaseFirestore,
        collectionName: String,
        function: (List<DocumentSnapshot>?, Boolean) -> Unit
    ) {
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { result ->
                val documentList = arrayListOf<DocumentSnapshot>()
                for (document in result.documents) {
                    documentList.add(document)
                }
                function(documentList, true)
            }
            .addOnFailureListener { exception ->
                function(emptyList(), false)
            }
    }


    fun addMovie(
        db: FirebaseFirestore,
        collectionName: String,
        movie: Movie,
        function: (Boolean) -> Unit
    ) {
        db.collection(collectionName).add(movie).addOnSuccessListener { documentReference ->
            function(true)
        }.addOnFailureListener { e ->
            function(false)
        }
    }


    fun updateMovie(
        db: FirebaseFirestore,
        collectionName: String,
        documentId: String,
        movie: Movie,
        function: (Boolean) -> Unit
    ) {
        val docRef = db.collection(collectionName).document(documentId)

        docRef.set(movie)
            .addOnSuccessListener {
                function(true)
            }
            .addOnFailureListener { e ->
                function(false)
            }
    }


    fun deleteMovie(
        db: FirebaseFirestore,
        collectionName: String,
        documentId: String,
        function: (Boolean) -> Unit
    ) {
        val docRef = db.collection(collectionName).document(documentId)

        docRef.delete()
            .addOnSuccessListener {
                function(true)
            }
            .addOnFailureListener { e ->
                function(false)
            }
    }

    fun registerUser(
        db: FirebaseFirestore,
        collectionName: String,
        auth: FirebaseAuth,
        emailId: String,
        password: String,
        username: String,
        activity: Activity,
        function: (Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(emailId, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val data = hashMapOf("email" to emailId)

                    db.collection(collectionName).document(username).set(data, SetOptions.merge())
                        .addOnSuccessListener { documentReference ->
                            function(true)
                        }.addOnFailureListener { e ->
                        function(false)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.i("REGISTRATION", "Signed Up Failed")
                    function(false)
                }
            }
    }

    fun loginUser(
        db:FirebaseFirestore,
        auth: FirebaseAuth,
        username: String,
        password: String,
        activity: Activity,
        function: (Boolean) -> Unit
    ) {
        db.collection("usernames")
            .document(username)
            .get()
            .addOnSuccessListener { document ->
                val email = document.getString("email")
                auth.signInWithEmailAndPassword(email!!, password)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            function(true)
                        } else {
                            // If sign in fails, display a message to the user.
                            function(false)
                        }
                    }
            }
            .addOnFailureListener { exception ->
                function(false)
            }

    }

    fun uploadImage(storageRef: StorageReference, path: String,function: (String?,Boolean) -> Unit){
        val fileReference = storageRef.child("images/${System.currentTimeMillis()}")

        val uploadTask = fileReference.putFile(path.toUri())

        uploadTask.addOnSuccessListener {
            // File uploaded successfully

            // Get the download URL
            fileReference.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                function(downloadUrl,true)
            }.addOnFailureListener {
                function("",false)
            }
        }.addOnFailureListener {
            function("",false)
        }

    }

}