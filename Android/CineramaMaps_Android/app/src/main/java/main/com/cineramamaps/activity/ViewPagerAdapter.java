package main.com.cineramamaps.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.model.Bannerdata;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Bannerdata> images;

    public ViewPagerAdapter(Context context, List<Bannerdata> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
       // return 3;
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.coverimageslideritem, null);
        final RelativeLayout mainlay = (RelativeLayout) view.findViewById(R.id.mainlay);
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        final ProgressBar progress = (ProgressBar) view.findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        mainlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(context, ServiceDetailActivity.class);
                ii.putExtra("id",""+images.get(position).getBannerid());
                context.startActivity(ii);
            }
        });

         Picasso.get().load(images.get(position).getBannername()).placeholder(R.color.lightgrey).into(imageView);

//        Toast.makeText(context, String.valueOf(images.size()), Toast.LENGTH_SHORT).show();

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(images.get(position).getLink()));
//                    context.startActivity(i);
//                }catch(Exception e){
//                    e.printStackTrace();
//                    Toast.makeText(context,"Link not valid.", Toast.LENGTH_LONG).show();
//
//                }
//            }
//        });
//if(position == 0){
//    imageView.setImageResource(R.drawable.banner1);
//}
//        if(position == 1){
//            imageView.setImageResource(R.drawable.banner2);
//        }
//        if(position == 2){
//            imageView.setImageResource(R.drawable.banner1);
//        }
//        if(position == 3){
//            imageView.setImageResource(R.drawable.service4);
//        }


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}