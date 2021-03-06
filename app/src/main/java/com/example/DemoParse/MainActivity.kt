package com.example.DemoParse

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.parse.*
import java.io.File

/*
where user creates a post. By taking photo with their camera
 */


class MainActivity : AppCompatActivity() {

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //UI needed for main_activity
        // 1 Set description of post
        // 2 button to lauch the camera to take a pic
        // 3 an Image View to show the picture the user has taken
        // 4 A button to save and send the post to our parse server

        findViewById<Button>(R.id.btnPost).setOnClickListener{
            //send post to server, without image
            // get description they have inputted and the current user
            val description = findViewById<EditText>(R.id.tvDescription).text.toString()
            val user = ParseUser.getCurrentUser()
            if(photoFile != null){
                submitPost(description, user, photoFile!!)
            }else{
                Log.e(TAG, "Error taking photo")
            }
        }

        findViewById<Button>(R.id.btnTakePhoto).setOnClickListener{
            // launch camera to let user take a picture
            onLaunchCamera()
        }

        findViewById<Button>(R.id.btnLogOut).setOnClickListener{
            ParseUser.logOut()
            val currentUser = ParseUser.getCurrentUser() // this will now be null
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)

        }

        queryPosts()
    }



    //needs a description, and user
    //send a post object to out parse
    fun submitPost(description: String, user: ParseUser, file: File) {
        //create the post object
        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        //saveInBackground, takes parse object, sends to parse server, makes sure UI dont freeze
        post.saveInBackground{ exception ->
            if(exception != null){
                //something went wrong
                Log.e(TAG, "Error while saving post")
                exception.printStackTrace()
                //TODO show toast to tell user whats wrong
            }else {
                Log.i(TAG, "Successfuly posted post")
                //TODO reset the ediText field to be empty
                //TODO reset the image view to be empty
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                val ivPreview: ImageView = findViewById(R.id.ivPicture)
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }


    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }



    //query to gather all post in my server
    fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        //tells parse to give us our user also
        query.include(Post.KEY_USER)
        //find all posts
        query.findInBackground(object: FindCallback<Post>{
            //done tells parse to find all post objects that are in server and return all those objects to us
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                //and then We decide what to do with them
                if (e != null){
                    //something went wrong
                    Log.e(TAG, "Something Went wrong")
                } else{
                    if(posts != null){
                        for(post in posts){
                            Log.i(TAG, "Post" + post.getDescription() + " , username: " +
                            post.getUser()?.username)
                        }
                    }
                }

            }
        })
    }

    companion object{
        const val TAG = "MainActivity"
    }
}