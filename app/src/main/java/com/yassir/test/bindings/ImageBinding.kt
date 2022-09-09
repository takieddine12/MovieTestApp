package com.yassir.test.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.yassir.test.Utils

object ImageBinding {

    @BindingAdapter("Img")
    @JvmStatic
    fun showImgFromUrl(view  : ImageView , link : String?){
        Picasso.get().load(Utils.IMAGE_URL + link).into(view)
    }
}