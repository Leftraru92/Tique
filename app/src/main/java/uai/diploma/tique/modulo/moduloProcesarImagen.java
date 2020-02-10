package uai.diploma.tique.modulo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import uai.diploma.tique.listener.oclImageView;
import uai.diploma.tique.listener.olclImageView;
import uai.diploma.tique.util.Constantes;

import static android.app.Activity.RESULT_CANCELED;

public class moduloProcesarImagen {

    private int[] GALLERY = {1,3,5};
    private int[] CAMERA = {2,4,6};
    private Context context;
    LinearLayout llImagenes;
    TextView tvCountImage;

    public void procesarImagen(int requestCode, int resultCode, Intent data, Context ctx, LinearLayout llImagenes, TextView tvCountImage) {

        this.context = ctx;
        this.llImagenes = llImagenes;
        this.tvCountImage = tvCountImage;

        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (existeEnArreglo(GALLERY, requestCode)) {

        //if (requestCode == GALLERY.) {
            if (data != null) {

                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri contentURI = data.getClipData().getItemAt(i).getUri();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                            String path = saveImage(bitmap);
                            //Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                            addImageToLayout(bitmap, null);

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (data.getData() != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                        getResizedBitmap(bitmap, 720);
                        String path = saveImage(bitmap);
                        //Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                        addImageToLayout(bitmap, null);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if  (existeEnArreglo(CAMERA, requestCode)) {
        //} else if (requestCode == CAMERA) {
            //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            //addImageToLayout(thumbnail);

            String fotoPath = Constantes.LastPhotoUri;
            Bitmap bitmap = null;
            try {
                //bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(fotoPath));
                bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.parse("file://"+fotoPath)));

                //Escalo la imagen guardada
                File file = new File(fotoPath);

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    //Rotate
                    bitmap = (Bitmap) rotateImageIfRequired(context, bitmap, Uri.parse(fotoPath));
                    //Resize
                    bitmap = getResizedBitmap(bitmap, 720);
                    //elimino imagen original
                    if (file.exists())
                        file.delete();
                    //Compress and save
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                addImageToLayout(bitmap, fotoPath);

                //Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                //File f = new File(fotoPath);
                //Uri contentUri = Uri.fromFile(f);
                //mediaScanIntent.setData(contentUri);
                //context.sendBroadcast(mediaScanIntent);
                //saveImage(bitmap);
                // Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(Constantes.LOG_NAME, "Error " + e);
                Toast.makeText(context, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean existeEnArreglo(int[] arreglo, int busqueda) {
        for (int x = 0; x < arreglo.length; x++) {
            if (arreglo[x] == busqueda) {
                return true;
            }
        }
        return false;
    }

    private void addImageToLayout(Bitmap bitmap, String fotoPath) {

        bitmap = getResizedBitmap(bitmap, 1024);

        ImageView iv = new ImageView(context);
        iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv.setPaddingRelative(0, 0, 8, 0);
        iv.setAdjustViewBounds(true);
        iv.setImageBitmap(bitmap);
        iv.setTag(fotoPath);
        TypedValue outValue = new TypedValue();
        //Seleccionable
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        iv.setBackgroundResource(outValue.resourceId);
        //On click delete
        iv.setOnClickListener(new oclImageView(iv, context));
        iv.setOnLongClickListener(new olclImageView(context, iv, tvCountImage, llImagenes));
        llImagenes.addView(iv);

        if (tvCountImage != null)
            tvCountImage.setText(llImagenes.getChildCount() + " Adjuntos");
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        myBitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + Constantes.IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d(Constantes.LOG_NAME, "Imagen salvada:--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
/*
    private void setPic(ImageView imageView, Bitmap bitmap) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmapa = BitmapFactory.decodeFile(bitmap.get, bmOptions);
        imageView.setImageBitmap(bitmap);
    }*/

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) {

        // Detect rotation
        int rotation = getRotation(context, selectedImage);
        //int rotation = getOrientationFromExif(String.valueOf(selectedImage));

        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            img.recycle();
            return rotatedImg;
        }
        else{
            return img;
        }
    }

    private static int getRotation(Context context, Uri selectedImage) {

        Cursor cursor = context.getContentResolver().query(selectedImage,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor == null || cursor.getCount() != 1) {
            return 90;  //Assuming it was taken portrait
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    private static int getOrientationFromExif(String imagePath) {
        int orientation = 0;
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    orientation = 270;

                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    orientation = 180;

                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    orientation = 90;

                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                    orientation = 0;

                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            Log.e(Constantes.LOG_NAME, "Unable to get image exif orientation", e);
        }

        return orientation;
    }

}
