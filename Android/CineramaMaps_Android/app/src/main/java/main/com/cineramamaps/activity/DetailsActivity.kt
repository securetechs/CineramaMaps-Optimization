package main.com.cineramamaps.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import main.com.cineramamaps.R
import main.com.cineramamaps.Session.SessionManager
import main.com.cineramamaps.activity.ui.theme.CineramaMaps_AndroidTheme
import main.com.cineramamaps.activity.ui.theme.cairoFamily
import main.com.cineramamaps.model.PlaceBeanNew
import main.com.cineramamaps.model.PlaceDetail
import main.com.cineramamaps.model.PlacesImage
import main.com.cineramamaps.model.TagDetail
import main.com.cineramamaps.restapi.ApiCall
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.net.toUri

class DetailsActivity : ComponentActivity() {
    var language = ""
    private var isCurrentUserFavorite = false
    var currentUserFavourite: Boolean? = false
    var lat: String? = "0.0"
    var lon: String? = "0.0"
    lateinit var successData: PlaceBeanNew
    private lateinit var placeDetail: PlaceDetail
    lateinit var session: SessionManager
    var id: String? = null
    var place_id: String? = ""
    var fav: String = ""
    var videoLinkEn: String = ""
    var videoLinkAr: String = ""
    var _places: MutableState<List<TagDetail>?> = mutableStateOf(null)
    val places: List<TagDetail>? get() = _places.value
    var _images: MutableState<List<PlacesImage>?> = mutableStateOf(null)
    val images: List<PlacesImage>? get() = _images.value


    fun updatePlaces(newPlaces: List<TagDetail>) {
        _places.value = newPlaces
    }

    fun updateImage(newPlaces: List<PlacesImage>) {
        _images.value = newPlaces
    }


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = intent.getStringExtra("placeDetails")
        if (json != null) {
            val gson = Gson()
            val listType = object : TypeToken<List<TagDetail>>() {}.type
            val placeDetails: List<TagDetail> = gson.fromJson(json, listType)

            updatePlaces(newPlaces = placeDetails)
        }
        val images = intent.getStringExtra("images")
        if (images != null) {
            val mson = Gson()
            val listType = object : TypeToken<List<PlacesImage>>() {}.type
            val imageDetail: List<PlacesImage> = mson.fromJson(images, listType)

            updateImage(newPlaces = imageDetail)
        }
        id = intent.getStringExtra("id")
        val address = intent.getStringExtra("address") ?: "--"
        val placeName = intent.getStringExtra("placeName") ?: "placeName not available"
        val placeNameAr = intent.getStringExtra("placeNameAr") ?: "placeNameAr not available"
        val descriptionAr = intent.getStringExtra("descriptionAr") ?: "descriptionAr not available"
        videoLinkEn = intent.getStringExtra("videoLinkEn") ?: ""
        videoLinkAr = intent.getStringExtra("videoLinkAr") ?: ""

        Log.d("asdf", "onCreate: " + videoLinkEn +"and ar "+videoLinkAr)

        val description = intent.getStringExtra("description") ?: "description not available"
        val favstatus = intent.getStringExtra("favstatus") ?: "favstatus not available"
        val totalunfavplace = intent.getStringExtra("totalunfavplace") ?: "totalunfavplace not available"
        val totalfavplace = intent.getStringExtra("totalfavplace") ?: "totalfavplace not available"
        val promoCodeAndDiscount = intent.getStringExtra("promoCodeAndDiscount") ?: ""
        val promoCodePercentage: String? = intent.getStringExtra("promoCodePercentage")
        val suggestedTime = intent.getStringExtra("suggestedTime") ?:"--"
        val advice = intent.getStringExtra("advice")?:"--"
        val adviceArabic = intent.getStringExtra("adviceArabic")?:"--"
        val icon = intent.getStringExtra("icon")?:""
        val avgRating = intent.getStringExtra("avgRating")?:""
        Log.d("AvgRating", "onCreate: $avgRating")

        val rating: Float = try {
            Log.d("AvgRating", "before parse: ")
            avgRating.toFloat()
        }catch (e: Exception){
            Log.d("AvgRating", "Exception: " + e.message)
            0f
        }
        Log.d("AvgRating", "rating: $rating")

        val distance = intent.getStringExtra("distance")
        lat = intent.getStringExtra("lat")
        lon = intent.getStringExtra("lng")
        currentUserFavourite = intent.getBooleanExtra("currentUserFavourite", false)
        language = this.resources.configuration.locale?.language ?: "en"
        session = SessionManager.get(this)


