package com.example.henry.digidrop;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;

public class MainActivity extends AppCompatActivity {

    public KeyPair keyPair;
    public PublicKey pubKey;
    public PrivateKey privKey;
    public static PublicKey recipientKey;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    ////why mwotnfladlfadf
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private void keyPair() {
        SharedPreferences shared = getApplicationContext().getSharedPreferences("Context", Context.MODE_PRIVATE);
        String pubKeyString = shared.getString("PublicKey", null);
        String privKeyString = shared.getString("PrivateKey", null);
        SharedPreferences.Editor SPE;
        if (pubKeyString == null && privKeyString == null) {
            try {
                KeyPairGenerator generator;
                generator = KeyPairGenerator.getInstance("RSA", "BC");
                generator.initialize(256, new SecureRandom());
                keyPair = generator.generateKeyPair();
                pubKey = keyPair.getPublic();
                privKey = keyPair.getPrivate();

                byte[] publicKeyBytes = pubKey.getEncoded();
                String pubKeyStr = new String(Base64.encode(publicKeyBytes, Base64.DEFAULT));
                byte[] privKeyBytes = privKey.getEncoded();
                String privKeyStr = new String(Base64.encode(privKeyBytes, Base64.DEFAULT));
                SPE = shared.edit();
                SPE.putString("PublicKey", pubKeyStr);
                SPE.putString("PrivateKey", privKeyStr);
                SPE.commit();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
        }

    }
    public PublicKey getPublicKey(){
        SharedPreferences shared = getApplicationContext().getSharedPreferences("Context", MODE_PRIVATE);
        String pubKeyStr = shared.getString("PublicKey", "");
        byte[] sigBytes = Base64.decode(pubKeyStr, Base64.DEFAULT);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes);
        KeyFactory keyFact = null;
        try {
            keyFact = KeyFactory.getInstance("RSA", "BC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            return  keyFact.generatePublic(x509KeySpec);
        } catch (java.security.spec.InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PrivateKey getPrivateKey(){
        SharedPreferences shared = getApplicationContext().getSharedPreferences("Context", MODE_PRIVATE);
        String privKeyStr = shared.getString("PrivateKey", "");
        byte[] sigBytes = Base64.decode(privKeyStr, Base64.DEFAULT);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes);
        KeyFactory keyFact = null;
        try {
            keyFact = KeyFactory.getInstance("RSA", "BC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            return  keyFact.generatePrivate(x509KeySpec);
        } catch (java.security.spec.InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        pubKey = getPublicKey();
        privKey = getPrivateKey();

        if (pubKey == null && privKey == null){
            keyPair();
        }
        //Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), pubKey.toString(), Toast.LENGTH_LONG).show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    QRReaderFragment tab = new QRReaderFragment();
                    return tab;
                case 1:
                    GetMsgFragment tabtoo = new GetMsgFragment();
                    return tabtoo;
                case 2:
                    PutMsgFragment tabthree = new PutMsgFragment();
                    return tabthree;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "QR Reader";
                case 1:
                    return "Messages";
                case 2:
                    return "Generate QR";
            }
            return null;
        }
    }
}
