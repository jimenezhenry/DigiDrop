package com.example.henry.digidrop;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.henry.digidrop.services.DataService;
import com.example.henry.digidrop.services.QRUtils;

/**
 * Created by Evan on 5/10/17.
 */

public class DisplayMyPublicKeyActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_my_public_key);

        qrCodeImageView = (ImageView) findViewById(R.id.qr_code_image_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String mypub = DataService.loadMyKeys(this).getPub();
        if(mypub != null) {
            Bitmap qrcode = QRUtils.createQr(mypub);
            qrCodeImageView.setImageBitmap(qrcode);
            qrCodeImageView.postInvalidate();
        }
    }
}
