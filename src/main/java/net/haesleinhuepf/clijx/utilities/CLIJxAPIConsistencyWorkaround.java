package net.haesleinhuepf.clijx.utilities;

import com.sun.org.apache.regexp.internal.RE;
import ij.IJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCL;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.modules.*;
import net.haesleinhuepf.clijx.CLIJx;
import net.imglib2.ops.parse.token.Int;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * CLIJxAPIConsistencyWorkaround
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2019
 */
public class CLIJxAPIConsistencyWorkaround {

    public static boolean minimum3DBox(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY, Integer radiusZ ) {
        return Kernels.minimumBox(clij, input, output, radiusX, radiusY, radiusZ);
    }
    public static boolean minimum3DSphere(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY, Integer radiusZ ) {
        return Kernels.minimumSphere(clij, input, output, radiusX, radiusY, radiusZ);
    }
    public static boolean minimum2DBox(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY) {
        return Kernels.minimumBox(clij, input, output, radiusX, radiusY, 0);
    }
    public static boolean minimum2DSphere(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY ) {
        return Kernels.minimumSphere(clij, input, output, radiusX, radiusY, 0);
    }

    public static boolean maximum3DBox(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY, Integer radiusZ ) {
        return Kernels.maximumBox(clij, input, output, radiusX, radiusY, radiusZ);
    }
    public static boolean maximum3DSphere(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY, Integer radiusZ ) {
        return Kernels.maximumSphere(clij, input, output, radiusX, radiusY, radiusZ);
    }
    public static boolean maximum2DBox(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY) {
        return Kernels.maximumBox(clij, input, output, radiusX, radiusY, 0);
    }
    public static boolean maximum2DSphere(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY ) {
        return Kernels.maximumSphere(clij, input, output, radiusX, radiusY, 0);
    }

    public static boolean mean3DSphere(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY, Integer radiusZ ) {
        int kernelSizeX = radiusToKernelSize(radiusX);
        int kernelSizeY = radiusToKernelSize(radiusY);
        int kernelSizeZ = radiusToKernelSize(radiusZ);
        return Kernels.meanSphere(clij, input, output, kernelSizeX, kernelSizeY, kernelSizeZ);
    }
    public static boolean mean3DBox(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY, Integer radiusZ ) {
        return Kernels.meanBox(clij, input, output, radiusX, radiusY, radiusZ);
    }


    public static boolean mean2DSphere(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY) {
        int kernelSizeX = radiusToKernelSize(radiusX);
        int kernelSizeY = radiusToKernelSize(radiusY);
        return Kernels.meanSphere(clij, input, output, kernelSizeX, kernelSizeY);
    }
    public static boolean mean2DBox(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY) {
        return Kernels.meanBox(clij, input, output, radiusX, radiusY, 0);
    }


    public static boolean median2DSphere(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY){
        int kernelSizeX = radiusToKernelSize(radiusX);
        int kernelSizeY = radiusToKernelSize(radiusY);
        return Kernels.medianSphere(clij, input, output, kernelSizeX, kernelSizeY);
    }

    public static boolean median2DBox(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY) {
        int kernelSizeX = radiusToKernelSize(radiusX);
        int kernelSizeY = radiusToKernelSize(radiusY);
        return Kernels.medianBox(clij, input, output, kernelSizeX, kernelSizeY);
    }


    public static boolean median3DSphere(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY, Integer radiusZ){
        int kernelSizeX = radiusToKernelSize(radiusX);
        int kernelSizeY = radiusToKernelSize(radiusY);
        int kernelSizeZ = radiusToKernelSize(radiusZ);
        return Kernels.medianSphere(clij, input, output, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public static boolean median3DBox(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY, Integer radiusZ) {
        int kernelSizeX = radiusToKernelSize(radiusX);
        int kernelSizeY = radiusToKernelSize(radiusY);
        int kernelSizeZ = radiusToKernelSize(radiusZ);
        return Kernels.medianBox(clij, input, output, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public static boolean rotate3D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float angleX, Float angleY, Float angleZ, Boolean rotateAroundCenter) {
        Rotate3D rotate3D = new Rotate3D();
        rotate3D.setClij(clij);
        rotate3D.setArgs(new Object[]{input, output, angleX, angleY, angleZ, rotateAroundCenter});
        return rotate3D.executeCL();
    }

    public static boolean rotate2D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float angleX, Float angleY, Boolean rotateAroundCenter) {
        Rotate2D rotate2D = new Rotate2D();
        rotate2D.setClij(clij);
        rotate2D.setArgs(new Object[]{input, output, angleX, angleY, rotateAroundCenter});
        return rotate2D.executeCL();
    }

    public static boolean crop2D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer startX, Integer startY, Integer width, Integer height) {
        Crop2D crop2D = new Crop2D();
        crop2D.setClij(clij);
        crop2D.setArgs(new Object[]{input, output, startX, startY, width, height});
        return crop2D.executeCL();
    }

    public static boolean crop3D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer startX, Integer startY, Integer startZ, Integer width, Integer height, Integer depth) {
        Crop3D crop3D = new Crop3D();
        crop3D.setClij(clij);
        crop3D.setArgs(new Object[]{input, output, startX, startY, startZ, width, height, depth});
        return crop3D.executeCL();
    }

