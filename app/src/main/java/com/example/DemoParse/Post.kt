package com.example.DemoParse

import com.parse.*

//This is associated with the actual post class in back4app
//Every post has a Description : string, image : File and User : User
@ParseClassName("Post")
class Post : ParseObject(){

    //getters and setters for description
    fun getDescription() : String?{
        //gives access to the keys we want in back4app
        return getString(KEY_DESCRIPTION)
    }
    fun setDescription(description: String){
        put(KEY_DESCRIPTION,description)
    }


    //getters and setter for image
    fun getImage(): ParseFile?{
        return getParseFile(KEY_IMAGE)
    }
    fun setImage(parsefile: ParseFile){
        put(KEY_IMAGE, parsefile)
    }


    //getters and setters for User
    fun getUser() : ParseUser?{
        return getParseUser(KEY_USER)
    }
    fun setUser(user:ParseUser){
        put(KEY_USER, user)
    }


    companion object{
        const val KEY_DESCRIPTION ="description"
        const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
    }
}