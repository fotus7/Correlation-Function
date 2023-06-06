package org.bsu.CorrelationFunction;

import java.util.Random;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.SurfaceBuilder;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.selectable.SelectableScatter;

public class SampleGeom {
    static double[][][] R;
    public SampleGeom() {
    }

    public static Shape surface() {
        Range range1 = new Range(0, R[R.length - 1][R[0].length - 1][1]);
        Range range2 = new Range(0, R[R.length - 1][R[0].length - 1][2]);
        return surface(range1, range2);
    }

    public static Shape surface(Range xRange, Range yRange) {
        return surface(xRange, yRange, 1F);
    }

    public static Shape surface(Range xRange, Range yRange, float alpha) {
        Mapper mapper = new Mapper() {
            public double f(double x, double y) {
                return R[(int)x][(int)y][0];
            }
        };
        int steps = 50;
        Shape surface = (new SurfaceBuilder()).orthonormal(new OrthonormalGrid(xRange, steps, yRange, steps), mapper);
        ColorMapper colorMapper = new ColorMapper(new ColorMapRainbow(), (double)surface.getBounds().getZmin(), (double)surface.getBounds().getZmax(), new Color(1.0F, 1.0F, 1.0F, alpha));
        surface.setColorMapper(colorMapper);
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.BLACK);
        surface.setWireframeWidth(1.0F);
        return surface;
    }

    public static Scatter scatter(int size, int width) {
        Coord3d[] points = new Coord3d[size];
        Color[] colors = new Color[size];
        Random r = new Random();
        r.setSeed(0L);

        for(int i = 0; i < size; ++i) {
            float x = r.nextFloat() - 0.5F;
            float y = r.nextFloat() - 0.5F;
            float z = r.nextFloat() - 0.5F;
            points[i] = new Coord3d(x, y, z);
            float a = 0.75F;
            colors[i] = new Color(x, y, z, a);
        }

        Scatter scatter = new Scatter(points, colors);
        scatter.setWidth((float)width);
        return scatter;
    }

    public static SelectableScatter generateSelectableScatter(int npt) {
        Coord3d[] points = new Coord3d[npt];
        Color[] colors = new Color[npt];
        Random rng = new Random();
        rng.setSeed(0L);

        for(int i = 0; i < npt; ++i) {
            colors[i] = new Color(0.0F, 0.2509804F, 0.32941177F);
            points[i] = new Coord3d(rng.nextFloat(), rng.nextFloat(), rng.nextFloat());
        }

        SelectableScatter dots = new SelectableScatter(points, colors);
        dots.setWidth(1.0F);
        dots.setHighlightColor(Color.YELLOW);
        return dots;
    }
}