    public static boolean blur2D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float sigmaX, Float sigmaY) {
        return Kernels.blur(clij, input, output, sigmaX, sigmaY);
    }

    public static boolean blur3D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float sigmaX, Float sigmaY, Float sigmaZ) {
        return Kernels.blur(clij, input, output, sigmaX, sigmaY, sigmaZ);
    }

    public static boolean blur3DSliceBySlice(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float sigmaX, Float sigmaY) {
        return Kernels.blur(clij, input, output, sigmaX, sigmaY, 0.0f);
    }


    public static boolean downsample3D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY, Float factorZ) {
        return Kernels.downsample(clij, input, output, factorX, factorY, factorZ);
    }

    public static boolean downsample2D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY) {
        return Kernels.downsample(clij, input, output, factorX, factorY);
    }

    public static boolean translate2D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float translateX, Float translateY) {
        Translate2D translate2D = new Translate2D();
        translate2D.setClij(clij);
        translate2D.setArgs(new Object[]{input, output, translateX, translateY});
        return translate2D.executeCL();
    }

    public static boolean translate3D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float translateX, Float translateY, Float translateZ) {
        Translate3D translate3D = new Translate3D();
        translate3D.setClij(clij);
        translate3D.setArgs(new Object[]{input, output, translateX, translateY, translateZ});
        return translate3D.executeCL();
    }

    public static boolean applyVectorField2D(CLIJ clij, ClearCLBuffer src, ClearCLBuffer vectorX, ClearCLBuffer vectorY, ClearCLBuffer dst) {
        return Kernels.applyVectorfield(clij, src, vectorX, vectorY, dst);
    }

    public static boolean applyVectorField3D(CLIJ clij, ClearCLBuffer src, ClearCLBuffer vectorX, ClearCLBuffer vectorY, ClearCLBuffer vectorZ, ClearCLBuffer dst) {
        return Kernels.applyVectorfield(clij, src, vectorX, vectorY, vectorZ, dst);
    }

    public static boolean scale(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY) {
        return scale2D(clij, input, output, factorX, factorY);
    }

    public static boolean scale2D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY) {
        Scale2D scale2D = new Scale2D();
        scale2D.setClij(clij);
        scale2D.setArgs(new Object[]{input, output, factorX, factorY});
        return scale2D.executeCL();
    }

    public static boolean scale(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY, Float factorZ) {
        return scale3D(clij, input, output, factorX, factorY, factorZ);
    }

    public static boolean scale3D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY, Float factorZ) {
        Scale3D scale3D = new Scale3D();
        scale3D.setClij(clij);
        scale3D.setArgs(new Object[]{input, output, factorX, factorY, factorZ});
        return scale3D.executeCL();
    }

    public static boolean flip2D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Boolean flipX, Boolean flipY) {
        return Kernels.flip(clij, input, output, flipX, flipY);
    }

    public static boolean flip3D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Boolean flipX, Boolean flipY, Boolean flipZ) {
        return Kernels.flip(clij, input, output, flipX, flipY, flipZ);
    }


    public static boolean countNonZeroPixels2DSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY) {
        return Kernels.countNonZeroPixelsLocally(clij, src, dst, radiusX, radiusY);
    }

    public static boolean countNonZeroVoxels3DSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        return Kernels.countNonZeroVoxelsLocally(clij, src, dst, radiusX, radiusY, radiusZ);
    }

    public static boolean countNonZeroPixelsSliceBySliceSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY) {
        return Kernels.countNonZeroPixelsLocallySliceBySlice(clij, src, dst, radiusX, radiusY);
    }



    //{ Kernels. } public static boolean_clear
    //{ Kernels. } public static booleanx_nonzeroMaximumBox
    //{ Kernels. } public static booleanx_sorensenDiceJaccardIndex
    //{ Kernels. } public static booleanx_blurBuffers3D
    //{ Kernels. } public static booleanx_resultsTableToImage2D


    //{ Kernels. } public static boolean_pull
    //{ Kernels. } public static boolean_convertUInt8

    //{ Kernels. } public static booleanx_splitStackInto11
    //{ Kernels. } public static booleanx_splitStackInto10
    //{ Kernels. } public static boolean_pushCurrentSlice
    //{ Kernels. } public static boolean_pushCurrentZStack
    //{ Kernels. } public static boolean_crop2D
    //{ Kernels. } public static booleanx_splitStackInto12
    //{ Kernels. } public static boolean_crop3D
    //{ Kernels. } public static booleanx_automaticThresholdInplace
    //{ Kernels. } public static boolean_blur2D
    //{ Kernels. } public static booleanx_absolute
    //{ Kernels. } public static boolean_create3D
    //{ Kernels. } public static boolean_blur3D
    //{ Kernels. } public static booleanx_listAvailableGPUs
    //{ Kernels. } public static booleanx_differenceOfGaussian3D
    //{ Kernels. } public static boolean_create2D
    //{ Kernels. } public static boolean_countNonZeroVoxels3DSphere
    //{ Kernels. } public static booleanx_fastParticleImageVelocimetry
    //{ Kernels. } public static booleanx_subtractBackground2D
    //{ Kernels. } public static boolean_reportMemory
    //{ Kernels. } public static booleanx_differenceOfGaussian2D
    //{ Kernels. } public static boolean_push
    //{ Kernels. } public static booleanx_blurInplace3D
    //{ Kernels. } public static boolean_countNonZeroPixels2DSphere
    //{ Kernels. } public static boolean_demonise
    //{ Kernels. } public static booleanx_blurImages3D
    //{ Kernels. } public static boolean_help
    //{ Kernels. } public static boolean_sumOfAllPixels
    //{ Kernels. } public static boolean_absolute
    //{ Kernels. } public static boolean_downsample2D
    //{ Kernels. } public static boolean_meanOfAllPixels
    //{ Kernels. } public static booleanx_standardDeviationOfMaskedPixels
    //{ Kernels. } public static booleanx_pushArray
    //{ Kernels. } public static boolean_pullBinary
    //{ Kernels. } public static boolean_convertFloat
    //{ Kernels. } public static booleanx_varianceOfMaskedPixels
    //{ Kernels. } public static boolean_translate3D
    //{ Kernels. } public static booleanx_tenengradFusionOf9
    //{ Kernels. } public static booleanx_tenengradFusionOf8
    //{ Kernels. } public static booleanx_tenengradFusionOf7
    //{ Kernels. } public static booleanx_tenengradFusionOf6
    //{ Kernels. } public static booleanx_tenengradFusionOf5
    //{ Kernels. } public static booleanx_tenengradFusionOf4
    //{ Kernels. } public static booleanx_tenengradFusionOf3
    //{ Kernels. } public static booleanx_tenengradFusionOf2
    //{ Kernels. } public static boolean_countNonZeroPixelsSliceBySliceSphere
    //{ Kernels. } public static booleanx_getGPUProperties
    //{ Kernels. } public static booleanx_standardDeviationZProjection
    //{ Kernels. } public static boolean_convertUInt16
    //{ Kernels. } public static booleanx_lfrecon
    //{ Kernels. } public static booleanx_paste2D
    //{ Kernels. } public static boolean_release
    //{ Kernels. } public static booleanx_pullToROIManager
    //{ Kernels. } public static booleanx_meanClosestSpotDistance
    //{ Kernels. } public static booleanx_pullLabelsToROIManager
    //{ Kernels. } public static booleanx_splitStackInto9
    //{ Kernels. } public static booleanx_splitStackInto8
    //{ Kernels. } public static booleanx_splitStackInto7
    //{ Kernels. } public static booleanx_splitStackInto6
    //{ Kernels. } public static booleanx_tenengradFusionOf11
    //{ Kernels. } public static booleanx_splitStackInto5
    //{ Kernels. } public static booleanx_tenengradFusionOf12
    //{ Kernels. } public static booleanx_splitStackInto4
    //{ Kernels. } public static boolean_clInfo
    //{ Kernels. } public static booleanx_splitStackInto3
    //{ Kernels. } public static booleanx_tenengradFusionOf10
    //{ Kernels. } public static booleanx_splitStackInto2
}
