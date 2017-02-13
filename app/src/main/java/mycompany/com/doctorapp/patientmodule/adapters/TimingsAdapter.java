package mycompany.com.doctorapp.patientmodule.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.patientmodule.models.HoursModel;

/**
 * Created by Dell on 9/16/2016.
 */
public class TimingsAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private Context context;
    private List<HoursModel> hoursModelList;
    public TimingsAdapter(Context patiantTimeSlotActivity, List<HoursModel> hoursModelList) {
    this.context = patiantTimeSlotActivity;
        this.hoursModelList = hoursModelList;
    }

    @Override
    public int getCount() {
        return hoursModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return hoursModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        if (inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        if (view == null) {
            view = inflater.inflate(R.layout.inflator_timings, null);

            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }
        HoursModel hoursModel = hoursModelList.get(i);
        holder.showtime = (TextView) view.findViewById(R.id.showtime);
       holder.showtime.setText(hoursModel.getTime());
        if (hoursModel.getStatus() == 1){
            holder.showtime.setTextColor(context.getResources().getColor(R.color.red));
            holder.showtime.setTag("noClick");
        }
        else{
            holder.showtime.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.showtime.setTag("click");
        }

        return view;
    }
    public class Holder{
        private TextView showtime;
    }
}
