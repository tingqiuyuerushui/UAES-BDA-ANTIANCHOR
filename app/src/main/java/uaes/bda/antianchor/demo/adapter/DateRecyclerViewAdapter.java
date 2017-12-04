package uaes.bda.antianchor.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.myInterface.MyItemOnClickListener;

/**
 * Created by lun.zhang on 11/2/2017.
 */

public class DateRecyclerViewAdapter extends RecyclerView.Adapter<DateRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<String> listDate;
    private MyItemOnClickListener mMyItemOnClickListener;
    public static List<Boolean> isClicks;
    public DateRecyclerViewAdapter(Context context, ArrayList<String> listDate) {
        mInflater = LayoutInflater.from(context);
        this.listDate = listDate;
        isClicks = new ArrayList<>();
        for(int i = 0;i<listDate.size();i++){
            isClicks.add(false);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvDate.setText(listDate.get(position));
        if (isClicks.get(position)){
            holder.tvDate.setBackgroundResource(R.drawable.tv_date_checked_shape);
        }else{
            holder.tvDate.setBackgroundResource(R.drawable.tv_date_no_checked_shape);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvDate;
        MyItemOnClickListener mListener;
        public ViewHolder(View itemView,MyItemOnClickListener myItemOnClickListener) {
            super(itemView);
            this.mListener = myItemOnClickListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if(mListener!=null){
                mListener.onItemOnClick(view,getPosition());
                for(int i = 0; i <isClicks.size();i++){
                    isClicks.set(i,false);
                }
                isClicks.set(getPosition(),true);
                notifyDataSetChanged();

            }
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.redate_recyclerview_list_item,viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view,mMyItemOnClickListener);
        viewHolder.tvDate = view
                .findViewById(R.id.tv_date);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return listDate.size();
    }
    public void setItemOnClickListener(MyItemOnClickListener listener){
        this.mMyItemOnClickListener = listener;
    }
}
