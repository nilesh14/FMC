package v1.pyroteck.com.pyroteck.data;

import java.io.Serializable;

/**
 * Created by Nilesh on 02/05/15.
 */
public class SizeData implements Serializable{

    String size,customeSizes;
    double metricCM;

    public String getCustomeSizes() {
        return customeSizes;
    }

    public void setCustomeSizes(String customeSizes) {
        this.customeSizes = customeSizes;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getMetricCM() {
        return metricCM;
    }

    public void setMetricCM(double metricCM) {
        this.metricCM = metricCM;
    }
}
