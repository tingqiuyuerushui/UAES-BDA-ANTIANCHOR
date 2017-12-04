package uaes.bda.antianchor.demo.customsizeview;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.entity.SingleRefuelingRecordEntity;

/**
 * Created by lun.zhang on 10/19/2017.
 */

public class LinearChartMarkerView extends MarkerView {
    private TextView tvFuelStation;
    private TextView tvAccountFuel;
    private TextView tvFuelDate;
    private SingleRefuelingRecordEntity singleRefuelingRecordEntity;
    public LinearChartMarkerView(Context context, int layoutResource,SingleRefuelingRecordEntity singleRefuelingRecordEntity) {
        super(context, layoutResource);

        // find your layout components
        tvFuelStation = findViewById(R.id.tv_fuel_station);
        tvAccountFuel = findViewById(R.id.tv_account_fuel);
        tvFuelDate = findViewById(R.id.tv_fuel_date);
        this.singleRefuelingRecordEntity = singleRefuelingRecordEntity;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

//        tvContent.setText("" + e.getY());

        // this will perform necessary layouting
        int i = (int)(e.getX());
        tvAccountFuel.setText("加油量："+singleRefuelingRecordEntity.getData().get(i).getFuelFillingVolume()+"L");
        tvFuelDate.setText("日期："+singleRefuelingRecordEntity.getData().get(i).getFuelFillingDate());
        tvFuelStation.setText(singleRefuelingRecordEntity.getData().get(i).getStationName());
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}
