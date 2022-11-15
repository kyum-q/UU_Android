package com.example.android_sns_project.data

data class Content(var uid : String? = null, var userId : String?= null,var explain : String?= null,
                   var imageUri : String? = null, var likeCount :Int =0, var time : Long?=0,
                   var likes : Map<String, Boolean> = HashMap()){
    data class comment(var uid: String?= null, var comment: String? = null, var time : Long?=0,var date:String?=null )
}