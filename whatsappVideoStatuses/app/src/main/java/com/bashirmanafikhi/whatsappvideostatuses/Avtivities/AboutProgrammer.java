package com.bashirmanafikhi.whatsappvideostatuses.Avtivities;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bashirmanafikhi.whatsappvideostatuses.Helpers.MyClass;
import com.bashirmanafikhi.whatsappvideostatuses.R;

public class AboutProgrammer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_programmer);

        /*Button programmerWhatsapp = (Button) findViewById(R.id.programmerWhatsapp);
        programmerWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp("+905370466004");
            }
        });*/


    }

    public void gotofacebook(View view) {

        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = MyClass.getFacebookPageURL(this,"https://www.facebook.com/bashir.manafikhi/");
        facebookIntent.setData(Uri.parse(facebookUrl));
        startActivity(facebookIntent);
    }

    public void gotoyoutube(View view) {
        MyClass.youtube(this,"UCF92yxj5eK3scRY0Wvnk05w");
    }



    private void openWhatsApp(String number) {
        try {
            number = number.replace(" ", "").replace("+", "");

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number)+"@s.whatsapp.net");
            startActivity(sendIntent);

        } catch(Exception e) {
            Log.e("MyErrors", "ERROR_OPEN_MESSANGER"+e.toString());
        }
    }


}
