package net.haesleinhuepf.clijx.temp;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clijx.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_detectMinimaBox")
public class DetectMinimaBox extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return detectMinimaBox(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]));
    }

    public static boolean detectMinimaBox(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Integer radius) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (detectOptima)");
        }
        clijx.execute(DetectMaximaBox.class, "detect_minima_" + src.getDimension() + "d_x.cl", "detect_minima_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radius";
    }

    @Override
    public String getDescription() {
        return "Detects local minima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if\n" +
                "there is no other pixel in a given radius which has a lower intensity, and to 0 otherwise.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
