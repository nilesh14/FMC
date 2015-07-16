package listheaders.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import se.emilsjolander.listheaders.StickyListHeadersAdapter;

public class TestBaseAdapter extends BaseAdapter implements
        StickyListHeadersAdapter {

    private final Context mContext;
    private List<Contacts> data;
    private LayoutInflater mInflater;

    public TestBaseAdapter(Context context, List<Contacts> data) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.test_list_item_layout, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.imgMoveUp = (ImageView) convertView.findViewById(R.id.imgMoveUp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(data.get(position).getContactTitle());

        holder.imgMoveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //smoveValueUp(position);

                Contacts con = (Contacts) getItem(position);

                //data.remove(position);

                if(con.getContactType().equalsIgnoreCase(TestActivity.InvitationReceived)){
                    ((TestActivity)mContext).getInvitationContacts().remove(con);
                    ((TestActivity)mContext).addDataToVisibleContacts(con);

                }

                if(con.getContactType().equalsIgnoreCase(TestActivity.InvitationSend)){
                    ((TestActivity)mContext).getInvitationSend().remove(con);
                    ((TestActivity)mContext).addDataToInvitationReceivedContacts(con);

                }

                if(con.getContactType().equalsIgnoreCase(TestActivity.AddToTrustedNetwork)){
                    ((TestActivity)mContext).getAddToTrustedNetwork().remove(con);
                    ((TestActivity)mContext).addDataToInvitationSendContacts(con);

                }

                if(con.getContactType().equalsIgnoreCase(TestActivity.InviteContacts)){
                    ((TestActivity)mContext).getInviteContacts().remove(con);
                    ((TestActivity)mContext).addDataToAddToTrustedNetworkContacts(con);

                }





                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        // set header text as first char in name
        CharSequence headerChar = data.get(position).getContactType();
        holder.text.setText(headerChar);

        return convertView;
    }

    /*private void moveValueUp(int position){
        CharSequence headerChar = data.get(position).subSequence(0, 1);
        for(int i = position; i > 0 ; i -- ){
            CharSequence value1 = data.get(i).subSequence(0, 1);
            CharSequence value2 = data.get(i-1).subSequence(0, 1);
            if(!value2.toString().equalsIgnoreCase(value1.toString())){
                String swapValue = data.get(i-1);
                String newValue = data.get(position);
                //data[position] = swapValue;
                //Collections.swap(data,i-1,position);
                data.remove(position);
                data.add(i, swapValue);
               //data[i-1] = newValue;
                data.remove(i - 1);
                data.add(i - 1, newValue);
                return;
            }
        }
    }*/

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {
        // return the first character of the country as ID because this is what
        // headers are based upon
        long val;
        val = ((Contacts)getItem(position)).getHeaderID();
        return val;
        //return position;
    }

    public void clear() {
       // data = new String[0];
        data.clear();
        //mSectionIndices = new int[0];
        //mSectionLetters = new Character[0];
        notifyDataSetChanged();
    }

    /*public void restore() {
        data = new LinkedList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.countries)));
        //mSectionIndices = getSectionIndices();
        //mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }*/

    public void restore(List<Contacts> arrData) {
        data = arrData;
        //mSectionIndices = getSectionIndices();
        //mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
        ImageView imgMoveUp;
    }

    /*public String [] getmCountries() {
        return data;
    }*/
}
