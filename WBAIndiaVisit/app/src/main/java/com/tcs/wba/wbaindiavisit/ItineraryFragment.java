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
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.BasePDFPagerAdapter;
import es.voghdev.pdfviewpager.library.asset.CopyAsset;
import es.voghdev.pdfviewpager.library.asset.CopyAssetThreadImpl;


public class ItineraryFragment extends Fragment {


    FrameLayout root;

    PDFViewPager pdfViewPager;

    File pdfFolder;
    // Initializing Itinerary array
    String[] fileArray = {"01Sat.pdf", "02Sun.pdf", "03Mon.pdf", "04Tue.pdf", "05Wed.pdf", "06Thu.pdf", "07Fri.pdf", "08Sat.pdf", "09Sun.pdf"};

    private OnFragmentInteractionListener mListener;

    ToggleButton oct01, oct02, oct03, oct04, oct05, oct06, oct07, oct08, oct09;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pdfFolder = Environment.getExternalStorageDirectory();
        // Display TATA PDF as default
        createPdfViewer(fileArray[0]);
    }

    protected void createPdfViewer(String pdfFile) {
        clearObjects();
        final Context context = getContext();
        final String pdfFileName = pdfFile;
        CopyAsset copyAsset = new CopyAssetThreadImpl(context.getApplicationContext(), new Handler(), new CopyAsset.Listener() {

            @Override
            public void success(String assetName, String destinationPath) {
                // Remove all views to prevent OutOfMemory exception
                pdfViewPager = new PDFViewPager(context, new File(pdfFolder, pdfFileName).getAbsolutePath());
                root.addView(pdfViewPager,
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }

            @Override
            public void failure(Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        copyAsset.copy(pdfFile, new File(pdfFolder, pdfFile).getAbsolutePath());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_itinerary, container, false);

        oct01 = (ToggleButton) view.findViewById(R.id.oct1);
        //Default selected
        oct01.setChecked(true);
        oct01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                // Toggle other buttons
                oct01.setChecked(true); oct02.setChecked(false); oct03.setChecked(false);
                oct04.setChecked(false); oct05.setChecked(false); oct06.setChecked(false);
                oct07.setChecked(false); oct08.setChecked(false); oct09.setChecked(false);

                createPdfViewer(fileArray[0]);
            }
        });

        oct02 = (ToggleButton) view.findViewById(R.id.oct2);

        oct02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                // Toggle other buttons
                oct01.setChecked(false); oct02.setChecked(true); oct03.setChecked(false);
                oct04.setChecked(false); oct05.setChecked(false); oct06.setChecked(false);
                oct07.setChecked(false); oct08.setChecked(false); oct09.setChecked(false);

                createPdfViewer(fileArray[1]);
            }
        });

        oct03 = (ToggleButton) view.findViewById(R.id.oct3);

        oct03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                // Toggle other buttons
                oct01.setChecked(false); oct02.setChecked(false); oct03.setChecked(true);
                oct04.setChecked(false); oct05.setChecked(false); oct06.setChecked(false);
                oct07.setChecked(false); oct08.setChecked(false); oct09.setChecked(false);

                createPdfViewer(fileArray[2]);
            }
        });

        oct04 = (ToggleButton) view.findViewById(R.id.oct4);

        oct04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                // Toggle other buttons
                oct01.setChecked(false); oct02.setChecked(false); oct03.setChecked(false);
                oct04.setChecked(true); oct05.setChecked(false); oct06.setChecked(false);
                oct07.setChecked(false); oct08.setChecked(false); oct09.setChecked(false);

                createPdfViewer(fileArray[3]);
            }
        });


        oct05 = (ToggleButton) view.findViewById(R.id.oct5);

        oct05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                // Toggle other buttons
                oct01.setChecked(false); oct02.setChecked(false); oct03.setChecked(false);
                oct04.setChecked(false); oct05.setChecked(true); oct06.setChecked(false);
                oct07.setChecked(false); oct08.setChecked(false); oct09.setChecked(false);

                createPdfViewer(fileArray[4]);
            }
        });


        oct06 = (ToggleButton) view.findViewById(R.id.oct6);

        oct06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                // Toggle other buttons
                oct01.setChecked(false); oct02.setChecked(false); oct03.setChecked(false);
                oct04.setChecked(false); oct05.setChecked(false); oct06.setChecked(true);
                oct07.setChecked(false); oct08.setChecked(false); oct09.setChecked(false);

                createPdfViewer(fileArray[5]);
            }
        });

        oct07 = (ToggleButton) view.findViewById(R.id.oct7);

        oct07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                // Toggle other buttons
                oct01.setChecked(false); oct02.setChecked(false); oct03.setChecked(false);
                oct04.setChecked(false); oct05.setChecked(false); oct06.setChecked(false);
                oct07.setChecked(true); oct08.setChecked(false); oct09.setChecked(false);

                createPdfViewer(fileArray[6]);
            }
        });

        oct08 = (ToggleButton) view.findViewById(R.id.oct8);

        oct08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                // Toggle other buttons
                oct01.setChecked(false); oct02.setChecked(false); oct03.setChecked(false);
                oct04.setChecked(false); oct05.setChecked(false); oct06.setChecked(false);
                oct07.setChecked(false); oct08.setChecked(true); oct09.setChecked(false);

                createPdfViewer(fileArray[7]);
            }
        });

        oct09 = (ToggleButton) view.findViewById(R.id.oct9);

        oct09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                // Toggle other buttons
                oct01.setChecked(false); oct02.setChecked(false); oct03.setChecked(false);
                oct04.setChecked(false); oct05.setChecked(false); oct06.setChecked(false);
                oct07.setChecked(false); oct08.setChecked(false); oct09.setChecked(true);

                createPdfViewer(fileArray[8]);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        root = (FrameLayout) getView().findViewById(R.id.itinerary_frame);
    }

    public void onButtonPressed(Uri uri) {
        BasePDFPagerAdapter adapter = (BasePDFPagerAdapter) pdfViewPager.getAdapter();
        if (adapter != null) {
            adapter.close();
            adapter = null;
        }
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
        BasePDFPagerAdapter adapter = (BasePDFPagerAdapter) pdfViewPager.getAdapter();
        if (adapter != null) {
            adapter.close();
            adapter = null;
        }
    }

    // Method to prevent OutOfMemory exception
    public void clearObjects() {
        if (pdfViewPager != null) {
            pdfViewPager.removeAllViews();
            pdfViewPager = null;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}