package com.chiore.chiorenews.data

sealed class Category(val category: String) {

    object Business: Category("business")
    object Entertainment: Category("entertainment")
    object Health: Category("health")
    object Science: Category("science")
    object Sports: Category("sports")
    object Technology: Category("technology")

}
