package net.haesleinhuepf.clijx.matrix;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         August 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_multiplyMatrix")
public class MultiplyMatrix extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = multiplyMatrix(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
        releaseBuffers(args);
        return result;
    }

    public static boolean multiplyMatrix(CLIJx clijx, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer output) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", input1);
        parameters.put("src2", input2);
        parameters.put("dst_matrix", output);

        clijx.activateSizeIndependentKernelCompilation();
        clijx.execute(MultiplyMatrix.class, "multipl_matrix_x.cl", "multiply_matrix", output.getDimensions(), output.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image matrix1, Image matrix2, Image matrix_destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        ClearCLBuffer input1 = (ClearCLBuffer) args[0];
        ClearCLBuffer input2 = (ClearCLBuffer) args[1];
        return clij.createCLBuffer(new long[]{input2.getWidth(), input1.getHeight()}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Multiplies two matrices with each other.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
