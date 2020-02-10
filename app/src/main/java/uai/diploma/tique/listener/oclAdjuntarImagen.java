package uai.diploma.tique.listener;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import uai.diploma.tique.R;
import uai.diploma.tique.activity.NuevoNegocioActivity;
import uai.diploma.tique.util.Constantes;

public class oclAdjuntarImagen implements View.OnClickListener {

    Context context;
    int id_galeria, id_camara;
    //private int GALLERY = 1, CAMERA = 2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    String currentPhotoPath;

    public oclAdjuntarImagen(Context context, int id_galeria, int id_camara) {
        this.context = context;
        this.id_galeria = id_galeria;
        this.id_camara = id_camara;
    }

    @Override
    public void onClick(View view) {

        showPictureDialog();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
        pictureDialog.setTitle(context.getString(R.string.selec_opt));
        String[] pictureDialogItems = {
                context.getString(R.string.selec_gale),
                context.getString(R.string.cap_foto)};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                    takePhotoFromCamera();
                                } else {
                                    if (context.checkSelfPermission(Manifest.permission.CAMERA)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        ((NuevoNegocioActivity) context).requestPermissions(new String[]{Manifest.permission.CAMERA},
                                                MY_CAMERA_REQUEST_CODE);
                                    } else {
                                        takePhotoFromCamera();
                                    }
                                }
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        try {
            ((NuevoNegocioActivity) context).startActivityForResult(galleryIntent, id_galeria);
        } catch (ActivityNotFoundException e) {
            Snackbar.make(((NuevoNegocioActivity) context).findViewById(R.id.btnCamera), "No se encontró una aplicación compatible", Snackbar.LENGTH_SHORT).show();
        }
    }


    private void takePhotoFromCamera() {

        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(Constantes.LOG_NAME, "Error " + ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(context,
                        "com.edensa.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Constantes.LastPhotoUri = photoFile.getAbsolutePath();
                Log.d(Constantes.LOG_NAME, "Foto path " + Constantes.LastPhotoUri);
                ((NuevoNegocioActivity) context).startActivityForResult(takePictureIntent, id_camara);

            } else {
                Snackbar.make(((NuevoNegocioActivity) context).findViewById(R.id.btnCamera), "Error al crear el archivo", Snackbar.LENGTH_SHORT).show();
            }

        } else {
            Snackbar.make(((NuevoNegocioActivity) context).findViewById(R.id.btnCamera), "No se encontró una aplicación compatible", Snackbar.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
       // currentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
