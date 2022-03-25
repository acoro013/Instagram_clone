package com.example.DemoParse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //check if user is logged in
        //if they are then take em to MainActivity
        if (ParseUser.getCurrentUser() != null ){
            goToMainActivity()
        }

        findViewById<Button>(R.id.btnLogIn).setOnClickListener{
            val username = findViewById<EditText>(R.id.tvUsername).text.toString()
            val password = findViewById<EditText>(R.id.tvPassword).text.toString()
            loginUser(username, password)
        }


        findViewById<Button>(R.id.btnSignUp).setOnClickListener{
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

    }


    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                Log.i(TAG, "Successfully log in")
                goToMainActivity()
            } else {
                Toast.makeText(this, "Error loggin in", Toast.LENGTH_SHORT).show()
            }})
        )
    }

    private fun goToMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        //closes logInActivity
        finish()
    }

    companion object{
        const val TAG = "LogInActivity"
    }
}