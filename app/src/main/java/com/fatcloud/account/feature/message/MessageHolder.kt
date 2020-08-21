package com.fatcloud.account.feature.message

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.list.BaseItemViewHolder
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.message.Message
import com.fatcloud.account.extend.RoundTransFormation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_message.*

/**
 * Created by Wangsw on 2020/7/30 0030 16:13.
 * </br>
 *
 */
class MessageHolder(parent: ViewGroup?) : BaseItemViewHolder<Message>(parent, R.layout.item_message), LayoutContainer {

    override val containerView: View? get() = itemView


    override fun setData(data: Message?) {
        if (data == null) {
            return
        }
        title_tv.text = data.title
        content_tv.text = data.content

        Glide.with(context).load(image_iv).error(R.drawable.ic_image_p1).into(image_iv)


        when (data.readFlag) {
            Constants.READ0 -> {
                new_flag_tv.visibility = View.VISIBLE
            }
            Constants.READ1 -> {
                new_flag_tv.visibility = View.GONE
            }
            else -> {
            }
        }


    }

}