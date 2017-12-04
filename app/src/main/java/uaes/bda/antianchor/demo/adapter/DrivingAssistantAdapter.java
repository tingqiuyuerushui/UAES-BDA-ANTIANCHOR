package uaes.bda.antianchor.demo.adapter;

import android.content.Context;
import android.content.Intent;
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
import uaes.bda.antianchor.demo.activity.DoNoteActivity;
import uaes.bda.antianchor.demo.activity.OilHelperActivity;
import uaes.bda.antianchor.demo.entity.DrivingAssistantEntity;
import uaes.bda.antianchor.demo.fragment.DrivingAssistantFragment;

/**
 * Created by lun.zhang on 4/23/2017.
 */

public class DrivingAssistantAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater inflater;
    private ArrayList<DrivingAssistantEntity> listEntity;

    public DrivingAssistantAdapter(Context context, ArrayList<DrivingAssistantEntity> listEntity) {
        this.context = context;
        this.listEntity = listEntity;

    }

    @Override
    public int getCount() {
        return listEntity.size();
    }

    @Override
    public DrivingAssistantEntity getItem(int position) {
        return listEntity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.listview_driving_assistant_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (DrivingAssistantFragment.currentPosition == position && holder.llTextView.getVisibility() == View.GONE) {
            holder.llTextView.setVisibility(View.VISIBLE);
            switch (position) {
                case 0:
                    holder.tvFirst.setText("油品状态：" + listEntity.get(position).getItemEntity().getFuleQuality());
                    holder.tvSecond.setText("剩余里程：" + listEntity.get(position).getItemEntity().getRemainMileageMaxMin());
                    holder.tvThird.setText("剩余燃油：" + listEntity.get(position).getItemEntity().getRemainFuelPer());
                    holder.tvFourth.setText("经济性排名：" + listEntity.get(position).getItemEntity().getEcoRanking());
                    holder.tvDetail.setTextColor(context.getResources().getColor(R.color.red));
                    holder.tvDoNote.setTextColor(context.getResources().getColor(R.color.red));
                    break;
            }
        } else if (holder.llTextView.getVisibility() == View.VISIBLE) {
            holder.llTextView.setVisibility(View.GONE);
        } else {
            holder.llTextView.setVisibility(View.GONE);
        }
        holder.imgIcon.setImageResource(listEntity.get(position).getImgResource());
        holder.tvText.setText(listEntity.get(position).getItemText());
        holder.tvDoNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, DoNoteActivity.class);
                context.startActivity(intent);
            }
        });
        holder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "点击了", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                switch (position) {
                    //加油助手
                    case 0:
                        intent.setClass(context, OilHelperActivity.class);
//                        intent.setClass(context, Fighting_helper_child_Activity.class);
                        context.startActivity(intent);
                        break;
                }

            }
        });

        return view;
    }



    static class ViewHolder {
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.tv_text)
        TextView tvText;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;
        @BindView(R.id.tv_first)
        TextView tvFirst;
        @BindView(R.id.tv_second)
        TextView tvSecond;
        @BindView(R.id.tv_third)
        TextView tvThird;
        @BindView(R.id.tv_fourth)
        TextView tvFourth;
        @BindView(R.id.tv_do_note)
        TextView tvDoNote;
        @BindView(R.id.tv_detail)
        TextView tvDetail;
        @BindView(R.id.ll_text_view)
        LinearLayout llTextView;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
