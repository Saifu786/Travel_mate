package io.github.project_travel_mate.friend;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import utils.CircleImageView;

/***************************************************************************
 * File Name : 
 * Author : Saif
 * Date of Creation : 2019-12-14
 * Description : 
 * Revision History :
 * ____________________________________________________________________________
 * 29/12/17  Aarth Tandel - Initial Commit
 * ____________________________________________________________________________
 * 29/12/17 Version 1.0
 * ____________________________________________________________________________
 *
 *****************************************************************************/
public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendHolder> {

    private LayoutInflater mInflater;
    public List<ContactModel> contact;
    public Context context;
    public OnOptionListner onOptionListner;
        AddFriendAdapter(Context context, List<ContactModel> contacts,OnOptionListner listner){
            this.context = context;
            this.contact = contacts;
            this.onOptionListner = listner;
            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }

    @NonNull
    @Override
    public AddFriendHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View rootView = mInflater.inflate(R.layout.item_contact_list,viewGroup,false);

        return new AddFriendHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFriendHolder addFriendHolder, int i) {
       Picasso.with(context).load(String.valueOf(contact.get(i).photo)).placeholder(R.drawable.default_user_icon)
               .error(R.drawable.default_user_icon).into(addFriendHolder.profile_image);
       String contact_name = contact.get(i).name;
       addFriendHolder.contact_name.setText(contact_name);
       String pnoneNumber = contact.get(i).mobileNumber;
       addFriendHolder.contactNumber.setText(pnoneNumber);
       addFriendHolder.add_friend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
            onOptionListner.onOptionsClicked((ContactModel) contact);
           }
       });
    }

    @Override
    public int getItemCount() {
        return contact.size();
    }

    class AddFriendHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.profile_image)
        CircleImageView profile_image;
        @BindView(R.id.text_name)
        TextView contact_name;
        @BindView(R.id.contact_number)
        TextView contactNumber;
        @BindView(R.id.click_add)
        Button add_friend;


        public AddFriendHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


