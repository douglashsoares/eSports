package br.com.douglas.esports

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import br.com.douglas.esports.databinding.PlayerEditBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerEditBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: PlayerEditBottomSheetBinding
    private val args: PlayerEditBottomSheetArgs by navArgs()

    val spinnerAdapter by lazy {
        ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_dropdown_item)
    }

    val teamsList = mutableListOf<Team>()
    var teamSelected: Team? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlayerEditBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtPlayer.setText(args.player.name)


        binding.btnUpdatePlayer.setOnClickListener {
            putPlayer(
                args.player.id,
                Player(
                    name = binding.edtPlayer.text.toString(),
                    teamId = teamSelected?.id
                )
            )
        }

        setupSpinner()
        getTeams()

    }

    private fun setupSpinner() {
        binding.spUpdatePlayer.adapter = spinnerAdapter

        spinnerAdapter.add("Sem Time")

        binding.spUpdatePlayer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    private fun getTeams(){
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
                args.player.team?.let {playerTeam ->
                   val playerTeamFind = teamsList.find {
                        it.id == playerTeam.id
                    }
                   val index =  teamsList.indexOf(playerTeamFind) + 1
                    binding.spUpdatePlayer.setSelection(index)
                }
            }
        })
    }

    private fun putPlayer(playerId: Int?, player: Player) {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance()

        val endpoint = retrofitClient.create(PlayersEndpoint::class.java)
        val callback = endpoint.putPlayer(playerId.toString(), player)

        callback.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                setNavigationResult("")
                dismiss()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

}