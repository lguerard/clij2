package net.haesleinhuepf.clijx.matrix;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.advancedfilters.ConnectedComponentsLabeling;
import net.haesleinhuepf.clijx.advancedfilters.CountNonZeroPixels;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_labelledSpotsToPointList")
public class LabelledSpotsToPointList extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input_labelled_spots, Image destination_pointlist";
    }

    @Override
    public boolean executeCL() {
        return labelledSpotsToPointList(getCLIJx(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
    }

    public static boolean labelledSpotsToPointList(CLIJx clijx, ClearCLBuffer input_labelmap, ClearCLBuffer output) {

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src", input_labelmap);
        parameters.put("dst_point_list", output);

        long[] globalSizes = input_labelmap.getDimensions();
        clijx.activateSizeIndependentKernelCompilation();
        clijx.execute(LabelledSpotsToPointList.class, "spots_to_point_list_" + input_labelmap.getDimension() + "d_x.cl", "spots_to_point_list_" + input_labelmap.getDimension() + "d", globalSizes, globalSizes, parameters);
        input_labelmap.close();
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        long numberOfSpots = (long) CountNonZeroPixels.countNonZeroPixels(getCLIJx(), input);

        return clij.create(new long[]{numberOfSpots, input.getDimension()}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Transforms a labelmap of spots (single pixels with values 1, 2, ..., n for n spots) as resulting from connected components analysis in an image where every column contains d \n" +
                "pixels (with d = dimensionality of the original image) with the coordinates of the maxima/minima.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
