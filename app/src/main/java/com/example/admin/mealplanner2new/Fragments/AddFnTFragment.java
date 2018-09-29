package com.example.admin.mealplanner2new.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.mealplanner2new.Common.PrefMeal;
import com.example.admin.mealplanner2new.Common.RetrofitClient;
import com.example.admin.mealplanner2new.Common.SessionManager;
import com.example.admin.mealplanner2new.Models.Ingredient;
import com.example.admin.mealplanner2new.Models.ResRecipeItem;
import com.example.admin.mealplanner2new.R;
import com.example.admin.mealplanner2new.Views.AddTodayMealActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class AddFnTFragment extends Fragment {

    private static final String BASE_URL = "http://code-fuel.in/healthbotics/api/auth/";
    GetRecipesAPI getRecipesAPI;

    SessionManager sessionManager;
    PrefMeal prefMeal;

    View view_main;
    TextView textView_noFnT;
    RecyclerView recyclerView_FnT;
    ProgressBar progressBar;
    Button button_next;

    RecAdapter recAdapter;
    private ArrayList<ResRecipeItem> resRecipeItemArrayList;
    private ArrayList<ResRecipeItem> selectedItemReciepList;

    private Ingredient ingredient;

    public static AddFnTFragment newInstance(int page, String title) {
        AddFnTFragment fragmentAddFnT = new AddFnTFragment();
        Bundle args = new Bundle();
        args.putInt("5", page);
        args.putString("Add Fruit and Nuts", title);
        fragmentAddFnT.setArguments(args);
        return fragmentAddFnT;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_main = inflater.inflate(R.layout.fragment_add_fnt, container, false);

        resRecipeItemArrayList = new ArrayList<>();
        selectedItemReciepList = new ArrayList<>();

        getRecipesAPI = getGetRecipesAPIService(BASE_URL);
        sessionManager = new SessionManager(getContext());
        prefMeal = new PrefMeal(getContext());

        textView_noFnT = view_main.findViewById(R.id.addFnT_tv_noRecipes);
        progressBar = view_main.findViewById(R.id.addFnT_progressBar);
        recyclerView_FnT = view_main.findViewById(R.id.addFnT_recView_recipes);
        button_next = view_main.findViewById(R.id.addFnT_btn_next);

        //setAllRecipes();

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedItemReciepList.size() > 0) {

                    if (selectedItemReciepList.size() > 0) {
                        ingredient.setfNtList(selectedItemReciepList);
                    }
                    ((AddTodayMealActivity) getActivity()).setCurrentItem(5, true);
                } else {
                    Toast.makeText(getActivity(), "Select any recipe", Toast.LENGTH_LONG).show();
                }


            }
        });

        return view_main;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ingredient = ((AddTodayMealActivity) (context)).ingredient;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ingredient = ((AddTodayMealActivity) (activity)).ingredient;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            setAllRecipes();
        }
    }

    private void setAllRecipes() {

        progressBar.setVisibility(View.VISIBLE);

        String token = sessionManager.getAccessToken();
        String category = "2";
        String type = prefMeal.getMealType();

        getRecipesAPI.get_recipes("Bearer " + token, category, type).enqueue(new Callback<List<ResRecipeItem>>() {
            @Override
            public void onResponse(Call<List<ResRecipeItem>> call, Response<List<ResRecipeItem>> response) {

                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        resRecipeItemArrayList = (ArrayList<ResRecipeItem>) response.body();


                        if (selectedItemReciepList.size() > 0 && resRecipeItemArrayList.size() > 0) {

                            for (int i = 0; i < selectedItemReciepList.size(); i++) {

                                for (int j = 0; j < resRecipeItemArrayList.size(); j++) {

                                    if (selectedItemReciepList.get(i).getId().equals(
                                            resRecipeItemArrayList.get(j).getId()
                                    )) {

                                        resRecipeItemArrayList.get(j).setSelected(true);

                                    }

                                }
                            }


                        }


                        recAdapter = new RecAdapter(resRecipeItemArrayList);

                        recyclerView_FnT.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                        if (getActivity() != null)
                            recyclerView_FnT.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

                        recyclerView_FnT.setAdapter(recAdapter);

                        if (recAdapter.getItemCount() > 0) {
                            textView_noFnT.setVisibility(View.GONE);
                        }

                    } else {
                        //response body is null

                    }

                } else {
                    //response not successful
                }

            }

            @Override
            public void onFailure(Call<List<ResRecipeItem>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }


//---------------------------------------- APIs --------------------------------------------------//

    GetRecipesAPI getGetRecipesAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetRecipesAPI.class);
    }

    interface GetRecipesAPI {
        @Headers("X-Requested-With:XMLHttpRequest")
        @POST("getRecipesByCategory")
        @FormUrlEncoded
        Call<List<ResRecipeItem>> get_recipes(@Header("Authorization") String token,
                                              @Field("category") String category,
                                              @Field("type") String type
        );
    }


