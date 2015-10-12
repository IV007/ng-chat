package com.sidelance.ngchat;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sidelance.ngchat.utilities.FileHelper;

import java.util.ArrayList;
import java.util.List;

public class RecipientsActivity extends ListActivity {

    private static final String TAG = RecipientsActivity.class.getSimpleName();

    protected Uri mMediaUri;
    protected String mFileType;
    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Create");

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipients);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ParseObject message = createMessage();
                if (message == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
                    builder.setMessage(R.string.ERROR_SELECTING_FILE_FOR_UPLOAD)
                            .setTitle(R.string.ERROR_TITLE)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();




                } else {

                    send(message);

                }
            }
        });

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mMediaUri = getIntent().getData();
        mFileType = getIntent().getExtras().getString(ParseConstants.KEY_FILE_TYPE);
    }



    @Override
    public void onResume() {
        super.onResume();


        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        setProgressBarIndeterminateVisibility(true);

        displayFriendList();

    }

    private void displayFriendList() {

        if (mFriendsRelation != null) {
            ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
            query.addAscendingOrder(ParseConstants.KEY_USERNAME);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> friends, ParseException e) {

                    setProgressBarIndeterminateVisibility(false);

                    if (e == null) {
                        // list returned - look for a match

                        String[] friendSize = new String[0];
                        if (friends.size() > 0) {

                            mFriends = friends;
                            friendSize = new String[mFriends.size()];
                            int i = 0;
                            for (ParseUser user : mFriends) {
                                friendSize[i] = user.getUsername();
                                i++;
                            }


                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getListView().getContext(),
                                android.R.layout.simple_list_item_checked,
                                friendSize);
                        setListAdapter(adapter);

                    } else {
                        Log.e(TAG, e.getMessage());
                        Tools.displayErrorDialog(e.getMessage(), getString(R.string.ERROR_TITLE), RecipientsActivity.this);

                    }
                }
            });
        }
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (l.getCheckedItemCount() > 0) {
            fab.setVisibility(View.VISIBLE);
            Snackbar.make(v, "Send message now ?", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
//                    .setActionTextColor(ContextCompat.getColor(RecipientsActivity.this, R.color.colorNeutral))
                    .show();
        } else {
            fab.setVisibility(View.INVISIBLE);
        }
    }

    protected void send(ParseObject message){
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    //successful


                } else {


                }
            }
        });

    }

    protected ParseObject createMessage(){

        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
        message.put(ParseConstants.KEY_SENDER_IDS, ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstants.KEY_RECIPIENT_IDS, getRecipientIds());
        message.put(ParseConstants.KEY_FILE_TYPE, mFileType);

        byte[] fileByte = FileHelper.getByteArrayFromFile(this, mMediaUri);

        if (fileByte == null){

            return null;

        } else {

            if (mFileType.equals(ParseConstants.TYPE_IMAGE)){
                fileByte = FileHelper.reduceImageForUpload(fileByte);
            }

            String fileName= FileHelper.getFileName(this, mMediaUri, mFileType);
            ParseFile file = new ParseFile(fileName, fileByte);
            message.put(ParseConstants.KEY_FILE, file);

            return message;
        }

    }

    private ArrayList<String> getRecipientIds() {
        ArrayList<String> recipientIds = new ArrayList<>();
        for(int i = 0; i < getListView().getCount(); i++){
            if (getListView().isItemChecked(i)){
                recipientIds.add(mFriends.get(i).getObjectId());
            }
        }

        return recipientIds;
    }
}
