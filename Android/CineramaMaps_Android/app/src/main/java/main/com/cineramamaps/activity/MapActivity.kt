package main.com.cineramamaps.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.gson.Gson
import main.com.cineramamaps.R
import main.com.cineramamaps.Session.SessionManager
import main.com.cineramamaps.activity.ui.theme.CineramaMaps_AndroidTheme
import main.com.cineramamaps.activity.ui.theme.cairoFamily
import main.com.cineramamaps.model.PlaceBeanNew
import main.com.cineramamaps.model.PlaceDetail
import main.com.cineramamaps.model.PlacesBean
import main.com.cineramamaps.model.PlacesBeanList
import main.com.cineramamaps.model.PlacesImage
import main.com.cineramamaps.model.Tag
import main.com.cineramamaps.restapi.ApiCall
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class DetailListActivity : ComponentActivity() {
    var currentUserFavourite: Boolean? = false
//    var progressbar: ProgressBar? = null
    lateinit var session: SessionManager
    private val language = ""
    var lat: String? = "0.0"
    var lon: String? = "0.0"
    private var cityId: String = ""
    var successData by mutableStateOf<PlacesBean?>(null)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            session = SessionManager(this)

            cityId = intent.getStringExtra("CITY_ID") ?: ""

        enableEdgeToEdge()
        setContent {
            val configuration = LocalConfiguration.current
            val locale = remember { mutableStateOf(configuration.locale) }

            CompositionLocalProvider(
                LocalLayoutDirection provides when (TextUtils.getLayoutDirectionFromLocale(locale.value)) {
                    View.LAYOUT_DIRECTION_RTL -> LayoutDirection.Rtl
                    else -> LayoutDirection.Ltr
                }
            ) {
                CineramaMaps_AndroidTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        val place = successData?.result?.placeDetails ?: emptyList()
                        val tags = successData?.result?.tags ?: emptyList()
                        val image = successData?.result?.placesImages ?: emptyList()
                        SearchBar(
                            modifier = Modifier.padding(innerPadding),
                            place = place,
                            tags = tags,
                            image = image,
                            isFav = currentUserFavourite ?: false,
                        )
                    }
                }
            }
        }
        getCountryMaps()
    }
    private fun getCountryMaps() {

        ApiCall.get().Create().getCityMapsDetails(getProductParam("Item"))
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                    if (response.isSuccessful) {
                        try {
                            val responseData = response.body()!!.string()
                            Log.d("API_RESPONSE", responseData)  // Add logging
                            val `object` = JSONObject(responseData)

                            if (`object`.getString("status") == "1") {
                                successData = Gson().fromJson(
                                    responseData,
                                    PlacesBean::class.java
                                )
                            }
                        } catch (e: Exception) {

                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("API_FAILURE", "API call failed", t)
                }
            })
    }

    fun getProductParam(type: String): HashMap<String, String> {
        val param = HashMap<String, String>()
        param["user_id"] = session.getUserID() ?: ""
        param["city_id"] = "" + cityId
        param["lang"] = "" + language
        param["lat"] = lat ?: "0.0"
        param["lon"] = lon ?: "0.0"


        return param
    }
    private fun favorite(placeId: String, toggleHeartIcon: (Boolean) -> Unit) {
        ApiCall.get().Create().addTofavorite(
            session.getUserID() ?: "",
            placeId
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val responseData = response.body()!!.string()
                        val `object` = JSONObject(responseData)

                        if (`object`.getString("status") == "1") {
                            val isFavorite = !`object`.getString("result")
                                .equals("Removed From Favorites", ignoreCase = true)
                            toggleHeartIcon(isFavorite)

                            // Update the corresponding place in your list
                            successData?.result?.placeDetails?.find { it.id == placeId }
                                ?.isCurrentUserFavorite = isFavorite
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@DetailListActivity,
                    "Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchBar(
        modifier: Modifier = Modifier,
        place: List<PlaceDetail>,
        tags: List<Tag>,
        image: List<PlacesImage>,
        isFav: Boolean,
    ) {
        var text by remember { mutableStateOf("") }
        val context = LocalContext.current
        val currentLocale = LocalConfiguration.current.locales[0]
        var selectedIndex by remember { mutableStateOf(-1) }
        val horizontalScrollState = rememberScrollState()

        // Suggestions
        var suggestions by remember { mutableStateOf(listOf<String>()) }
        var showSuggestions by remember { mutableStateOf(false) }

        // Filtered list by tag or favorites
        val filteredPlaces = remember(place, tags, selectedIndex, text) {
            val baseList = when {
                selectedIndex == -1 || tags.isEmpty() -> place
                selectedIndex == -2 -> place.filter { it.isCurrentUserFavorite == true } // ❤️ Favorites
                else -> {
                    val selectedTag = tags.getOrNull(selectedIndex)
                    place.filter { placeDetail ->
                        placeDetail.tagDetails?.any { it.id == selectedTag?.id } == true
                    }
                }
            }

            if (text.isNotEmpty()) {
                baseList.filter {
                    (it.placeName?.contains(text, ignoreCase = true) == true) ||
                            (it.placeNameAr?.contains(text, ignoreCase = true) == true)
                }
            } else baseList
        }


        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Row (Back + Search)
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
            ) {
                // Back button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(end = 8.dp, top = 8.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .clickable {
                            if (context is ComponentActivity) {
                                context.finish()
                            }
                        }
                ) {
                    val layoutDirection = LocalLayoutDirection.current
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(if (layoutDirection == LayoutDirection.Rtl) 180f else 0f),
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }

                // Search Row
                Column(modifier = Modifier.weight(1f)
//                    .height(56.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(28.dp)),
                ) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = text,
                            onValueChange = {
                                text = it

                                val isArabic = currentLocale.language == "ar"

                                suggestions = place.mapNotNull { p ->
                                    val name = if (isArabic) p.placeNameAr else p.placeName
                                    if (name != null && name.startsWith(it, ignoreCase = true)) name else null
                                }

                                showSuggestions = suggestions.isNotEmpty() && it.isNotEmpty()
                            },
                            placeholder = { Text("Search...") },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp),
                            singleLine = true
                        )

                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF6F2C))
                                .clickable {
                                    // trigger search
                                    showSuggestions = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "Search Image",
                                modifier = Modifier.size(20.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                    }

                    // Suggestions dropdown
                    if (showSuggestions) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 150.dp)