        getItemDetail()
        enableEdgeToEdge()
        setContent {
            val configuration = LocalConfiguration.current
            val currentLocale = remember { configuration.locales[0] }
            CineramaMaps_AndroidTheme {
                CompositionLocalProvider(
                    LocalLayoutDirection provides
                            if (currentLocale.language == "ar") LayoutDirection.Rtl
                            else LayoutDirection.Ltr
                ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        isFav = currentUserFavourite ?: false,
                        modifier = Modifier.padding(innerPadding),
                        id = id!!,
                        address = address,
                        placeName = placeName,
                        placeNameAr = placeNameAr,
                        description = description,
                        descriptionAr = descriptionAr,
                        favstatus = favstatus,
                        distance = distance,
                        totalunfavplace = totalunfavplace,
                        totalfavplace = totalfavplace,
                        promoCodeAndDiscount = promoCodeAndDiscount,
                        lat = lat?.toDouble() ?: 0.0,
                        lng = lon?.toDouble() ?: 0.0,
                        onClickFav = ::favorite,
                        place = _places.value,
                        image = _images.value,
                        videoLinkEn =  videoLinkEn,
                        videoLinkAr = videoLinkAr,
                        suggestedTime = suggestedTime,
                        advice = advice,
                        adviceArabic = adviceArabic,
                        promoCodePercentage = promoCodePercentage,
                        icon = icon,
                        currentLanguage = currentLocale.language,
                        rating = rating.toInt(),
                        actionNew = {
                            val intent = Intent(this, CreateTripActivity::class.java).apply {
                                putExtra("id", "")
                                putExtra("place_id",
                                    "" + successData
                                        .getResult()
                                        .getId()
                                )
                                putExtra("map_place_id",
                                    "" + successData
                                        .getResult()
                                        .getPlaceid()
                                )
                                putExtra("city_id",
                                    "" + successData
                                        .getResult()
                                        .getCityId()
                                )
                                putExtra(
                                    "city_name",
                                    "" + successData
                                        .getResult()
                                        .getCityDetails()
                                        .getName()
                                )
                                putExtra(
                                    "city_name_ar",
                                    "" + successData
                                        .getResult()
                                        .getCityDetails()
                                        .getNameAr()
                                )
                                putExtra("place_name",
                                    "" + successData
                                        .getResult()
                                        .getPlaceName()
                                )
                                putExtra("place_name_ar",
                                    "" + successData
                                        .getResult()
                                        .getPlaceNameAr()
                                )
                                putExtra("address",
                                    "" + successData
                                        .getResult()
                                        .getAddress()
                                )
                                putExtra("lat",
                                    "" + successData
                                        .getResult()
                                        .getLat()
                                )
                                putExtra("lon",
                                    "" + successData
                                        .getResult()
                                        .getLon()
                                )
                                // add others if needed
                            }
                            startActivity(intent)
                        }
                    )
                }
            }
            }
        }
    }

    private fun getItemDetail() {
//        binding.progressbar.setVisibility(View.VISIBLE)
        ApiCall.get().Create().getPlaceDetail1(session.userID, "", "", lat, lon, language)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
//                    binding.progressbar.setVisibility(View.GONE)
                    if (response.isSuccessful) {
                        try {
                            val responseData = response.body()!!.string()
                            val `object` = JSONObject(responseData)
                            if (`object`.getString("status").equals("1", ignoreCase = true)) {
                                successData = Gson().fromJson<PlaceBeanNew>(
                                    responseData,
                                    PlaceBeanNew::class.java
                                )

                                // Initialize placeDetail here
                                placeDetail = PlaceDetail()
                                placeDetail.setCurrentUserFavorite(
                                    successData.getResult().getFavStatus() == "Like"
                                )
                                placeDetail.setId(successData.getResult().getId())

                                // Set other properties as needed

                                // Rest of your code...
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    binding.progressbar.setVisibility(View.GONE)
                }
            })
    }
    private fun favorite(toggleHeartIcon:(Boolean)->Unit) {
        ApiCall.get().Create().addTofavorite(
            session.getUserID(),
            id
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val responseData = response.body()!!.string()
                        val `object` = JSONObject(responseData)

                        if (`object`.getString("status").equals("1", ignoreCase = true)) {
                            if (`object`.getString("result")
                                    .equals("Removed From Favorites", ignoreCase = true)
                            ) {
                                placeDetail.setCurrentUserFavorite(false)
                                toggleHeartIcon(false)
                                //                                Toast.makeText(PlacesDetailsActivity.this,
//                                        object.getString("result"), Toast.LENGTH_SHORT).show();
                            } else {
                                placeDetail.setCurrentUserFavorite(true)
                                toggleHeartIcon(true)
                                //                                Toast.makeText(PlacesDetailsActivity.this,
//                                        object.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(
                                this@DetailsActivity,
                                `object`.getString("message"), Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        //                        placeDetail.setCurrentUserFavorite(currentUserFavourite);
//                        toggleHeartIcon(currentUserFavourite);
                    }
                } else {
//                    placeDetail.setCurrentUserFavorite(currentUserFavourite);
//                    toggleHeartIcon(currentUserFavourite);
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                placeDetail.setCurrentUserFavorite(currentUserFavourite ?: false)
//                toggleHeartIcon(currentUserFavourite)
                Toast.makeText(
                    this@DetailsActivity,
                    "Error: " + t.message, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

//    private fun toggleHeartIcon(currentUserFavourite: Boolean) {
//        fun toggleHeartIcon(isCurrentUserFavorite: Boolean) {
//            this.isCurrentUserFavorite = isCurrentUserFavorite
//            if (isCurrentUserFavorite) {
////                binding.heartIcon.setImageResource(R.drawable.ic_fav)
//            } else {
////                binding.heartIcon.setImageResource(R.drawable.ic_favorite)
//            }
//        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(
    id: String,
    modifier: Modifier = Modifier,
    descriptionAr:String?= null,
    isFav: Boolean,
    address: String? = null,
    favstatus:String?= null,
    placeName:String? = null,
    placeNameAr: String?= null,
    description:String? = null,
    totalunfavplace:String ?= null,
    totalfavplace:String?= null,
    lat: Double = 0.0,lng: Double = 0.0,
    promoCodeAndDiscount:String?=null,
    videoLinkEn:String?= null,
    videoLinkAr: String? = null,
    suggestedTime: String? = null,
    advice: String? = null,
    adviceArabic: String? = null,
    promoCodePercentage: String? = null,
    icon: String? = null,
    currentLanguage: String? = null,
    rating: Int = 0,
    distance:String? = null, onClickFav: (toggleHeartIcon:(Boolean)->Unit)-> Unit, place: List<TagDetail>?, image: List<PlacesImage>?,
    actionNew:()->Unit
) {
    val placeImages = remember(image, id) {
        image?.filter { it.placeId.toString() == id } ?: emptyList()
    }
    val scrollState = rememberScrollState()
//    var rating by remember { mutableStateOf(0) }
    var isFav by remember {
        mutableStateOf(isFav)
    }
    val configuration = LocalConfiguration.current
    val currentLocale = remember { configuration.locales[0] }
    val isRtl = currentLocale.language == "ar"
    val context = LocalContext.current

//    val context = LocalContext.current
//    val currentLocale = LocalConfiguration.current.locales[0]
    val isArabic = currentLocale.language == "ar"
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            when {
                placeImages.isEmpty() -> {
                    println("No image found for place ${id}")
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent) // Optional: Explicitly set transparent
                    )
                }
                placeImages.size == 1 -> {
                    AsyncImage(
                        model = placeImages.first().image,
                        contentDescription = placeName,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    ImageCarousel(images = placeImages, placeName = placeName)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            ) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = Color.LightGray, shape = CircleShape)
                        .clickable {
                            if (context is ComponentActivity) {
                                val resultIntent = Intent()
                                resultIntent.putExtra("placeTitle", placeName)
                                resultIntent.putExtra("currentUserFavourite", isFav)
                                context.setResult(Activity.RESULT_OK, resultIntent)
                                context.finish()
                            }
                        }
                ) {
                    val configuration = LocalConfiguration.current
                    val currentLocale = remember { configuration.locales[0] }
                    val isRtl = currentLocale.language == "ar"

                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(if (isRtl) 180f else 0f), // Rotate 180 degrees in RTL
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = Color.LightGray, shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = if (!isFav) R.drawable.ic_heart else R.drawable.ic_fav),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                onClickFav { isFavr ->
//                                    val message =
//                                        if (isFavr) "" else ""
//                                    Toast
//                                        .makeText(context, message, Toast.LENGTH_SHORT)
//                                        .show()
                                    isFav = isFavr;
                                }
                            }
                    )
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
                    .background(color = Color(0xFFFF7D36), shape = CircleShape)
                    .align(Alignment.BottomEnd)
            ) {

                AsyncImage(
                    modifier = Modifier.size(24.dp),
                    model = ImageRequest.Builder(context)
                        .data(icon)
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    contentScale = ContentScale.Fit
                )
//                Image(
//                    painter = painterResource(id = R.drawable.ic_restaurant),
//                    contentDescription = "",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.size(24.dp),
//                    colorFilter = ColorFilter.tint(Color.White)
//                )
            }
        }

        val localizedPlaceName = if (currentLocale.language == "ar") placeNameAr else placeName

        Text(
            text = localizedPlaceName.takeIf { !it.isNullOrBlank() } ?: stringResource(id = R.string.Kale_outlet_center),
            fontSize = 18.sp,
            fontFamily = cairoFamily,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )


        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val customColor = Color(0xFFE7E7E9)
            var favStatus by remember {
            mutableStateOf(
                when {
                    totalfavplace == "1" -> "Like"
                    totalunfavplace == "1" -> "Dislike"
                    else -> ""
                }
            )
        }

            var totalFavPlace by remember { mutableStateOf(if (totalfavplace == "1") "1" else "0") }
            var totalUnfavPlace by remember { mutableStateOf(if (totalunfavplace == "1") "1" else "0") }

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 16.dp, bottom = 10.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(50)
                    )
                    .background(color = customColor, shape = RoundedCornerShape(50))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Like Button
                Row(
                    modifier = Modifier.clickable {
                        favStatus = when (favStatus) {
                            "Like" -> {
                                totalFavPlace = "0"
                                ""
                            }
                            "Dislike" -> {
                                totalUnfavPlace = "0"
                                totalFavPlace = "1"
                                "Like"
                            }
                            else -> {
                                totalFavPlace = "1"
                                "Like"
                            }
                        }
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_thumb_up),
                        contentDescription = "Like",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(
                            if (favStatus == "Like") Color(0xFFFF7D36) else Color.White
                        )
                    )

                    // Like Count
                    if (totalFavPlace == "1") {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = totalFavPlace,
                            fontFamily = cairoFamily,
                            fontSize = 14.sp
                        )
                    }
                }

                // Divider
                Text(
                    text = "|",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.Gray
                )

                // Dislike Button
                Row(
                    modifier = Modifier.clickable {
                        favStatus = when (favStatus) {
                            "Dislike" -> {
                                totalUnfavPlace = "0"
                                ""
                            }
                            "Like" -> {
                                totalFavPlace = "0"
                                totalUnfavPlace = "1"
                                "Dislike"
                            }
                            else -> {
                                totalUnfavPlace = "1"
                                "Dislike"
                            }
                        }
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_thumbdown),
                        contentDescription = "Dislike",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(
                            if (favStatus == "Dislike") Color(0xFFFF7D36) else Color.Black
                        )
                    )

                    // Dislike Count
                    if (totalUnfavPlace == "1") {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = totalUnfavPlace,
                            fontFamily = cairoFamily,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))


            // Add this state variable at the beginning of your Greeting composable
