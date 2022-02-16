package com.example.android.politicalpreparedness.models

import com.example.android.politicalpreparedness.models.Office
import com.example.android.politicalpreparedness.models.Official

data class Representative (
        val official: Official,
        val office: Office
)