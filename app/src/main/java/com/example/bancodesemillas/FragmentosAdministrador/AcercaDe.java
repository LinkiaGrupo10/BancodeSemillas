package com.example.bancodesemillas.FragmentosAdministrador;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bancodesemillas.R;

public class AcercaDe extends AppCompatActivity {
    private ImageView facebookImageView;
    private ImageView twitterImageView;
    private ImageView instagramImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acercade);

        facebookImageView = findViewById(R.id.facebookImageView);
        twitterImageView = findViewById(R.id.twitterImageView);
        instagramImageView = findViewById(R.id.instagramImageView);

        // Facebook
        facebookImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookUrl = "https://www.facebook.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });

        // Twitter
        twitterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String twitterUrl = "https://twitter.com";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl));
                startActivity(intent);
            }
        });

        // Instagram
        instagramImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instagramUrl = "https://www.instagram.com";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl));
                startActivity(intent);
            }
        });
    }
}