//            var rating by remember { mutableStateOf(0) }

// Then replace your current star rating implementation with:
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(3) { index ->   // Always show 3 stars
                        val starColor = if (index < rating) {
                            Color(0xFFE25E16) // Selected       // Selected stars
                        } else {
                            Color.LightGray // Unselected stars
                        }

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    starColor,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(2.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_star),
                                contentDescription = "Star",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Text(
                    text = stringResource(id = R.string.Recommendation),
                    fontSize = 12.sp,
                    fontFamily = cairoFamily,
                    color = Color.Gray
                )
            }
        }
        Text(
            text = stringResource(id = R.string.Description),
            fontSize = 14.sp,
            fontFamily = cairoFamily,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp)
        )
//        Text(
//            text = stringResource(id = R.string.loretext),
//            fontSize = 14.sp,
//            fontFamily = cairoFamily,
////            fontWeight = FontWeight.Normal,
//            color = Color.Black,
//
//            modifier = Modifier.padding(start = 16.dp)
//        )
        val localizedDescription = if (currentLocale.language == "ar") descriptionAr else description
        HtmlTextWithMaxLinesScroll(htmlContent = localizedDescription ?: stringResource(id = R.string.loretext))
        Spacer(modifier = Modifier.height(16.dp))
        val horizontalScrollState = rememberScrollState()
        var selectedIndex by remember { mutableIntStateOf(-1) }

        Row(
            modifier = Modifier
                .horizontalScroll(horizontalScrollState)
                .padding(top = 12.dp, start = 16.dp)
        ) {
            place?.forEachIndexed { index, tag ->
                val isSelected = selectedIndex == index
                val backgroundColor = if (isSelected) {
                    Color(android.graphics.Color.parseColor(tag.colorCode ?: "#FF7D36"))
                } else {
                    Color(android.graphics.Color.parseColor(tag.colorCode ?: "#FF7D36"))
                }

//                val textColor = if (isSelected) Color.White else Color(android.graphics.Color.parseColor(tag.colorCode ?: "#FF7D36"))

                Button(
                    onClick = { selectedIndex = if (selectedIndex == index) -1 else index },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = backgroundColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(32.dp)
                ) {
                    // Show tag icon if available


                        // You would need to load the icon here - this is a placeholder
                        // For example, using Coil or another image loading library
                        // Icon(Icons.Default.Tag, contentDescription = null)

                        val currentLocale = LocalConfiguration.current.locales[0]
                        Text(
                            text = if (currentLocale.language == "ar") tag.tagNameAr else tag.tagName,
                            fontSize = 14.sp,
                            fontFamily = cairoFamily,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                }
            }
        }
        Divider(
            color = Color.Gray,
            thickness = 0.75.dp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_watch),
                contentDescription = "Watch",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(id = R.string.Suggested_time),
                fontSize = 14.sp,
                fontFamily = cairoFamily,
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text =if (suggestedTime.isNullOrBlank() || suggestedTime == "∞") {
                    "∞"
                } else {
                    "$suggestedTime ${stringResource(R.string.ha)}"
                },
//                currentTime ?:
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                fontFamily = cairoFamily,
                fontSize = if (suggestedTime.isNullOrBlank() || suggestedTime == "∞") {
                    25.sp
                } else {
                   14.sp
                },
                modifier = Modifier.padding(end = 16.dp)
            )
        }

