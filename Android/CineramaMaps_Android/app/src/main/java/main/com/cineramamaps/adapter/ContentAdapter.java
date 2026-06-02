package main.com.cineramamaps.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.model.ContentItem;
public class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ContentItem> list;

    public ContentAdapter(Context context, List<ContentItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == 0)
            return new TextHolder(inflater.inflate(R.layout.item_text, parent, false));
        else if (viewType == 1)
            return new ImageHolder(inflater.inflate(R.layout.item_image, parent, false));
        else
            return new VideoHolder(inflater.inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ContentItem item = list.get(position);

        // ================= TEXT =================
        if (holder instanceof TextHolder) {

            TextHolder h = (TextHolder) holder;

            WebSettings settings = h.webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);

            h.webView.setVerticalScrollBarEnabled(false);
            h.webView.setHorizontalScrollBarEnabled(false);

            h.webView.setWebViewClient(new WebViewClient());

            String html = item.content;

            if (html != null) {
                html = html.replaceAll("(?i)<li>(.*?)</li>", "<p>&#8226; $1</p>");
                html = html.replaceAll("(?i)</?ul>", "");
                html = html.replace("\\/", "/");
                html = html.replaceAll("<ul>", "<ul style='list-style-type:disc;'>");
                // ❌ REMOVE base64 image (black box fix)
                html = html.replaceAll("(?i)<img[^>]*src\\s*=\\s*['\"]data:image[^'\"]*['\"][^>]*>", "");

                // ❌ REMOVE empty paragraphs
                html = html.replaceAll("<p>(\\s|&nbsp;|<br\\s*/?>)*</p>", "");

                // ❌ REMOVE google translate junk div
                html = html.replaceAll("(?i)<div[^>]*gtx[^>]*>.*?</div>", "");

                // ✅ MAKE iframe responsive
                html = html.replaceAll("<iframe", "<iframe style='width:100%;height:300px;border:0;' ");

                // ✅ MAKE images responsive
                html = html.replaceAll("<img", "<img style='max-width:100%;height:auto;' ");
                html = html.replaceAll("(?i)<li>(.*?)</li>", "<p>• $1</p>");
                html = html.replaceAll("(?i)</?ul>", "");

            }
            Log.e("HTML_CHECK", html);
            System.out.println("ssssssssssssssssssssss "+html);

//            String finalHtml =
//                    "<html>" +
//                            "<head>" +
//                            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
//                            "<style>" +
//
//                            "body{font-family:sans-serif;line-height:1.6;margin:0;padding:0;color:#000;}" +
//
//                            /* ❌ REMOVE default bullet */
//                            "ul{list-style:none; padding-left:0;}" +
//
//                            /* ✅ CUSTOM BULLET (FORCE) */
//                            "li{position:relative; padding-left:20px; margin-bottom:10px;}" +
//                            "li:before{content:'\\2022'; position:absolute; left:0; top:0; color:black; font-size:18px;}" +
//
//                            /* responsive */
//                            "iframe{max-width:100%;}" +
//                            "img{max-width:100%;height:auto;}" +
//
//                            "</style>" +
//                            "</head>" +
//                            "<body>" +
//                            html +
//                            "</body></html>";
            String finalHtml =
                    "<html>" +
                            "<head>" +
                            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                            "<style>" +
                            "body{font-family:Arial, sans-serif;line-height:1.6;margin:0;padding:0;color:#000;}" +
                            "p{margin-bottom:10px;}" +
                            "</style>" +
                            "</head>" +
                            "<body>" +
                            html +
                            "</body></html>";

            h.webView.loadDataWithBaseURL(null, finalHtml, "text/html", "UTF-8", null);
        }

        // ================= IMAGE =================
        else if (holder instanceof ImageHolder) {

            Glide.with(context)
                    .load(item.content)
                    .into(((ImageHolder) holder).image);
        }

        // ================= VIDEO =================
        else if (holder instanceof VideoHolder) {

            String videoUrl = item.content;

            if (videoUrl == null || videoUrl.isEmpty()) return;

            String videoId = "";

            if (videoUrl.contains("embed/")) {
                videoId = videoUrl.split("embed/")[1];
            } else if (videoUrl.contains("watch?v=")) {
                videoId = videoUrl.split("v=")[1];
            }

            if (videoId.contains("?")) {
                videoId = videoId.split("\\?")[0];
            }

            final String finalVideoId = videoId;

            if (finalVideoId.isEmpty()) return;

            String thumb = "https://img.youtube.com/vi/" + finalVideoId + "/0.jpg";

            Glide.with(context).load(thumb).into(((VideoHolder) holder).thumb);

            ((VideoHolder) holder).thumb.setOnClickListener(v -> {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=" + finalVideoId)));
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ================= HOLDERS =================

    class TextHolder extends RecyclerView.ViewHolder {
        WebView webView;

        TextHolder(View v) {
            super(v);
            webView = v.findViewById(R.id.webViewText);
        }
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        ImageView image;

        ImageHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
        }
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        ImageView thumb;

        VideoHolder(View v) {
            super(v);
            thumb = v.findViewById(R.id.thumb);
        }
    }
}
/*
public class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ContentItem> list;

    public ContentAdapter(Context context, List<ContentItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == 0)
            return new TextHolder(inflater.inflate(R.layout.item_text, parent, false));
        else if (viewType == 1)
            return new ImageHolder(inflater.inflate(R.layout.item_image, parent, false));
        else if (viewType == 2)
            return new VideoHolder(inflater.inflate(R.layout.item_video, parent, false));
        else
            return new MapHolder(inflater.inflate(R.layout.item_map, parent, false));
    }
    public String getTrimmedHtml(String html, int limit) {

        Document doc = Jsoup.parse(html);
        String text = doc.text();

        if (text.length() <= limit) return html;

        String trimmedText = text.substring(0, limit);

        // 🔥 simple safe return (no broken tags)
        return trimmedText;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ContentItem item = list.get(position);

        if (holder instanceof TextHolder) {

            TextHolder h = (TextHolder) holder;

            WebSettings settings = h.webView.getSettings();
            settings.setJavaScriptEnabled(true);

            String html = item.content;

            String finalHtml =
                    "<html>" +
                            "<head>" +
                            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +

                            "<style>" +
                            "body{font-family:sans-serif;line-height:1.6;margin:0;padding:0;}" +
                            "#content{overflow:hidden;max-height:200px;cursor:pointer;}" +
                            "#btn{color:blue;font-weight:bold;margin-top:8px;display:none;cursor:pointer;}" +
                            "</style>" +

                            "<script>" +

                            "var isExpandable = false;" +

                            "function checkHeight(){ " +
                            "var content=document.getElementById('content');" +
                            "var btn=document.getElementById('btn');" +

                            "if(content.scrollHeight > 200){" +
                            "isExpandable = true;" +
                            "btn.style.display='block';" +
                            "}else{" +
                            "content.style.maxHeight='none';" +
                            "btn.style.display='none';" +
                            "}" +
                            "}" +

                            "function toggle(){ " +
                            "if(!isExpandable) return;" +

                            "var content=document.getElementById('content');" +
                            "var btn=document.getElementById('btn');" +

                            "if(content.style.maxHeight==='none'){" +
                            "content.style.maxHeight='200px';" +
                            "btn.innerHTML='Read More';" +
                            "}else{" +
                            "content.style.maxHeight='none';" +
                            "btn.innerHTML='Read Less';" +
                            "}" +
                            "}" +

                            "</script>" +

                            "</head>" +

                            "<body onload='checkHeight()'>" +

                            "<div id='content' onclick='toggle()'>" + html + "</div>" +

                            "<div id='btn' onclick='toggle()'>Read More</div>" +

                            "</body></html>";

            h.webView.loadDataWithBaseURL(null, finalHtml, "text/html", "UTF-8", null);
        }
        else if (holder instanceof ImageHolder) {
            Glide.with(context).load(item.content).into(((ImageHolder) holder).image);
        }

        else if (holder instanceof VideoHolder) {



            String videoUrl = item.content;
            String videoId = "";

            if (videoUrl.contains("embed/")) {
                videoId = videoUrl.split("embed/")[1];
            } else if (videoUrl.contains("watch?v=")) {
                videoId = videoUrl.split("v=")[1];
            }

            if (videoId.contains("?")) {
                videoId = videoId.split("\\?")[0];
            }

// ✅ FINAL variable
            final String finalVideoId = videoId;

            String thumb = "https://img.youtube.com/vi/" + finalVideoId + "/0.jpg";

            Glide.with(context).load(thumb).into(((VideoHolder) holder).thumb);

            ((VideoHolder) holder).thumb.setOnClickListener(v -> {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=" + finalVideoId)));
            });
        }

        else if (holder instanceof MapHolder) {

            MapHolder h = (MapHolder) holder;

            h.mapView.getMapAsync(map -> {

                LatLng loc = new LatLng(item.lat, item.lng);

                map.addMarker(new MarkerOptions().position(loc));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 12));

                map.setOnMapClickListener(l -> {
                    String uri = "geo:" + item.lat + "," + item.lng;
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TextHolder extends RecyclerView.ViewHolder {
        WebView webView;
        TextHolder(View v) {
            super(v);
            webView = v.findViewById(R.id.webViewText);
        }
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageHolder(View v) { super(v); image = v.findViewById(R.id.image); }
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        ImageView thumb;
        VideoHolder(View v) { super(v); thumb = v.findViewById(R.id.thumb); }
    }

    class MapHolder extends RecyclerView.ViewHolder {
        MapView mapView;
        MapHolder(View v) {
            super(v);
            mapView = v.findViewById(R.id.mapView);
            mapView.onCreate(null);
            mapView.onResume();
        }
    }
}*/
