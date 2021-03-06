package com.sohayb.contactsProjects

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sohayb.contactsProjects.Database.DataBaseHandler
import com.sohayb.contactsProjects.Model.Contact
import kotlinx.android.synthetic.main.content_main.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    val context = this
    var db = DataBaseHandler(context)
    // @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var bunldes: Bundle? = intent.extras

        //var contacts=DataSource.GetDataSet(this,db) //get(this,"Contacts.json")

        db.getAllData()?.let { initRecyclerView(it) } ?: Log.i("tag", "contacts is null")


        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->

            /* var t=db.getAllData()
             for( n in t!!){
                 Log.i("print",n!!.Nom)
             }*/
            val destination = Intent(this@MainActivity, CreateNewContact::class.java)
            startActivityForResult(destination, 4)
        }
    }


    private fun initRecyclerView(contacts: ArrayList<Contact?>) {


        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            val topSpacingDecorator = TopSpacingItemDecoration(16)
            addItemDecoration(topSpacingDecorator)
            adapter = ContactRecyclerAdapter(contacts)
            (adapter as ContactRecyclerAdapter).notifyDataSetChanged()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            Toast.makeText(this, "Done back", Toast.LENGTH_LONG).show()
        }
        if (requestCode == 23) {
            Toast.makeText(this, "Done BACKKK FROM 23", Toast.LENGTH_LONG).show()
            if (resultCode == Activity.RESULT_OK) { // Get String data from Intent
                val nom = data!!.getStringExtra("ContactSurname")
                val prenom = data.getStringExtra("ContactName")
                val address = data.getStringExtra("ContactAddress")
                val numero = data.getStringExtra("ContactNumber")
                val image = data.getStringExtra("ContactImage")

                //contacts!!.add(0, Contact(nom!!, prenom!!, numero!!, image!!, address!!))
                //toastS(image)
                //@TODO IMPLEMENT MODIFY CONTACT
                // var fileUri: Uri = Uri.parse(image)
                //recycler_view.adapter!!.notifyDataSetChanged()
                db.getAllData()?.let { initRecyclerView(it) }

            }
        }
        if (requestCode == 4) {

            if (resultCode == Activity.RESULT_OK) { // Get String data from Intent
                val nom = data!!.getStringExtra("ContactSurname")
                val prenom = data.getStringExtra("ContactName")
                val address = data.getStringExtra("ContactAddress")
                val numero = data.getStringExtra("ContactNumber")
                val image = data.getStringExtra("ContactImage")

                var contact = Contact(nom!!, prenom!!, numero!!, image!!, address!!)
                //var db = DataBaseHandler(context)
                db.insertData(contact)
                db.getAllData()?.let { initRecyclerView(it) }
                //contacts!!.add(0, Contact(nom!!, prenom!!, numero!!, image!!, address!!))
                //toastS(image)

                // var fileUri: Uri = Uri.parse(image)
                //recycler_view.adapter!!.notifyDataSetChanged()
            }
        }
    }

    fun Context.toastL(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun Context.toastS(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }




}
