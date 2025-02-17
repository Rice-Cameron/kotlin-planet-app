package edu.oregonstate.cs492.final_project_team_20.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.final_project_team_20.data.PlanetRepository
import edu.oregonstate.cs492.final_project_team_20.data.PlanetService
import kotlinx.coroutines.launch

class PlanetViewModel: ViewModel() {
    private val repository = PlanetRepository(PlanetService.create())

    val planetNames = MutableLiveData<List<String>>()

    fun fetchPlanetNames() {
        viewModelScope.launch {
            val names = repository.getPlanetNames()
            planetNames.value = names
        }
    }

}