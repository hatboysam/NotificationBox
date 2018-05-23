package com.habosa.notificationbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class LicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        TextView textView = findViewById(R.id.text_licenses);

        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Licenses</h1>");
        sb.append("<h2>Icons</h2>");
        sb.append("Icons made by <a href=\"https://www.flaticon.com/authors/dinosoftlabs\" title=\"DinosoftLabs\">DinosoftLabs</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a>");
        sb.append("<h2>Libraries</h2>");
        sb.append("<pre>material-intro-screen</pre> used under the MIT License.");

        textView.setText(Html.fromHtml(sb.toString()));
    }
}
