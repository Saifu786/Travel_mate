// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.friend;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import utils.CircleImageView;

public class AddFriendAdapter$AddFriendHolder_ViewBinding implements Unbinder {
  private AddFriendAdapter.AddFriendHolder target;

  @UiThread
  public AddFriendAdapter$AddFriendHolder_ViewBinding(AddFriendAdapter.AddFriendHolder target,
      View source) {
    this.target = target;

    target.profile_image = Utils.findRequiredViewAsType(source, R.id.profile_image, "field 'profile_image'", CircleImageView.class);
    target.contact_name = Utils.findRequiredViewAsType(source, R.id.text_name, "field 'contact_name'", TextView.class);
    target.contactNumber = Utils.findRequiredViewAsType(source, R.id.contact_number, "field 'contactNumber'", TextView.class);
    target.add_friend = Utils.findRequiredViewAsType(source, R.id.click_add, "field 'add_friend'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AddFriendAdapter.AddFriendHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.profile_image = null;
    target.contact_name = null;
    target.contactNumber = null;
    target.add_friend = null;
  }
}
