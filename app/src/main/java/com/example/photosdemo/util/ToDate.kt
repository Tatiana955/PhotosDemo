package com.example.photosdemo.util

import java.util.*

fun Int.toDate(): Date = Date(this.toLong() * 1000L)