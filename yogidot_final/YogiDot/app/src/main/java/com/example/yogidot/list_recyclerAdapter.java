package com.example.yogidot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class list_recyclerAdapter extends RecyclerView.Adapter<list_recyclerAdapter.ItemViewHolder>{
    private ArrayList<showlist> listData=new ArrayList<>();
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private ItemViewHolder holder;
    private int position;

    public list_recyclerAdapter(Context context, ArrayList<showlist> list){
        this.context = context;
        this.listData = list;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public list_recyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false);
        return new ItemViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull list_recyclerAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position),position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(showlist data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
}

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView textView2;
        private ImageButton url;
        private showlist data;
        private int position;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.store);
            textView2 = itemView.findViewById(R.id.address);
            url = itemView.findViewById(R.id.url_button);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        void onBind(final showlist _data, int position) {
            this.data = _data;
            this.position = position;
            String add_full=_data.getAddress();
            String [] add_arr = add_full.split("\\s");
            String sto=add_arr[add_arr.length-1];
            add_arr[add_arr.length-1]="";
            String add_f="";
            for(int i=1;i<add_arr.length-1;i++){
                add_f=add_f+add_arr[i]+' ';
            }
            textView1.setText(sto);
            textView2.setText(add_f);
            url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_data.getUrl()));
                    context.startActivity(intent);
                }
            });
        }
    }
}


