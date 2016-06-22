package com.example.nthucs.sleepingalarm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ShopActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ArrayList<View> viewList;
    private static String TIME_TICKET_PRICE = " $ 30 ";
    private static String RING_TICKET_PRICE = " $ 20 ";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shop);

        mViewPager = (ViewPager) findViewById(R.id.ItemView);
        final LayoutInflater mInflater = getLayoutInflater().from(this);

        View v1 = mInflater.inflate(R.layout.item1_fragment, null);
        View v2 = mInflater.inflate(R.layout.item2_fragment, null);

        viewList = new ArrayList<View>();

        viewList.add(v1);
        viewList.add(v2);

        mViewPager.setAdapter(new PagerAdapterItem(viewList));
        mViewPager.setCurrentItem(0);

        final TextView priceText = (TextView)findViewById(R.id.priceText);

        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        priceText.setText(TIME_TICKET_PRICE);
        radioGroup.check(R.id.radioButton);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.radioButton);
                        priceText.setText(TIME_TICKET_PRICE);
                        break;
                    case 1:
                        radioGroup.check(R.id.radioButton2);
                        priceText.setText(RING_TICKET_PRICE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final Button buyButton = (Button)findViewById(R.id.buyButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ShopActivity.this)
                        .setTitle("Buy this ?")
                        .setMessage("Do you want to buy this ticket ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton) {
                                    Toast.makeText(ShopActivity.this, "Buy first", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ShopActivity.this, "Buy second", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Not Buy", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }
}
