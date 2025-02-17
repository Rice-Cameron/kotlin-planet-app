package edu.oregonstate.cs492.final_project_team_20.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import edu.oregonstate.cs492.final_project_team_20.R

class PlanetFragment: Fragment(R.layout.fragment_planet) {
    private val viewModel: PlanetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mercury: View = view.findViewById(R.id.mercury)
        val venus: View = view.findViewById(R.id.venus)
        val earth: View = view.findViewById(R.id.earth)
        val mars: View = view.findViewById(R.id.mars)
        val jupiter: View = view.findViewById(R.id.jupiter)
        val saturn: View = view.findViewById(R.id.saturn)
        val neptune: View = view.findViewById(R.id.neptune)
        val uranus: View = view.findViewById(R.id.uranus)

        mercury.setOnClickListener {
           navigateToDetails("Mercury")
        }

        venus.setOnClickListener {
           navigateToDetails("Venus")
        }

        earth.setOnClickListener {
           navigateToDetails("Earth")
        }

        mars.setOnClickListener {
           navigateToDetails("Mars")
        }

        jupiter.setOnClickListener {
            navigateToDetails("Jupiter")
        }

        saturn.setOnClickListener {
            navigateToDetails("Saturn")
        }

        neptune.setOnClickListener {
            navigateToDetails("Neptune")
        }

        uranus.setOnClickListener {
            navigateToDetails("Uranus")
        }
    }

    private fun navigateToDetails(planet_name: String) {
        val action = PlanetFragmentDirections.navigateToDetails(planet_name)
        findNavController().navigate(action)
    }

}