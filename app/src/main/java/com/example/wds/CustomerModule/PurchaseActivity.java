package com.example.wds.CustomerModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.wds.Adapter.ProductAdapter;
import com.example.wds.Converter;
import com.example.wds.Model.ProductModel;
import com.example.wds.R;

import java.util.ArrayList;

public class PurchaseActivity extends AppCompatActivity implements ProductAdapter.CallBackUs, ProductAdapter.HomeCallBack {
    public static ArrayList<ProductModel> arrayList = new ArrayList<>();
    public static int cart_count = 0;
    ProductAdapter productAdapter;
    RecyclerView productRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        addProduct();

        productAdapter = new ProductAdapter(arrayList, this, this);
        productRecyclerView = (RecyclerView) findViewById(R.id.product_recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productRecyclerView.setAdapter(productAdapter);

    }


    private void addProduct()
    {
        ProductModel productModelTwenty = new ProductModel("20 litre :: Qr 20", "20", "20", R.drawable.water_twenty);
                    arrayList.add(productModelTwenty);
       /* ProductModel productModeloNeLittre = new ProductModel("1 litre :: Qr 10", "10", "10", R.drawable.water_oneltr);
                    arrayList.add(productModeloNeLittre);
        ProductModel productModelFiveMilli = new ProductModel("5 mili ::  Qr 5", "5", "10", R.drawable.waterfiveml);
                    arrayList.add(productModelFiveMilli);*/

    }

    @Override
    public void addCartItemView() {
        //addItemToCartMethod();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(PurchaseActivity.this, cart_count, R.drawable.shoppingcart));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.cart_action:
                if (cart_count < 1) {
                    Toast.makeText(this, "there is no item in cart", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(PurchaseActivity.this, CartActivity.class);
                    startActivity(intent);
                }
                break;
            default:
               // return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void updateCartCount(Context context) {
        invalidateOptionsMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();
       invalidateOptionsMenu();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ProductAdapter.productsArray.clear();
    }

}
