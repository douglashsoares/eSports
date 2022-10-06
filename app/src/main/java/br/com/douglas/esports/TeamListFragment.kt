package br.com.douglas.esports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.douglas.esports.databinding.FragmentTeamListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TeamListFragment : Fragment() {

    private lateinit var binding: FragmentTeamListBinding
    private lateinit var adapter: TeamListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTeamListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TeamListAdapter()
        binding.rvTeamList.adapter = adapter

        getTeams()

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
                val teamList = response.body()
                adapter.submitList(teamList)
            }
        })
    }
}