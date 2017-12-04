package uaes.bda.antianchor.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.entity.MaintenanceAssistantEntity;

/**
 * Created by lun.zhang on 4/23/2017.
 */

public class MaintenanceAssistantAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MaintenanceAssistantEntity> listEntity;

    public MaintenanceAssistantAdapter(Context context, ArrayList<MaintenanceAssistantEntity> listEntity) {
        this.context = context;
        this.listEntity = listEntity;


    }

    @Override
    public int getCount() {
        return listEntity.size();
    }

    @Override
    public MaintenanceAssistantEntity getItem(int position) {
        return listEntity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        MaintenanceAssistantAdapter.ViewHolder holder = null;
        if (view == null) {
            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.listview_maintenance_item, null);
            holder = new MaintenanceAssistantAdapter.ViewHolder(view);
            view.setTag(holder);

        } else {
            holder = (MaintenanceAssistantAdapter.ViewHolder) view.getTag();
        }
        holder.imgIcon.setImageResource(listEntity.get(position).getImgResource());
        holder.tvText.setText(listEntity.get(position).getItemText());
        holder.tvStatus.setText(listEntity.get(position).getItemTextStatus());
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.tv_text)
        TextView tvText;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
