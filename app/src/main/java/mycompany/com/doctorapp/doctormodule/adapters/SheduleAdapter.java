package mycompany.com.doctorapp.doctormodule.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.doctormodule.Fragments.EditSheduleFragment;
import mycompany.com.doctorapp.doctormodule.models.SheduleModel;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by santhu on 8/13/2016.
 */
public class SheduleAdapter extends BaseAdapter {
    private final int mUserId;
    LayoutInflater inflater;
    Context mContext;
    List<SheduleModel> sheduleModelList;
    FragmentManager fragmentManager;
    private  TextView textView;
    private Util mUtil;
    public SheduleAdapter(Activity activity, List<SheduleModel> sheduleModelList, FragmentManager fragmentManager) {
        this.mContext = activity;
        this.sheduleModelList = sheduleModelList;
        this.fragmentManager = fragmentManager;
        mUtil = new Util();
        mUserId = Integer.valueOf(mUtil.getString(StringConstants.USERID));
    }

    @Override
    public int getCount() {
        return sheduleModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return sheduleModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        Holder holder = new Holder();
//        SheduleModel sheduleModel = null;
        if (inflater == null){
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.shedule_adapter_list_item,null);
            holder.mId = (TextView)convertView.findViewById(R.id.shedule_adpter_id_tv);
            holder.mEditShedule = (ImageView) convertView.findViewById(R.id.shedule_adpter_edit_iv);
            holder.mCancelShedule = (ImageView) convertView.findViewById(R.id.shedule_adpter_cancel_iv);
            holder.tvFromDate = (TextView) convertView.findViewById(R.id.shedule_adpter_from_date_tv);
            holder.tvToDate = (TextView) convertView.findViewById(R.id.shedule_adpter_to_date_tv);
            holder.llTimings = (LinearLayout) convertView.findViewById(R.id.shedule_adpter_timings_ll);


            final SheduleModel sheduleModel = sheduleModelList.get(i);

            ArrayList<String> stringArrayList = sheduleModel.getTimeSlots();
            for (int i1 = 0; i1 < stringArrayList.size(); i1++) {
                textView = new TextView(mContext);
                textView.setText(stringArrayList.get(i1));
                textView.setTextSize(mContext.getResources().getDimension(R.dimen.bp_margin_7));
                holder.llTimings.addView(textView);
            }
            holder.tvFromDate.setText(sheduleModel.getFromDate());
            holder.tvToDate.setText(sheduleModel.getToDate());
            holder.mId.setText(String.valueOf(i+1));

            holder.mEditShedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    EditSheduleFragment editSheduleFragment = new EditSheduleFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("selectedSechedule", Integer.parseInt(sheduleModel.getBatch_id()));
                    bundle.putString("fromdate",sheduleModel.getFromDate());
                    bundle.putString("todate",sheduleModel.getToDate());
                    editSheduleFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.container,editSheduleFragment);
                    fragmentTransaction.commit();
                }
            });
            holder.mCancelShedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelShedule(Integer.parseInt(sheduleModel.getBatch_id()),mUserId);
                }
            });

            
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }







        return convertView;
    }



    public class Holder{
        private TextView mId,tvFromDate,tvToDate;
        private ImageView mEditShedule,mCancelShedule;
        private LinearLayout llTimings;
    }

    private void cancelShedule(int batchId, int mUserId) {
        mUtil.getBaseClassService(mContext,"").cancelShedule(mUserId, batchId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("Success")) {
                    mUtil.dismissDialog();
                    callToast(jsonObject.get("message").getAsString());

                } else {
                    mUtil.dismissDialog();
                    try {
                        Toast.makeText(mContext, jsonObject.get("data").getAsString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mUtil.dismissDialog();callToast("Retro Error!!");
                Log.d("Lokesh",error.toString());
            }
        });
    }

    private void callToast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

}
