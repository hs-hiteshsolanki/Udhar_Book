package com.ub.udharbook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class contact_us extends Fragment {
    // implements OnMapReadyCallback
//implements OnMapReadyCallback
    LinearLayout wa,call,email;
    ImageView fb,github,linkedin,skype;
//    GoogleMap mGooglemap;
//    MapView mapView;
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_contact_us, container, false);;

        wa = root.findViewById(R.id.wa);
        call = root.findViewById(R.id.call);
        email = root.findViewById(R.id.email);
        fb = root.findViewById(R.id.fb);
        github = root.findViewById(R.id.github);
        linkedin = root.findViewById(R.id.linkedin);
        skype = root.findViewById(R.id.skype);
//

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "8866103098"));
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "solankihitesh7058@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help & Support");
                startActivity(Intent.createChooser(emailIntent, null));
            }
        });

        wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone=918866103098";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.facebook.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.linkedin.com/in/solanki-hitesh/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/hs-hiteshsolanki";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        skype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return root;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mapView = (MapView) root.findViewById(R.id.map);
//        if (mapView!=null){
//            mapView.onCreate(null);
//            mapView.onResume();
//            mapView.getMapAsync(this);
//        }
//    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        MapsInitializer.initialize(getContext());
//
//        mGooglemap = googleMap;
//
//        LatLng sydney = new LatLng(22.29161, 70.79322);
//
//        mGooglemap.addMarker(new MarkerOptions().position(sydney).title("Office")).showInfoWindow();
//
//        mGooglemap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16.06f));
//
//        mGooglemap.getUiSettings().setMapToolbarEnabled(true);
//
//    }
}