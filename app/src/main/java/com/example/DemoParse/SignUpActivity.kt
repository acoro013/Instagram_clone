package com.example.DemoParse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        findViewById<Button>(R.id.btnAccount).setOnClickListener{
            val username = findViewById<EditText>(R.id.tvUserUsername).text.toString()
            val password = findViewById<EditText>(R.id.tvUserPassword).text.toString()
            signUpUser(username, password)
        }
    }

    //code Used to sign up a user
    private fun signUpUser(username: String, password: String){
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // User has succesfully signed up
                    /*
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(intent)
                     */
                Toast.makeText(this, "Account has been created", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                // Sign up didn't succeed. Look at the ParseException
                e.printStackTrace()
            }
        }
    }


}