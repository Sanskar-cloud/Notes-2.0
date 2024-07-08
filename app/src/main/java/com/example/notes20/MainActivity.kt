package com.example.notes20

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes20.adapter.NotesAdapter
import com.example.notes20.database.NoteDatabaseHelper
import com.example.notes20.databinding.ActivityMainBinding
import com.example.notes20.fragments.AddNoteFragment
import com.example.notes20.fragments.LoginFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var db: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            // Example from MainActivity or any fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AddNoteFragment())
                .addToBackStack(null)
                .commit()

            binding.fragmentContainer.visibility = View.VISIBLE
            binding.notesRecyclerView.visibility = View.GONE
            binding.addButton.visibility = View.GONE
            return
        }

        db = NoteDatabaseHelper(this)
        setupRecyclerView()

        binding.addButton.setOnClickListener {
            showAddNoteFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh notes list onResume
        notesAdapter.refreshData(db.getAllNotes())
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(db.getAllNotes(), this)
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter
    }

    private fun showAddNoteFragment() {
        binding.notesRecyclerView.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, AddNoteFragment())
            .addToBackStack(null)
            .commit()
        binding.fragmentContainer.visibility = View.VISIBLE
    }

    // Method to refresh notes list in NotesAdapter
    fun refreshNotesList() {
        notesAdapter.refreshData(db.getAllNotes())
    }
}
