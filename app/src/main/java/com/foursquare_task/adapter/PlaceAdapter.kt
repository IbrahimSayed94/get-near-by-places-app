package com.foursquare_task.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foursquare_task.R
import com.foursquare_task.network.responsemodel.VenuesItem
import kotlinx.android.synthetic.main.item_place.view.*

class PlaceAdapter(val context: Context,var placeList:List<VenuesItem>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>()
{

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        internal var txt_place_name: TextView = itemView.place_name
        internal var txt_place_address: TextView = itemView.place_address
        internal var place_image: ImageView = itemView.place_image

    } // class of ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_place, parent, false)
        return ViewHolder(v)

    } // createViewHolder

    override fun getItemCount(): Int {
       return  placeList.size
    } // ItemCount

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val place = placeList.get(position)

        holder.txt_place_name.text= place.name
        holder.txt_place_address.text = place.location.address

        try {

            val imageUrl =
                place.categories?.get(0)?.icon?.prefix + "100" + place.categories?.get(0)?.icon?.suffix
            Glide.with(context)
                .load(imageUrl)
                .into(holder.place_image)
        }catch (e: Exception)
        {
            e.printStackTrace()
        }

        animate(holder)

    } // bindViewHolder

    private fun animate(viewHolder: RecyclerView.ViewHolder) {
        val animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        viewHolder.itemView.animation = animAnticipateOvershoot
    } // fun of animate



} // class of PlaceAdapter