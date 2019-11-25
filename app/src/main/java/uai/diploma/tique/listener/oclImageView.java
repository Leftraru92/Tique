package uai.diploma.tique.listener;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import uai.diploma.tique.R;
import uai.diploma.tique.activity.DetailActivity;

public class oclImageView implements View.OnClickListener {

    boolean isImageFitToScreen = false;
    ImageView imageView;
    Context context;

    public oclImageView(ImageView iv, Context context) {
        this.imageView = iv;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = ((DetailActivity) context).getLayoutInflater().inflate(R.layout.dialog_image_fullsize, null);
        PhotoView photoView = mView.findViewById(R.id.imageView);
        photoView.setImageDrawable(imageView.getDrawable());
        mBuilder.setView(mView);
        final AlertDialog mDialog = mBuilder.create();

        FloatingActionButton fabClose = mView.findViewById(R.id.fabClose);
        fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        mDialog.show();

    }
}
