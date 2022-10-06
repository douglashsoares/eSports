package br.com.douglas.esports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                    name = binding.edtPlayer.text.toString()
                )
            )
        }

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