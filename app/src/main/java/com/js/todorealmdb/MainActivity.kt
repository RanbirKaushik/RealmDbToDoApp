package com.js.todorealmdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.js.todorealmdb.model.ArticleModel
import com.js.todorealmdb.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var edTitle: AppCompatEditText

    private lateinit var btnAddNew: ImageView

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private var articleAdapter: ArticleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initView()

        getArticles()

        viewModel.allArticle.observe(this) {
            articleAdapter?.submitList(it)
        }


        btnAddNew.setOnClickListener { addNewArticle(ArticleModel()) }


        articleAdapter?.onActionSetClickListner(object : OnActionClick {
            override fun onClickDelete(view: View, article: ArticleModel) {
                deleteArticle(article, this@MainActivity)
            }

            override fun onClickUpdate(view: View, article: ArticleModel) {
                updateArticle(article, this@MainActivity)
            }

        })

    }

    private fun addNewArticle(article: ArticleModel) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.layout_dialog_input, null)
        val edDialogTitle = dialogView.findViewById<AppCompatEditText>(R.id.edDialogTitle)
        val checkBoxAddNew = dialogView.findViewById<CheckBox>(R.id.checkBox)
        val btnUpdate = dialogView.findViewById<Button>(R.id.updateBtn)

        edDialogTitle.setText(article.title)
        checkBoxAddNew.isChecked = article.completed == true


        val builder = AlertDialog.Builder(this)

        builder.setView(dialogView)

        builder.setTitle(null)
        builder.setMessage(null)

        val alertDialog = builder.create()
        alertDialog.show()

        btnUpdate.setOnClickListener {
            val id = article.id.toString()
            val title = edDialogTitle.text.toString()
            val checkBox2 = checkBoxAddNew.isChecked

            viewModel.addArticle(title, checkBox2, this@MainActivity).apply {
                getArticles()
                alertDialog.dismiss()
            }
        }

        // getArticles()
    }


    private fun updateArticle(article: ArticleModel, mainActivity: MainActivity) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.layout_dialog_input, null)
        val edDialogTitle = dialogView.findViewById<AppCompatEditText>(R.id.edDialogTitle)
        val checkBoxUpdateTodo = dialogView.findViewById<CheckBox>(R.id.checkBox)
        val btnUpdate = dialogView.findViewById<Button>(R.id.updateBtn)

        edDialogTitle.setText(article.title)
        checkBoxUpdateTodo.isChecked = article.completed == true

        val builder = AlertDialog.Builder(this)

        builder.setView(dialogView)

        builder.setTitle(null)
        builder.setMessage(null)

        val alertDialog = builder.create()
        alertDialog.show()

        btnUpdate.setOnClickListener {
            val id = article.id.toString()
            val title = edDialogTitle.text.toString()
            val check2 = checkBoxUpdateTodo.isChecked

            viewModel.updateArticle(id, title, check2, mainActivity)

            getArticles()
            alertDialog.dismiss()
        }
    }

    private fun deleteArticle(articleModel: ArticleModel, activity: MainActivity) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure you want to delete the item")
        builder.setIcon(R.drawable.baseline_delete_forever_24)
        builder.setPositiveButton("Yes") { d, _ ->

            viewModel.deleteArticle(articleModel.id.orEmpty(), activity)
            viewModel.getAllArticles()
            d.dismiss()
        }

        builder.setNegativeButton("No") { d, _ ->
            d.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun getArticles() {
        viewModel.getAllArticles()
    }

    private fun initView() {

        btnAddNew = findViewById(R.id.addTodo)
        recyclerView = findViewById(R.id.rvArticles)

        recyclerView.layoutManager = LinearLayoutManager(this)
        articleAdapter = ArticleAdapter()
        recyclerView.adapter = articleAdapter
    }
}