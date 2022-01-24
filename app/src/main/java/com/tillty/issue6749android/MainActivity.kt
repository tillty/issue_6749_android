package com.tillty.issue6749android

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tillty.issue6749android.databinding.ActivityMainBinding
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity(), EventListener<DocumentSnapshot> {

  private lateinit var binding: ActivityMainBinding
  private val ref = Firebase.firestore.document("issues/6749-android")
  private var listener: ListenerRegistration? = null

  public override fun onStart() {
    super.onStart()

    Firebase.auth.signInAnonymously().addOnCompleteListener {
      listener = ref.addSnapshotListener(this)
    }
  }

  public override fun onStop() {
    super.onStop()

    listener?.remove()
    listener = null
  }

  override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
    if (error != null) {
      Log.w(TAG, "exception", error)
      return
    }

    value?.let {
      val data = value.data
      if (data != null) {
        Log.e(TAG, "Data received")
        val local = data["local_timestamp"] as Timestamp
        val server = data["server_timestamp"] as Timestamp?

        if (server != null) {
          val diff = (server.toDate().time - local.toDate().time).absoluteValue / 1000
          Log.e(TAG, "Difference between local and server: $diff seconds")
        } else {
          Log.e(TAG, "No server response yet")
        }

        Log.e(TAG, "-------------------------------")
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setSupportActionBar(binding.toolbar)

    binding.fab.setOnClickListener {
      Log.e(TAG, "Updating data")
      ref.set(
        mapOf(
          "local_timestamp" to Timestamp.now(),
          "server_timestamp" to FieldValue.serverTimestamp(),
        )
      )
    }
  }
}