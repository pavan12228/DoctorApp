package mycompany.com.doctorapp.patientmodule.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.patientmodule.PatiantTimeSlotActivity;
import mycompany.com.doctorapp.patientmodule.models.SearchDoctorModel;

/**
 * Created by Dell on 8/12/2016.
 */
public class SearchDoctorsAdapter extends BaseAdapter implements Serializable{
    Context mApplicationContext;
    List<SearchDoctorModel> mSearchDoctorModelList;
    LayoutInflater inflater;
    public SearchDoctorsAdapter(Context applicationContext, List<SearchDoctorModel> searchDoctorModelList) {
        this.mApplicationContext = applicationContext;
        this.mSearchDoctorModelList =searchDoctorModelList;
    }

    @Override
    public int getCount() {
        return  mSearchDoctorModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return mSearchDoctorModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView( int i, View view, ViewGroup viewGroup) {
        ViewHold viewHolder = new ViewHold();
        if(inflater == null){
            inflater = (LayoutInflater) mApplicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(view == null){
            view = inflater.inflate(R.layout.inflator_doctorlist,null);
            viewHolder.inflatordoctorlist_name_tv = (TextView) view.findViewById(R.id.inflatordoctorlist_name_tv);
            viewHolder.inflatordoctorlist_book_tv =(TextView) view.findViewById(R.id.inflatordoctorlist_book_tv);
            viewHolder.tvHostpitalNAddress = (TextView) view.findViewById(R.id.inflatordoctorlist_address_tv);
            viewHolder.tvFee = (TextView) view.findViewById(R.id.inflatordoctorlist_fee_tv);
            viewHolder.tvDistance = (TextView) view.findViewById(R.id.inflatordoctorlist_km_tv);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHold) view.getTag();
        }




        final SearchDoctorModel searchDoctorModel =  mSearchDoctorModelList.get(i);
        viewHolder.inflatordoctorlist_name_tv.setText(searchDoctorModel.getName());
        viewHolder.tvHostpitalNAddress.setText(searchDoctorModel.getHospitol() + " "+searchDoctorModel.getAddress());
        viewHolder.tvFee.setText(searchDoctorModel.getFee());
        viewHolder.tvDistance.setText(searchDoctorModel.getDistance());

        viewHolder.inflatordoctorlist_book_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mApplicationContext,PatiantTimeSlotActivity.class);
                intent.putExtra("model", /*(Serializable) */searchDoctorModel);
                intent.putExtra("adapter","SearchAdapter");
//                intent.putExtra("selected",String.valueOf(i));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mApplicationContext.startActivity(intent);
            }
        });
        return view;
    }

    private class ViewHold {
    private TextView inflatordoctorlist_name_tv,inflatordoctorlist_book_tv,
        tvHostpitalNAddress,tvFee, tvDistance;
    }
}
