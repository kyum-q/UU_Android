package com.example.android_sns_project.data

data class UserInfo (
    var email: String? = null,
    var nickname: String? = null,
    var followingCount: Int ?= 0 ,
    var followerCount: Int ?= 0,
    var profileImagePath : String ?= null
)