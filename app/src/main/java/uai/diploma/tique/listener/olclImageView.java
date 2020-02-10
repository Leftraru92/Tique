package uai.diploma.tique.listener;

import android.content.Context;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

import androidx.appcompat.widget.PopupMenu;
import uai.diploma.tique.R;

import static android.content.Context.VIBRATOR_SERVICE;

public class olclImageView implements View.OnLongClickListener {

    Context context;
    final Vibrator vibrator;
    View img;
    TextView tvCountImage;
    LinearLayout llImagenes;

    public olclImageView(Context context, View img, TextView tvCountImage, LinearLayout llImagenes){
        this.context = context;
        this.img = img;
        this.tvCountImage = tvCountImage;
        this.llImagenes = llImagenes;
        vibrator = (Vibrator)context.getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public boolean onLongClick(View view) {
        vibrator.vibrate(100);
        showMenu(img);
        return true;
    }

    public void showMenu(View anchor) {
        PopupMenu popup = new PopupMenu(context, anchor);
        popup.getMenuInflater().inflate(R.menu.menu_imagen, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //Elimino de la vista
                llImagenes.removeView(img);

                //Elimino de la ruta
                String fotoPath = (String) img.getTag();
                if(fotoPath != null) {
                    File file = new File(fotoPath);
                    if (file.exists())
                        file.delete();
                }
                Toast.makeText(context, "Se eliminó la imagen", Toast.LENGTH_SHORT).show();
                if(tvCountImage != null) {
                    if (llImagenes.getChildCount() > 0) {
                        tvCountImage.setText(llImagenes.getChildCount() + " Adjuntos");
                    } else {
                        tvCountImage.setText("");
                    }
                }
                return true;
            }
        });
        popup.show();
    }

}

