package uaes.bda.antianchor.demo.entity;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.NumberFormat;

/**
 * Created by lun.zhang on 10/23/2017.
 */

public class MyAxisValueFormatter implements IAxisValueFormatter {
    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return
     */
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
//        return MyUtil.getDateToString(Long.parseLong(value+""),"yyyy/MM/dd");
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
//        return MyUtil.getDateToString(Long.parseLong(nf.format(value)),"yyyy/MM/dd");
        return "2017-08-01";
    }

    /**
     * Returns the number of decimal digits this formatter uses or -1, if unspecified.
     *
     * @return
     */
    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
