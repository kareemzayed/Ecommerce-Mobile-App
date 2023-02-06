package com.example.e_commerce.fragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.e_commerce.Adapter.ProductAdabter;
import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.Model.ProductModel;
import com.example.e_commerce.R;
import com.example.e_commerce.activity.ScanCodeActivity;


import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.getIntent;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    private ListView listView;
    private ArrayList<ProductModel> products = new ArrayList<>();
    private ProductAdabter adabter;
    private MyDatabase database;

    private EditText search_keyword;
    private ImageButton voiceSearch,imageSearch;
    protected static final int RESULT_SPEECH = 1;

    private Spinner categories;
    ArrayAdapter adapter_cate;
    ScanCodeActivity scanCodeActivity;


    public Home() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = view.findViewById(R.id.list_products);
        search_keyword=view.findViewById(R.id.search_keyword);
        categories=view.findViewById(R.id.cate_search);
        voiceSearch=view.findViewById(R.id.voicesearch);
        imageSearch=view.findViewById(R.id.imagesearch);
        scanCodeActivity=new ScanCodeActivity();

        voiceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
            try {
                startActivityForResult(intent,RESULT_SPEECH);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(),"your device doesn't support spech to text",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            }
        });
        imageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),ScanCodeActivity.class);
                startActivity(intent);
            }
        });

        database = new MyDatabase(getActivity());

        getAllcategory();
        getAllProduct();

        if (adabter==null) {
            adabter = new ProductAdabter(getContext(), 0, products);
            listView.setAdapter(adabter);
        }


        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(categories.getSelectedItem().toString().equals("All")) {

                    getAllProduct();
                    adabter=new ProductAdabter(getContext(),0,products);
                    listView.setAdapter(adabter);
                }
                else

                searchByCategory(categories.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().equals(""))
                    filter(s.toString());
                else
                {
                    getAllProduct();
                    adabter=new ProductAdabter(getContext(),0,products);
                    listView.setAdapter(adabter);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        search_keyword.setText(scanCodeActivity.scanName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                ArrayList<String> text=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                search_keyword.setText(text.get(0));
                break;
        }
    }

    private void getAllProduct() {
        Cursor cursor = database.getProducts();

        products.clear();
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                ProductModel productModel=new ProductModel(getContext(),Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)),
                        cursor.getString(1), cursor.getBlob(2),
                        Double.parseDouble(cursor.getString(3)));
                productModel.setPro_id(Integer.parseInt(cursor.getString(0)));
                products.add(productModel);
                cursor.moveToNext();
            }
        }
    }
    private void getAllcategory(){

        List<String> cate=new ArrayList<>();
        cate.add("All");
        Cursor cursor=database.getCategory();
        if (cursor!=null){
            while (!cursor.isAfterLast()){
                cate.add(cursor.getString(1));
                cursor.moveToNext();
            }
            adapter_cate=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,cate);
            categories.setAdapter(adapter_cate);
        }
    }

    private void searchByCategory(String name) {

        ArrayList<ProductModel>filterlist=new ArrayList<>();


        String cat_id = database.getCatId(name);
        Cursor cursor = database.getProductbyCategor(cat_id);
        if (cursor != null) {
            while (!cursor.isAfterLast()) {

                ProductModel productModel=new ProductModel(getContext(),Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)),
                        cursor.getString(1), cursor.getBlob(2),
                        Double.parseDouble(cursor.getString(3)));
                productModel.setPro_id(Integer.parseInt(cursor.getString(0)));
                filterlist.add(productModel);

                cursor.moveToNext();
            }


               adabter.filter(filterlist);

            if (filterlist.size()==0)
                Toast.makeText(getActivity(), "No products to show", Toast.LENGTH_SHORT).show();
        }
    }

    private void filter(String text) {
        ArrayList<ProductModel> filteredList = new ArrayList<>();

        for (ProductModel item : products) {
            if (item.getProName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adabter.filter(filteredList);
    }


}

