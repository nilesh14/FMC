package listheaders.sample;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import listheaders.sample.constants.CommonMethods;
import listheaders.sample.constants.Constants;
import se.emilsjolander.listheaders.StickyListHeadersListView;

/**
 * @author Nilesh
 */
public class TestActivity extends ActionBarActivity implements
        AdapterView.OnItemClickListener, StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {

    private static final String TAG = "TestActivity";
    public static final String VisibleContacts = "Visible Contacts";
    public static final String InvitationReceived = "Invitation Received";
    public static final String InvitationSend = "InvitationSend";
    public static final String AddToTrustedNetwork = "Add to trusted network";
    public static final String InviteContacts = "Invite Contacts";

    public static final int HeaderVisibleContacts = 1;
    public static final int HeaderInvitationReceivedContacts = 2;
    public static final int HeaderInvitationSendContacts = 3;
    public static final int HeaderAddToTrustedNetworkContacts = 4;
    public static final int HeaderInviteContacts = 5;
    private TestBaseAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean fadeHeader = false;

    private StickyListHeadersListView listHeader;
    private SwipeRefreshLayout refreshLayout;

    private Button restoreButton;
    private Button updateButton;
    private Button clearButton;

    private CheckBox stickyCheckBox;
    private CheckBox fadeCheckBox;
    private CheckBox drawBehindCheckBox;
    private CheckBox fastScrollCheckBox;
    private Button openExpandableListButton;
    private EditText edtSearch;
    private List<Contacts> data = new ArrayList<>();
    private List<Contacts> visibleContacts = new ArrayList<>();
    private List<Contacts> invitationContacts = new ArrayList<>();
    private List<Contacts> invitationSend = new ArrayList<>();
    private List<Contacts> addToTrustedNetwork = new ArrayList<>();
    private List<Contacts> inviteContacts = new ArrayList<>();
    Button btnGetContacts;
    ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        edtSearch = (EditText) findViewById(R.id.edtSearch);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        mAdapter = new TestBaseAdapter(this, data);

        listHeader = (StickyListHeadersListView) findViewById(R.id.list);
        listHeader.setOnItemClickListener(this);
        listHeader.setOnHeaderClickListener(this);
        listHeader.setOnStickyHeaderChangedListener(this);
        listHeader.setOnStickyHeaderOffsetChangedListener(this);
        listHeader.setEmptyView(findViewById(R.id.empty));
        listHeader.setDrawingListUnderStickyHeader(true);
        listHeader.setAreHeadersSticky(true);
        listHeader.setAdapter(mAdapter);
        listHeader.setAreHeadersSticky(true);
        listHeader.setFastScrollEnabled(true);
        listHeader.setFastScrollAlwaysVisible(true);
        //data = Arrays.asList(this.getResources().getStringArray(R.array.countries));

        btnGetContacts = (Button) findViewById(R.id.btnGetContacts);
        btnGetContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                visibleContacts.clear();
                invitationContacts.clear();
                invitationSend.clear();
                addToTrustedNetwork.clear();
                mAdapter.notifyDataSetChanged();
                fetchContact();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //String []  adapterData = mAdapter.getmCountries();
                //mAdapter.clear();
                List<Contacts> dataToAdd = new ArrayList<Contacts>();
                for (Contacts s1 : data) {
                    if(s1.getContactTitle().toLowerCase().contains(s.toString().toLowerCase().trim())){
                        dataToAdd.add(s1);
                    }
                }
                //mAdapter.clear();
                if(dataToAdd.size() > 0){
                    /*String [] adapterData = new String[dataToAdd.size()];
                    dataToAdd.toArray(adapterData);*/
                    // restore method is called to save new String array(containing the search result) in the adapter. As Restore() method does some more processing
                    // whith out which the app may crash
                    mAdapter.restore(dataToAdd);
                }

                mAdapter.notifyDataSetChanged();
                if(TextUtils.isEmpty(s)){
                    data.clear();
                    loadAllData();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listHeader.setStickyHeaderTopOffset(-20);
    }

    private void fetchContact() {
        new LoginTask(TestActivity.this).execute(Constants.LOGIN_URL);
    }

    class LoginTask extends AsyncTask<String, Void, String> {


        Context context;

        LoginTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (pDialog == null) {
                pDialog = new ProgressDialog(context);
            }

            pDialog.setMessage(getString(R.string.loading_contact));
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return CommonMethods.callSyncService(this.context, params[0], TAG);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (s != null) {
                try {
                    JSONObject j = new JSONObject(s);
                    JSONObject jobj = j.optJSONObject("contacts");

                    JSONArray arrVisible = jobj.optJSONArray("visible-contacts");
                    for (int i = 0; i < arrVisible.length(); i++) {
                        JSONObject jo = arrVisible.optJSONObject(i);
                        Contacts cData = new Contacts();
                        cData.setHeaderID(HeaderVisibleContacts);
                        cData.setContactTitle(jo.optString("title"));
                        cData.setContactType(VisibleContacts);
                        cData.setDesg(jo.optString("desg"));
                        cData.setProfileImg(jo.optString("profileimg"));
                        cData.setSkill(jo.optString("skill"));

                        visibleContacts.add(cData);

                    }

                    JSONArray arrInvitationReceived = jobj.optJSONArray("invitation-received");
                    for (int i = 0; i < arrInvitationReceived.length(); i++) {
                        JSONObject jo = arrInvitationReceived.optJSONObject(i);
                        Contacts cData = new Contacts();
                        cData.setHeaderID(HeaderInvitationReceivedContacts);
                        cData.setContactTitle(jo.optString("title"));
                        cData.setContactType(InvitationReceived);
                        cData.setDesg(jo.optString("desg"));
                        cData.setProfileImg(jo.optString("profileimg"));
                        cData.setSkill(jo.optString("skill"));

                        invitationContacts.add(cData);

                    }

                    /*JSONArray arrInvitationSend = jobj.optJSONArray("invitation-send");
                    for (int i = 0; i < arrInvitationSend.length(); i++) {
                        JSONObject jo = arrInvitationSend.optJSONObject(i);
                        Contacts cData = new Contacts();
                        cData.setContactTitle(jo.optString("title"));
                        cData.setContactType(InvitationSend);
                        cData.setDesg(jo.optString("desg"));
                        cData.setProfileImg(jo.optString("profileimg"));
                        cData.setSkill(jo.optString("skill"));

                        invitationSend.add(cData);

                    }*/

                    JSONArray inviSend = jobj.optJSONArray("invitation-send");
                    for (int i = 0; i < inviSend.length(); i++) {
                        JSONObject jo = inviSend.optJSONObject(i);
                        Contacts cData = new Contacts();
                        cData.setHeaderID(HeaderInvitationSendContacts);
                        cData.setContactTitle(jo.optString("title"));
                        cData.setContactType(InvitationSend);
                        cData.setDesg(jo.optString("desg"));
                        cData.setProfileImg(jo.optString("profileimg"));
                        cData.setSkill(jo.optString("skill"));

                        invitationSend.add(cData);

                    }

                    JSONArray arrAddToNetwork = jobj.optJSONArray("add-to-trusted-network");
                    for (int i = 0; i < arrAddToNetwork.length(); i++) {
                        JSONObject jo = arrAddToNetwork.optJSONObject(i);
                        Contacts cData = new Contacts();
                        cData.setHeaderID(HeaderAddToTrustedNetworkContacts);
                        cData.setContactTitle(jo.optString("title"));
                        cData.setContactType(AddToTrustedNetwork);
                        cData.setDesg(jo.optString("desg"));
                        cData.setProfileImg(jo.optString("profileimg"));
                        cData.setSkill(jo.optString("skill"));

                        addToTrustedNetwork.add(cData);

                    }

                    JSONArray arrInviteContacts = jobj.optJSONArray("invite-contacts");
                    for (int i = 0; i < arrInviteContacts.length(); i++) {
                        JSONObject jo = arrInviteContacts.optJSONObject(i);
                        Contacts cData = new Contacts();
                        cData.setHeaderID(HeaderInviteContacts);
                        cData.setContactTitle(jo.optString("title"));
                        cData.setContactType(InviteContacts);
                        cData.setDesg(jo.optString("desg"));
                        cData.setProfileImg(jo.optString("profileimg"));
                        cData.setSkill(jo.optString("skill"));

                        inviteContacts.add(cData);

                    }

                    loadAllData();

                    mAdapter = new TestBaseAdapter(TestActivity.this,data);
                    listHeader.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(TestActivity.this,"No Network Or Data Failed to Load",Toast.LENGTH_LONG).show();
            }


        }
    }

    public List<Contacts> getVisibleContacts() {
        return visibleContacts;
    }

    public void setVisibleContacts(List<Contacts> visibleContacts) {
        this.visibleContacts = visibleContacts;
    }

    public List<Contacts> getInvitationContacts() {
        return invitationContacts;
    }

    public void setInvitationContacts(List<Contacts> invitationContacts) {
        this.invitationContacts = invitationContacts;
    }

    public List<Contacts> getInvitationSend() {
        return invitationSend;
    }

    public void setInvitationSend(List<Contacts> invitationSend) {
        this.invitationSend = invitationSend;
    }

    public List<Contacts> getAddToTrustedNetwork() {
        return addToTrustedNetwork;
    }

    public void setAddToTrustedNetwork(List<Contacts> addToTrustedNetwork) {
        this.addToTrustedNetwork = addToTrustedNetwork;
    }

    public List<Contacts> getInviteContacts() {
        return inviteContacts;
    }

    public void setInviteContacts(List<Contacts> inviteContacts) {
        this.inviteContacts = inviteContacts;
    }

    private void loadAllData(){
        data.clear();
        data.addAll(visibleContacts);
        data.addAll(invitationContacts);
        data.addAll(invitationSend);
        data.addAll(addToTrustedNetwork);
        data.addAll(inviteContacts);
        mAdapter.notifyDataSetChanged();
    }

    public void addDataToVisibleContacts(Contacts data){
        data.setHeaderID(HeaderVisibleContacts);
        data.setContactType(VisibleContacts);
        visibleContacts.add(data);
        //clearAllData();
        loadAllData();
    }

    public void addDataToInvitationReceivedContacts(Contacts data){
        data.setHeaderID(HeaderInvitationReceivedContacts);
        data.setContactType(InvitationReceived);
        invitationContacts.add(data);
        //clearAllData();
        loadAllData();
    }

    public void addDataToInvitationSendContacts(Contacts data){
        data.setHeaderID(HeaderInvitationSendContacts);
        data.setContactType(InvitationSend);
        invitationSend.add(data);
        //clearAllData();
        loadAllData();
    }

    public void addDataToAddToTrustedNetworkContacts(Contacts data){
        data.setHeaderID(HeaderAddToTrustedNetworkContacts);
        data.setContactType(AddToTrustedNetwork);
        addToTrustedNetwork.add(data);
        //clearAllData();
        loadAllData();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Item " + position + " clicked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
        Toast.makeText(this, "Header " + headerId + " currentlySticky ? " + currentlySticky, Toast.LENGTH_SHORT).show();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
        if (fadeHeader && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        header.setAlpha(1);
    }

}