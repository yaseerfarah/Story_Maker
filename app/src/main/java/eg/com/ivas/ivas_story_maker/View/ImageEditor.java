package eg.com.ivas.ivas_story_maker.View;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gturedi.views.StatefulLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import eg.com.ivas.ivas_story_maker.Util.ImageUtils;
import es.dmoral.toasty.Toasty;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ja.burhanrashid52.photoeditor.ItemSelectListener;
import ja.burhanrashid52.photoeditor.LayerItem;
import eg.com.ivas.ivas_story_maker.R;
import eg.com.ivas.ivas_story_maker.Util.ReadAndWriteToFile;
import io.reactivex.Single;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.ViewType;
import yuku.ambilwarna.AmbilWarnaDialog;

import static android.app.Activity.RESULT_OK;

public class ImageEditor extends Fragment  implements ItemSelectListener<View> {

    private static final int ADD_ANOTHER_IMAGE = 500;

    public static String SAVED_IMAGE="SavedImage";
    public static String TEMPLATE_LINK="TemplateLink";

    public static Bitmap CROP_BITMAB;
    public static boolean isDoneCroping=false;
    private ja.burhanrashid52.photoeditor.PhotoEditor photoEditor;

    private View activeViewImage;
    private boolean isNewImage;

    private String templateUri;

    private int templateImgCount=1;
    private int currentImgCount;

    @BindView(R.id.photoEditorView)
    PhotoEditorView photoEditorView;

    @BindView(R.id.add_txt)
    ImageButton addTxt;
    @BindView(R.id.crop)
    ImageButton crop;
    @BindView(R.id.add_img)
    ImageButton addImg;
    @BindView(R.id.filter)
    ImageButton filter;
    @BindView(R.id.done)
    ImageButton done;
    @BindView(R.id.spiral)
    ImageButton spiral;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;




    NavController navController;

    Typeface typeface;
    int txtDefaultColor;



    private List<LayerItem> layerItemList=new ArrayList<>();

    private String savedImagePath;




    private final int PICK_FROM_GALLARY=400;


