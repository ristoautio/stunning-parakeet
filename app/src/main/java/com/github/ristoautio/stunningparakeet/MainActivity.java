package com.github.ristoautio.stunningparakeet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDDocumentInformation;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

//import org.apache.pdfbox.pdmodel.PDDocument;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @ViewById
    TextView tvHello;

    @ViewById
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void bindActionBar() {
        Log.d(TAG, "bindActionBar: afterviews");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if(intent.getAction().compareTo(Intent.ACTION_VIEW) == 0){

            Log.d(TAG, "action: " + intent.getAction());
            Log.d(TAG, "dataString: " + intent.getDataString());
            Log.d(TAG, "mimetype: " + intent.getType());

            // TODO refactor
            String[] permissions = {
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE"
            };
            int requestCode = 200;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, requestCode);
            }


            if (intent.getType().contentEquals("application/pdf")) {
                try {
                    parsePdf(intent);
                } catch (Exception e) {
                    Log.d(TAG, "bindActionBar: error parsing");
                }
            } else {
                Log.d(TAG, "bindActionBar: not a pdf");
            }
        }

    }

    private void parsePdf(Intent intent) throws URISyntaxException, IOException {
        Log.d(TAG, "bindActionBar: open das file at: " + intent.getDataString());
        File file = new File(new URI(intent.getDataString()));
        Log.d(TAG, "bindActionBar: found file " + file.getName());
        PDFBoxResourceLoader.init(getApplicationContext());

        Log.d(TAG, "bindActionBar: load document");
        PDDocument document = PDDocument.load(file);
        PDDocumentInformation info = document.getDocumentInformation();
        Log.d(TAG, "bindActionBar: " + info.getAuthor());

        Set<String> keys = info.getMetadataKeys();
        if (!keys.isEmpty()) {
            Log.d(TAG, "has metakeys");
            for (String key : keys) {
                Log.d(TAG, key + " -- " + (String) info.getPropertyStringValue(key));
            }
        }
    }

    @Click(R.id.fab)
    void onFabClick(View view) {
        Log.d(TAG, "onFabClick: click");
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        tvHello.setText("testing");
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
}
