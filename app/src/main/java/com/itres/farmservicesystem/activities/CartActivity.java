package com.itres.farmservicesystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itres.farmservicesystem.R;
import com.itres.farmservicesystem.adapters.MyCartAdapter;
import com.itres.farmservicesystem.models.MyCartModel;
import com.itres.farmservicesystem.models.ShowAllModel;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    int overAllTotalAmount;
    TextView overAllAmount;

    Toolbar toolbar;
    RecyclerView recyclerView;
    List<MyCartModel> myCartModelList;
    MyCartAdapter myCartAdapter;

    //Button buyNow;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_cart );

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        toolbar=findViewById( R.id.my_cart_toolbar );
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        //arrow navigation
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );

        //get data from my cart adapter
        LocalBroadcastManager.getInstance( this )
                .registerReceiver( mMessageReceiver,new IntentFilter("MyTotalAmount") );

        overAllAmount=findViewById( R.id.textView3 );
        recyclerView=findViewById( R.id.cart_rec );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        myCartModelList=new ArrayList<>();
        myCartAdapter= new MyCartAdapter(this,myCartModelList);
        recyclerView.setAdapter( myCartAdapter );

       /* buyNow=findViewById( R.id.buy_now );
        //BuyNow
        buyNow.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(CartActivity.this,AddressActivity.class) );
            }
        } );
*/


        firestore.collection( "AddToCart" ).document(auth.getCurrentUser().getUid())
                .collection( "User" ).get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc: task.getResult().getDocuments()){
                        MyCartModel myCartModel=doc.toObject( MyCartModel.class );
                        myCartModelList.add( myCartModel );
                        myCartAdapter.notifyDataSetChanged();
                    }
                }
            }
        } );
    }
    public BroadcastReceiver mMessageReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int totalBill=intent.getIntExtra( "totalAmount",0 );
            overAllAmount.setText("Total Amount : Rs "+totalBill );
        }
    };
}