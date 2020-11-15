package com.boyko.imgur

object Constants {
    /*
      Logging flag
     */
    const val LOGGING = false

    /*
      Your imgur client id. You need this to upload to imgur.

      More here: https://api.imgur.com/
     */
    const val MY_IMGUR_CLIENT_ID = "0553d510825b4de"


    /*
      Client Auth
     */
    val clientAuth: String
        get() = "Client-ID $MY_IMGUR_CLIENT_ID"

}
