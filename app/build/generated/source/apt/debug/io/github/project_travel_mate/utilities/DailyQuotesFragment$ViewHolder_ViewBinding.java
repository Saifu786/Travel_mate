// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import com.dd.processbutton.FlatButton;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DailyQuotesFragment$ViewHolder_ViewBinding implements Unbinder {
  private DailyQuotesFragment.ViewHolder target;

  private View view2131362015;

  private View view2131361927;

  @UiThread
  public DailyQuotesFragment$ViewHolder_ViewBinding(final DailyQuotesFragment.ViewHolder target,
      View source) {
    this.target = target;

    View view;
    target.rootLayout = Utils.findRequiredViewAsType(source, R.id.root_layout, "field 'rootLayout'", LinearLayout.class);
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.quoteTv = Utils.findRequiredViewAsType(source, R.id.quote_textView, "field 'quoteTv'", TextView.class);
    target.authorTv = Utils.findRequiredViewAsType(source, R.id.author_textView, "field 'authorTv'", TextView.class);
    view = Utils.findRequiredView(source, R.id.fab, "field 'share' and method 'onFloatingActionBarClicked'");
    target.share = Utils.castView(view, R.id.fab, "field 'share'", FloatingActionButton.class);
    view2131362015 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onFloatingActionBarClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.continue_button, "field 'continueButton' and method 'onContinueButtonClicked'");
    target.continueButton = Utils.castView(view, R.id.continue_button, "field 'continueButton'", FlatButton.class);
    view2131361927 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onContinueButtonClicked();
      }
    });
    target.quotesCheckBox = Utils.findRequiredViewAsType(source, R.id.dont_show_quotes_checkBox, "field 'quotesCheckBox'", CheckBox.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DailyQuotesFragment.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rootLayout = null;
    target.animationView = null;
    target.quoteTv = null;
    target.authorTv = null;
    target.share = null;
    target.continueButton = null;
    target.quotesCheckBox = null;

    view2131362015.setOnClickListener(null);
    view2131362015 = null;
    view2131361927.setOnClickListener(null);
    view2131361927 = null;
  }
}
