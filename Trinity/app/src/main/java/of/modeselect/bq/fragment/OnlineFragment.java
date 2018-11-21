package of.modeselect.bq.fragment;
import of.modeselect.bq.R;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import of.modeselect.bq.convertPXAndDP.DensityUtil;

/**
 * Created by MR.XIE on 2018/10/23.
 */
public class OnlineFragment extends Fragment {
    private TextView musicTextView;
    private TextView videoTextView;
    private TextView galleryTextView;
    private TableLayout slideLine;
    private Fragment onlineMusicFragment,onlineVideoFragment,onlineGalleryFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.online_fragment,container,false);
        return view;
    }


}
