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


public class AboutFragment extends Fragment {


    FrameLayout root;

    PDFViewPager pdfViewPager;

    File pdfFolder;

    String[] fileArray = {"about_tata.pdf", "about_tcs.pdf", "about_wbatcs.pdf"};
    ToggleButton aboutTataBtn;
    ToggleButton aboutTcsBtn;
    ToggleButton aboutWbaBtn;
    private OnFragmentInteractionListener mListener;

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

        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // Listener for about TATA
        aboutTataBtn = (ToggleButton) view.findViewById(R.id.aboutTATA);
        // Selecting by default
        aboutTataBtn.setChecked(true);
        aboutTataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                aboutTataBtn.setChecked(true);
                aboutTcsBtn.setChecked(false);
                aboutWbaBtn.setChecked(false);
                createPdfViewer(fileArray[0]);
            }
        });

        // Listener for about TCS
        aboutTcsBtn = (ToggleButton) view.findViewById(R.id.aboutTCS);
        aboutTcsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                aboutTataBtn.setChecked(false);
                aboutTcsBtn.setChecked(true);
                aboutWbaBtn.setChecked(false);
                createPdfViewer(fileArray[1]);
            }
        });

        // Listener for about WBA/TCS
        aboutWbaBtn = (ToggleButton) view.findViewById(R.id.aboutWBATCS);
        aboutWbaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                aboutTataBtn.setChecked(false);
                aboutTcsBtn.setChecked(false);
                aboutWbaBtn.setChecked(true);
                createPdfViewer(fileArray[2]);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        root = (FrameLayout) getView().findViewById(R.id.about_us_frame);
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