//        if(videoLinkEn != "" || videoLinkAr != ""){
//            Log.d("asdf", "Greeting: inside if  "+videoLinkEn + ""+ videoLinkAr)
//            Divider(
//                color = Color.Gray,
//                thickness = 0.75.dp,
//                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
//            )
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 16.dp, top = 4.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_video),
//                    contentDescription = "Video",
//                    modifier = Modifier.size(20.dp)
//                )
//                Text(
//                    text = stringResource(id = R.string.Video),
//                    fontSize = 14.sp,
//                    fontFamily = cairoFamily,
//                    color = Color.Gray,
//                    modifier = Modifier.padding(start = 8.dp)
//                )
//                Spacer(modifier = Modifier.weight(1f))
////            val localizedLink = if (currentLocale.language == "ar") linkAr else link
//                Text(
//                    text = stringResource(id = R.string.Watch_Video),
//                    fontFamily = cairoFamily,
//                    fontSize = 14.sp,
//                    color = Color(0xFF2A74FF)
//                )
//                Image(
//                    painter = painterResource(id = R.drawable.ic_chevron),
//                    contentDescription = "Video",
//                    modifier = Modifier
//                        .padding(end = 16.dp)
//                        .size(14.dp)
//                        .rotate(if (isRtl) 180f else 0f),
//                )
//            }
//        }


        if (videoLinkEn != "" || videoLinkAr != "") {

            Divider(
                color = Color.Gray,
                thickness = 0.75.dp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val selectedLink = if (isArabic) {
                            videoLinkAr
                        } else {
                            videoLinkEn
                        }

                        if (selectedLink?.isNotEmpty() ?: false) {
//                            val intent = Intent(context, VideoWebViewActivity::class.java)
//                            intent.putExtra("video_url", selectedLink)
//                            context.startActivity(intent)
                            val intent = Intent(Intent.ACTION_VIEW, selectedLink!!.toUri())
                            context.startActivity(intent)
                        }
                    }
                    .padding(start = 16.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_video),
                    contentDescription = "Video",
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = stringResource(id = R.string.Video),
                    fontSize = 14.sp,
                    fontFamily = cairoFamily,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(id = R.string.Watch_Video),
                    fontFamily = cairoFamily,
                    fontSize = 14.sp,
                    color = Color(0xFF2A74FF)
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_chevron),
                    contentDescription = "Video",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(14.dp)
                        .rotate(if (isRtl) 180f else 0f),
                )
            }
        }

        Divider(
            color = Color.Gray,
            thickness = 0.75.dp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_discount),
                contentDescription = "Discount",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(id =R.string.Discount ),
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = cairoFamily,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            if(!promoCodeAndDiscount.isNullOrEmpty()){
                OutlinedButton(
                    onClick = { },
                    border = BorderStroke(1.dp, Color(0xFFFF7D36)),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFFFF7F3)
                    ),
                    modifier = Modifier.height(26.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text(
                        text = promoCodeAndDiscount ?: "",
                        color = Color(0xFFFF7D36),
                        fontFamily = cairoFamily,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            if (!promoCodePercentage.isNullOrEmpty()){
                OutlinedButton(
                    onClick = { },
                    border = BorderStroke(1.dp, Color(0xFFFF7D36)),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFFFF7F3)
                    ),
                    modifier = Modifier
                        .height(26.dp)
                        .padding(end = 16.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "$promoCodePercentage%",
                            color = Color(0xFFFF7D36),
                            fontFamily = cairoFamily,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_chevron),
//                            contentDescription = "",
//                            modifier = Modifier
//                                .size(14.dp)
//                                .rotate(if (isRtl) 180f else 0f),
//                            colorFilter = ColorFilter.tint(Color(0xFFFF7D36))
//                        )
                    }
                }
            }

        }
        Divider(
            color = Color.Gray,
            thickness = 0.75.dp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 4.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Left Side (Icon + Title)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = "Advice",
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = stringResource(id = R.string.Advice),
                    fontSize = 14.sp,
                    fontFamily = cairoFamily,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Right Side (Expandable Text)
            ExpandableClickableText(
                text = if (isArabic) (adviceArabic ?: "--") else (advice ?: "--"),
                modifier = Modifier
                    .weight(1.5f)
                    .padding(start = 8.dp)
            )
        }
        Divider(
            color = Color.Gray,
            thickness = 0.75.dp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.Location),
            fontWeight = FontWeight.Bold,
            fontFamily = cairoFamily,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        Card(
            onClick = {
                val gmmIntentUri = Uri.parse("geo:$lat,$lng?q=${Uri.encode(placeName)}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(mapIntent)
                } else {
                    val webIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng")
                    )
                    context.startActivity(webIntent)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 110.dp, height = 100.dp)
                        .padding(start = 4.dp, top = 4.dp, bottom = 4.dp)
                ) {
                    // Background Image
                    Image(
                        painter = painterResource(id = R.drawable.ic_locations),
                        contentDescription = "Location Background",
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(12.dp))
                    )

                    // Map overlay
                    CustomMapView(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(12.dp)),
                        lat = lat,
                        long = lng,
                        placeName = placeName ?: ""
                    )
                }

                // Right side column
                Column(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_routing),
                            contentDescription = "routing",
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${distance} km",
                            fontSize = 14.sp,
                            fontFamily = cairoFamily,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_locate),
                            contentDescription = "locate",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = address ?: "--",
                            maxLines = 1,
                            fontSize = 14.sp,
                            fontFamily = cairoFamily,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

