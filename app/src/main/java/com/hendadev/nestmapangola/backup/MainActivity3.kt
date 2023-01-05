//package com.hendadev.nestmapangola.backup
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.location.Location
//import android.os.Bundle
//import android.support.v4.app.ActivityCompat
//import android.support.v7.app.AppCompatActivity
//import com.mapbox.android.core.location.LocationEngine
//import com.mapbox.android.core.location.LocationEngineListener
//import com.mapbox.android.core.location.LocationEnginePriority
//import com.mapbox.android.core.location.LocationEngineProvider
//import com.mapbox.android.core.permissions.PermissionsListener
//import com.mapbox.android.core.permissions.PermissionsManager
//import com.mapbox.mapboxsdk.Mapbox
//import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
//import com.mapbox.mapboxsdk.geometry.LatLng
//import com.mapbox.mapboxsdk.maps.MapView
//import com.mapbox.mapboxsdk.maps.MapboxMap
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
//import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
//import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
//import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
//
//
//class MainActivity3 : AppCompatActivity(), OnMapReadyCallback, LocationEngineListener,
//    PermissionsListener {
//    private lateinit var mapView: MapView
//    private lateinit var map: MapboxMap
//    lateinit var locationEngine: LocationEngine
//    lateinit var locationLayerPlugin: LocationLayerPlugin
//    lateinit var permissionsManager: PermissionsManager
//    lateinit var originLayout: Location
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState);
//        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
//        setContentView(R.layout.activity_main);
//        mapView = findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this);
//    }
//
//    override fun onMapReady(mapboxMap: MapboxMap) {
//        /* LocationPluginActivity.this.map = map;
//        enableLocationPlugin();*/
//        map = mapboxMap
//        locationEnable()
//        mapboxMap.uiSettings.isZoomControlsEnabled = true
//        mapboxMap.uiSettings.isZoomGesturesEnabled = true
//        mapboxMap.uiSettings.isScrollGesturesEnabled = true
//        mapboxMap.uiSettings.setAllGesturesEnabled(true)
//    }
//
//    private fun locationEnable() {
//        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//            intialLocationEngine()
//            intializLocationLayer()
//        } else {
//            permissionsManager = PermissionsManager(this)
//            permissionsManager.requestLocationPermissions(this)
//        }
//    }
//
//    private fun intialLocationEngine() {
//        locationEngine = LocationEngineProvider(this).obtainBestLocationEngineAvailable()
//        locationEngine.priority = LocationEnginePriority.HIGH_ACCURACY
//        locationEngine.activate()
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        val lastLocation = locationEngine.lastLocation
//        if (lastLocation != null) {
//            originLayout = lastLocation
//            setCamerpostion(lastLocation)
//        } else {
//            locationEngine.addLocationEngineListener(this)
//        }
//    }
//
//    fun intializLocationLayer() {
//        locationLayerPlugin = LocationLayerPlugin(mapView, map, locationEngine)
//        locationLayerPlugin.isLocationLayerEnabled = true
//        locationLayerPlugin.cameraMode = CameraMode.TRACKING
//        locationLayerPlugin.renderMode = RenderMode.NORMAL
//    }
//
//    fun setCamerpostion(camerpostion: Location) {
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(camerpostion.latitude,
//            camerpostion.longitude), 13.0))
//    }
//
//    override fun onConnected() {
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        locationEngine.requestLocationUpdates()
//    }
//
//    override fun onLocationChanged(location: Location) {
//        originLayout = location
//        setCamerpostion(location)
//    }
//
//    override fun onExplanationNeeded(permissionsToExplain: List<String?>?) {}
//
//    override fun onPermissionResult(granted: Boolean) {
//        if (granted) {
//            locationEnable()
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray,
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//    }
//
//    override fun onStart() {
//        super.onStart()
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        locationEngine.requestLocationUpdates()
//        mapView.onStart()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        mapView.onResume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        mapView.onPause()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        mapView.onStop()
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//        mapView.onLowMemory()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        locationEngine.deactivate()
//        mapView.onDestroy()
//    }
////
////    protected fun onSaveInstanceState(outState: Bundle?) {
////        super.onSaveInstanceState(outState!!)
////        mapView.onSaveInstanceState(outState)
////    }
//}
//
