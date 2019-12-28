package net.haesleinhuepf.clijx.advancedfilters;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Mean3DSphereTest {
    @Test
    public void mean3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Mean 3D...", "x=1 y=1 z=1");

        // do operation with CLIJ
        ClearCLImage inputCL = clijx.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clijx.create(inputCL);

        clijx.mean3DSphere(inputCL, outputCL, 1, 1, 1);

        ImagePlus result = clijx.convert(outputCL, ImagePlus.class);

        // ignore edges and first and last slice
        reference.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        result.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        reference = new Duplicator().run(reference, 2, result.getNSlices() - 2);
        result = new Duplicator().run(result, 2, result.getNSlices() - 2);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.1));
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void mean3d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Mean 3D...", "x=1 y=1 z=1");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clijx.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clijx.create(inputCL);

        //Kernels.copy(clij, inputCL, outputCL);

        clijx.mean3DSphere(inputCL, outputCL, 1, 1, 1);
        //Kernels.meanSphere(clij, inputCL, outputCL, 3, 3, 3);

        //if (true) return;
        ImagePlus result = clijx.convert(outputCL, ImagePlus.class);

        // ignore edges and first and last slice
        reference.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        result.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        reference = new Duplicator().run(reference, 2, result.getNSlices() - 2);
        result = new Duplicator().run(result, 2, result.getNSlices() - 2);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
        IJ.exit();
        clijx.clear();
    }

}