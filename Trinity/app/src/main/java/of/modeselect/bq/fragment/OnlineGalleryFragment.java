package of.modeselect.bq.fragment;
import of.modeselect.bq.R;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * Created by MR.XIE on 2018/10/23.
 */
public class OnlineGalleryFragment  extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.online_gallery_fragment,container,false);
        return view;
    }
}
