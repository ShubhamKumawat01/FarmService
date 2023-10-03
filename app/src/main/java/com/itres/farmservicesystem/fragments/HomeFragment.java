
package com.itres.farmservicesystem.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itres.farmservicesystem.R;
import com.itres.farmservicesystem.activities.ShowAllActivity;
import com.itres.farmservicesystem.adapters.CategoryAdapter;
import com.itres.farmservicesystem.adapters.NewProductsAdapter;
import com.itres.farmservicesystem.adapters.PopularProductsAdapter;
import com.itres.farmservicesystem.models.CategoryModel;
import com.itres.farmservicesystem.models.NewProductsModel;
import com.itres.farmservicesystem.models.PopularProductsModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    TextView catShowAll,popularShowAll,newProductShowAll;

    LinearLayout linearLayout;
    ProgressDialog progressDialog;

    RecyclerView catRecyclerView,newProductRecyclerView,popularRecyclerView;
    //Category RecyclerView
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    // new product recycler view
    NewProductsAdapter newProductsAdapter;
    List<NewProductsModel> newProductsModelList;

    // popular product recycler view
    PopularProductsAdapter popularProductsAdapter;
    List<PopularProductsModel> popularProductsModelList;

    //fireStore cloud
    FirebaseFirestore db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate( R.layout.fragment_home, container, false );

        progressDialog= new ProgressDialog( getActivity() );
        catRecyclerView=root.findViewById( R.id.rec_category );
        newProductRecyclerView=root.findViewById( R.id.new_product_rec );
        popularRecyclerView=root.findViewById( R.id.popular_rec );
        //see all from xml
        catShowAll=root.findViewById( R.id.category_see_all );
        popularShowAll=root.findViewById( R.id.popular_see_all );
        newProductShowAll=root.findViewById( R.id.newProducts_see_all );

        catShowAll.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ShowAllActivity.class );
                startActivity( intent );
            }
        } );

        newProductShowAll.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ShowAllActivity.class );
                startActivity( intent );
            }
        } );

        popularShowAll.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ShowAllActivity.class );
                startActivity( intent );
            }
        } );


        db = FirebaseFirestore.getInstance();

        linearLayout=root.findViewById( R.id.home_layout );
        linearLayout.setVisibility( View.GONE );

        //image slider create
        ImageSlider imageSlider=root.findViewById( R.id.image_slider );
        List<SlideModel> slideModels=new ArrayList<>();

        slideModels.add( new SlideModel( R.drawable.banner1,"New Wheat Quality", ScaleTypes.CENTER_CROP ) );
        slideModels.add( new SlideModel( R.drawable.banner2,"Buy Soyabean Seeds ", ScaleTypes.CENTER_CROP ) );
        slideModels.add( new SlideModel( R.drawable.banner3,"Kabuli Chana", ScaleTypes.CENTER_CROP ) );

        imageSlider.setImageList( slideModels );

        //progress Dialog
        progressDialog.setTitle( "Welcome To My FarmService App" );
        progressDialog.setMessage( "Please Wait..." );
        progressDialog.setCanceledOnTouchOutside( false );
        progressDialog.show();

        //Category
        catRecyclerView.setLayoutManager( new LinearLayoutManager( getActivity(),RecyclerView.HORIZONTAL,false ) );
        categoryModelList=new ArrayList<>();
        categoryAdapter= new CategoryAdapter( getContext(),categoryModelList);
        catRecyclerView.setAdapter( categoryAdapter );

        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                CategoryModel categoryModel=document.toObject( CategoryModel.class );
                                categoryModelList.add( categoryModel );
                                categoryAdapter.notifyDataSetChanged();
                                linearLayout.setVisibility( View.VISIBLE );
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText( getActivity(), ""+task.getException(), Toast.LENGTH_SHORT ).show();
                        }
                    }
                });

        // new Products
        newProductRecyclerView.setLayoutManager( new LinearLayoutManager( getActivity(),RecyclerView.HORIZONTAL,false ) );
        newProductsModelList=new ArrayList<>();
        newProductsAdapter=new NewProductsAdapter(getContext(),newProductsModelList);
        newProductRecyclerView.setAdapter( newProductsAdapter);

        db.collection("NewProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                NewProductsModel newProductsModel=document.toObject( NewProductsModel.class);
                                newProductsModelList.add( newProductsModel );
                                newProductsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText( getActivity(), ""+task.getException(), Toast.LENGTH_SHORT ).show();
                        }
                    }
                });

        // Popular Products
        //popularRecyclerView.setLayoutManager( new LinearLayoutManager( getActivity(),RecyclerView.HORIZONTAL,false) );
        popularRecyclerView.setLayoutManager( new GridLayoutManager( getActivity(),2) );
        popularProductsModelList=new ArrayList<>();
        popularProductsAdapter=new PopularProductsAdapter(getContext(),popularProductsModelList);
        popularRecyclerView.setAdapter( popularProductsAdapter );

        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                PopularProductsModel popularProductsModel=document.toObject( PopularProductsModel.class );
                                popularProductsModelList.add( popularProductsModel );
                                popularProductsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText( getActivity(), ""+task.getException(), Toast.LENGTH_SHORT ).show();
                        }
                    }
                });

        return root;
    }
}