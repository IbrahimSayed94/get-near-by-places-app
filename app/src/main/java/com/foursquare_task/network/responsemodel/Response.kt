package com.foursquare_task.network.responsemodel

data class Response(val confident: Boolean = false,
                    val venues: List<VenuesItem>?)