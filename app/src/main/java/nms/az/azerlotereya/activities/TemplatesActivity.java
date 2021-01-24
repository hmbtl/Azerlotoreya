package nms.az.azerlotereya.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.adapters.TemplateAdapter;

/**
 * Created by anar on 10/8/15.
 */
public class TemplatesActivity extends AppCompatActivity {

    private ListView listTemplates;
    private TemplateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);

        listTemplates = (ListView) findViewById(R.id.list_templates);

    }

}
