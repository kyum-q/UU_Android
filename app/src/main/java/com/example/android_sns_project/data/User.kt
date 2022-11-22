package com.example.android_sns_project.data

data class User (
    var key: String? = null,
    var email: String? = null,
    var password: String? = null,
    var name: String? = null,
    var nickname: String? = null,
    var followerCount : Int?=0,
    var followingCount : Int?=0
)