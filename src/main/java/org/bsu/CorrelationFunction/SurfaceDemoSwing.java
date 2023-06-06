package org.bsu.CorrelationFunction;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.SwingChartFactory;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

public class SurfaceDemoSwing extends AbstractAnalysis {
    double[][][] R;
    public SurfaceDemoSwing() {
        super(new SwingChartFactory());
    }

    public void graph(double[][][] R) throws Exception {
        this.R = R;
        SampleGeom.R = R;
        SurfaceDemoSwing d = new SurfaceDemoSwing();
        AnalysisLauncher.open(d);
    }


    @Override
    public void init() {
        Shape surface = SampleGeom.surface();
        chart = new SwingChartFactory().newChart(Quality.Advanced());
        chart.add(surface);
    }
}
