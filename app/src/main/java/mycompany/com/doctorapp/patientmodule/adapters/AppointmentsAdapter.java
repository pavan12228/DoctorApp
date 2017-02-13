package mycompany.com.doctorapp.patientmodule.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.patientmodule.PatiantTimeSlotActivity;
import mycompany.com.doctorapp.patientmodule.models.AppointmentsModel;

/**
 * Created by Dell on 8/11/2016.
 */
public class AppointmentsAdapter extends BaseAdapter implements Serializable {
    Context context;
    List<AppointmentsModel> appointmentsModelList;
    LayoutInflater inflater;

    public AppointmentsAdapter(Context context, List<AppointmentsModel> appointmentsModelList) {
        this.context = context;
        this.appointmentsModelList = appointmentsModelList;
    }


    @Override
    public int getCount() {
        return appointmentsModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return appointmentsModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder = new Holder();
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = inflater.inflate(R.layout.inflator_appointments, null);
            holder.name = (TextView) view.findViewById(R.id.inflator_appointments_name_tv);
            holder.time = (TextView) view.findViewById(R.id.inflator_appointments_date_time_tv);
            holder.specialization = (TextView) view.findViewById(R.id.inflator_appointment_specialization);
            holder.mReSchedule = (Button) view.findViewById(R.id.inflator_appointments_reschedule_bt);
            holder.relative = (RelativeLayout) view.findViewById(R.id.inflator_appointment_relative);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }


      final AppointmentsModel appointmentsModel = appointmentsModelList.get(i);
        holder.name.setText(appointmentsModel.getName());
        holder.time.setText(appointmentsModel.getDate());
        holder.specialization.setText(appointmentsModel.getDoctorQualification() + " " +
                appointmentsModel.getDoctorSpecialization());

        holder.mReSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PatiantTimeSlotActivity.class);
                intent.putExtra("model", (Serializable) appointmentsModel);
                intent.putExtra("adapter", "appointment");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return view;
    }

    private class Holder {
        TextView name, place, time, specialization;
        Button mReSchedule;
        RelativeLayout relative;
    }
}
