package main.com.cineramamaps.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.model.Menudata;
import main.com.cineramamaps.model.PlacesBean;
import main.com.cineramamaps.model.PlacesImage;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ImagesFragment extends Fragment
{
    String videoId = "_4715-D3OUc";
    YouTubePlayerView youTubePlayerView;
    String cityid="";
    private String language = "";
    MyLanguageSession myLanguageSession;
    SessionManager session;
    ProgressBar progressbar;
        RecyclerView providerrecyclerview;
        TextView noimagedata;
        private View view;
    RecyclerItemAdapter1 adapter;
        private RecyclerView rv_product;
        ArrayList<Menudata> menulist = new ArrayList<>();
        String itemid="0",status="No",cart_cat_id="";
        String item_id="",item_price="",cartcount="",catid="",subcatid="",childsubcatid="",suppliesstatus="No";
        boolean btnclick = false;
        ProgressBar progress_bar;
        RelativeLayout botumlay;
        Button continuebtn;
        TextView cart_item_txt,totalservicetxt ;
        String image ="";
    public static String getYoutubeVideoId(String url) {
        String videoId1 = null;

        String regex = "(?<=v=|youtu.be/|embed/|shorts/|v/)[^#&?]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            videoId1 = matcher.group();
        }

        return videoId1;
    }
    @Override
    public void onResume() {
        super.onResume();
      //  getCountryMaps();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // 🔥 Memory leak + crash fix
        if (youTubePlayerView != null) {
            youTubePlayerView.release();
        }
    }

    public ImagesFragment(ArrayList<Menudata> menulist, String image, String cityid) {
            this.menulist = menulist;
            this.image = image;
            this.cityid = cityid;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Tools.get().updateResources(getActivity());
            myLanguageSession = new MyLanguageSession(getActivity());
            language = myLanguageSession.getLanguage();
            myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
            view = inflater.inflate(R.layout.fragment_images, container, false);
//        cart_item_txt = getActivity().findViewById(R.id.cart_item_txt);
            providerrecyclerview = view.findViewById(R.id.providerrecyclerview);
            noimagedata = view.findViewById(R.id.noimagedata);
            session = SessionManager.get(getActivity());
            progressbar = view.findViewById(R.id.progressbar);
//        if (!image.equalsIgnoreCase("")) {
//            Picasso.with(getActivity()).load(image).placeholder(R.color.lightgrey).into(imagee);
//        }

//            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
//            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
//            providerrecyclerview.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

//             youTubePlayerView = view.findViewById(R.id.youtube_player_view);
//
//// lifecycle attach
//            getViewLifecycleOwner().getLifecycle().addObserver(youTubePlayerView);
//
//            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//
//                @Override
//                public void onReady(YouTubePlayer youTubePlayer) {
//                    youTubePlayer.cueVideo("dQw4w9WgXcQ", 0);
//                }
//
//                @Override
//                public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError error) {
//
//                    Log.e("YT_ERROR", "Error: " + error.name());
//
//                    new AlertDialog.Builder(getContext())
//                            .setTitle("Video cannot play")
//                            .setMessage("Open in YouTube app?")
//                            .setPositiveButton("Yes", (d, w) -> {
//                                Intent intent = new Intent(Intent.ACTION_VIEW,
//                                        Uri.parse("vnd.youtube:dQw4w9WgXcQ"));
//                                startActivity(intent);
//                            })
//                            .setNegativeButton("No", null)
//                            .show();
//                }
//            });
            YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtube_player_view);
            ImageView thumbnail = view.findViewById(R.id.imgThumbnail);
            ImageView playBtn = view.findViewById(R.id.imgPlay);

            // String videoId = "dQw4w9WgXcQ";
              videoId = "_4715-D3OUc";
            if(language.equalsIgnoreCase("en")){
                videoId = ""+getYoutubeVideoId(""+MapDetailsActivity.youtubelink);
            }else{
                videoId = ""+getYoutubeVideoId(""+MapDetailsActivity.youtubelink_arabic);
            }

// lifecycle
            getViewLifecycleOwner().getLifecycle().addObserver(youTubePlayerView);

