package com.example.tp_01.view

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tp_01.adapter.MovieAdapter
import com.example.tp_01.databinding.ActivityMainBinding
import com.example.tp_01.model.Movie
import com.example.tp_01.viewmodel.MovieViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()

        binding.fabAddMovie.setOnClickListener {
            showMovieDialog(null)
        }
    }

    private fun setupRecyclerView() {
        adapter = MovieAdapter(emptyList()) { clickedMovie ->
            showMovieDialog(clickedMovie)
        }

        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMovies.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.moviesList.observe(this) { movies ->
            adapter.setMovies(movies)
        }
    }

    private fun showMovieDialog(movieToEdit: Movie?) {
        val isEditing = movieToEdit != null
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(if (isEditing) "Editar Filme" else "Novo Filme")

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val nameInput = EditText(this).apply {
            hint = "Nome do Filme"
            setText(movieToEdit?.name ?: "")
        }
        layout.addView(nameInput)

        val ratingInput = EditText(this).apply {
            hint = "Nota (Ex: 8.5)"
            setText(movieToEdit?.rating?.toString() ?: "")
        }
        layout.addView(ratingInput)

        val imageUrlInput = EditText(this).apply {
            hint = "URL da Imagem (Capa)"
            setText(movieToEdit?.imageUrl ?: "")
        }
        layout.addView(imageUrlInput)

        dialogBuilder.setView(layout)

        dialogBuilder.setPositiveButton("Salvar") { _, _ ->
            val name = nameInput.text.toString()
            val rating = ratingInput.text.toString().toDoubleOrNull() ?: 0.0
            val imageUrl = imageUrlInput.text.toString()

            if (isEditing) {
                val updatedMovie = movieToEdit!!.copy(name = name, rating = rating, imageUrl = imageUrl)
                viewModel.updateMovie(updatedMovie)
            } else {
                val newMovie = Movie(
                    id = System.currentTimeMillis().toInt(),
                    name = name,
                    rating = rating,
                    imageUrl = imageUrl
                )
                viewModel.addMovie(newMovie)
            }
        }

        dialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        dialogBuilder.show()
    }
}