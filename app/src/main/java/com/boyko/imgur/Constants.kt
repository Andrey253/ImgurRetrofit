package com.boyko.imgur

object Constants {
    /*
      Logging flag
     */
    val LOGGING = false

    /*
      Your imgur client id. You need this to upload to imgur.

      More here: https://api.imgur.com/
     */
    val MY_IMGUR_CLIENT_ID = "0553d510825b4de"
    val MY_IMGUR_CLIENT_SECRET = ""

    /*
      Redirect URL for android.
     */
    val MY_IMGUR_REDIRECT_URL = "https://www.getpostman.com/oauth2/callback"

    /*
      Client Auth
     */
    val clientAuth: String
        get() = "Client-ID $MY_IMGUR_CLIENT_ID"

}
