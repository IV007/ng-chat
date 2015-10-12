package com.sidelance.ngchat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int CHOOSE_PHOTO_REQUEST = 2;
    public static final int CHOOSE_VIDEO_REQUEST = 3;

    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;

    public static final int FILE_SIZE_LIMIT = 1024 * 1024 * 10;

    /**
     * Resource identifier
     */
    protected Uri mMediaUri;

    protected DialogInterface.OnClickListener mDialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){

                        case 0:
                            //Take picture

                            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                            if (mMediaUri == null){

                                Toast.makeText(MainActivity.this, R.string.ERROR_EXTERNAL_STORAGE,
                                        Toast.LENGTH_LONG).show();
                            } else {

                                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                                startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);

                            }

                            break;

                        case 1:
                            //Take Video
                            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                            if (mMediaUri == null){

                                Toast.makeText(MainActivity.this, R.string.ERROR_EXTERNAL_STORAGE,
                                        Toast.LENGTH_LONG).show();
                            } else {

                                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                                videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                                videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); //0 - lowest quality
                                startActivityForResult(videoIntent, TAKE_VIDEO_REQUEST);
                            }

                            break;

                        case 2:
                            //Choose Picture
                            Intent choosePicIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            choosePicIntent.setType("image/*");
                            startActivityForResult(choosePicIntent, CHOOSE_PHOTO_REQUEST);

                            break;

                        case 3:
                            //Choose video
                            Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            chooseVideoIntent.setType("video/*");

                            Toast.makeText(MainActivity.this, getString(R.string.VIDEO_FILE_SIZE_WARNING), Toast.LENGTH_SHORT).show();
                            startActivityForResult(chooseVideoIntent, CHOOSE_VIDEO_REQUEST);

                            break;
                    }
                }
            };

    private Uri getOutputMediaFileUri(int mediaType) {

        if (isExternalStorageAvailable()){
            //get the Uri
            //Get external storage dir
            String appName = MainActivity.this.getString(R.string.app_name);
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), appName);

            //create dir
            if (!mediaStorageDir.exists()){

                if(!mediaStorageDir.mkdir()){
                    Log.e(TAG, "Failed to create directory");
                    return null;
                }
            }

            //Create a file name
            //create file
            File mediaFile;
            Date now = new Date();
            String timeStamp = new SimpleDateFormat(
                    "MM/dd/yyyy_hhmmss a", Locale.ENGLISH).format(now);

            String path = mediaStorageDir.getPath() + File.separator;
            if (mediaType == MEDIA_TYPE_IMAGE){

                mediaFile = new File(path + "IMG_" + timeStamp + ".jpg");

            } else if (mediaType == MEDIA_TYPE_VIDEO){

                mediaFile = new File(path + "VID_" + timeStamp + ".mp4");

            } else {
                return  null;
            }


            //return file uri
            Log.i(TAG,  "File: " + Uri.fromFile(mediaFile));

            return Uri.fromFile(mediaFile);

        } else{

            return null;
        }

    }

    private boolean isExternalStorageAvailable(){

        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)){

            return  true;

        } else {

            return false;
        }
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /**
     * Variable used to access current user
     */
    private ParseUser currentUser;

    /**
     * Non-static utility class object
     */
    private Tools tools = new Tools();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Create");

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {

            navigateToLogin();

        } else {

            Log.i(TAG, "User found");
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Powered by SideLance", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            if (requestCode == CHOOSE_PHOTO_REQUEST || requestCode == CHOOSE_VIDEO_REQUEST){

                if (data == null){

                    Toast.makeText(this, getString(R.string.GENERAL_ERROR), Toast.LENGTH_LONG).show();

                } else{

                    mMediaUri = data.getData();

                }

                Log.i(TAG, "MediaUri: " + mMediaUri);
                if (requestCode == CHOOSE_VIDEO_REQUEST){
                    //Check files size
                    int fileSize = 0;
                    InputStream inputStream = null;

                    try {
                        if (mMediaUri != null) {
                            inputStream = getContentResolver().openInputStream(mMediaUri);
                            if (inputStream != null) {
                                fileSize = inputStream.available();
                            }
                        }
                    } catch (FileNotFoundException e){

                        Toast.makeText(this, getString(R.string.ERROR_OPENING_FILE), Toast.LENGTH_LONG).show();
                        return;

                    } catch (IOException e){
                        Toast.makeText(this, getString(R.string.ERROR_OPENING_FILE), Toast.LENGTH_LONG).show();
                        return;

                    } finally {

                        try {
                            if (inputStream!= null) {
                                inputStream.close();
                            }
                        } catch (IOException e) {

                        }
                    }

                    if (fileSize >= FILE_SIZE_LIMIT){
                        Toast.makeText(this, getString(R.string.ERROR_FILE_SIZE_TOO_LARGE), Toast.LENGTH_LONG).show();
                        return;

                    }
                }
            } else {

                //add to gallery
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
            }

            Intent recipientsIntent = new Intent(this, RecipientsActivity.class);
            recipientsIntent.setData(mMediaUri);

            String fileType;
            if (requestCode == TAKE_PHOTO_REQUEST || requestCode == CHOOSE_PHOTO_REQUEST){

                fileType = ParseConstants.TYPE_IMAGE;
            } else {

                fileType = ParseConstants.TYPE_VIDEO;
            }

            recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE, fileType);
            startActivity(recipientsIntent);


        } else if (resultCode != RESULT_CANCELED){

            Toast.makeText(this, R.string.GENERAL_ERROR, Toast.LENGTH_LONG).show();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){

            case R.id.action_logout:

                ParseUser.logOut();
                Log.i(TAG, "Logging out " + currentUser.getUsername());
                navigateToLogin();
                break;

            case R.id.action_edit_friends:

                Intent intent = new Intent(this, EditFriendsActivity.class);
                startActivity(intent);
                break;

            case R.id.action_camera:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.CAMERA_CHOICES, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();

        }

        return super.onOptionsItemSelected(item);
    }




}
