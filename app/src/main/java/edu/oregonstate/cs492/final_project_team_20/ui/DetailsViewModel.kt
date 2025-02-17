package edu.oregonstate.cs492.final_project_team_20.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.final_project_team_20.data.DetailsRepository
import edu.oregonstate.cs492.final_project_team_20.data.Planet
import edu.oregonstate.cs492.final_project_team_20.data.PlanetService
import kotlinx.coroutines.launch

class DetailsViewModel: ViewModel() {
    private val repository = DetailsRepository(PlanetService.create())

    private val _planets = MutableLiveData<List<Planet?>>(emptyList())
    val planets: LiveData<List<Planet?>> = _planets

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _planetDetails = MutableLiveData<Planet>()
    val planetDetails: LiveData<Planet?> = _planetDetails


    fun loadPlanetInfo(name: String?, apiKey: String) {
        viewModelScope.launch {
            _loading.value = true
            val res = repository.loadCurrentPlanet(name, apiKey)
            _loading.value = false
            _error.value = res.exceptionOrNull()
            _planets.value = res.getOrNull() ?: emptyList()
            _planetDetails.value = _planets.value?.find { it?.name == name }
            Log.d("DetailsViewModel", "Planet info loaded: ${_planetDetails.value}")
        }
    }

}