//                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            painter = painterResource(id = R.drawable.ic_map),
                            contentDescription = "map",
                            modifier = Modifier
                                .size(30.dp)
                                .padding(end = 16.dp)
                        )
                    }
                }
            }
        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "Nearest Location",
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp,
//                modifier = Modifier.padding(start = 16.dp)
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            Text(
//                text = "See more",
//                fontSize = 14.sp,
//                color = Color(0xFF2A74FF)
//            )
//            Image(
//                painter = painterResource(id = R.drawable.ic_chevron),
//                contentDescription = "Video",
//                modifier = Modifier
//                    .padding(end = 16.dp)
//                    .size(14.dp)
//            )
//        }
//        Column {
//            Divider(
//                color = Color.Gray,
//                thickness = 0.75.dp,
//                modifier = Modifier
//                    .padding(horizontal = 16.dp)
//                    .padding(top = 8.dp)
//            )
//
//            repeat(3) { index ->
//                if (index > 0) {
//                    Divider(
//                        color = Color.Gray,
//                        thickness = 0.75.dp,
//                        modifier = Modifier
//                            .padding(horizontal = 16.dp)
//                            .padding(vertical = 8.dp)
//                    )
//                }
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp)
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_cafe),
//                        contentDescription = "Location",
//                        modifier = Modifier
//                            .size(height = 100.dp, width = 100.dp)
//                            .padding(start = 16.dp, top = 2.dp, bottom = 2.dp)
//                    )
//                    Column(
//                        modifier = Modifier.padding(start = 8.dp)
//                    ) {
//                        Text(
//                            text = "Kale outlet center",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(top = 8.dp)
//                        )
//                        Text(
//                            text = "Lorem ipsum dolor sit amet consectetur.Nisl aenean dui viverra",
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis,
//                            fontSize = 12.sp
//                        )
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Image(
//                                painter = painterResource(id = R.drawable.ic_routing),
//                                contentDescription = "routing",
//                                modifier = Modifier.size(20.dp)
//                            )
//                            Text(
//                                text = "200 M",
//                                fontSize = 14.sp,
//                                color = Color.Gray,
//                                modifier = Modifier.padding(start = 8.dp)
//                            )
//                        }
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Image(
//                                painter = painterResource(id = R.drawable.ic_locate),
//                                contentDescription = "locate",
//                                modifier = Modifier.size(20.dp)
//                            )
//                            Text(
//                                text = "Beyazit, 34126 Faith/Istabul",
//                                maxLines = 1,
//                                fontSize = 14.sp,
//                                color = Color.Gray,
//                                modifier = Modifier.padding(start = 8.dp)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = stringResource(id = R.string.Visit_recommendations),
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp,
//                fontFamily = cairoFamily,
//                modifier = Modifier.padding(start = 16.dp)
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            Text(
//                text = stringResource(id = R.string.See_more),
//                fontSize = 14.sp,
//                fontFamily = cairoFamily,
//                color = Color(0xFF2A74FF)
//            )
//            Image(
//                painter = painterResource(id = R.drawable.ic_chevron),
//                contentDescription = "chevron",
//                modifier = Modifier
//                    .padding(end = 16.dp)
//                    .size(14.dp)
//                    .rotate(if (isRtl) 180f else 0f),
//            )
//        }
//        LazyRow(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items(3) { index ->
//                Card(
//                    modifier = Modifier
//                        .width(300.dp)
//                        .padding(vertical = 12.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    border = BorderStroke(1.dp, Color.LightGray),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color.Transparent // no fill
//                    ),
//                    elevation = CardDefaults.cardElevation(0.dp) // no shadow
//                ) {
//                    Column {
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_kitchen),
//                            contentDescription = "Location",
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier
//                                .padding(start = 4.dp, top = 4.dp, end = 4.dp)
//                                .fillMaxWidth()
//                                .height(160.dp)
//                                .clip(
//                                    RoundedCornerShape(
//                                        topStart = 14.dp,
//                                        topEnd = 14.dp,
//                                        bottomEnd = 12.dp,
//                                        bottomStart = 12.dp
//                                    )
//                                )
//                        )
//
//                        Column(
//                            modifier = Modifier.padding(12.dp)
//                        ) {
//                            Text(
//                                text =  stringResource(id = R.string.Kale_outlet_center),
//                                fontSize = 18.sp,
//                                fontFamily = cairoFamily,
//                                fontWeight = FontWeight.Bold
//                            )
//                            Text(
//                                text = stringResource(id = R.string.ipsum),
//                                maxLines = 2,
//                                fontFamily = cairoFamily,
//                                overflow = TextOverflow.Ellipsis,
//                                fontSize = 12.sp,
//                                color = Color.Gray,
//                                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
//                            )
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_locate),
//                                    contentDescription = "location icon",
//                                    tint = Color(0xFFFF7E36),
//                                    modifier = Modifier.size(16.dp)
//                                )
//                                Text(
//                                    text = stringResource(id = R.string.Beyazit),
//                                    fontSize = 13.sp,
//                                    color = Color.Gray,
//                                    fontFamily = cairoFamily,
//                                    modifier = Modifier.padding(start = 8.dp)
//                                )
//                            }
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                modifier = Modifier.padding(top = 4.dp)
//                            ) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_routing),
//                                    contentDescription = "distance icon",
//                                    tint = Color(0xFFFF7E36),
//                                    modifier = Modifier.size(16.dp)
//                                )
//                                Text(
//                                    text = stringResource(id = R.string.away),
//                                    fontSize = 13.sp,
//                                    fontFamily = cairoFamily,
//                                    color = Color.Gray,
//                                    modifier = Modifier.padding(start = 8.dp)
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = stringResource(id = R.string.Reviews),
//                fontWeight = FontWeight.Bold,
//                fontFamily = cairoFamily,
//                fontSize = 16.sp,
//                modifier = Modifier.padding(start = 16.dp)
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            Text(
//                text = stringResource(id = R.string.See_more),
//                fontSize = 14.sp,
//                fontFamily = cairoFamily,
//                color = Color(0xFF2A74FF)
//            )
//            Image(
//                painter = painterResource(id = R.drawable.ic_chevron),
//                contentDescription = "chevron",
//                modifier = Modifier
//                    .padding(end = 16.dp)
//                    .size(14.dp)
//                    .rotate(if (isRtl) 180f else 0f),
//            )
//        }
//        Column {
//            repeat(3) {
//                Divider(
//                    color = Color.Gray,
//                    thickness = 0.75.dp,
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
//                )
//                Column {
//                    Row {
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_person),
//                            contentDescription = "Location",
//                            modifier = Modifier
//                                .size(height = 50.dp, width = 50.dp)
//                                .padding(start = 16.dp, bottom = 2.dp)
//                        )
//                        Column {
//                            Text(
//                                text = stringResource(id = R.string.Kathryn_Murphy),
//                                maxLines = 1,
//                                fontSize = 14.sp,
//                                fontFamily = cairoFamily,
//                                color = Color.Black,
//                                fontWeight = FontWeight.Bold,
//                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
//                            )
//                            Text(
//                                text = stringResource(id = R.string.day),
//                                maxLines = 1,
//                                fontSize = 12.sp,
//                                fontFamily = cairoFamily,
//                                color = Color.Gray,
//                                modifier = Modifier.padding(start = 8.dp)
//                            )
//                        }
//                        Spacer(modifier = Modifier.weight(1f))
//                        OutlinedButton(
//                            onClick = { },
//                            border = BorderStroke(1.dp, Color(0xFFFF7D36)),
//                            shape = RoundedCornerShape(50),
//                            colors = ButtonDefaults.outlinedButtonColors(
//                                containerColor = Color(0xFFFFF7F3)
//                            ),
//                            modifier = Modifier
//                                .padding(top = 8.dp, end = 16.dp)
//                                .height(36.dp),
//                            contentPadding = PaddingValues(horizontal = 12.dp)
//                        ) {
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.spacedBy(6.dp)
//                            ) {
//                                Image(
//                                    painter = painterResource(id = R.drawable.ic_fav),
//                                    contentDescription = "",
//                                    modifier = Modifier.size(20.dp),
//                                    colorFilter = ColorFilter.tint(Color(0xFFFF7D36))
//                                )
//                                Text(
//                                    "27",
//                                    fontFamily = cairoFamily,
//                                    color = Color(0xFFFF7D36),
//                                    style = MaterialTheme.typography.bodySmall,
//                                    textAlign = TextAlign.Center
//                                )
//                            }
//                        }
//                    }
//                    Text(
//                        maxLines = 3,
//                        overflow = TextOverflow.Ellipsis,
//                        text = stringResource(id = R.string.loretext),
//                        fontSize = 14.sp,
//                        fontFamily = cairoFamily,
//                        color = Color.Gray,
//                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
//                    )
//                }
//            }
//        }
    }
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier
//            .fillMaxSize(), // Fill the parent to access bottomEnd
//    ) {
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .padding(16.dp)
//                .size(52.dp)
//                .background(color = Color(0xFFFF7D36), shape = CircleShape)
//                .align(Alignment.BottomEnd)
//                .clickable {
//                    actionNew()
//
//                }
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_adds),
//                contentDescription = "",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.size(14.dp),
//                colorFilter = ColorFilter.tint(Color.White)
//            )
//        }
//    }


}
//@Composable
//fun StarRating(
//    modifier: Modifier = Modifier,
//    rating: Int,
//    onRatingChanged: (Int) -> Unit,
//    starCount: Int = 3
//) {
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.spacedBy(4.dp)
//    ) {
//        repeat(starCount) { index ->
//            Box(
//                modifier = Modifier
//                    .size(24.dp)
//                    .clickable { onRatingChanged(index + 1) }
//                    .background(
//                        color = if (index < rating) Color(0xFF00AF29) else Color.LightGray,
//                        shape = RoundedCornerShape(4.dp)
//                    )
//                    .padding(2.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_star),
//                    contentDescription = "Star",
//                    modifier = Modifier.fillMaxSize(),
//                    colorFilter = ColorFilter.tint(Color.White) // Ensures the star is visible
//                )
//            }
//        }
//    }
//}

