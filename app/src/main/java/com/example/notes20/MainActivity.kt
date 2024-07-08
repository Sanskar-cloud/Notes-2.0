package com.example.notes20

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes20.adapter.NotesAdapter
import com.example.notes20.database.NoteDatabaseHelper
import com.example.notes20.fragments.AddNoteFragment
import com.example.notes20.fragments.LoginFragment
import com.example.notes20.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var db: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            // Start LoginFragment if user is not signed in
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LoginFragment())
                .commit()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)
        notesAdapter = NotesAdapter(db.getAllNotes(), this)
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter

        binding.addButton.setOnClickListener {
            // Start AddNoteFragment when addButton is clicked
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AddNoteFragment())
                .addToBackStack(null) // Optional: Add to back stack to allow navigation back
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
    }
}
