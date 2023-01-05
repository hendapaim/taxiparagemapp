package com.hendadev.nestmapangola

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.hendadev.nestmapangola.utils.LocationPermissionHelper
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.lifecycle.MapboxNavigationObserver
import com.mapbox.navigation.core.lifecycle.requireMapboxNavigation
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.ui.maps.NavigationStyles
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    companion object {
        private const val LATITUDE = -8.827741894599471
        private const val LONGITUDE = 13.244361877441408
        private const val ZOOM = 14.0
        private const val ZOOM2 = 16.0
    }

    private lateinit var mapView: MapView
    private lateinit var locationPermissionHelper: LocationPermissionHelper
    lateinit var enhancedLocation: Location

    // ViewGroup
    lateinit var btnIndicar: Button
    lateinit var voltarView: Button
    lateinit var listaParagemView: ListView
    lateinit var txtView: TextView

    lateinit var routeCoordinates: ArrayList<Point>

    var start: String = ""
    var end: String = ""

    // TODO Teste debug
//    private var start: String = "13.262954950332642, -8.936043952172165"
//    private var end: String = "13.269596099853517,-8.910765383860866"

    /**
     * [NavigationLocationProvider] é uma classe utilitária que ajuda a fornecer atualizações de localização geradas pelo Navigation SDK
     * ao Maps SDK para atualizar o indicador de localização do usuário no mapa.
     */
    private val navigationLocationProvider = NavigationLocationProvider()

    private val locationObserver = object : LocationObserver {
        override fun onNewRawLocation(rawLocation: Location) {

        }

        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            enhancedLocation = locationMatcherResult.enhancedLocation
            navigationLocationProvider.changePosition(
                enhancedLocation,
                locationMatcherResult.keyPoints,
            )
            updateCamera(enhancedLocation)
            start = "${enhancedLocation.longitude},${enhancedLocation.latitude}"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapView)

        btnIndicar = findViewById(R.id.btnIndicar)
        voltarView = findViewById(R.id.voltarView)
        txtView = findViewById(R.id.txtInfo)
        listaParagemView = findViewById(R.id.listaParagemView)

        val myListAdapter = Adapter(this@MainActivity, paragens)
        listaParagemView.adapter = myListAdapter

        listaParagemView.setOnItemClickListener() { adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position) as Paragem

            txtView.text = "Destino: ${itemAtPos.nomeParagem}, ${itemAtPos.localidade}"
            end = "${itemAtPos.longitude},${itemAtPos.latitude}"
//            Log.e("juni", "${txtView.text}: $end")
        }

        // TODO: Inicio do Map - ponto inicial onde a magia acontece 😊
        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
            AppInicio()
        }

    }

    private fun AppInicio() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder().center(
                Point.fromLngLat(
                    LONGITUDE,
                    LATITUDE
                )
            ).zoom(ZOOM).build()
        )
        mapView.getMapboxMap()
            .loadStyleUri(NavigationStyles.NAVIGATION_DAY_STYLE) {

                btnIndicar.setOnClickListener {
                    if (end.isEmpty()) {
                        Toast.makeText(this@MainActivity,
                            "Selecione um destino!",
                            Toast.LENGTH_LONG).show()
                    } else {
                        createRoute()
                        updateCamera(enhancedLocation, ZOOM2)

                        listaParagemView.visibility = GONE
                        btnIndicar.visibility = GONE
                        voltarView.visibility = VISIBLE
                    }
                }
                addMacaDeParagemNoMap()
                addMacaDoUsuarioNoMap()
            }
    }

    private fun createRoute() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java)
                .getRoute("5b3ce3597851110001cf624815f8a85cbc724abda2286a5c601b77b4", start, end)
            if (call.isSuccessful) {
//                Log.e("juni", "Deu Certooooooooooo")
                drawRota(call.body())
            } else {
//                Log.e("juni", "Deu Errado")
                Toast.makeText(this@MainActivity,
                    "Opss... Nao conseguimos definir a routa!",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun drawRota(body: RouteResponse?) {
// Definir uma lista de coordenadas das rotas
        routeCoordinates = arrayListOf()
        body?.features?.first()?.geometry?.coordinates?.forEach {
            routeCoordinates.add(Point.fromLngLat(it[0], it[1]))
        }
        runOnUiThread {
            val annotationApi = mapView.annotations
            val polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()
            val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
                .withPoints(routeCoordinates)
                .withLineColor("#2196F3")
                .withLineWidth(6.0)
// Adicionar o resultado da linha ao Map.
            polylineAnnotationManager.create(polylineAnnotationOptions)
            voltarView.setOnClickListener {
                end = ""
                mapView.annotations.removeAnnotationManager(polylineAnnotationManager)
                voltarView.visibility = GONE
                btnIndicar.visibility = VISIBLE
                listaParagemView.visibility = VISIBLE
                txtView.text = getString(R.string.selecione_uma_paragerem)
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private val mapboxNavigation: MapboxNavigation by requireMapboxNavigation(onResumedObserver = object :
        MapboxNavigationObserver {
        @SuppressLint("MissingPermission")
        override fun onAttached(mapboxNavigation: MapboxNavigation) {
            mapboxNavigation.registerLocationObserver(locationObserver)
            mapboxNavigation.startTripSession()
        }

        override fun onDetached(mapboxNavigation: MapboxNavigation) {
            mapboxNavigation.unregisterLocationObserver(locationObserver)
        }
    }, onInitialize = this::initNavigation)

    private fun initNavigation() {
        MapboxNavigationApp.setup(NavigationOptions.Builder(this)
            .accessToken(getString(R.string.mapbox_access_token)).build())
        mapView.location.apply {
            setLocationProvider(navigationLocationProvider)
            start =
                "${navigationLocationProvider.lastLocation?.longitude},${navigationLocationProvider.lastLocation?.latitude}"
            /*locationPuck = LocationPuck2D(
                bearingImage = ContextCompat.getDrawable(
                    this@ShowCurrentLocationActivity,
                    R.drawable.mapbox_navigation_puck_icon
                )
            )*/
            enabled = true
        }
    }

    private fun updateCamera(location: Location, zoom: Double = ZOOM) {
        val mapAnimationOptions = MapAnimationOptions.Builder().duration(1500L).build()
        mapView.camera.easeTo(
            CameraOptions.Builder()
                .center(Point.fromLngLat(location.longitude, location.latitude))
//                .center(Point.fromLngLat(13.262954950332642, -8.936043952172165))
                .zoom(zoom)
                .padding(EdgeInsets(400.0, 0.0, 0.0, 0.0))
                .build(),
            mapAnimationOptions
        )
    }

    private fun addMacaDeParagemNoMap() {
        paragens.forEach { it ->
            val paragem = it
            bitmapFromDrawableRes(this@MainActivity, paragem.imagemParagem)?.let {
                val annotationApi = mapView.annotations
                val pointAnnotationManager = annotationApi.createPointAnnotationManager()
                val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                    .withPoint(Point.fromLngLat(paragem.longitude, paragem.latitude))
                    .withIconImage(it)
                pointAnnotationManager.create(pointAnnotationOptions)
            }
        }
    }

    private fun addMacaDoUsuarioNoMap() {
        val coordenadas = start.split(",")

        bitmapFromDrawableRes(this@MainActivity, R.drawable.ic_meu_foreground)?.let {
            val annotationApi = mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager()
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(coordenadas[0].toDouble(), coordenadas[1].toDouble()))
                .withIconImage(it)
            pointAnnotationManager.create(pointAnnotationOptions)
        }
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