@Composable
fun HtmlTextWithMaxLinesScroll(
    htmlContent: String,
    modifier: Modifier = Modifier
) {
    val spanned: Spanned = remember(htmlContent) {
        Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY)
    }
    val annotatedString = buildAnnotatedString {
        append(spanned.toString())
    }

    val scrollState = rememberScrollState()

    // Approx height of 4–5 lines (adjust for your font)
    val visibleTextHeight = 160.dp // or 120.dp for 5 lines

    Box(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .height(visibleTextHeight)
            .clip(RoundedCornerShape(6.dp))
    ) {
        // Scrollable Text Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Text(
                text = annotatedString,
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = cairoFamily,
                textAlign = TextAlign.Start
            )
        }

        // Fake Scrollbar (like image)
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(4.dp)
        ) {
            val scrollRatio = scrollState.value.toFloat() / (scrollState.maxValue.toFloat().coerceAtLeast(1f))
            val thumbHeight = 30.dp
            val scrollAreaHeight = visibleTextHeight - thumbHeight

            Box(
                modifier = Modifier
                    .offset(y = scrollAreaHeight * scrollRatio)
                    .align(Alignment.TopEnd)
                    .width(4.dp)
                    .height(thumbHeight)
                    .background(Color(0xFFFF6F3C), shape = RoundedCornerShape(2.dp))
            )
        }
    }
}



