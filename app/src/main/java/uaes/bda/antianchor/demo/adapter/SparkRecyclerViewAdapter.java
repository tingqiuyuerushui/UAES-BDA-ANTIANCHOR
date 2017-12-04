package uaes.bda.antianchor.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import uaes.bda.antianchor.demo.R;

/**
 * Created by gang.cao on 11/6/2017.
 */

public class SparkRecyclerViewAdapter extends RecyclerView.Adapter<SparkRecyclerViewAdapter.MyViewHolder>{

    private final ArrayList<String> mList;
    private final Context mCxt;
    @BindView(R.id.sparking_item_name)
    TextView sparkingItemName;
    @BindView(R.id.sparking_item_img)
    ImageView sparkingItemImg;
    @BindView(R.id.sparking_item_healthy)
    TextView sparkingItemHealthy;
    @BindView(R.id.speed_view)
    LinearLayout speedView;

    public SparkRecyclerViewAdapter(ArrayList<String> list, Context context) {
        this.mList = list;
        this.mCxt =context;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.sparkrecyclerview_item, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.healthy.setText(mList.get(position));
        holder.name.setText(mList.get(position));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView healthy,name;
        ImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.sparking_item_name);
            img = (ImageView) itemView.findViewById(R.id.sparking_item_img);
            healthy = (TextView) itemView.findViewById(R.id.sparking_item_healthy);


        }
    }
}
