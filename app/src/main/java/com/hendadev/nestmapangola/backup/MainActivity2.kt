//package com.hendadev.nestmapangola.backup
//
//import android.location.Location
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.DefaultLifecycleObserver
//import androidx.lifecycle.LifecycleOwner
//import com.mapbox.maps.MapView
//import com.mapbox.maps.Style
//import com.mapbox.navigation.base.options.NavigationOptions
//import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
//import com.mapbox.navigation.core.trip.session.LocationMatcherResult
//import com.mapbox.navigation.core.trip.session.LocationObserver
//import com.mapbox.navigation.ui.maps.NavigationStyles
//import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
//
//class MainActivity2 : AppCompatActivity() {
//    lateinit var mapView: MapView
//    private val navigationLocationProvider = NavigationLocationProvider()
//
//    init {
//        lifecycle.addObserver(object : DefaultLifecycleObserver {
//            override fun onResume(owner: LifecycleOwner) {
//                MapboxNavigationApp.attach(owner)
//            }
//
//            override fun onPause(owner: LifecycleOwner) {
//                MapboxNavigationApp.detach(owner)
//            }
//        })
//    }
//
//    private val locationObserver = object : LocationObserver {
//        /**
//         * Invoked as soon as the [Location] is available.
//         */
//        override fun onNewRawLocation(rawLocation: Location) {
//// Not implemented in this example. However, if you want you can also
//// use this callback to get location updates, but as the name suggests
//// these are raw location updates which are usually noisy.
//        }
//
//        /**
//         * Provides the best possible location update, snapped to the route or
//         * map-matched to the road if possible.
//         */
//        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
//            val enhancedLocation = locationMatcherResult.enhancedLocation
//            navigationLocationProvider.changePosition(
//                enhancedLocation,
//                locationMatcherResult.keyPoints,
//            )
//// Invoke this method to move the camera to your current location.
//            updateCamera(enhancedLocation)
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        mapView = findViewById(R.id.mapView)
//
//        if (!MapboxNavigationApp.isSetup()) {
//            MapboxNavigationApp.setup {
//                NavigationOptions.Builder(this)
//                    .accessToken(getString(R.string.mapbox_access_token))
//                    .build()
//            }
//        }
//        mapView.getMapboxMap().loadStyleUri(NavigationStyles.NAVIGATION_DAY_STYLE)
//    }
//}