package eg.com.ivas.ivas_story_maker.View;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import eg.com.ivas.ivas_story_maker.POJO.CropModel;
import eg.com.ivas.ivas_story_maker.R;
import eg.com.ivas.ivas_story_maker.Util.ImageUtils;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;
import static eg.com.ivas.ivas_story_maker.View.ImageEditor.CROP_BITMAB;

public class ImageCrop extends Fragment {

    private static final int NONE=0;
    private static final int SQUARE=1;
    private static final int CIRCLE=2;

    @BindView(R.id.cropImageView)
    CropImageView cropImageView;
    @BindView(R.id.gal)
    ImageButton gallery;
    @BindView(R.id.crop)
    ImageButton crop;
    @BindView(R.id.close)
    ImageButton close;
    @BindView(R.id.done)
    ImageButton done;
    @BindView(R.id.r_right)
    ImageButton r_Right;
    @BindView(R.id.r_left)
    ImageButton r_Left;
    @BindView(R.id.c_sq)
    ImageButton c_Square;
    @BindView(R.id.c_cr)
    ImageButton c_Circle;
    @BindView(R.id.c_fr)
    ImageButton c_Freehand;

    @BindView(R.id.progress)
    ProgressBar progressBar;


    private Path clipPath;
    private Bitmap bmp;
    private Bitmap alteredBitmap;
    private Canvas canvas;
    private Paint paint;
    private float downx = 0;
    private float downy = 0;
    private float tdownx = 0;
    private float tdowny = 0;
    private float upx = 0;
    private float upy = 0;

    private Display display;
    private Point size;
    private int screen_width,screen_height;
    private float smallx,smally,largex,largey;
    private ArrayList<CropModel> cropModelArrayList;
    private int lastRotate=0;
    private int lastR=0;
    private int lastRC=0;
    private int diff=0;

    private int cropType;




    private boolean is_Pick,isCropEnable,isDone,isCanvas;


    private Bitmap bitmap;
    private Uri imageUri;
    private int imageNum;

    private final int PICK_FROM_GALLARY=400;



    public ImageCrop() {
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
        return inflater.inflate(R.layout.fragment_image_crop, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                gallery.addCategory(Intent.CATEGORY_OPENABLE);
                String[] mimeTypes = {"image/png", "image/jpg", "image/jpeg"};
                gallery.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"*/*");
                gallery.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);

