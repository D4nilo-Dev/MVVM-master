package com.example.tp_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp_01.model.Movie

class MovieViewModel : ViewModel() {

    private val _moviesList = MutableLiveData<List<Movie>>(emptyList())
    val moviesList: LiveData<List<Movie>> get() = _moviesList

    fun addMovie(movie: Movie) {
        val currentList = _moviesList.value?.toMutableList() ?: mutableListOf()
        currentList.add(movie)
        _moviesList.value = currentList
    }

    fun updateMovie(updatedMovie: Movie) {
        val currentList = _moviesList.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.id == updatedMovie.id }

        if (index != -1) {
            currentList[index] = updatedMovie
            _moviesList.value = currentList
        }
    }
}