package br.com.douglas.esports

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.douglas.esports.databinding.ItemPlayersListBinding


class PlayersListAdapter(
    private val editCallBack: (Player) -> Unit,
    private val deleteCallback: (String) -> Unit
) : ListAdapter<Player, PlayersListAdapter.PlayersViewHolder>(PlayersListAdapter) {



    class PlayersViewHolder(
        private var  binding: ItemPlayersListBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player, editCallBack: (Player) -> Unit, deleteCallback: (String) -> Unit){
            binding.txtPlayer.text = player.name
            binding.txtTeam.text = player.team?.name

            binding.btnUpdatePlayer.setOnClickListener {
                editCallBack(player)
            }

            binding.btnDeletePlayer.setOnClickListener {
                deleteCallback(player.id.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {
        val itemBinding = ItemPlayersListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayersViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        val team = getItem(position)
        holder.bind(team, editCallBack, deleteCallback)
    }

    private companion object: DiffUtil.ItemCallback<Player>(){

        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return  oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return  oldItem == newItem
        }


    }

}


