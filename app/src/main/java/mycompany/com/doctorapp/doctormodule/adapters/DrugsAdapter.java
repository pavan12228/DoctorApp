package mycompany.com.doctorapp.doctormodule.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.doctormodule.models.DrugsModel;
import mycompany.com.doctorapp.utils.DoctorAppApplication;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;

/**
 * Created by sciens1 on 8/12/2016.
 */
public class DrugsAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context mContext;
    List<DrugsModel> drugsModelList /*= new ArrayList<>()*/;
    private ImageLoader mImageLoader;
    private DisplayImageOptions logooptions;
    DoctorAppApplication application;
    Util mUtil;
//    DoctorAppApplication application;

    public DrugsAdapter(Activity activity, List<DrugsModel> drugsModelList) {
        this.mContext = activity;
        this.drugsModelList = drugsModelList;

        mUtil = new Util();
        application = (DoctorAppApplication)  activity.getApplication();

        mImageLoader = application.rounderCornerImageLoader;

    }

    @Override
    public int getCount() {
        return drugsModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return drugsModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Holder holder = new Holder();
        if (inflater == null){
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drugs_adapter_list_item, null);

            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }


//        holder.mUsername = (TextView)convertView.findViewById(R.id.drugs_adapter_name_tv);
        holder.tvName = (TextView) convertView.findViewById(R.id.drugs_adapter_name_tv);
        holder.tvDescription = (TextView) convertView.findViewById(R.id.drugs_adapter_drugs_description);
        holder.ivDrugImage = (ImageView) convertView.findViewById(R.id.drugs_adapter_imageview);


        DrugsModel drugsModel = drugsModelList.get(i);
//        holder.mUsername.setText(drugsModel.getDrugName());
        holder.tvName.setText(drugsModel.getDrugName());
        holder.tvDescription.setText(drugsModel.getDescription());
        mImageLoader.displayImage(StringConstants.IMAGE_BASE_URL +"drugs/"+ drugsModel.getImageUrl(),  holder.ivDrugImage);
        Log.e("Lokesh","image url in drugs"+StringConstants.IMAGE_BASE_URL + "drugs/"+ drugsModel.getImageUrl());

        return convertView;
    }
    public class Holder{
        private TextView mUsername,tvName,tvDescription;
        private ImageView ivDrugImage;
    }
}
