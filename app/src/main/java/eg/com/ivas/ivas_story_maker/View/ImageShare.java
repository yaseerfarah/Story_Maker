package eg.com.ivas.ivas_story_maker.View;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eg.com.ivas.ivas_story_maker.BuildConfig;
import eg.com.ivas.ivas_story_maker.R;

import static eg.com.ivas.ivas_story_maker.View.ImageEditor.CROP_BITMAB;
import static eg.com.ivas.ivas_story_maker.View.ImageEditor.SAVED_IMAGE;


public class ImageShare extends Fragment {

    @BindView(R.id.share)
    ImageButton share;
    @BindView(R.id.image)
    ImageView imageView;


    public ImageShare() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_share, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        String imagePath=getArguments().getString(SAVED_IMAGE);
        imageView.setImageBitmap(CROP_BITMAB);

        share.setOnClickListener(v -> {
            shareToSocialMedia(new File(imagePath));
        });


    }



    private void shareToSocialMedia(File file){
        String media_type;

        media_type = "image/*";


        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(media_type);

        // Create the URI from the media
        Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID+".fileProvider", file);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PackageManager packageManager = getActivity().getPackageManager();
        if (share.resolveActivity(packageManager) != null) {

            Intent chooser=Intent.createChooser(share, "Share to");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                // special case
                List<ResolveInfo> resInfoList = getActivity().getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    getActivity().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }

            startActivity(chooser);

        } else {
            Toast.makeText(getContext(), "No Social Media apps ", Toast.LENGTH_SHORT).show();
        }


    }


}