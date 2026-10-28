package com.example.movilaw.model

data class ResourceItem(
    val id: String,
    val referenceTitle: String,
    val legalCategory: String,
    val content: String,
    val lastModificationYear: Int,
    var isHighlighted: Boolean = false,
    val isUpdated: Boolean = true
)
