package br.com.douglas.esports

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import br.com.douglas.esports.databinding.PlayerAddBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerAddBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: PlayerAddBottomSheetBinding
    val spinnerAdapter by lazy {
        ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
    }

    val teamsList = mutableListOf<Team>()

    var teamSelected: Team? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlayerAddBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddPlayer.setOnClickListener {
            val playerName = binding.edtPlayer.text
            val player = Player(playerName.toString())

            postPlayer(player)
        }

        setupSpinner()
        getTeams()
    }

    private fun setupSpinner() {

        binding.spJogador.adapter = spinnerAdapter

        spinnerAdapter.add("Sem Time")

        binding.spJogador.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = parent.getItemAtPosition(position)
                val teamNameSelected = teamsList.find {
                    it.name == item
                }

                if (teamNameSelected != null){
                    teamSelected = teamNameSelected
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun getTeams() {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance()

        val endpoint = retrofitClient.create(TeamEndpoint::class.java)
        val callback = endpoint.getTeams()

        callback.enqueue(object : Callback<List<Team>> {

            override fun onFailure(call: Call<List<Team>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Team>>, response: Response<List<Team>>) {
                teamsList.addAll(response.body() ?: emptyList())

                val teamNamesList = teamsList.map {
                    it.name
                }

                spinnerAdapter.addAll(teamNamesList)

            }
        })

    }

    private fun postPlayer(player: Player) {

        teamSelected?.let {
            player.teamId = it.id
        }

        val retrofitClient = NetworkUtils.getRetrofitInstance()

        val endpoint = retrofitClient.create(PlayersEndpoint::class.java)
        val callback = endpoint.postPlayer(player)

        callback.enqueue(
            object : Callback<Player> {
                override fun onFailure(call: Call<Player>, t: Throwable) {
                    Log.i("Salguod", "Error")
                }

                override fun onResponse(call: Call<Player>, response: Response<Player>) {
                    setNavigationResult("")
                    dismiss()
                }
            }
        )
    }
}