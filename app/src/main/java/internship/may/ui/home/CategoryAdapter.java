package internship.may.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import internship.may.R;
import internship.may.SubCategoryActivity;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {

    Context context;
    ArrayList<CategoryList> arrayList;
    String sType;

    public CategoryAdapter(Context context, ArrayList<CategoryList> arrayList,String sType) {
        this.context = context;
        this.arrayList = arrayList;
        this.sType = sType;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(sType.equalsIgnoreCase("Category")){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category, parent, false);
            return new MyHolder(view);
        }
        else if(sType.equalsIgnoreCase("SubCategory")){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sub_category, parent, false);
            return new MyHolder(view);
        }
        return null;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_category_name);
            image = itemView.findViewById(R.id.custom_category_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sType.equalsIgnoreCase("Category")) {
                    Intent intent = new Intent(context, SubCategoryActivity.class);
                    context.startActivity(intent);
                }
                else if(sType.equalsIgnoreCase("SubCategory")){

                }
                else{

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
