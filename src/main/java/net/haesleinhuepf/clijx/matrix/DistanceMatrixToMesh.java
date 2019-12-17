package net.haesleinhuepf.clijx.matrix;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         December 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_distanceMatrixToMesh")
public class DistanceMatrixToMesh extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image pointlist, Image distance_matrix, Image mesh_destination, Number maximumDistance";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer pointlist = (ClearCLBuffer) args[0];
        ClearCLBuffer touch_matrix = (ClearCLBuffer) args[1];
        ClearCLBuffer mesh = (ClearCLBuffer) args[2];
        Float distanceThreshold = asFloat(args[3]);

        return distanceMatrixToMesh(getCLIJx(), pointlist, touch_matrix, mesh, distanceThreshold);
    }

    public static boolean distanceMatrixToMesh(CLIJx clijx, ClearCLBuffer pointlist, ClearCLBuffer distance_matrix, ClearCLBuffer mesh, Float distanceThreshold) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_pointlist", pointlist);
        parameters.put("src_distance_matrix", distance_matrix);
        parameters.put("dst_mesh", mesh);
        parameters.put("distance_threshold", distanceThreshold);

        long[] dimensions = {distance_matrix.getDimensions()[0], 1, 1};
        clijx.execute(DistanceMatrixToMesh.class, "distance_matrix_to_mesh_3d_x.cl", "distance_matrix_to_mesh_3d", dimensions, dimensions, parameters);
        return true;
    }

    @Override
    public String getDescription() {
        return "Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a distance matrix of " +
                "size n*n to draw lines from all points to points if the corresponding pixel in the distance matrix is " +
                "smaller than a given distance threshold.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
