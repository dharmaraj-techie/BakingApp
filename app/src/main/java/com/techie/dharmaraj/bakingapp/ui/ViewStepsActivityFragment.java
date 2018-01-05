package com.techie.dharmaraj.bakingapp.ui;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.techie.dharmaraj.bakingapp.R;
import com.techie.dharmaraj.bakingapp.data.Steps;
import com.techie.dharmaraj.bakingapp.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewStepsActivityFragment extends Fragment {
    //constant key for recipe index,current step
    public static final String RECIPE_INDEX_KEY = "recipeIndex";
    public static final String STEP_AT_POSITION_KEY = "stepAtPosition";
    public static final String CURRENT_STEP_KEY = "currentStep";
    public static final String CURRENT_POSITION_KEY = "current_position";

    ArrayList<Steps> mSteps;
    boolean isTwoPaneMode;
    private SimpleExoPlayer mSimpleExoPlayer;
    private boolean mExoPlayerFullscreen = false;
    private Dialog mFullScreenDialog;
    private int mRecipeIndex;
    private int mStepAtPosition;
    private Unbinder unbinder;
    private long position;

    @BindView(R.id.simpleExoPlayerView)
    SimpleExoPlayerView mSimpleExoPlayerView;
    @BindView(R.id.description_tv)
    TextView mDescriptionTextView;
    @BindView(R.id.next_button)
    Button mNextButton;
    @BindView(R.id.previous_button)
    Button mPreviousButton;
    @BindView(R.id.container_of_exo_player)
    FrameLayout mExoPlayerContainer;
    @BindView(R.id.recipe_image_view)
    ImageView mRecipeImageView;

    public ViewStepsActivityFragment() {
        // Required empty public constructor
    }

    /**
     * this method is used to construct this Fragment by passing a current recipe index through this
     * method and initializing it in this fragment
     */
    public static ViewStepsActivityFragment newInstance(int recipeIndex, int stepAtPosition) {
        ViewStepsActivityFragment fragment = new ViewStepsActivityFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_INDEX_KEY, recipeIndex);
        args.putInt(STEP_AT_POSITION_KEY, stepAtPosition);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = C.TIME_UNSET;

        if (getArguments() != null) {
            mRecipeIndex = getArguments().getInt(RECIPE_INDEX_KEY);
            mStepAtPosition = getArguments().getInt(STEP_AT_POSITION_KEY);
        }
        if (savedInstanceState != null) {
            position = savedInstanceState.getLong(CURRENT_POSITION_KEY);
            mStepAtPosition = savedInstanceState.getInt(CURRENT_STEP_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_steps_activity, container, false);
        isTwoPaneMode = getActivity().getResources().getBoolean(R.bool.isTwoPaneMode);
        unbinder = ButterKnife.bind(this, view);
        //get the all steps data using the recipe index
        mSteps = JsonUtils.getRecipeSteps(mRecipeIndex);
        updateUi();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_STEP_KEY, mStepAtPosition);
        outState.putLong(CURRENT_POSITION_KEY,position);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSimpleExoPlayer != null) {
            position = mSimpleExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void updateUi() {
        //get the the corresponding step data using the position variable
        Steps step = mSteps.get(mStepAtPosition);
        String videoUri = step.videoUrl;
        String thumbnailURL= step.thumbnail;

        //set the description textView with correct text
        mDescriptionTextView.setText(step.description);
        if (videoUri == null || Objects.equals(videoUri, "")) {
            mRecipeImageView.setVisibility(View.VISIBLE);
            if(thumbnailURL.isEmpty()||thumbnailURL==null){
                mRecipeImageView.setImageResource(JsonUtils.getRecipeImageResourceId(mRecipeIndex));
            }else{
                try {
                    mRecipeImageView.setImageBitmap(retriveVideoFrameFromVideo(thumbnailURL));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
//                Glide.with(this).load(thumbnailURL).into(mRecipeImageView);
                Log.v("ViewStepsActivity","url : "+thumbnailURL);
            }
            mSimpleExoPlayerView.setVisibility(View.GONE);
        } else {
            mRecipeImageView.setVisibility(View.INVISIBLE);
            mSimpleExoPlayerView.setVisibility(View.VISIBLE);
            if (getResources().getBoolean(R.bool.isLandScape)) {
                //if device is LandScape
                initFullscreenDialog();
                openFullscreenDialog();
            }
            initializePlayer(Uri.parse(videoUri));
        }
        //if the device is a tablet we make next and previous button invisible
        if (isTwoPaneMode) {
            mPreviousButton.setVisibility(View.INVISIBLE);
            mNextButton.setVisibility(View.INVISIBLE);
            return;
        }
        //if it is the last step we make next button invisible
        if (mStepAtPosition == mSteps.size() - 1) {
            mNextButton.setVisibility(View.GONE);
        } else {
            mNextButton.setVisibility(View.VISIBLE);
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = C.POSITION_UNSET;
                    mStepAtPosition++;
                    if (mSimpleExoPlayer != null) mSimpleExoPlayer.stop();
                    updateUi();
                }
            });
        }

        //in the first position we make the previous button to launch the ingredients activity when clicked
        if (mStepAtPosition == 0) {
            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), IngredientsActivity.class);
                    i.putExtra("mRecipeIndex", mRecipeIndex);
                    getActivity().finish();
                    startActivity(i);
                }
            });

        } else {
            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = C.POSITION_UNSET;
                    mStepAtPosition--;
                    if (mSimpleExoPlayer != null) mSimpleExoPlayer.stop();
                    updateUi();
                }
            });
        }
    }

    /**
     * method to retrive tumbnail from video url
     * @param videoPath
     * @return
     * @throws Throwable
     */
    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        // Create an instance of the ExoPlayer.
        if (mSimpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);
        }
        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(getContext(), "BakingApp");
        MediaSource mediaSource =
                new ExtractorMediaSource(mediaUri,
                        new DefaultHttpDataSourceFactory(userAgent),
                        new DefaultExtractorsFactory(),
                        null,
                        null);
        if (position != C.TIME_UNSET) mSimpleExoPlayer.seekTo(position);
        mSimpleExoPlayer.prepare(mediaSource);
        mSimpleExoPlayer.setPlayWhenReady(true);
    }

    /**
     * create a full screen dialog to make exoPlayer play the video in full screen when rotated to portrait
     */
    private void initFullscreenDialog() {
        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    /**
     * initialize the full screen dialog to show video using exoPlayer
     */
    private void openFullscreenDialog() {
        ((ViewGroup) mSimpleExoPlayerView.getParent()).removeView(mSimpleExoPlayerView);
        mFullScreenDialog.addContentView(mSimpleExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    /**
     * close the full screen dialog
     */
    private void closeFullscreenDialog() {
        ((ViewGroup) mSimpleExoPlayerView.getParent()).removeView(mSimpleExoPlayerView);
        mExoPlayerContainer.addView(mSimpleExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
        }
        mSimpleExoPlayer = null;
    }


}
