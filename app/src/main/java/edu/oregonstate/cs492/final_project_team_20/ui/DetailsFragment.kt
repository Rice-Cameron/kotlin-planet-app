package edu.oregonstate.cs492.final_project_team_20.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.final_project_team_20.R
import edu.oregonstate.cs492.final_project_team_20.data.Planet
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.preference.PreferenceManager

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val viewModel: DetailsViewModel by viewModels()

    private var currentPlanet: Planet? = null

    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    private lateinit var videoButton: ImageButton
    private lateinit var video: VideoView
    private lateinit var planetImageIV: ImageView
    private lateinit var planetDetails: View
    private lateinit var planetNameTV: TextView
    private lateinit var planetDataTV: TextView

    private val planetImageMap = mapOf(
        "mercury" to R.drawable.mercury,
        "venus" to R.drawable.venus,
        "earth" to R.drawable.earth,
        "mars" to R.drawable.mars,
        "jupiter" to R.drawable.jupiter,
        "saturn" to R.drawable.saturn,
        "uranus" to R.drawable.uranus,
        "neptune" to R.drawable.neptune
    )

    private val planetVideoMap = mapOf(
        "mercury" to "https://v.ftcdn.net/01/88/78/65/700_F_188786545_aRXu0HprzbrRwSWSDI2Nf38WHVIi8AZY_ST.mp4",
        "venus" to "https://v.ftcdn.net/02/96/78/39/700_F_296783910_riijVmCA5A6dwondartwfjHrPuRyVx9D_ST.mp4",
        "earth" to "https://v.ftcdn.net/00/54/76/80/700_F_54768003_grIQFNzEUzUDXIPjpyARJDe0BOMHevxA_ST.mp4",
        "mars" to "https://v.ftcdn.net/02/15/03/24/700_F_215032499_PwFoDxZx1HfgYL3spXPg4pbeClWuWfzl_ST.mp4",
        "jupiter" to "https://v.ftcdn.net/03/06/09/57/700_F_306095714_O8t1MNAOUzJTHmUkrUMFSslf3Yru8OBN_ST.mp4",
        "saturn" to "https://v.ftcdn.net/02/75/69/23/700_F_275692310_SSbadItnvfHvtuOV4Vtl3WbM3MqGeKxw_ST.mp4",
        "uranus" to "https://v.ftcdn.net/01/43/41/74/700_F_143417468_l7XIRn5IwUNVfENznb5lOUCoTej5vJOX_ST.mp4",
        "neptune" to "https://v.ftcdn.net/03/20/52/72/700_F_320527271_vXdLkqNyZRIvrZlWpzYo3ZxlkPSvQ0cB_ST.mp4"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get references
        planetDetails = view.findViewById(R.id.planet_details)
        planetNameTV = view.findViewById(R.id.planet_name)
        planetDataTV = view.findViewById(R.id.planet_data)
        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)
        planetImageIV = view.findViewById(R.id.planet_image)
        videoButton = view.findViewById(R.id.video_button)
        video = view.findViewById(R.id.video_view)

        // Observe planet details
        viewModel.planetDetails.observe(viewLifecycleOwner) { details ->
            if(details != null){
                val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
                val selectedOption = prefs.getString(
                    getString(R.string.pref_option_key),
                    getString(R.string.pref_option_default)
                )
                when (selectedOption) {
                    "distance_light_year" -> {
                        // Display distance
                        planetNameTV.text = details.name

                        // Distance comes in scientific notation, so we need to convert it to a decimal
                        var decimalDistance: String?
                        decimalDistance = String.format("%.6f", details.distance)
                        decimalDistance += " Light Years from Earth"
                        planetDataTV.text = decimalDistance
                    }
                    "temperature" -> {
                        // Display temperature
                        planetNameTV.text = details.name
                        planetDataTV.text = details.temperature.toString() + " Kelvin"
                    }
                    "mass" -> {
                        // Display mass
                        planetNameTV.text = details.name
                        var decimalMass: String?
                        decimalMass = String.format("%.6f", details.mass)
                        decimalMass += " Jupiters"
                        planetDataTV.text = decimalMass
                    }
                    "semi_major_axis" -> {
                        // Display semi major axis
                        planetNameTV.text = details.name
                        planetDataTV.text = details.semi_major_axis.toString() + " AU"
                    }
                    "period" -> {
                        // Display orbital period
                        planetNameTV.text = details.name
                        planetDataTV.text = details.orbital_period.toString() + " Earth Days"
                    }
                }
                // Update AppBar Title based on planet name
                (activity as AppCompatActivity).supportActionBar?.title = details.name + " Details"

                // Insert image of the planet being detailed
                val planetImageResId = planetImageMap[details.name.lowercase()]
                if (planetImageResId != null) {
                    planetImageIV.setImageResource(planetImageResId)
                } else {
                    Log.d("DetailsFragment", "No image found for planet ${details.name}")
                }

                planetDetails.visibility = View.VISIBLE
                currentPlanet = details
            }
            else {
                Log.d("DetailsFragment", "Planet details is null")
            }
        }

        // Show error message when error occurs
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if(error != null){
                loadingErrorTV.text = getString(R.string.loading_error, error.message)
                loadingErrorTV.visibility = View.VISIBLE
                Log.e(tag, "Error fetching planet details: ${error.message}")
                error.printStackTrace()
            }
        }

        // Show loading indicator when loading
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if(loading){
                loadingIndicator.visibility = View.VISIBLE
                planetDetails.visibility = View.INVISIBLE
                loadingErrorTV.visibility = View.INVISIBLE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object: MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.details_menu, menu)
                    val infoItem = menu.findItem(R.id.action_info)
                    infoItem.isVisible = false
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_share -> {
                            share(currentPlanet!!)
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.STARTED
        )

        // Click listener for video button - display video when clicked
        videoButton.setOnClickListener {
            val mediaController = MediaController(requireContext())

            // get the corresponding video path for the current planet
            val videoPath = planetVideoMap[currentPlanet?.name?.lowercase()]

            // parse the video path and set the video view
            video.setVideoURI(android.net.Uri.parse(videoPath))
            video.setVideoPath(videoPath)
            video.visibility = View.VISIBLE

            // enable media controller to allow user to control video
            mediaController.setAnchorView(video)
            video.setMediaController(mediaController)

            // onPreparedListener to start video when it's ready
            video.setOnPreparedListener {
                video.start()
            }

            // onCompletionListener to close video when it's done
            video.setOnCompletionListener {
                video.visibility = View.INVISIBLE
            }
        }
    }

    override fun onResume(){
        super.onResume()
        Log.d("DetailsFragment", "In onResume")

        val planetName = arguments?.getString("planet_name")
        viewModel.loadPlanetInfo(planetName, getString(R.string.planet_api_key))
    }

    private fun share(planet: Planet){
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val selectedOption = prefs.getString(
            getString(R.string.pref_option_key),
            getString(R.string.pref_option_default)
        )
        var shareText = ""
        planetNameTV.text = planet.name
        when (selectedOption) {
            "distance_light_year" -> {
                // Distance comes in scientific notation, so we need to convert it to a decimal
                var decimalDistance: String?
                decimalDistance = String.format("%.6f", planet.distance)
                decimalDistance += " Light Years"
                shareText = getString(R.string.share_distance_text, planet.name, "Earth", decimalDistance)
            }
            "temperature" -> {
                // Display temperature
                shareText = getString(R.string.share_temperature_text, planet.name, planet.temperature.toString()) + " Kelvin"
            }
            "mass" -> {
                // Display mass
                var decimalMass = String.format("%.6f", planet.mass)
                decimalMass += " Jupiters"
                shareText = getString(R.string.share_mass_text, planet.name, decimalMass)
            }
            "semi_major_axis" -> {
                // Display semi major axis
                shareText = getString(R.string.share_axis_text, planet.name, planet.semi_major_axis.toString()) + " AU"
            }
            "period" -> {
                // Display orbital period
                shareText = getString(R.string.share_period_text, planet.name, planet.orbital_period.toString()) + " Earth Days"
            }
        }
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    }
}