    public ImageEditor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getArguments() != null) {
            templateUri=getArguments().getString(TEMPLATE_LINK);
        }
        return inflater.inflate(R.layout.fragment_image_editor, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        navController= Navigation.findNavController(view);
        txtDefaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        if (currentImgCount==templateImgCount){
            addImg.setEnabled(false);
        }




        if (photoEditor==null) {
            photoEditor = new ja.burhanrashid52.photoeditor.PhotoEditor.Builder(getContext(), photoEditorView)
                    .setPinchTextScalable(true)
                    .build();
            addTemplate();
            currentImgCount=0;
        }else {

            if (isDoneCroping) {
                if (isNewImage) {
                    photoEditor.setParentView(photoEditorView, CROP_BITMAB,this);
                } else {
                    photoEditor.setParentView(photoEditorView, null,this);
                    ((ImageView) activeViewImage.findViewById(R.id.imgPhotoEditorImage)).setImageBitmap(CROP_BITMAB);
                }
                isDoneCroping=false;
            }else {
                photoEditor.setParentView(photoEditorView, null,this);
            }

        }








        photoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
            @Override
            public void onEditTextChangeListener(View rootView, String text, int colorCode) {

                createTxtDialog(rootView,text,colorCode);

            }

            @Override
            public void onAddViewListener(ViewType viewType, View view1, int numberOfAddedViews) {
                switch (viewType){

                    case TEXT:
                        view1.setElevation(2);
                        setEnable(false);
                        break;
                    case IMAGE:
                        view1.setElevation(0);
                        activeViewImage=view1;
                        setEnable(true);
                        currentImgCount++;
                        if (currentImgCount==templateImgCount){
                            addImg.setEnabled(false);
                        }
                        break;
                    case TEMPLATE:
                        view1.setElevation(1);
                        break;

                }


            }

            @Override
            public void onRemoveViewListener(ViewType viewType,View view1, int numberOfAddedViews) {

                if (viewType==ViewType.IMAGE){
                    currentImgCount--;
                    addImg.setEnabled(true);
                }

            }

            @Override
            public void onStartViewChangeListener(ViewType viewType) {

            }

            @Override
            public void onStopViewChangeListener(ViewType viewType) {

            }
        });







        addTxt.setOnClickListener(v -> {
            createTxtDialog(null,null,txtDefaultColor);
        });



        crop.setOnClickListener(v -> {
            isNewImage=false;
            navController.navigate(R.id.action_imageEditor_to_imageCrop);

        });

        spiral.setOnClickListener(v -> {
            isNewImage=false;
            navController.navigate(R.id.action_imageEditor_to_imageSpiral);

        });

        filter.setOnClickListener(v -> {
            isNewImage=false;
            navController.navigate(R.id.action_imageEditor_to_imageFilter);

        });




        addImg.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            gallery.addCategory(Intent.CATEGORY_OPENABLE);
            String[] mimeTypes = {"image/png", "image/jpg", "image/jpeg"};
            gallery.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"*/*");
            gallery.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);

            startActivityForResult(gallery, ADD_ANOTHER_IMAGE);
        });













        done.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
            String imageName=time.format(calendar.getTime());


            statefulLayout.showLoading(" ");
            photoEditor.saveAsBitmap(new SaveSettings.Builder()
                    .setClearViewsEnabled(true).build(),new OnSaveBitmap() {
                @Override
                public void onBitmapReady(Bitmap saveBitmap) {
                    saveImage(saveBitmap,imageName)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<String>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(String s) {
                                    savedImagePath=s;
                                    //shareToWhatsApp(new File(s));
                                    CROP_BITMAB=saveBitmap;
                                    Bundle bundle=new Bundle();
                                    bundle.putString(SAVED_IMAGE,savedImagePath);
                                    navController.navigate(R.id.action_imageEditor_to_imageShare,bundle);
                                    Toasty.success(view.getContext(),getResources().getString(R.string.success),Toast.LENGTH_SHORT).show();
                                    photoEditor=null;
                                    layerItemList.clear();
                                    progressBar.setVisibility(View.INVISIBLE);

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("ERROR RXjava SAVE IMAGE",e.getMessage());
                                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("ERROR SAVE IMAGE",e.getMessage());
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            },getActivity());
        });


        // photoEditor.addTemplate(BitmapFactory.decodeResource(getResources(),R.mipmap.tem));



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode==RESULT_OK&&requestCode==ADD_ANOTHER_IMAGE){


            try {

                isNewImage=true;
                /*BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                CROP_BITMAB=BitmapFactory.decodeStream(inputStream,null,options);*/

                DisplayMetrics displayMetrics = new DisplayMetrics();
                Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int displayWidth = displayMetrics.widthPixels;
                int displayHeight=displayMetrics.heightPixels;

                CROP_BITMAB= ImageUtils.decodeSampledBitmapFromResource(getContext(),data.getData(),displayWidth,displayHeight);

                if (CROP_BITMAB!=null){
                    navController.navigate(R.id.action_imageEditor_to_imageCrop);
                }else {
                    Toasty.warning(getContext(),getResources().getString(R.string.try_again),Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                        e.printStackTrace();
            }


        }

    }








    private void createTxtDialog(View rootView,String name,int txt_color){


        int[] colors=new int[1];
        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_txt);
        typeface= Typeface.defaultFromStyle(Typeface.NORMAL);
        final EditText txt=(EditText) dialog.findViewById(R.id.txt_edit);
        ToggleButton txtBold=(ToggleButton)dialog.findViewById(R.id.txt_bold);
        ToggleButton txtitalic=(ToggleButton)dialog.findViewById(R.id.txt_italic);
        final ImageButton color=(ImageButton) dialog.findViewById(R.id.txt_color);
        ImageButton done=(ImageButton) dialog.findViewById(R.id.add);
        txt.setTextColor(txt_color);
        colors[0]=txt_color;
        if (name!=null){
            txt.setText(name);
            TextView inputTextView = rootView.findViewById(R.id.tvPhotoEditorText);
            typeface=inputTextView.getTypeface();
            txt.setTypeface(typeface);
            if (typeface.isBold()){

                txtBold.setChecked(true);
            }
            if (typeface.isItalic()){
                txtitalic.setChecked(true);
            }

        }

        txtBold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (!txtitalic.isChecked()) {
                        txt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        typeface=Typeface.defaultFromStyle(Typeface.BOLD);
                    }else {
                        txt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                        typeface=Typeface.defaultFromStyle(Typeface.BOLD_ITALIC);
                    }
                }else {
                    if (!txtitalic.isChecked()) {
                        txt.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        typeface=Typeface.defaultFromStyle(Typeface.NORMAL);
                    }else {
                        txt.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                        typeface=Typeface.defaultFromStyle(Typeface.ITALIC);
                    }
                }
            }
        });


        txtitalic.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                if (!txtBold.isChecked()) {
                    txt.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                    typeface=Typeface.defaultFromStyle(Typeface.ITALIC);
                }else {
                    txt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                    typeface=Typeface.defaultFromStyle(Typeface.BOLD_ITALIC);
                }
            }else {
                if (!txtBold.isChecked()) {
                    txt.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    typeface=Typeface.defaultFromStyle(Typeface.NORMAL);
                }else {
                    txt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    typeface=Typeface.defaultFromStyle(Typeface.BOLD);
                }
            }


        });



        color.setOnClickListener(v -> {

            colorPickerDialog(colors[0],new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {

                }

                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    colors[0]=color;

                    txt.setTextColor(colors[0]);

                }
            });


        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt.getText().toString().trim().length()>0){

                    if (name!=null){
                        photoEditor.editText(rootView,typeface,txt.getText().toString(),colors[0]);

                    }else {
                        photoEditor.addText(typeface,txt.getText().toString(),colors[0],ImageEditor.this::onSelect);
                    }
                    dialog.dismiss();



                }else {
                    Toast.makeText(getContext(),"Write Something",Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }








    private void colorPickerDialog(int color,AmbilWarnaDialog.OnAmbilWarnaListener onAmbilWarnaListener ){

        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), color, onAmbilWarnaListener);
        colorPicker.show();




    }



    private Single<String> saveImage(Bitmap bitmap1, String imageName){
        return  Single.create(emitter -> {
            //  String[] uri_arr=uri.split("/");
            //String[] name_arr=Uri.parse(uri).getLastPathSegment().split(".");
            // String name=name_arr[0];
            try {
                String path= ReadAndWriteToFile.writeImageToSpecificFile(getContext(),bitmap1, imageName);
                if (path!=null){

                    if (!emitter.isDisposed()){

                        emitter.onSuccess(path);
                    }
                }
            }catch (Exception e){
                if (!emitter.isDisposed()){
                    emitter.onError(e);
                }
            }


        });
    }




    private void addTemplate(){
        statefulLayout.showLoading(" ");
        setEnable(false);
        Glide.with(getContext()).load(templateUri)
                .apply(RequestOptions.timeoutOf(60*1000))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        statefulLayout.showContent();
                        Bitmap template=((BitmapDrawable)resource).getBitmap();
                        photoEditor.addTemplate(template);
                        addImg.callOnClick();
                    }
                });
    }



    private void setEnable(boolean enable){
        crop.setEnabled(enable);
        spiral.setEnabled(enable);
        filter.setEnabled(enable);
    }


    @Override
    public void onSelect(View item) {
        onFocus(item);
    }


    public void onFocus(View view) {
        if (view.getTag()==ViewType.IMAGE){
            activeViewImage=view;
            CROP_BITMAB=((BitmapDrawable)((ImageView)view.findViewById(R.id.imgPhotoEditorImage)).getDrawable()).getBitmap();
          setEnable(true);
        }else {
           setEnable(false);
        }
    }


}