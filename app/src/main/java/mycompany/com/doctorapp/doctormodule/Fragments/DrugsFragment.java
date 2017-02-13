package mycompany.com.doctorapp.doctormodule.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.doctormodule.adapters.DrugsAdapter;
import mycompany.com.doctorapp.doctormodule.models.DrugsModel;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sciens1 on 8/12/2016.
 */
public class DrugsFragment extends Fragment {
    ListView mListView;
    List<DrugsModel> drugsModelList = new ArrayList<>();
    DrugsAdapter drugsAdapter;
    Util mUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_drugs_fragment, container, false);
        mUtil = new Util();
        mUtil.getSharedpreferences(getActivity());

        initViews(view);
        getDrugs();

        return view;
    }

    private void initViews(View view) {
        mListView = (ListView) view.findViewById(R.id.drugs_fragment_listview);

    }

    public void callToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void getDrugs() {
        if (mUtil.isNetworkAvailable(getActivity())) {
            mUtil.showProgressDialog(getActivity());
            mUtil.getBaseClassService(getActivity(), "").getDrugs(new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("sucess")) {
                        JsonArray asJsonArray = jsonObject.getAsJsonArray(StringConstants.DATA);
                        for (int i = 0; i < asJsonArray.size(); i++) {
                            JsonObject object = asJsonArray.get(i).getAsJsonObject();
                            DrugsModel drugsModel = new DrugsModel();
                            drugsModel.setDrugName(object.get("company_name").getAsString());
                            drugsModel.setDescription(object.get("description").getAsString());
                            drugsModel.setImageUrl(object.get("image").getAsString());
                            drugsModelList.add(drugsModel);
                        }
                        drugsAdapter = new DrugsAdapter(getActivity(), drugsModelList);
                        mListView.setAdapter(drugsAdapter);
                        mUtil.dismissDialog();
                    } else {
                        mUtil.dismissDialog();
                        try {
                            Toast.makeText(getActivity(), jsonObject.get("errorMessages").getAsString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mUtil.dismissDialog();
                    Log.e("Lokesh", error.toString());
                    mUtil.serviceCallFailermsg(error,getActivity());

                }
            });
        }
    }
}