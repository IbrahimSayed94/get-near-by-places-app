package com.foursquare_task.network.responsemodel

data class VenuesItem(val hasPerk: Boolean = false,
                      val venuePage: VenuePage,
                      val referralId: String = "",
                      val name: String = "",
                      val location: Location,
                      val id: String = "",
                      val categories: List<CategoriesItem>?)