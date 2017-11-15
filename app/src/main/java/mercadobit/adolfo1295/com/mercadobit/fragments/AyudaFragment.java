package mercadobit.adolfo1295.com.mercadobit.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mercadobit.adolfo1295.com.mercadobit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AyudaFragment extends Fragment {

    private View v;

    public AyudaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_ayuda, container, false);
        return v;
    }

}
