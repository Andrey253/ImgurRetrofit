package com.boyko.imgur.model

import java.io.File

/*
 * Basic object for upload.
 */
class Upload {
    var image: File? = null
    var title: String? = null
    var description: String? = null
    var albumId: String? = null
}
