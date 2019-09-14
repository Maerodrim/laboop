package ru.ssau.tk.sergunin.lab.functions;

import java.util.Arrays;
public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Insertable{
    private double[] xValues;
    private double[] yValues;
    private int count;

    ArrayTabulatedFunction(double[] xValues, double[] yValues) {
        this.count = xValues.length;
        this.xValues = Arrays.copyOf(xValues, count);
        this.yValues = Arrays.copyOf(yValues, count);
    }

    ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count){
        this.count = count;
        if (xFrom > xTo) {
            xFrom = xFrom - xTo;
            xTo = xFrom + xTo;
            xFrom = -xFrom + xTo;
        }
        xValues = new double[count];
        yValues = new double[count];
        double buff = xFrom;
        double step = (xTo - xFrom) / (count-1);
        if (xFrom != xTo) {
            for (int i = 0; i < count; i++) {
                xValues[i] = buff;
                yValues[i] = source.apply(buff);
                buff += step;
            }
        }
        else {
            double funcXFrom = source.apply(xFrom);
            for (int i = 0; i < count; i++) {
                xValues[i] = xFrom;
                yValues[i] = funcXFrom;
                buff += step;
            }
        }
    }

    @Override
    int floorIndexOfX(double x) {
        int i;
        if (x < leftBound()) {
            return 0;
        }
        for(i = 0; i < count; i++) {
            if (xValues[i] > x){
                return i-1;
            }
        }
        return count;
    }

    @Override
    double extrapolateLeft(double x) {
        return interpolate(x, xValues[0], xValues[1], yValues[0], yValues[1]);
    }

    @Override
    double extrapolateRight(double x) {
        return interpolate(x, xValues[count - 2], xValues[count - 1], yValues[count - 2], yValues[count - 1]);
    }

    @Override
    double interpolate(double x, int floorIndex) {
        return interpolate(x, xValues[floorIndex], xValues[floorIndex + 1], yValues[floorIndex], yValues[floorIndex + 1]);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double getX(int index) {
        return xValues[index];
    }

    @Override
    public double getY(int index) {
        return yValues[index];
    }

    @Override
    public void setY(int index, double value) {
        yValues[index] = value;
    }

    @Override
    public int indexOfX(double x) {
        int i;
        for(i = 0; i < count; i++) {
            if (xValues[i] == x){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        int i;
        for(i = 0; i < count; i++) {
            if (yValues[i] == y){
                return i;
            }
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return xValues[0];
    }

    @Override
    public double rightBound() {
        return xValues[count - 1];
    }

    @Override
    public void insert(double x, double y) {
        if (indexOfX(x) != -1) {
            setY(indexOfX(x), y);
        } else {
            int index = floorIndexOfX(x);
            double[] xValues2=new double[count+1];
            double[] yValues2=new double[count+1];
            if (index == 0) {
                xValues2[0]=x;
                yValues2[0]=y;
                System.arraycopy(xValues, 0, xValues2, 1, count);
                System.arraycopy(yValues, 0, yValues2, 1, count);
                count++;
            } else if (index == count) {
                xValues2 = Arrays.copyOf(xValues, count);
                yValues2 = Arrays.copyOf(yValues, count);
                xValues2[count+1]=x;
                yValues2[count+1]=y;

                count++;
            } else {
                System.arraycopy(xValues, 0, xValues2, 0, index-1);
                System.arraycopy(yValues, 0, yValues2, 0, index-1);
                xValues2[index]=x;
                yValues2[index]=y;
                System.arraycopy(xValues, index, xValues2, index+1, (count-index));
                System.arraycopy(yValues, index, yValues2, index+1, (count-index));
                count++;
            }
            xValues=xValues2;
            yValues=yValues2;
        }
    }
}
