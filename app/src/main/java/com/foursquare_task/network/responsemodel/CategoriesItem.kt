package com.foursquare_task.network.responsemodel

data class CategoriesItem(val pluralName: String = "",
                          val name: String = "",
                          val icon: Icon,
                          val id: String = "",
                          val shortName: String = "",
                          val primary: Boolean = false)