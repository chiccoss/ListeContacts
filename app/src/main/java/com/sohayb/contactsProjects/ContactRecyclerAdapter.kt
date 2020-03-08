package com.sohayb.contactsProjects

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sohayb.contactsProjects.Model.Contact
import kotlinx.android.synthetic.main.contacts_view.view.*
import kotlin.collections.ArrayList


class ContactRecyclerAdapter(val contacts: ArrayList<Contact>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.getContext();
        return ContactsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.contacts_view,
                parent,
                false
            )
        )


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContactsViewHolder -> {
                holder.bind(contacts.get(position), position, contacts)
            }
        }
    }

    override fun getItemCount(): Int = contacts.size


    inner class ContactsViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val contactName = itemView.nameSurname
        val ContactImage = itemView.ContactImages


        fun bind(
            contact: Contact,
            position: Int,
            contacts: ArrayList<Contact>
        ) {


            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            val fileUri: Uri?

            if (contact.image.contains("http")) {
                Glide.with(itemView.context)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(contact.image)
                    .into(ContactImage)
            } else {

                fileUri = Uri.parse(contact.image)
                ContactImage.setImageURI(fileUri)
            }


            //Picasso.get().load(contact.image).into(profile_image);
            contactName.text = contact.Nom + " " + contact.Prenom
            // blog_author.setText(blogPost.username)


            itemView.setOnClickListener {
                /*  val destination = Intent(context, ShowContact::class.java)
                      .also { it.putExtra("name", contactName?.text)
                           }
                  (context as Activity).startActivityForResult(destination, 1)*/
                val destination = Intent(context, ViewContact::class.java).apply {
                    putExtra("ContactName", contact.Nom)
                    putExtra("ContactSurname", contact.Prenom)
                    putExtra("ContactAddress", contact.address)
                    putExtra("ContactImage", contact.image)
                    putExtra("ContactNumber", contact.phoneNum)
                }
                (context as Activity).startActivity(destination)
                Toast.makeText(it!!.context, position.toString(), Toast.LENGTH_SHORT).show()
            }

            itemView.setOnLongClickListener {
                val items =
                    arrayOf("Edit", "View", "Place on home screen", "Delete", "Call")

                val builder =
                    context?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.setTitle("Select an option")
                builder?.setItems(
                    items
                ) { _, which ->//dialog , which
                    testSelectedItem(
                        items[which],
                        contact,
                        contacts,
                        context,
                        this@ContactRecyclerAdapter
                    )
                    //Toast.makeText(it.context, WindowOptions[which], Toast.LENGTH_SHORT).show()
                }
                builder?.show()

                Toast.makeText(it.context, "long click", Toast.LENGTH_SHORT).show()
                true
            }


        }


    }
}


fun testSelectedItem(
    option: String,
    contact: Contact,
    contacts: ArrayList<Contact>,
    context: Context?,
    contactRecyclerAdapter: ContactRecyclerAdapter
) {
    if (option == "Delete") {
        contacts.remove(contact)
        contactRecyclerAdapter.notifyDataSetChanged()
    }

    if (option == "Edit") {
        val destination = Intent(context, ModifyContact::class.java).apply {
            putExtra("ContactName", contact.Prenom)
            putExtra("ContactSurname", contact.Nom)
            putExtra("ContactAddress", contact.address)
            putExtra("ContactImage", contact.image)
            putExtra("ContactNumber", contact.phoneNum)
        }
        (context as Activity).startActivityForResult(destination, 23)
    }

    if (option == "View") {
        val destination = Intent(context, ViewContact::class.java).apply {
            putExtra("ContactName", contact.Prenom)
            putExtra("ContactSurname", contact.Nom)
            putExtra("ContactAddress", contact.address)
            putExtra("ContactImage", contact.image)
            putExtra("ContactNumber", contact.phoneNum)
        }
        (context as Activity).startActivity(destination)
    }

    if (option == "Place on home screen") {
        //@TODO contacts.PlaceOnHomeScreen(contact)
    }

    if (option == "Call") {

        callPhone(context, contact.phoneNum) //contact.phoneNum)
    }

}

fun callPhone(context: Context?, phoneNum: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNum")
    (context as Activity).startActivity(intent)

}