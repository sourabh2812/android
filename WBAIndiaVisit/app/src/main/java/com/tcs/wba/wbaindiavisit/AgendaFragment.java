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


public class AgendaFragment extends Fragment {


    FrameLayout root;

    PDFViewPager pdfViewPager;

    File pdfFolder;

    String[] fileArray = {"03_oct.pdf", "07_oct.pdf"};
    ToggleButton oct03;
    ToggleButton oct07;
    ToggleButton oct09;
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

        View view = inflater.inflate(R.layout.fragment_agenda, container, false);

        // Listener for oct 03
        oct03 = (ToggleButton) view.findViewById(R.id.oct03);
        oct03.setChecked(true);
        oct03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                oct03.setChecked(true);
                oct07.setChecked(false);
                oct09.setChecked(false);
                createPdfViewer(fileArray[0]);
            }
        });

        // Listener for oct 07
        oct07 = (ToggleButton) view.findViewById(R.id.oct07);
        oct07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                oct07.setChecked(true);
                oct03.setChecked(false);
                oct09.setChecked(false);
                createPdfViewer(fileArray[1]);
            }
        });

        // Listener for oct 09
        oct09 = (ToggleButton) view.findViewById(R.id.oct09);
        oct09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearObjects();
                oct09.setChecked(true);
                oct03.setChecked(false);
                oct07.setChecked(false);
                createPdfViewer(fileArray[0]);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        root = (FrameLayout) getView().findViewById(R.id.agenda_frame);
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