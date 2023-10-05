package com.example.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagramclone.Models.User
import com.example.instagramclone.databinding.ActivitySignupBinding
import com.example.instagramclone.utils.USER_NODE
import com.example.instagramclone.utils.USER_PROFILE_FOLDER
import com.example.instagramclone.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class Signup : AppCompatActivity() {
    val binding by lazy {
  ActivitySignupBinding.inflate(layoutInflater)
    }
    lateinit var user: User
   private val launcher= registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->
        uri?.let {
           uploadImage(uri, USER_PROFILE_FOLDER){
               if (it!=null){ user. image=it
                   binding.profileImage.setImageURI(uri)
               }

            }
        }}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val text = "Already have an Accounts</font> <font color=#1E88E5>Login ?</font>"
        binding.textlogin.setText(Html.fromHtml(text))
        user = User()
        if (intent. hasExtra(  "MODE" )) {
            if (intent. getIntExtra("MODE",  - 1) ==1) {
            binding.registerbtn.text="Update Profile"
            Firebase.firestore.collection (USER_NODE).document(Firebase.auth.currentUser!!.uid) .get ()
                .addOnSuccessListener {
                   user = it.toObject<User>()!!
                    if (!user.image.isNullOrEmpty()){
                        Picasso.get().load(user.image).into(binding.profileImage)
                    }
                    binding.namelogin.editText?.setText (user.name)
                    binding.emaillogin.editText?.setText (user .email)


                }}}
        binding.registerbtn.setOnClickListener() {
            if (intent. hasExtra(  "MODE" )) {
                if (intent. getIntExtra("MODE",  - 1) ==1) {
                    Firebase.firestore.collection (  USER_NODE )
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this@Signup, HomeActivity::class.java))
                            finish()}
                }}else {

                if (binding.namelogin.editText?.text.toString().equals("") or
                    binding.emaillogin.editText?.text.toString().equals("") or
                    binding.passwordlogin.editText?.text.toString().equals("")
                ) {
                    Toast.makeText(this@Signup, "Please fill all info", Toast.LENGTH_SHORT).show()
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.emaillogin.editText?.text.toString(),
                        binding.passwordlogin.editText?.text.toString()
                    ).addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            user.name = binding.namelogin.editText?.text.toString()
                            user.password = binding.passwordlogin.editText?.text.toString()
                            user.email = binding.emaillogin.editText?.text.toString()
                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    startActivity(Intent(this@Signup, HomeActivity::class.java))
                                    finish()
                                }
                        } else {
                            Toast.makeText(
                                this@Signup,
                                result.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                }


            }

    }
        binding.addimage.setOnClickListener{
        launcher.launch(  "image/*")}
        binding.textlogin.setOnClickListener {
            startActivity(Intent (  this@Signup, LoginActivity::class.java))
            finish()}
}}