@Composable
fun CustomMapView(
    modifier: Modifier = Modifier,
    lat: Double = 0.0,
    long: Double = 0.0,
    coordinate: LatLng = LatLng(lat, long), // Beyazıt
    placeName: String = ""
) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
            .clickable {
                val gmmIntentUri = Uri.parse("geo:$lat,$long?q=${Uri.encode(placeName)}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(mapIntent)
                } else {
                    val webIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$long")
                    )
                    context.startActivity(webIntent)
                }
            },
        factory = { context ->
            val mapView = MapView(context)
            mapView.onCreate(null)
            mapView.getMapAsync { googleMap ->
                googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                val location = CameraUpdateFactory.newLatLngZoom(coordinate, 17f)
                googleMap.moveCamera(location)
                googleMap.uiSettings.setAllGesturesEnabled(false)

                val marker = MarkerOptions()
                    .position(coordinate)
                    .title(placeName)
                googleMap.addMarker(marker)
            }
            mapView
        },
        update = {
            it.onResume()
        }
    )
}

@Composable
fun ExpandableClickableText(
    text: String,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 2
) {
    var expanded by remember { mutableStateOf(false) }
    var isOverflowing by remember { mutableStateOf(false) }

    Column(modifier = modifier.clickable(enabled = isOverflowing || expanded) {
        expanded = !expanded
    }) {
        Text(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                if (!expanded) {
                    isOverflowing = textLayoutResult.hasVisualOverflow
                }
            },
            fontFamily = cairoFamily,
            fontSize = 14.sp,
            color = Color.Gray
        )
        if (isOverflowing || expanded) {
            Text(
                text = if (expanded) stringResource(R.string.read_less) else stringResource(R.string.read_more),
                color = Color(0xFF2A74FF),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = cairoFamily,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}





//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    CineramaMaps_AndroidTheme {
//        Greeting()
//    }
//}
@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(images: List<PlacesImage>, placeName: String?) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    // Auto-scroll logic
    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000L) // delay for 3 seconds
            val nextPage = (pagerState.currentPage + 1) % images.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        // Image Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { page ->
            AsyncImage(
                model = images[page].image,
                contentDescription = "$placeName image ${page + 1}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomEnd = 4.dp,
                            bottomStart = 4.dp
                        )
                    ),
                placeholder = painterResource(R.drawable.ic_placeholder),
                error = painterResource(R.drawable.ic_placeholder)
            )
        }

        // Indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .height(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pagerState.pageCount) { index ->
                val isSelected = pagerState.currentPage == index
                val width by animateDpAsState(targetValue = if (isSelected) 20.dp else 8.dp, label = "")
                val color = if (isSelected) Color(0xFFFF7D36) else Color.White.copy(alpha = 0.5f)

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(8.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

