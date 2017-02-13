package mycompany.com.doctorapp.patientmodule.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.patientmodule.SearchDoctorsActivity;
import mycompany.com.doctorapp.patientmodule.models.SearchDoctorModel;
import mycompany.com.doctorapp.utils.Util;

/**
 * Created by Dell on 8/9/2016.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private TextView mSpeciality, mLocation, mHospitalName,fragmenthome_search_tv,tvSpeciality,tvLocation,tvHospitalName;
    private ImageView ivDentist,ivCardiologist,ivAyurveda,ivGynecologist,ivDermotologist,ivNeurologist,ivHomeopth,
            ivGastro,ivGeneral,ivPsychiatrist,ivEarNoseThroat;
    private EditText mEdittext1, mEdittext2;
    private Util mUtil;
    private int mUserId,selectedFields=0;
    private List<SearchDoctorModel> searchDoctorModelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeViews(view);
        mUtil = new Util();
        mUtil.getSharedpreferences(getActivity());
//        mUserId = Integer.parseInt(mUtil.getString(StringConstants.USERID));
        return view;
    }

    private void initializeViews(View view) {
        mSpeciality = (TextView) view.findViewById(R.id.fragmenthome_speciality_tv);
        mSpeciality.setOnClickListener(this);

        mLocation = (TextView) view.findViewById(R.id.fragmenthome_location_tv);
        mLocation.setOnClickListener(this);

        mHospitalName = (TextView) view.findViewById(R.id.fragmenthome_hospitalname_tv);
        mHospitalName.setOnClickListener(this);

        fragmenthome_search_tv = (TextView) view.findViewById(R.id.fragmenthome_search_tv);
        fragmenthome_search_tv.setOnClickListener(this);





//            Specialities
        ivDentist = (ImageView) view.findViewById(R.id.fragmenthome_dentist_iv);
        ivDentist.setOnClickListener(this);

        ivCardiologist = (ImageView) view.findViewById(R.id.fragmenthome_cardiologist_iv);
        ivCardiologist.setOnClickListener(this);

        ivAyurveda = (ImageView) view.findViewById(R.id.fragmenthome_ayurveda_iv);
        ivAyurveda.setOnClickListener(this);

        ivGynecologist = (ImageView) view.findViewById(R.id.fragmenthome_gynocologist_iv);
        ivGynecologist.setOnClickListener(this);

        ivDermotologist = (ImageView) view.findViewById(R.id.fragmenthome_dermo_iv);
        ivDermotologist.setOnClickListener(this);

        ivNeurologist = (ImageView) view.findViewById(R.id.fragmenthome_neurologist_iv);
        ivNeurologist.setOnClickListener(this);

        ivHomeopth = (ImageView) view.findViewById(R.id.fragmenthome_homeopath_iv);
        ivHomeopth.setOnClickListener(this);

        ivGastro = (ImageView) view.findViewById(R.id.fragmenthome_gastro_iv);
        ivGastro.setOnClickListener(this);

        ivGeneral = (ImageView) view.findViewById(R.id.fragmenthome_general_iv);
        ivGeneral.setOnClickListener(this);

        ivPsychiatrist = (ImageView) view.findViewById(R.id.fragmenthome_psych_iv);
        ivPsychiatrist.setOnClickListener(this);

        ivEarNoseThroat= (ImageView) view.findViewById(R.id.fragmenthome_ear_nose_throat_iv);
        ivEarNoseThroat.setOnClickListener(this);

        mEdittext1 = (EditText) view.findViewById(R.id.fragmenthome_edittext1);
        mEdittext2 = (EditText) view.findViewById(R.id.fragmenthome_edittext2);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.fragmenthome_speciality_tv:
                mEdittext1.setText("");
                mEdittext2.setText("");
                mSpeciality.setBackground(getResources().getDrawable(R.drawable.linecorner_left_filled));
                mLocation.setBackground(getResources().getDrawable(R.drawable.square_white));
                mHospitalName.setBackground(getResources().getDrawable(R.drawable.linecorner_right));

                mSpeciality.setTextColor(getResources().getColor(R.color.white));
                mLocation.setTextColor(getResources().getColor(R.color.flash_green));
                mHospitalName.setTextColor(getResources().getColor(R.color.flash_green));
                mEdittext1.setHint("Specialization");
                mEdittext2.setHint("Location");
                selectedFields=1;
//                location_tv.setBackground(getResources().getDrawable(R.drawable.linecorner_left));
//        Drawable mDrawable = getActivity().getResources().getDrawable(R.drawable.square);
//        mDrawable.setColorFilter((new PorterDuffColorFilter(getResources().getColor(R.color.flash_green), PorterDuff.Mode.MULTIPLY)));
                break;
            case R.id.fragmenthome_location_tv:
                mEdittext1.setText("");
                mEdittext2.setText("");
                mSpeciality.setBackground(getResources().getDrawable(R.drawable.linecorner_left));
                mLocation.setBackground(getResources().getDrawable(R.drawable.square));
                mHospitalName.setBackground(getResources().getDrawable(R.drawable.linecorner_right));
                mLocation.setTextColor(getResources().getColor(R.color.white));
                mHospitalName.setTextColor(getResources().getColor(R.color.flash_green));
                mSpeciality.setTextColor(getResources().getColor(R.color.flash_green));
                mEdittext1.setHint("Area Name");
                mEdittext2.setHint("City");
                selectedFields=2;
                break;
            case R.id.fragmenthome_hospitalname_tv:
                mEdittext1.setText("");
                mEdittext2.setText("");
                mSpeciality.setBackground(getResources().getDrawable(R.drawable.linecorner_left));
                mLocation.setBackground(getResources().getDrawable(R.drawable.square_white));
                mHospitalName.setBackground(getResources().getDrawable(R.drawable.linecorner_right_filled));
                mHospitalName.setTextColor(getResources().getColor(R.color.white));
                mLocation.setTextColor(getResources().getColor(R.color.flash_green));
                mSpeciality.setTextColor(getResources().getColor(R.color.flash_green));
                mEdittext1.setHint("Hospital Name");
                mEdittext2.setHint("Doctor name");
                selectedFields =3;
                break;
            case  R.id.fragmenthome_search_tv:

                bundle.putString("field1",""+mEdittext1.getText().toString());
                bundle.putString("field2",""+mEdittext2.getText().toString());
                if(mEdittext1.length() !=0 | mEdittext2.length() !=0 ) {
                    bundle.putInt("selectedFields", selectedFields); //selectedFields is identify which 2 fields are taken.
                    intent = new Intent(getActivity(), SearchDoctorsActivity.class);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);

                }else {

                }
                break;
            case R.id.fragmenthome_dentist_iv:
                bundle.putString("speciality","Dentist");
                bundle.putInt("selectedFields", 4);
                intent = new Intent(getActivity(),SearchDoctorsActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                break;

            case R.id.fragmenthome_cardiologist_iv:
                bundle.putString("speciality","Cardiology");
                bundle.putInt("selectedFields", 4);
                intent = new Intent(getActivity(),SearchDoctorsActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                break;

            case R.id.fragmenthome_ayurveda_iv:
                bundle.putString("speciality","ayurveda");
                bundle.putInt("selectedFields", 4);
                intent = new Intent(getActivity(),SearchDoctorsActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);

                break;

            case R.id.fragmenthome_gynocologist_iv:
                bundle.putString("speciality","Gynecology");
                bundle.putInt("selectedFields", 4);
                intent = new Intent(getActivity(),SearchDoctorsActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                break;

            case R.id.fragmenthome_dermo_iv:
                bundle.putString("speciality","dermo");
                bundle.putInt("selectedFields", 4);
                intent = new Intent(getActivity(),SearchDoctorsActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                break;

            case R.id.fragmenthome_neurologist_iv:
                bundle.putString("speciality","Neurology");
                bundle.putInt("selectedFields", 4);
                intent = new Intent(getActivity(),SearchDoctorsActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                break;

            case R.id.fragmenthome_homeopath_iv:
                bundle.putString("speciality","homeopath");
                bundle.putInt("selectedFields", 4);
                intent = new Intent(getActivity(),SearchDoctorsActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                break;

            case R.id.fragmenthome_gastro_iv:
                bundle.putString("speciality","gastro");
                bundle.putInt("selectedFields", 4);
                intent = new Intent(getActivity(),SearchDoctorsActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                break;

            case R.id.fragmenthome_general_iv:
                bundle.putString("speciality","general");
                bundle.putInt("selectedFields", 4);
                intent = new Intent(getActivity(),SearchDoctorsActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                break;

            case R.id.fragmenthome_psych_iv:
                bundle.putString("speciality","psych");
                bundle.putInt("selectedFields", 4);
                intent = new Intent(getActivity(),SearchDoctorsActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                break;

            case R.id.fragmenthome_ear_nose_throat_iv:
                bundle.putString("speciality","ent");
                bundle.putInt("selectedFields", 4);
                intent = new Intent(getActivity(),SearchDoctorsActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                break;

        }
    }

//    private void initializeViews() {
//        speciality_tv = (TextView) getView().findViewById(R.id.speciality_tv);
//        location_tv = (TextView) getView().findViewById(R.id.location_tv);
//        hospital_name_tv = (TextView) getView().findViewById(R.id.hospital_name_tv);
//        edittext1 = (EditText) getView().findViewById(R.id.edittext1);
//        edittext2 = (EditText) getView().findViewById(R.id.edittext2);
//
//
//    }



    void callToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