//                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(top = 4.dp)
                        ) {
                            items(suggestions) { suggestion ->
                                Text(
                                    text = suggestion,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            text = suggestion
                                            showSuggestions = false
                                        }
                                        .padding(12.dp),
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
            val currentLocale = LocalConfiguration.current.locales[0]
            Row(
                modifier = Modifier
                    .horizontalScroll(horizontalScrollState)
                    .padding(top = 12.dp)
            ) {
                val isAllSelected = selectedIndex == -1
                val isFavoriteSelected = selectedIndex == -2  // New state for favorites

                // "All" Button
                OutlinedButton(
                    onClick = { selectedIndex = -1 },
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, if (isAllSelected) Color(0xFFFF7D36) else Color.Gray),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFFF7D36),
                        contentColor = if (isAllSelected) Color.White else Color.Black
                    ),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(38.dp)
                ) {
                    Text(
                        text = if (currentLocale.language == "ar") "الكل" else "All",
                        fontSize = 14.sp,
                        fontFamily = cairoFamily
                    )
                }
                OutlinedButton(
                    onClick = {
                        selectedIndex = -2
                    },
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, if (isFavoriteSelected) Color(0xFFFF7D36) else Color.Gray),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFFF7D36),
                        contentColor = if (isFavoriteSelected) Color.White else Color.Black
                    ),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(38.dp)
                ) {
                    Image(
                        painter = painterResource(
                            id = if (isFavoriteSelected)
                                R.drawable.ic_fav_heart
                            else
                                R.drawable.ic_heart
                        ),
                        contentDescription = "Favorite",
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 4.dp, top = 4.dp)
                    )
                    Text(
                        text = if (currentLocale.language == "ar") "المفضلة" else "Favorite",
                        fontSize = 14.sp,
                        fontFamily = cairoFamily
                    )
                }

                // Other tags
                tags.forEachIndexed { index, tag ->
                    val isSelected = selectedIndex == index
                    val backgroundColor = Color(android.graphics.Color.parseColor(tag.colorCode ?: "#FF7D36"))
                    OutlinedButton(
                        onClick = { selectedIndex = index },
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(
                            1.dp,
                            Color(android.graphics.Color.parseColor(tag.colorCode ?: "#FF7D36"))
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(android.graphics.Color.parseColor(tag.colorCode ?: "#FF7D36")),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(38.dp)
                    ) {
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
            var expanded by remember { mutableStateOf(false) }
            var selectedOption by remember { mutableStateOf("Popular") }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.Nearby_restaurant),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = cairoFamily,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
                val options = listOf(
                    stringResource(id = R.string.Popular),
                    stringResource(id = R.string.Name),
                    stringResource(id = R.string.Oldest),
                    stringResource(id = R.string.Distance)
                )

                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { expanded = true }
                    ) {
                        Text(
                            text = "Sort by : ",
                            fontSize = 14.sp,
                            fontFamily = cairoFamily,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            text = selectedOption,
                            fontSize = 14.sp,
                            fontFamily = cairoFamily,
                            color = Color(0xFFFF5722),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_arrowdown),
                            contentDescription = "Dropdown arrow",
                            modifier = Modifier
                                .size(30.dp)
                                .padding(top = 16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(162.dp)
                            .background(Color.White)
                    ) {
                        options.forEachIndexed { index, option ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedOption = option
                                    expanded = false
                                },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = option,
                                            fontSize = 14.sp,
                                            fontFamily = cairoFamily,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (option == selectedOption) {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_selectedring),
                                                contentDescription = "Selected",
                                                modifier = Modifier.size(16.dp)
                                            )
                                        } else {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_unselectedring),
                                                contentDescription = "Unselected",
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            )
                            if (index < options.lastIndex) Divider()
                        }
                    }
                }
            }

            // List of places
            LazyColumn {
                if (filteredPlaces.isEmpty()) {
                    item {
                        Text(
                            text = "No places match this filter",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(filteredPlaces.size) { index ->
                        RestaurntCard(
                            place = filteredPlaces[index],
                            image = image,
                            isFav = isFav,
                            onFavoriteClick = { placeId, toggleHeartIcon ->
                                favorite(placeId, toggleHeartIcon)
                            },
                            onClick = {
                                // Create intent to start DetailsActivity
                                val intent = Intent(context, DetailsActivity::class.java).apply {
                                    // Pass all necessary data
                                    putExtra("id", filteredPlaces[index].id)
                                    putExtra("placeName", filteredPlaces[index].placeName)
                                    putExtra("placeNameAr", filteredPlaces[index].placeNameAr)
                                    putExtra("description", filteredPlaces[index].description)
                                    putExtra("descriptionAr", filteredPlaces[index].descriptionAr)
                                    putExtra("address", filteredPlaces[index].address)
                                    putExtra("distance", filteredPlaces[index].distance)
                                    putExtra("lat", filteredPlaces[index].lat)
                                    putExtra("lng", filteredPlaces[index].lon)
                                    putExtra("currentUserFavourite", filteredPlaces[index].isCurrentUserFavorite)
                                    putExtra("totalfavplace", filteredPlaces[index].total_fav_place)
                                    putExtra("totalunfavplace", filteredPlaces[index].total_unfav_place)
                                    putExtra("suggestedTime", filteredPlaces[index].suggestedTime)
                                    putExtra("advice", filteredPlaces[index].advice)
                                    putExtra("videoLinkEn", filteredPlaces[index].video_link_en)
                                    putExtra("videoLinkAr", filteredPlaces[index].video_link_ar)
                                    putExtra("adviceArabic", filteredPlaces[index].adviceArabic)
                                    putExtra("promoCodeAndDiscount", filteredPlaces[index].promoCodeAndDiscount)
                                    putExtra("promoCodePercentage", filteredPlaces[index].promoCodePercentage)
                                    putExtra("icon", filteredPlaces[index].icon)
                                    putExtra("avgRating", filteredPlaces[index].rating)

                                    // Pass tag details as JSON
                                    val gson = Gson()
                                    val tagDetailsJson = gson.toJson(filteredPlaces[index].tagDetails)
                                    putExtra("placeDetails", tagDetailsJson)

                                    // Pass images as JSON
                                    val imagesJson = gson.toJson(image)
                                    putExtra("images", imagesJson)
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
@Composable
fun RestaurntCard(place: PlaceDetail,image: List<PlacesImage>,onFavoriteClick: (String, (Boolean) -> Unit) -> Unit,isFav: Boolean,onClick: () -> Unit ) {
    var isFavorite by remember { mutableStateOf(place.isCurrentUserFavorite) }

    // Update local state when place changes
    LaunchedEffect(place.isCurrentUserFavorite) {
        isFavorite = place.isCurrentUserFavorite
    }
//    var isFav by remember {
//        mutableStateOf(isFav)
//    }
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val context = LocalContext.current
    val currentLocale = LocalConfiguration.current.locales[0]
    val tagDetails = place.tagDetails ?: emptyList()
    val placeImage = remember(image, place.id) {
        println("All images: ${image?.joinToString { it.placeId }}")
        println("Looking for place: ${place.id}")
        image?.firstOrNull {
            println("Checking image with placeId: ${it.placeId}")
            it.placeId.toString() == place.id.toString()
        }?.also {
            println("Found matching image: ${it.image}")
        }
    }
    var rating: Float
    try {
        rating = place.rating.toFloat()
    }catch (e:Exception) {
        rating = 0.0F
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .border(
                width = 2.dp,
                color = Color(0xFFE7E7E9),
                shape = RoundedCornerShape(20.dp)
            ).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                if (placeImage != null && placeImage.image.isNotBlank()) {
                    AsyncImage(
                        model = placeImage.image,
                        contentDescription = place.placeName,
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
                        error = painterResource(R.drawable.ic_placeholder),
                        onError = { result ->
                            println("Failed to load image: $result")
                        }
                    )
                } else {
                    println("No image found for place ${place.id}")
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent) // Optional: Explicitly set transparent
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, end = 10.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .background(color = Color.Gray, shape = CircleShape)
                            .clickable {
                                // Optimistically update UI
                                isFavorite = !isFavorite
                                // Make API call
                                onFavoriteClick(place.id) { success ->
                                    if (!success) {
                                        // Revert if API call failed
                                        isFavorite = !isFavorite
                                    }
                                }
                            }
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (isFavorite) R.drawable.ic_fav else R.drawable.ic_heart
                            ),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp))
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(end = 10.dp, bottom = 6.dp)
                        .size(40.dp)
                        .background(color = Color(0xFFFF7D36), shape = CircleShape)
                        .align(Alignment.BottomEnd)
                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_restaurant),
//                        contentDescription = "",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.size(24.dp),
//                        colorFilter = ColorFilter.tint(Color.White)
//                    )

                    AsyncImage(
                        modifier = Modifier.size(24.dp),
                        model = ImageRequest.Builder(context)
                            .data(place.icon)
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            val currentLocale = LocalConfiguration.current.locales[0]
            Text(
                text = if (currentLocale.language == "ar") place.placeNameAr else place.placeName,
                fontSize = 18.sp,
                fontFamily = cairoFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                style = LocalTextStyle.current.copy(textDirection = TextDirection.ContentOrRtl)
            )
            var favStatus by remember {
                mutableStateOf(
                    when {
                        place.total_fav_place == "1" -> "Like"
                        place.total_unfav_place == "1" -> "Dislike"
                        else -> ""
                    }
                )
            }

            var totalFavPlace by remember { mutableStateOf(if (place.total_fav_place == "1") "1" else "0") }
            var totalUnfavPlace by remember { mutableStateOf(if (place.total_unfav_place == "1") "1" else "0") }
            Row(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_thumb_up),
                    contentDescription = "Like",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
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
                    colorFilter = ColorFilter.tint(
                        if (favStatus == "Like") Color(0xFFFF7D36) else Color.Gray
                    )
                )

                // Like Count - only show if count is "1"
                if (totalFavPlace == "1") {
                    Text(
                        text = totalFavPlace,
                        fontFamily = cairoFamily,
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = "|",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.Gray
                )

                // Dislike Button
                Image(
                    painter = painterResource(id = R.drawable.ic_thumbdown),
                    contentDescription = "Dislike",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
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
                    colorFilter = ColorFilter.tint(
                        if (favStatus == "Dislike") Color(0xFFFF7D36) else Color.Black
                    )
                )

                // Dislike Count - only show if count is "1"
                if (totalUnfavPlace == "1") {
                    Text(
                        text = totalUnfavPlace,
                        fontFamily = cairoFamily,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.padding(end = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(3) { index ->   // Always show 3 stars
                        val starColor = if (index < rating.toInt()) {
                            Color(0xFFE25E16)       // Selected stars
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
            }
            Row(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth(), // Ensure the Row takes full width
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Location icon
                Image(
                    painter = painterResource(id = R.drawable.ic_locate),
                    contentDescription = "locate",
                    modifier = Modifier.size(20.dp)
                )

                // Address (single line, ellipsis if too long)
                Text(
                    text = place.address,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // Show "..." if text is too long
                    fontFamily = cairoFamily,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .weight(1f), // Takes remaining space but stays in one line
                    style = LocalTextStyle.current.copy(textDirection = TextDirection.ContentOrRtl)
                )

                // Divider
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .width(1.dp)
                        .height(14.dp)
                        .background(Color.Gray)
                )

                // Routing icon
                Image(
                    painter = painterResource(id = R.drawable.ic_routing),
                    contentDescription = "routing",
                    modifier = Modifier.size(20.dp)
                )

                // Distance
                Text(
                    text = "${place.distance} km",
                    fontFamily = cairoFamily,
                    maxLines = 1,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                )
            }
            Text(
                text = buildAnnotatedString {
                    val htmlText = if (currentLocale.language == "ar") place.descriptionAr else place.description
                    append(
                        AnnotatedString(
                            HtmlCompat.fromHtml(
                                htmlText,
                                HtmlCompat.FROM_HTML_MODE_COMPACT
                            ).toString()
                        )
                    )
                },
                fontSize = 14.sp,
                fontFamily = cairoFamily,
                maxLines = 4,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                overflow = TextOverflow.Ellipsis
            )
            val horizontalScrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .horizontalScroll(horizontalScrollState)
                    .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            ) {
                tagDetails?.forEachIndexed { index, tag ->
                    val backgroundColor =
                        Color(android.graphics.Color.parseColor(tag.colorCode ?: "#FF7D36"))


//                val textColor = if (isSelected) Color.White else Color(android.graphics.Color.parseColor(tag.colorCode ?: "#FF7D36"))

                    Button(
                        onClick = {},
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
                            modifier = Modifier
                        )

                    }
                }
            }
        }
    }
}
}
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview2() {
//    CineramaMaps_AndroidTheme {
//        SearchBar()
//    }
//}