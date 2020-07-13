package com.example.placesdemo.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.placesdemo.R
import com.example.placesdemo.adapter.PlaceRecyclerViewAdapter
import com.example.placesdemo.model.Photo
import com.example.placesdemo.model.Place
import com.example.placesdemo.model.Result
import com.example.placesdemo.room.database.PlaceDatabase
import com.example.placesdemo.room.entities.LocationEntity
import com.example.placesdemo.room.entities.PhotoEntity
import com.example.placesdemo.room.entities.PlaceEntity
import com.example.placesdemo.room.entities.ResultEntity
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.Places
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList


class PlaceActivity : AppCompatActivity() {

    companion object {
        private const val MAX_ATTEMPT = 10
        private const val RADIUS_INCREMENTER = 1000
    }

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private val gson = GsonBuilder().create()
    private lateinit var queue: RequestQueue
    private var placeRecyclerViewAdapter: PlaceRecyclerViewAdapter? = null
    private var rvResult: RecyclerView? = null
    private var pb1: ProgressBar? = null
    private var db: PlaceDatabase? = null

    private var lat = 0.0
    private var lng = 0.0
    private var currentRadius = 1000L
    private var searchKeyword = ""
    private var retryCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        initViews()
        checkPermissions(this)


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        db = PlaceDatabase.getDataBase(this)

        locationRequest = LocationRequest.create()
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest?.interval = 20 * 1000

        queue = Volley.newRequestQueue(this)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            showDialog()
        }

        requestNewLocationData()
    }

    private fun initViews() {
        rvResult = findViewById(R.id.rvResult)
        pb1 = findViewById(R.id.pb1)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.key), Locale.US);
        }

        rvResult?.layoutManager = LinearLayoutManager(this)
        placeRecyclerViewAdapter = PlaceRecyclerViewAdapter(Places.createClient(this), getString(R.string.key))
        rvResult?.adapter = placeRecyclerViewAdapter
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient?.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            lat = mLastLocation.latitude
            lng = mLastLocation.longitude
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog)

        val etRadius: EditText? = dialog.findViewById(R.id.etRadius)
        val etKeyword: EditText? = dialog.findViewById(R.id.etKeyword)

        etRadius?.setText(currentRadius.toString())
        etKeyword?.setText(searchKeyword)

        val tvPositive: TextView = dialog.findViewById(R.id.tvPositive) as TextView
        tvPositive.setOnClickListener {
            currentRadius = etRadius?.text?.toString()?.toLong() ?: 0L
            searchKeyword = etKeyword?.text?.toString() ?: ""

            if (!fetchResultsFromLocal(searchKeyword)) {
                geocodePlaceAndDisplay(lat, lng, currentRadius, searchKeyword)
            }

            dialog.dismiss()
        }

        val tvNegative: TextView = dialog.findViewById(R.id.tvNegative) as TextView
        tvNegative.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun fetchResultsFromLocal(keyword: String): Boolean {
        val place = db?.placeDao()?.getPlaceByKeyword(keyword)
        if (place == null) {
            return false
        } else {
            val items: MutableList<Result> = ArrayList()
            val results = db?.resultDao()?.getAllResultsByPlaceId(place.id)

            results?.let {
                for (i in results) {
                    val photos = db?.photoDao()?.getAllPhotosByPlaceId(i.id)
                    if (!photos.isNullOrEmpty()) {
                        items.add(Result(i.businessStatus, null, i.name, listOf(Photo(photos[0].height, photos[0].photoReference, photos[0].width)), i.rating))
                    } else {
                        items.add(Result(i.businessStatus, null, i.name, null, i.rating))
                    }
                }
            }
            placeRecyclerViewAdapter?.setData(items)
            return true
        }
    }

    private fun geocodePlaceAndDisplay(lat: Double, lng: Double, radius: Long, keyword: String) {
        pb1?.visibility = View.VISIBLE
        val apiKey = getString(R.string.key)
        val lat1 = -33.8670522
        val lng1 = 151.1957362
        val requestURL =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$lat1,$lng1&radius=$radius&keyword=$keyword&key=$apiKey"
        val request = JsonObjectRequest(Request.Method.GET, requestURL, null, { response ->
            pb1?.visibility = View.GONE
            try {
                val result: Place = gson.fromJson(response.toString(), Place::class.java)

                if (result.results.isNullOrEmpty()) {
                    if (retryCount <= MAX_ATTEMPT) {
                        retryCount++
                        geocodePlaceAndDisplay(lat, lng, radius + RADIUS_INCREMENTER, keyword)
                    }
                } else {
                    for (i in result.results) {
                        var insertedEntity = db?.placeDao()?.getPlaceByKeyword(keyword)
                        if (insertedEntity == null) {
                            val placeEntity = PlaceEntity(0, keyword)
                            db?.placeDao()?.insertPlace(placeEntity)
                            insertedEntity = db?.placeDao()?.getPlaceByKeyword(keyword)
                        }
                        db?.resultDao()?.insertResult(ResultEntity(
                                0,
                                insertedEntity?.id ?: 0,
                                i.businessStatus,
                                i.name,
                                i.rating))

                        val lastRecord = db?.resultDao()?.getLastRecord()
                        db?.locationDao()?.insertLocation(LocationEntity(
                                0,
                                lastRecord ?: 0,
                                i.geometry?.location?.lat,
                                i.geometry?.location?.lng
                        ))

                        if (!i.photos.isNullOrEmpty()) {
                            db?.photoDao()?.insertPhoto(PhotoEntity(
                                    0,
                                    lastRecord ?: 0,
                                    i.photos[0].height,
                                    i.photos[0].photoReference,
                                    i.photos[0].width
                            ))
                        }
                    }
                    placeRecyclerViewAdapter?.setData(result.results)
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error ->
            pb1?.visibility = View.GONE
            error.printStackTrace()
        })

        // Add the request to the Request queue.
        queue.add(request)
    }

    fun checkPermissions(activity: Activity) {
        val arrPerm = java.util.ArrayList<String>()
        arrPerm.add(Manifest.permission.INTERNET)
        arrPerm.add(Manifest.permission.ACCESS_FINE_LOCATION)
        arrPerm.add(Manifest.permission.ACCESS_WIFI_STATE)
        if (!arrPerm.isEmpty()) {
            var permissions: Array<String?>? = arrayOfNulls(arrPerm.size)
            permissions = arrPerm.toArray(permissions)
            ActivityCompat.requestPermissions(activity, permissions, 1)
        }
    }
}