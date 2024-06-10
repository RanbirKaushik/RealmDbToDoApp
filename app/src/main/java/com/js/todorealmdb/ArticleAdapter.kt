package com.js.todorealmdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.js.todorealmdb.model.ArticleModel
import com.pranavpandey.android.dynamic.toasts.DynamicToast

class ArticleAdapter: ListAdapter<ArticleModel,ArticleViewHolder>(MyDiffUtil) {

    private var onActionClick : OnActionClick? = null

    object MyDiffUtil : DiffUtil.ItemCallback<ArticleModel>(){
        override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return  oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return  ArticleViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        onActionClick?.let { holder.bindView(getItem(position), it) }
    }

    fun onActionSetClickListner(onActionClick: OnActionClick){
        this.onActionClick = onActionClick
    }

}

class ArticleViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {

    private lateinit var tvTitle : AppCompatTextView
    private lateinit var tvStatus : AppCompatTextView
    private lateinit var checkBox: CheckBox
    private lateinit var ivDelete : AppCompatImageView
    private lateinit var ivEdit : AppCompatImageView


    fun bindView(article : ArticleModel, onActionClick: OnActionClick){
        tvTitle = view.findViewById(R.id.tvTitle)
        tvStatus = view.findViewById(R.id.tvStatus)
        checkBox = view.findViewById(R.id.checkBoxViewHolder)
        ivDelete = view.findViewById(R.id.ivDelete)
        ivEdit = view.findViewById(R.id.ivEdit)

        tvTitle.text = article.title
        checkBox.isChecked = article.completed == true

        if (checkBox.isChecked){
            tvStatus.text = "Completed!"
        }else{
            tvStatus.text = "Pending!"
        }

        ivDelete.setOnClickListener {

            onActionClick.onClickDelete(it,article)
        }

        ivEdit.setOnClickListener {

            onActionClick.onClickUpdate(it,article)
        }

    }

    companion object {
        const val LAYOUT = R.layout.article_view_holder
        fun create(parent: ViewGroup) = ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(LAYOUT,parent,false)
        )
    }

}


interface OnActionClick{
    fun onClickDelete(view: View, article: ArticleModel)

    fun onClickUpdate(view: View, article: ArticleModel)
}