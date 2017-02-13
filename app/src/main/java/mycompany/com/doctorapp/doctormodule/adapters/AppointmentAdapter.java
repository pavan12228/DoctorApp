package mycompany.com.doctorapp.doctormodule.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.doctormodule.models.AppointmentModel;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sciens1 on 8/12/2016.
 */
public class AppointmentAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context mContext;
    List<AppointmentModel> appointmentModelList;
    Util mUtil;

    public AppointmentAdapter(Context activity, List<AppointmentModel> appointmentModelList) {
        this.appointmentModelList = appointmentModelList;
        this.mContext = activity;
        Log.d("Lokesh",""+appointmentModelList.size());
        mUtil = new Util();

    }

    @Override
    public int getCount() {
        return appointmentModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return appointmentModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        if (inflater == null){
            inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }if (convertView == null) {
            convertView = inflater.inflate(R.layout.appointment_adapter_list_item, null);

      /*      convertView.setTag(holder);
        }else {
            holder = (Holder)convertView.getTag();
        }*/
        }
        Holder holder = new Holder();
        holder.mTime = (TextView) convertView.findViewById(R.id.appointment_adapter_time_tv);
        holder.mMobile = (TextView) convertView.findViewById(R.id.appointment_adapter_mobile_tv);
        holder.mEmail = (TextView) convertView.findViewById(R.id.appointment_adapter_email_tv);
        holder.tvStatus = (TextView) convertView.findViewById(R.id.appointment_adapter_status);


        holder.tvCancel = (TextView) convertView.findViewById(R.id.appointment_adapter_cancel_tv);
        holder.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAppointment(i);
            }
        });

        AppointmentModel appointmentModel = appointmentModelList.get(i);
        holder.mTime.setText(appointmentModel.getTime());
        holder.mMobile.setText(appointmentModel.getMobile());
        holder.mEmail.setText(appointmentModel.getEmail());
        int statusCode  = Integer.parseInt(appointmentModel.getAppointmentStatus());
        switch (statusCode){
            case 0:
                holder.tvStatus.setText("Pending");
                break;
            case 1:
                holder.tvStatus.setText("Confirmed");
                break;
            case 2:
                holder.tvStatus.setText("In Progress");
                break;
            case 3:
                holder.tvStatus.setText("Completed");
                break;
            case 4:
                holder.tvStatus.setText("Cancelled by Patient");
                break;
            case 5:
                holder.tvStatus.setText("Reschedule Requested");
                break;
            case 6:
                holder.tvStatus.setText("Cancelled by You");
                break;
        }

        return convertView;
    }

    private void cancelAppointment(final int d) {
        if (mUtil.isNetworkAvailable(mContext)) {
            mUtil.showProgressDialog(mContext);
            mUtil.getBaseClassService(mContext, "").cancelAppointmentFromDoctor(appointmentModelList.get(d).getAppointmentId(),appointmentModelList.get(d).getPatientId(), new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if(jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("Success")) {
                        mUtil.dismissDialog();
                        callToast(jsonObject.get("message").getAsString());
                        appointmentModelList.remove(d);
                    }
                    else {
                        mUtil.dismissDialog();
                        try {
                            callToast(jsonObject.get("message").getAsString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    callToast("Retro Error!");
                }
            });
        }
    }


    public class Holder{
        private TextView mTime,mMobile,mEmail,tvCancel,tvStatus;
    }
    public  void callToast(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
