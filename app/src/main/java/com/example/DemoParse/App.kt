package com.example.DemoParse

import android.app.Application
import android.util.Log
import com.parse.Parse
import com.parse.ParseObject


//SPeaks to the database, needs to be the first thing to run when app runs
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        ParseObject.registerSubclass(Post::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());

    }

}