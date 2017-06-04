package com.tcs.wba.wbaindiavisit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.BasePDFPagerAdapter;
import es.voghdev.pdfviewpager.library.asset.CopyAsset;
import es.voghdev.pdfviewpager.library.asset.CopyAssetThreadImpl;


public class ContactsFragment extends Fragment {


    FrameLayout root;

    ToggleButton wbaContacts, tcsContacts;

    View contactFrame;

    TextView ctc1, ctc1no, ctc2, ctc2no,ctc3, ctc3no, ctc4, ctc4no, ctc5
            , ctc5no, ctc6, ctc6no, ctc7, ctc7no;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        contactFrame = view.findViewById(R.id.contacts_frame);

        // Listener for wba Contacts
        wbaContacts = (ToggleButton) view.findViewById(R.id.wbaContacts);
        // Listener for tcs Contacts
        tcsContacts = (ToggleButton) view.findViewById(R.id.tcsContacts);

        // Text views
        ctc1 =  (TextView) view.findViewById(R.id.ctc1);
        ctc1no =  (TextView) view.findViewById(R.id.ctc1no);
        ctc2 =  (TextView) view.findViewById(R.id.ctc2);
        ctc2no =  (TextView) view.findViewById(R.id.ctc2no);
        ctc3 =  (TextView) view.findViewById(R.id.ctc3);
        ctc3no =  (TextView) view.findViewById(R.id.ctc3no);
        ctc4 =  (TextView) view.findViewById(R.id.ctc4);
        ctc4no =  (TextView) view.findViewById(R.id.ctc4no);
        ctc5 =  (TextView) view.findViewById(R.id.ctc5);
        ctc5no =  (TextView) view.findViewById(R.id.ctc5no);
        ctc6 =  (TextView) view.findViewById(R.id.ctc6);
        ctc6no =  (TextView) view.findViewById(R.id.ctc6no);
        ctc7 =  (TextView) view.findViewById(R.id.ctc7);
        ctc7no =  (TextView) view.findViewById(R.id.ctc7no);

        tcsContacts.setChecked(true);

        wbaContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ctc1.setText("Josh Barrington");
                ctc1no.setText("(224) 645-1025");

                ctc2.setText("John Czyz");
                ctc2no.setText("(847) 971-1436");

                ctc3.setText("Jon Cline");
                ctc3no.setText("(224) 525-0182");

                ctc4.setText("Erwin");
                ctc4no.setText("+44 7768 396778");

                ctc5.setVisibility(View.GONE);
                ctc5no.setVisibility(View.GONE);

                ctc6.setVisibility(View.GONE);
                ctc6no.setVisibility(View.GONE);

                ctc7.setVisibility(View.GONE);
                ctc7no.setVisibility(View.GONE);

                wbaContacts.setChecked(true);
                tcsContacts.setChecked(false);
            }
        });

        tcsContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ctc1.setText("Aseem Sehgal");
                ctc1no.setText("+919210070588");

                ctc2.setText("Manish Khanna");
                ctc2no.setText("+919250008178");

                ctc3.setText("Manjinder Singh");
                ctc3no.setText("+919818653855");

                ctc4.setText("Mohit Bhatnagar");
                ctc4no.setText("+919643103191");

                ctc5.setVisibility(View.VISIBLE);
                ctc5no.setVisibility(View.VISIBLE);

                ctc6.setVisibility(View.VISIBLE);
                ctc6no.setVisibility(View.VISIBLE);

                ctc7.setVisibility(View.VISIBLE);
                ctc7no.setVisibility(View.VISIBLE);

                wbaContacts.setChecked(false);
                tcsContacts.setChecked(true);

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}