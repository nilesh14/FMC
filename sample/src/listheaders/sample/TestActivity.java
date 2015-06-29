package listheaders.sample;

import android.annotation.TargetApi;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * @author Nilesh
 */
public class TestActivity extends ActionBarActivity implements
        AdapterView.OnItemClickListener, StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {

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
    private List<String> data = new ArrayList<>();

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

        mAdapter = new TestBaseAdapter(this);

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
        data = Arrays.asList(this.getResources().getStringArray(R.array.countries));

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //String []  adapterData = mAdapter.getmCountries();
                mAdapter.clear();
                List<String> dataToAdd = new ArrayList<String>();
                for (String s1 : data) {
                    if(s1.toLowerCase().contains(s.toString().toLowerCase().trim())){
                        dataToAdd.add(s1);
                    }
                }
                //mAdapter.clear();
                if(dataToAdd.size() > 0){
                    String [] adapterData = new String[dataToAdd.size()];
                    dataToAdd.toArray(adapterData);
                    // restore method is called to save new String array(containing the search result) in the adapter. As Restore() method does some more processing
                    // whith out which the app may crash
                    mAdapter.restore(adapterData);
                }

                mAdapter.notifyDataSetChanged();
                if(TextUtils.isEmpty(s)){
                    mAdapter.restore();
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