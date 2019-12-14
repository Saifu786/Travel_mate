package io.github.project_travel_mate.friend;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.TravelmateSnackbars;

import static android.support.v7.widget.LinearLayoutManager.*;
import static android.view.View.GONE;
import static android.widget.LinearLayout.*;
import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class MyFriendsFragment extends Fragment implements OnOptionListner{

    private final List<User> mFriends = new ArrayList<>();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.contact_recycler)
    RecyclerView contact_recycler;
    @BindView(R.id.add_contact)
    FloatingActionButton retreiveContact;

    private String mToken;
    private Handler mHandler;
    private Activity mActivity;
    private MyFriendsAdapter mAdapter;
    List<ContactModel> list = new ArrayList<>();


    public MyFriendsFragment() {
        // Required empty public constructor
    }

    public static MyFriendsFragment newInstance() {
        return new MyFriendsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_friends, container, false);
        ButterKnife.bind(this, view);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mToken = sharedPreferences.getString(USER_TOKEN, null);

        mHandler = new Handler(Looper.getMainLooper());
        retreiveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationView.setVisibility(GONE);
                contact_recycler.setVisibility(View.VISIBLE);
                getContacts(mActivity.getApplication());
            }
        });

        AddFriendAdapter contactAdapter = new AddFriendAdapter(mActivity.getApplicationContext(), list,this::onOptionsClicked);
        contact_recycler.setAdapter(contactAdapter);
        myFriends();
        return view;

    }

    public List<ContactModel> getContacts(Context ctx) {
        list.clear();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                    Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    Bitmap photo = null;
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }
                    while (cursorInfo.moveToNext()) {
                        ContactModel info = new ContactModel();
                        info.id = id;
                        info.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        info.mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        info.photo = photo;
                        info.photoURI= pURI;
                        list.add(info);
                    }

                    cursorInfo.close();
                }
            }
            cursor.close();
        }
        return list;
    }

    private void myFriends() {

        String uri = API_LINK_V2 + "trip-friends-all";

        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("Request Failed", "Message : " + e.getMessage());
                mHandler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, final Response response) {

                mHandler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        JSONArray arr;
                        try {
                            final String res = response.body().string();
                            Log.v("Response", res);
                            arr = new JSONArray(res);

                            if (arr.length() <= 1) {
                                noResults();
                                return;
                            }

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject object = arr.getJSONObject(i);
                                String userName = object.getString("username");
                                String firstName = object.getString("first_name");
                                String lastName = object.getString("last_name");
                                int id = object.getInt("id");
                                String imageURL = object.getString("image");
                                String dateJoined = object.getString("date_joined");
                                String status = object.getString("status");
                                mFriends.add(new User(userName, firstName, lastName, id, imageURL, dateJoined, status));
                                animationView.setVisibility(GONE);
                                mAdapter = new MyFriendsAdapter(mActivity.getApplicationContext(), mFriends);
                                recyclerView.setAdapter(mAdapter);
                            }
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                            Log.e("ERROR", "Message : " + e.getMessage());
                            networkError();
                        }
                    } else {
                        networkError();
                    }
                });
            }
        });
    }


    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }

    /**
     * Plays the no results animation in the view
     */
    private void noResults() {
        Toast.makeText(mActivity, R.string.no_friends_message, Toast.LENGTH_SHORT).show();
        animationView.setAnimation(R.raw.empty_list);
        animationView.playAnimation();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
    }

    @Override
    public void onOptionsClicked(ContactModel contactModel) {
        String uri = API_LINK_V2 + "add-friend";
        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", contactModel.name)
                .addFormDataPart("number", contactModel.mobileNumber)
                .addFormDataPart("photo", String.valueOf(contactModel.photo))
                .build();
        final Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .post(requestBody)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("Request Failed", "Message : " + e.getMessage());
                mHandler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try{
                    final String res = Objects.requireNonNull(response.body()).string();
                    final int responseCode = response.code();
                    mHandler.post(() -> {
                        if (responseCode == HttpsURLConnection.HTTP_CREATED) {
                            TravelmateSnackbars.createSnackBar(mActivity.findViewById(R.id.activityAddNewTrip),
                                    "Friend Added", Snackbar.LENGTH_LONG
                            ).show();
                            //Call back to MytripsFragment
                            Intent returnIntent = new Intent();
                            mActivity.setResult(Activity.RESULT_OK, returnIntent);
                            mActivity.finish();

                        } else {
                            TravelmateSnackbars.createSnackBar(mActivity.findViewById(R.id.activityAddNewTrip),
                                    res, Snackbar.LENGTH_LONG
                            ).show();
                        }
                    });

                }catch (IOException e ) {
                    e.printStackTrace();
                    Log.e("ERROR", "Message : " + e.getMessage());
                    networkError();
                }

            }
        });

    }
}