                startActivityForResult(gallery,PICK_FROM_GALLARY);
            }
        });


        isCanvas=false;
        stopFreehandCrop();
        is_Pick=true;
        isCropEnable=true;
        isDone=false;
        cropType=SQUARE;
        cropImageView.setCropEnabled(isCropEnable);
        cropImageView.setCropMode(CropImageView.CropMode.FREE);
        cropImageView.setAnimationEnabled(false);
        bitmap=CROP_BITMAB;
        cropImageView.setImageBitmap(bitmap);




        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_Pick&&isCropEnable&&!isDone){
                    cropImageView.crop(null).execute(new CropCallback() {
                        @Override
                        public void onSuccess(Bitmap cropped) {
                            bitmap=cropped;
                            CROP_BITMAB=cropped;
                            cropImageView.setImageBitmap(bitmap);

                            isCropEnable=false;
                            cropImageView.setCropEnabled(isCropEnable);
                            isDone=true;
                            setVisibility();
                            //Toast.makeText(getContext(),"Please Press Again",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();


                        }
                    });
                }
            }
        });



        done.setOnClickListener(v -> {
            ImageEditor.isDoneCroping=true;
            CROP_BITMAB=bitmap;
            Navigation.findNavController(view).navigateUp();
                //Toast.makeText(CropImage.this,"Done",Toast.LENGTH_LONG).show();

        });


        close.setOnClickListener(v -> {

            ImageEditor.isDoneCroping=false;
            Navigation.findNavController(view).navigateUp();
        });


        r_Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_Pick&&isCropEnable&&!isDone) {

                    cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                    lastR=lastRotate;
                    lastRotate-=90;
                }else if(is_Pick&&isCanvas&&!isDone) {
                    rotate(alteredBitmap,-90);

                    // startFreehandCrop(rotateBitmap(((BitmapDrawable)cropImageView.getDrawable()).getBitmap(),-90));
                }

                Log.e("R_Left",String.valueOf(lastRotate));

            }
        });



        r_Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_Pick&&isCropEnable&&!isDone) {
                    cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                    lastR=lastRotate;
                    lastRotate+=90;
                }else if (is_Pick&&isCanvas&&!isDone){
                    rotate(alteredBitmap,90);

                }
                Log.e("R_Right",String.valueOf(lastRotate));
            }
        });




        c_Circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_Pick&&isCropEnable&&(cropType==SQUARE||cropType==NONE)&&!isDone||is_Pick&&isCanvas&&(cropType==SQUARE||cropType==NONE)&&!isDone) {

                    cropImageView.setImageBitmap(bitmap);
                    stopFreehandCrop();
                    isCropEnable=true;
                    cropImageView.setCropEnabled(isCropEnable);
                    cropImageView.setCropMode(CropImageView.CropMode.CIRCLE);
                    countRotate();
                    cropType=CIRCLE;
                }
            }
        });


        c_Square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_Pick&&isCropEnable&&(cropType==CIRCLE||cropType==NONE)&&!isDone||is_Pick&&isCanvas&&(cropType==CIRCLE||cropType==NONE)&&!isDone) {


                    cropImageView.setImageBitmap(bitmap);

                    stopFreehandCrop();
                    isCropEnable=true;
                    cropImageView.setCropEnabled(isCropEnable);
                    cropImageView.setCropMode(CropImageView.CropMode.FREE);
                    countRotate();
                    cropType=SQUARE;
                }
            }
        });


        c_Freehand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_Pick&&!isDone&&!isCanvas) {
                    isCropEnable = false;
                    cropType=NONE;
                    cropImageView.setCropEnabled(isCropEnable);
                    startFreehandCrop(((BitmapDrawable)cropImageView.getDrawable()).getBitmap());
                }


            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode==RESULT_OK&&requestCode==PICK_FROM_GALLARY){

            lastRotate=0;
            diff=0;
            lastR=0;
            lastRC=0;
            stopFreehandCrop();
            is_Pick=true;
            isCropEnable=true;
            isDone=false;
            cropImageView.setCropEnabled(isCropEnable);
            imageUri=data.getData();
            try {

                DisplayMetrics displayMetrics = new DisplayMetrics();
                Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int displayWidth = displayMetrics.widthPixels;
                int displayHeight=displayMetrics.heightPixels;

                bitmap= ImageUtils.decodeSampledBitmapFromResource(getContext(),data.getData(),displayWidth,displayHeight);

                if (bitmap!=null){
                    cropImageView.setImageBitmap(bitmap);
                }else {
                    Toasty.warning(getContext(),getResources().getString(R.string.try_again),Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }




        }

    }

    private byte[] getBitmapByte(Bitmap bmp){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }


    private void startFreehandCrop(Bitmap bitmap){

        isCanvas=true;
        init(bitmap);




        Rect rect;
        int orientation = getResources().getConfiguration().orientation;
        rect = new Rect(0,0,cropImageView.getWidth(), cropImageView.getHeight());



       /* Log.e(" FreeHand before Rotate",String.valueOf(lastRotate));
        Log.e(" FreeHand before diff",String.valueOf(diff));
        Log.e(" FreeHand before lastR",String.valueOf(lastR));
        Log.e(" FreeHand before lastRC",String.valueOf(lastRC));*/

        if (lastR!=lastRotate) {

            canvas.save();

            canvasCountRotate(rect);
            lastR=lastRotate;



            if (orientation == Configuration.ORIENTATION_LANDSCAPE){

                Bitmap bitmap1=Bitmap.createScaledBitmap(bmp,cropImageView.getHeight(),cropImageView.getHeight(),false);

                canvas.drawBitmap(bitmap1, (cropImageView.getWidth()-bitmap1.getWidth())/2,(cropImageView.getHeight()-bitmap1.getHeight())/2  , null);


            }else{
                canvas.drawBitmap(bmp, null,rect, null);
            }

           /* Log.e(" FreeHand after Rotate",String.valueOf(lastRotate));
            Log.e(" FreeHand after diff",String.valueOf(diff));
            Log.e(" FreeHand after lastR",String.valueOf(lastR));
            Log.e(" FreeHand after lastRC",String.valueOf(lastRC));*/

            canvas.restore();
            cropImageView.invalidate();
        }else {
            canvas.save();
            canvas.rotate(lastRotate, rect.exactCenterX(), rect.exactCenterY());
            if (orientation == Configuration.ORIENTATION_LANDSCAPE){

                Bitmap bitmap1=Bitmap.createScaledBitmap(bmp,cropImageView.getHeight(),cropImageView.getHeight(),false);

                canvas.drawBitmap(bitmap1, (cropImageView.getWidth()-bitmap1.getWidth())/2,(cropImageView.getHeight()-bitmap1.getHeight())/2  , null);


            }else{
                canvas.drawBitmap(bmp, null,rect, null);
            }
            canvas.restore();
        }


        cropImageView.setImageBitmap(alteredBitmap);

        cropImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ontouch(event);
                return true;
            }
        });
    }


    private void stopFreehandCrop(){

        if (canvas!=null){
            isCanvas=false;
            canvas.drawARGB(0, 0, 0, 0);
            canvas=null;
            cropImageView.invalidate();
            cropImageView.setOnTouchListener(null);
        }

    }


    private void init(Bitmap bitmap) {

        cropModelArrayList = new ArrayList<>();

        display = getActivity().getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        screen_width = size.x;
        screen_height = size.y;


        initcanvas(bitmap);
    }



    private void initcanvas(Bitmap bitmap) {

        Drawable d = cropImageView.getDrawable();
        bmp = ((BitmapDrawable)d).getBitmap();

        alteredBitmap = Bitmap.createBitmap(cropImageView.getWidth(), cropImageView.getHeight(), bitmap.getConfig());
        canvas = new Canvas(alteredBitmap);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(15);

        paint.setStyle(Paint.Style.STROKE);

    }


    private void ontouch(MotionEvent event){


        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:


                downx = event.getX();
                downy = event.getY();
                clipPath = new Path();
                clipPath.moveTo(downx, downy);
                tdownx = downx;
                tdowny = downy;
                smallx = downx;
                smally = downy;
                largex = downx;
                largey = downy;
                break;

            case MotionEvent.ACTION_MOVE:

                upx = event.getX()>cropImageView.getWidth()?cropImageView.getWidth(): event.getX();
                upy = event.getY()>cropImageView.getHeight()?cropImageView.getHeight(): event.getY();
                cropModelArrayList.add(new CropModel(upx, upy));
                clipPath = new Path();
                clipPath.moveTo(tdownx,tdowny);
                for(int i = 0; i<cropModelArrayList.size();i++){
                    clipPath.lineTo(cropModelArrayList.get(i).getY(),cropModelArrayList.get(i).getX());
                }
                canvas.drawPath(clipPath, paint);
                cropImageView.invalidate();
                downx = upx;
                downy = upy;
                break;

            case MotionEvent.ACTION_UP:

                if (!clipPath.isEmpty()&&cropModelArrayList.size()>10) {
                    if (upx != upy) {
                        upx = event.getX();
                        upy = event.getY();


                        canvas.drawLine(downx, downy, upx, upy, paint);
                        clipPath.lineTo(upx, upy);
                        cropImageView.invalidate();

                        crop();
                    }
                }else {
                    canvas = new Canvas(alteredBitmap);
                }


                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }


    }



    public void crop() {

        clipPath.close();
        clipPath.setFillType(Path.FillType.INVERSE_WINDING);

        for(int i = 0; i<cropModelArrayList.size();i++){
            if(cropModelArrayList.get(i).getY()<smallx&&cropModelArrayList.get(i).getY()>=0){

                smallx=cropModelArrayList.get(i).getY();
            }
            if(cropModelArrayList.get(i).getX()<smally&&cropModelArrayList.get(i).getX()>=0){

                smally=cropModelArrayList.get(i).getX();
            }
            if(cropModelArrayList.get(i).getY()>largex){

                largex=cropModelArrayList.get(i).getY();
            }
            if(cropModelArrayList.get(i).getX()>largey){

                largey=cropModelArrayList.get(i).getX();
            }
        }

        save();

    }


    private void save() {

        if(clipPath != null) {
            final int color = 0xff424242;
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPath(clipPath, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            canvas.drawBitmap(alteredBitmap, 0, 0, paint);

            float w = largex - smallx;
            float h = largey - smally;
            bitmap = Bitmap.createBitmap(alteredBitmap, (int) smallx, (int) smally, (int) w, (int) h);



        }else{
            bitmap = bmp;
        }

        cropImageView.setImageBitmap(bitmap);
        setVisibility();
        stopFreehandCrop();
        isDone=true;



    }




    private void rotate(Bitmap bitmap, int rotationAngleDegree){

        Rect rect;
        Bitmap bitmap1=Bitmap.createScaledBitmap(bmp,cropImageView.getHeight(),cropImageView.getHeight(),false);
        int orientation = getResources().getConfiguration().orientation;
        rect = new Rect(0,0,cropImageView.getWidth(), cropImageView.getHeight());

        Log.e(" before Rotate",String.valueOf(lastRotate));
        Log.e(" before diff",String.valueOf(diff));
        canvas.drawColor(Color.WHITE);
        canvas.save();
        lastRotate+=rotationAngleDegree;
        canvas.rotate(lastRotate,rect.exactCenterX(),rect.exactCenterY());


        //lastRotate=diff;
        //lastRC=lastRotate;
        lastR=lastRotate;

        Log.e(" after Rotate",String.valueOf(lastRotate));
        Log.e(" after diff",String.valueOf(diff));

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape

            canvas.drawBitmap(bitmap1, (cropImageView.getWidth()-bitmap1.getWidth())/2,(cropImageView.getHeight()-bitmap1.getHeight())/2  , null);


        } else {
            // In portrait
            canvas.drawBitmap(bmp, null, new Rect(0,0,cropImageView.getWidth(), cropImageView.getHeight()), null);

        }
        canvas.restore();
        cropImageView.invalidate();
    }




    private void countRotate(){


        boolean reverse;
        //Toast.makeText(getContext(),String.valueOf(lastRotate),Toast.LENGTH_SHORT).show();

        if (lastRotate > 0) {
            reverse = false;
        } else {
            reverse = true;
        }


        int rotate = Math.abs(lastRotate / 90);
        while (rotate > 4) {

            rotate = rotate - 4;

        }

        switch (rotate) {
            case 4:
                lastRotate=0;
                break;
            case 3:
                //Toast.makeText(getContext(),String.valueOf(rotate),Toast.LENGTH_SHORT).show();
                if (!reverse) {
                    cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_270D);
                    lastRotate=270;
                } else {
                    cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M270D);
                    lastRotate=-270;
                }
                break;
            case 2:
                if (!reverse) {
                    cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_180D);
                    lastRotate=180;
                } else {
                    cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M180D);
                    lastRotate=-180;
                }
                break;
            case 1:
                if (!reverse) {
                    cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                    lastRotate=90;
                } else {
                    //Toast.makeText(getContext(),"hi",Toast.LENGTH_SHORT).show();
                    cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                    lastRotate=-90;
                }

        }



    }


    private void canvasCountRotate(Rect rect){


        boolean reverse;
        //Toast.makeText(getContext(),String.valueOf(lastRotate),Toast.LENGTH_SHORT).show();

        if (lastRotate > 0) {
            reverse = false;
        } else {
            reverse = true;
        }


        int rotate = Math.abs(lastRotate / 90);
        while (rotate > 4) {

            rotate = rotate - 4;

        }

        switch (rotate) {
            case 4:
                lastRotate=0;
                canvas.rotate(lastRotate, rect.exactCenterX(), rect.exactCenterY());
                break;
            case 3:
                //Toast.makeText(getContext(),String.valueOf(rotate),Toast.LENGTH_SHORT).show();
                if (!reverse) {
                    lastRotate=270;
                    canvas.rotate(lastRotate, rect.exactCenterX(), rect.exactCenterY());

                } else {
                    lastRotate=-270;
                    canvas.rotate(lastRotate, rect.exactCenterX(), rect.exactCenterY());

                }
                break;
            case 2:
                if (!reverse) {
                    lastRotate=180;
                    canvas.rotate(lastRotate, rect.exactCenterX(), rect.exactCenterY());

                } else {
                    lastRotate=-180;
                    canvas.rotate(lastRotate, rect.exactCenterX(), rect.exactCenterY());

                }
                break;
            case 1:
                if (!reverse) {
                    lastRotate=90;
                    canvas.rotate(lastRotate, rect.exactCenterX(), rect.exactCenterY());

                } else {
                    lastRotate=-90;
                    //Toast.makeText(getContext(),"hi",Toast.LENGTH_SHORT).show();
                    canvas.rotate(lastRotate, rect.exactCenterX(), rect.exactCenterY());

                }

        }



    }

    private void setVisibility(){

        c_Square.setVisibility(View.INVISIBLE);
        c_Freehand.setVisibility(View.INVISIBLE);
        c_Circle.setVisibility(View.INVISIBLE);
        r_Left.setVisibility(View.INVISIBLE);
        r_Right.setVisibility(View.INVISIBLE);
        crop.setVisibility(View.INVISIBLE);
        gallery.setVisibility(View.INVISIBLE);

        done.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);

    }




}