// 👉 Thumbnail load (YouTube CDN)
            String thumbUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
            Glide.with(this).load(thumbUrl).into(thumbnail);

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    // try to load video
                    youTubePlayer.cueVideo(videoId, 0);
                }

                @Override
                public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError error) {

                    Log.e("YT", "Error: " + error.name());

                    // 🔥 fallback UI show karo
                    youTubePlayerView.setVisibility(View.GONE);
                    thumbnail.setVisibility(View.VISIBLE);
                    playBtn.setVisibility(View.VISIBLE);
                }
            });


// 👉 Click → YouTube open
            View.OnClickListener openYoutube = v -> {
                try {
//
                    Log.d("youtubeLink", "onClick try:  "+ videoId);
                    Intent intent = new Intent(getActivity(), VideoWebViewActivity.class);
                    intent.putExtra("video_url", "https://m.youtube.com/watch?v="+videoId);
                    startActivity(intent);
//                    Intent intent = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("https://m.youtube.com/watch?v=" + videoId));
//                    startActivity(intent);
                } catch (Exception e) {
                    Log.d("youtubeLink", "onClick catch:  "+ videoId);

                    Intent intent = new Intent(getActivity(), VideoWebViewActivity.class);
                    intent.putExtra("video_url", "https://www.youtube.com/watch?v=" + videoId);
                    startActivity(intent);

//                    startActivity(new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("https://www.youtube.com/watch?v=" + videoId)));
                }
            };

            thumbnail.setOnClickListener(openYoutube);
            playBtn.setOnClickListener(openYoutube);
            return view;
        }


    class RecyclerItemAdapter1 extends RecyclerView.Adapter<RecyclerItemAdapter1.ViewHolder> {

        private List<PlacesImage> friends;
        private Activity activity;
        private int pos;

        public RecyclerItemAdapter1(Activity activity, List<PlacesImage> friends, int pos) {
            this.friends = friends;
            this.activity = activity;
            this.pos = pos;
        }

        @Override
        public RecyclerItemAdapter1.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            //inflate your layout and pass it to view holder
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.photolistitemlay, viewGroup, false);
            RecyclerItemAdapter1.ViewHolder viewHolder = new RecyclerItemAdapter1.ViewHolder(view);
            return viewHolder;
        }

        public void filter(String charText) {
//            Log.e("COMEADDD","DDDDDDD");
//            //charText = charText.toLowerCase(Locale.getDefault());
//            charText = charText.toString().toLowerCase();
//            menulist.clear();
//            if (charText.length() == 0) {
//                menulist.addAll(searchbuymessageBeanListArrayList);
//                System.out.println("sssssssssssssssss 11"+menulist.size());
//                System.out.println("sssssssssssssssss 22"+searchbuymessageBeanListArrayList.size());
//            } else {
//                for (CompanyOffer wp : searchbuymessageBeanListArrayList) {
//                    if (wp.getCompanyName().toLowerCase().contains(charText) || wp.getCompanyNameAr().toLowerCase().contains(charText)|| wp.getDiscountCode().toLowerCase().contains(charText))//.toLowerCase(Locale.getDefault())
//                    {
//                        Log.e("COMEADDD","DDD");
//                        menulist.add(wp);
//                    }
//                }
//                if (menulist==null||menulist.isEmpty()){
//                    //nodatatxt.setVisibility(View.VISIBLE);
//                    // noitemtv.setText(""+getResources().getString(R.string.noitmewithsearch));
//                }
//                else {
//                    // nodatatxt.setVisibility(View.GONE);
//                }
//            }
//            notifyDataSetChanged();
        }



        @Override
        public void onBindViewHolder(final RecyclerItemAdapter1.ViewHolder viewHolder, final int position) {


//        viewHolder.submit.setText(menulist.get(position).getDiscountCode()+" "+menulist.get(position).getDiscountPercentage()+""+""+getResources().getString(R.string.percentdiscount));
//           // viewHolder.datetv.setText(""+menulist.get(position).getDescription());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                viewHolder.datetv.setText(Html.fromHtml(""+menulist.get(position).getDescription(), Html.FROM_HTML_MODE_COMPACT));
//            } else {
//                viewHolder.datetv.setText(Html.fromHtml(""+menulist.get(position).getDescription()));
//            }
            if (!friends.get(position).getImage().equalsIgnoreCase("")) {
                Picasso.get().load(friends.get(position).getImage()).into(viewHolder.catimage);
            }
//            viewHolder.fulllay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent ii = new Intent(getActivity(),HotelDetails.class);
//                    startActivity(ii);
//                }
//            });



        }

        @Override
        public int getItemCount() {
             return (null != friends ? friends.size() : 0);
           // return  10;
        }
        protected class ViewHolder extends RecyclerView.ViewHolder {
            private TextView datetv, submit,txt_name,txt_duration,pricetxt,pricetxt1,txt_count,customizetxt,salepricetxt,txt_qnty;
            ImageView catimage, offeric;
            //   SeeMoreTextView txt_term;
            ImageView favouritests,likeimg;
            RelativeLayout fulllay, ly1,minuslay,pluslay,likelay1;
            LinearLayout addminusbtnlay;
            Button btn_add;
            CheckBox checkb;
            public ViewHolder(View view) {
                super(view);

                catimage =  view.findViewById(R.id.catimage);
//                datetv =  view.findViewById(R.id.datetv);
//                submit =  view.findViewById(R.id.submit);
//                txt_name = (TextView) view.findViewById(R.id.txt_name);
//                txt_qnty1 = (TextView) view.findViewById(R.id.txt_qnty1);
//                txt_term = (SeeMoreTextView) view.findViewById(R.id.txt_term);
//                txt_term.setVisibility(View.VISIBLE);
//                pricetxt = (TextView) view.findViewById(R.id.txt_price);
//                pricetxt1 = (TextView) view.findViewById(R.id.txt_price1);
//                salepricetxt = (TextView) view.findViewById(R.id.txt_mrpprice);
//                txt_qnty = (TextView) view.findViewById(R.id.txt_qnty);
//                img_service = (ImageView) view.findViewById(R.id.pro_image);
//                likeimg = (ImageView) view.findViewById(R.id.likeimg1);
//                likelay1 = (RelativeLayout) view.findViewById(R.id.likelay1);
//                checkb = (CheckBox) view.findViewById(R.id.checkb);
//
            }
        }
    }
    private void getCountryMaps() {
        progressbar.setVisibility(View.VISIBLE);


        ApiCall.get().Create().getCityMapsDetails(getProductParam("Item")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressbar.setVisibility(View.GONE);
                Log.e("GetProducts  ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("-------------PLACES-----------", responseData);
                        JSONObject object = new JSONObject(responseData);


                        if (object.getString("status").equalsIgnoreCase("1")) {

                            PlacesBean successData = new Gson().fromJson(responseData, PlacesBean.class);

                         if( successData.getResult().getPlacesImages()!=null &&  successData.getResult().getPlacesImages().size()>0) {
                             adapter = new RecyclerItemAdapter1(getActivity(), successData.getResult().getPlacesImages(), 0);
                             providerrecyclerview.setAdapter(adapter);
                             adapter.notifyDataSetChanged();
                             providerrecyclerview.setVisibility(View.VISIBLE);
                             noimagedata.setVisibility(View.GONE);
                         }else{
                             providerrecyclerview.setVisibility(View.GONE);
                             noimagedata.setVisibility(View.VISIBLE);
                         }

                        } else {

                            providerrecyclerview.setVisibility(View.GONE);
                            noimagedata.setVisibility(View.VISIBLE);

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                        providerrecyclerview.setVisibility(View.GONE);
                        noimagedata.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
            }
        });

    }
    private HashMap<String,String> getProductParam(String type) {
        HashMap<String,String>param=new HashMap<>();
        param.put("user_id",session.getUserID());
        //  param.put("token",session.getUserDetails().getToken());
        param.put("city_id",""+cityid);
        param.put("lang",""+language);


        return param;

    }
}