//------------------------------------ Adapter Class ---------------------------------------------//

    public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {

        String BASE_IMG_URL = "http://code-fuel.in/healthbotics/storage/app/public/thumb/";
        private ArrayList<ResRecipeItem> mDataSet;


        RecAdapter(ArrayList<ResRecipeItem> mDataSet) {
            this.mDataSet = mDataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_add_recipe, viewGroup, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            if (mDataSet.get(position).isSelected()) {
                viewHolder.imageView_add.setImageResource(R.drawable.ic_red_remove);

            } else {
                viewHolder.imageView_add.setImageResource(R.drawable.ic_green_add);
            }

            viewHolder.getTextView_name().setText(mDataSet.get(position).getName());

            viewHolder.getImageView_recipeImage().setBackgroundColor(getResources().getColor(R.color.font_grey));

            String img_uri = BASE_IMG_URL + (mDataSet.get(position).getThumb());
            Glide.with(getContext()).load(img_uri).into(viewHolder.imageView_recipeImage);

            viewHolder.tv_protein.setText(mDataSet.get(position).getProteins());
            viewHolder.tv_fats.setText(mDataSet.get(position).getFats());
            viewHolder.tv_carbs.setText(mDataSet.get(position).getCarbs());
            viewHolder.tv_caloreis.setText(mDataSet.get(position).getCalories());

        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView textView_name;
            private final ImageView imageView_recipeImage, imageView_add;
            private final TextView tv_protein, tv_fats, tv_carbs, tv_caloreis;

            ViewHolder(View v) {
                super(v);

                textView_name = (TextView) v.findViewById(R.id.row_addRecipe_tv_name);
                imageView_recipeImage = (ImageView) v.findViewById(R.id.row_addRecipe_iv_image);
                imageView_add = (ImageView) v.findViewById(R.id.row_addRecipe_iv_addBtn);
                tv_protein = (TextView) v.findViewById(R.id.row_addRecipe_tv_proteins);
                tv_fats = (TextView) v.findViewById(R.id.row_addRecipe_tv_fats);
                tv_carbs = (TextView) v.findViewById(R.id.row_addRecipe_tv_carbs);
                tv_caloreis = (TextView) v.findViewById(R.id.row_addRecipe_tv_calories);


                imageView_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectedItemReciepList.clear();

                        if (mDataSet.get(getAdapterPosition()).isSelected()) {
                            mDataSet.get(getAdapterPosition()).setSelected(false);
                            imageView_add.setImageResource(R.drawable.ic_green_add);
                        } else {
                            mDataSet.get(getAdapterPosition()).setSelected(true);
                            imageView_add.setImageResource(R.drawable.ic_red_remove);
                        }

                        for (int i = 0; i < resRecipeItemArrayList.size(); i++) {

                            if (resRecipeItemArrayList.get(i).isSelected()) {
                                selectedItemReciepList.add(resRecipeItemArrayList.get(i));
                            }

                        }

                    }
                });
            }

            TextView getTextView_name() {
                return textView_name;
            }

            ImageView getImageView_recipeImage() {
                return imageView_recipeImage;
            }

            ImageView getImageView_add() {
                return imageView_add;
            }

        }

    }

}
