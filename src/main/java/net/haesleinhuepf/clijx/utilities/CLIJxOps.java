package net.haesleinhuepf.clijx.utilities;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import ij.measure.ResultsTable;
import ij.gui.Roi;
import ij.plugin.frame.RoiManager;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clijx.plugins.BinaryUnion;
import net.haesleinhuepf.clijx.plugins.BinaryIntersection;
import net.haesleinhuepf.clijx.plugins.ConnectedComponentsLabeling;
import net.haesleinhuepf.clijx.plugins.CountNonZeroPixels;
import net.haesleinhuepf.clijx.plugins.CrossCorrelation;
import net.haesleinhuepf.clijx.plugins.DifferenceOfGaussian2D;
import net.haesleinhuepf.clijx.plugins.DifferenceOfGaussian3D;
import net.haesleinhuepf.clijx.plugins.Extrema;
import net.haesleinhuepf.clijx.plugins.LocalExtremaBox;
import net.haesleinhuepf.clijx.plugins.LocalID;
import net.haesleinhuepf.clijx.plugins.MaskLabel;
import net.haesleinhuepf.clijx.plugins.MeanClosestSpotDistance;
import net.haesleinhuepf.clijx.plugins.MeanSquaredError;
import net.haesleinhuepf.clijx.plugins.MedianZProjection;
import net.haesleinhuepf.clijx.plugins.NonzeroMinimumDiamond;
import net.haesleinhuepf.clijx.plugins.Paste2D;
import net.haesleinhuepf.clijx.plugins.Paste3D;
import net.haesleinhuepf.clijx.plugins.Presign;
import net.haesleinhuepf.clijx.plugins.JaccardIndex;
import net.haesleinhuepf.clijx.plugins.SorensenDiceCoefficent;
import net.haesleinhuepf.clijx.plugins.StandardDeviationZProjection;
import net.haesleinhuepf.clijx.plugins.StackToTiles;
import net.haesleinhuepf.clijx.plugins.SubtractBackground2D;
import net.haesleinhuepf.clijx.plugins.SubtractBackground3D;
import net.haesleinhuepf.clijx.plugins.TopHatBox;
import net.haesleinhuepf.clijx.plugins.TopHatSphere;
import net.haesleinhuepf.clijx.plugins.Exponential;
import net.haesleinhuepf.clijx.plugins.Logarithm;
import net.haesleinhuepf.clijx.matrix.GenerateDistanceMatrix;
import net.haesleinhuepf.clijx.matrix.ShortestDistances;
import net.haesleinhuepf.clijx.matrix.SpotsToPointList;
import net.haesleinhuepf.clijx.matrix.TransposeXY;
import net.haesleinhuepf.clijx.matrix.TransposeXZ;
import net.haesleinhuepf.clijx.matrix.TransposeYZ;
import net.haesleinhuepf.clijx.piv.FastParticleImageVelocimetry;
import net.haesleinhuepf.clijx.piv.ParticleImageVelocimetry;
import net.haesleinhuepf.clijx.piv.ParticleImageVelocimetryTimelapse;
import net.haesleinhuepf.clijx.registration.DeformableRegistration2D;
import net.haesleinhuepf.clijx.registration.TranslationRegistration;
import net.haesleinhuepf.clijx.registration.TranslationTimelapseRegistration;
import net.haesleinhuepf.clijx.plugins.SetWhereXequalsY;
import net.haesleinhuepf.clijx.plugins.LaplaceSphere;
import net.haesleinhuepf.clijx.plugins.Image2DToResultsTable;
import net.haesleinhuepf.clijx.plugins.WriteValuesToPositions;
import net.haesleinhuepf.clijx.plugins.GetSize;
import net.haesleinhuepf.clijx.matrix.MultiplyMatrix;
import net.haesleinhuepf.clijx.matrix.MatrixEqual;
import net.haesleinhuepf.clijx.plugins.PowerImages;
import net.haesleinhuepf.clijx.plugins.Equal;
import net.haesleinhuepf.clijx.plugins.GreaterOrEqual;
import net.haesleinhuepf.clijx.plugins.Greater;
import net.haesleinhuepf.clijx.plugins.Smaller;
import net.haesleinhuepf.clijx.plugins.SmallerOrEqual;
import net.haesleinhuepf.clijx.plugins.NotEqual;
import net.haesleinhuepf.clijx.io.ReadImageFromDisc;
import net.haesleinhuepf.clijx.io.ReadRawImageFromDisc;
import net.haesleinhuepf.clijx.io.PreloadFromDisc;
import net.haesleinhuepf.clijx.plugins.EqualConstant;
import net.haesleinhuepf.clijx.plugins.GreaterOrEqualConstant;
import net.haesleinhuepf.clijx.plugins.GreaterConstant;
import net.haesleinhuepf.clijx.plugins.SmallerConstant;
import net.haesleinhuepf.clijx.plugins.SmallerOrEqualConstant;
import net.haesleinhuepf.clijx.plugins.NotEqualConstant;
import net.haesleinhuepf.clijx.painting.DrawBox;
import net.haesleinhuepf.clijx.painting.DrawLine;
import net.haesleinhuepf.clijx.painting.DrawSphere;
import net.haesleinhuepf.clijx.plugins.ReplaceIntensity;
import net.haesleinhuepf.clijx.plugins.BoundingBox;
import net.haesleinhuepf.clijx.plugins.MinimumOfMaskedPixels;
import net.haesleinhuepf.clijx.plugins.MaximumOfMaskedPixels;
import net.haesleinhuepf.clijx.plugins.MeanOfMaskedPixels;
import net.haesleinhuepf.clijx.plugins.LabelToMask;
import net.haesleinhuepf.clijx.matrix.NClosestPoints;
import net.haesleinhuepf.clijx.matrix.GaussJordan;
import net.haesleinhuepf.clijx.plugins.StatisticsOfLabelledPixels;
import net.haesleinhuepf.clijx.plugins.VarianceOfAllPixels;
import net.haesleinhuepf.clijx.plugins.StandardDeviationOfAllPixels;
import net.haesleinhuepf.clijx.plugins.VarianceOfMaskedPixels;
import net.haesleinhuepf.clijx.plugins.StandardDeviationOfMaskedPixels;
import net.haesleinhuepf.clijx.plugins.ExcludeLabelsOnEdges;
import net.haesleinhuepf.clijx.plugins.BinarySubtract;
import net.haesleinhuepf.clijx.plugins.BinaryEdgeDetection;
import net.haesleinhuepf.clijx.plugins.DistanceMap;
import net.haesleinhuepf.clijx.plugins.PullAsROI;
import net.haesleinhuepf.clijx.plugins.PullLabelsToROIManager;
import net.haesleinhuepf.clijx.plugins.NonzeroMaximumDiamond;
import net.haesleinhuepf.clijx.plugins.OnlyzeroOverwriteMaximumDiamond;
import net.haesleinhuepf.clijx.plugins.OnlyzeroOverwriteMaximumBox;
import net.haesleinhuepf.clijx.matrix.GenerateTouchMatrix;
import net.haesleinhuepf.clijx.plugins.DetectLabelEdges;
import net.haesleinhuepf.clijx.plugins.StopWatch;
import net.haesleinhuepf.clijx.matrix.CountTouchingNeighbors;
import net.haesleinhuepf.clijx.plugins.ReplaceIntensities;
import net.haesleinhuepf.clijx.painting.DrawTwoValueLine;
import net.haesleinhuepf.clijx.matrix.AverageDistanceOfNClosestPoints;
import net.haesleinhuepf.clijx.plugins.SaveAsTIF;
import net.haesleinhuepf.clijx.plugins.ConnectedComponentsLabelingInplace;
import net.haesleinhuepf.clijx.matrix.TouchMatrixToMesh;
import net.haesleinhuepf.clijx.plugins.AutomaticThresholdInplace;
import net.haesleinhuepf.clijx.plugins.DifferenceOfGaussianInplace3D;
import net.haesleinhuepf.clijx.plugins.AbsoluteInplace;
import net.haesleinhuepf.clijx.plugins.Resample;
import net.haesleinhuepf.clijx.plugins.EqualizeMeanIntensitiesOfSlices;
import net.haesleinhuepf.clijx.plugins.Watershed;
import net.haesleinhuepf.clijx.plugins.ResliceRadial;
import net.haesleinhuepf.clijx.plugins.ShowRGB;
import net.haesleinhuepf.clijx.plugins.ShowGrey;
import net.haesleinhuepf.clijx.plugins.Sobel;
import net.haesleinhuepf.clijx.plugins.Absolute;
import net.haesleinhuepf.clijx.plugins.LaplaceBox;
import net.haesleinhuepf.clijx.plugins.BottomHatBox;
import net.haesleinhuepf.clijx.plugins.BottomHatSphere;
import net.haesleinhuepf.clijx.plugins.ClosingBox;
import net.haesleinhuepf.clijx.plugins.ClosingDiamond;
import net.haesleinhuepf.clijx.plugins.OpeningBox;
import net.haesleinhuepf.clijx.plugins.OpeningDiamond;
import net.haesleinhuepf.clijx.plugins.MaximumXProjection;
import net.haesleinhuepf.clijx.plugins.MaximumYProjection;
import net.haesleinhuepf.clijx.plugins.ProjectMaximumZBounded;
import net.haesleinhuepf.clijx.plugins.ProjectMinimumZBounded;
import net.haesleinhuepf.clijx.plugins.ProjectMeanZBounded;
import net.haesleinhuepf.clijx.plugins.NonzeroMaximumBox;
import net.haesleinhuepf.clijx.plugins.NonzeroMinimumBox;
import net.haesleinhuepf.clijx.plugins.ProjectMinimumThresholdedZBounded;
import net.haesleinhuepf.clijx.plugins.MeanOfPixelsAboveThreshold;
import net.haesleinhuepf.clijx.gui.OrganiseWindows;
import net.haesleinhuepf.clijx.matrix.DistanceMatrixToMesh;
import net.haesleinhuepf.clijx.matrix.PointIndexListToMesh;
import net.haesleinhuepf.clijx.plugins.MinimumOctagon;
import net.haesleinhuepf.clijx.plugins.MaximumOctagon;
import net.haesleinhuepf.clijx.plugins.TopHatOctagon;
import net.haesleinhuepf.clij2.plugins.AddImages;
import net.haesleinhuepf.clij2.plugins.AddImagesWeighted;
import net.haesleinhuepf.clij2.plugins.SubtractImages;
import net.haesleinhuepf.clijx.plugins.ShowGlasbeyOnGrey;
import net.haesleinhuepf.clijx.weka.ApplyWekaModel;
import net.haesleinhuepf.clijx.weka.TrainWekaModel;
import net.haesleinhuepf.clijx.plugins.AffineTransform2D;
import net.haesleinhuepf.clijx.plugins.AffineTransform3D;
import net.haesleinhuepf.clijx.plugins.ApplyVectorField2D;
import net.haesleinhuepf.clijx.plugins.ApplyVectorField3D;
import net.haesleinhuepf.clijx.plugins.ArgMaximumZProjection;
import net.haesleinhuepf.clijx.plugins.Histogram;
import net.haesleinhuepf.clijx.plugins.AutomaticThreshold;
import net.haesleinhuepf.clijx.plugins.Threshold;
import net.haesleinhuepf.clijx.plugins.BinaryOr;
import net.haesleinhuepf.clijx.plugins.BinaryAnd;
import net.haesleinhuepf.clijx.plugins.BinaryXOr;
import net.haesleinhuepf.clijx.plugins.BinaryNot;
import net.haesleinhuepf.clijx.plugins.ErodeSphere;
import net.haesleinhuepf.clijx.plugins.ErodeBox;
import net.haesleinhuepf.clijx.plugins.ErodeSphereSliceBySlice;
import net.haesleinhuepf.clijx.plugins.ErodeBoxSliceBySlice;
import net.haesleinhuepf.clijx.plugins.DilateSphere;
import net.haesleinhuepf.clijx.plugins.DilateBox;
import net.haesleinhuepf.clijx.plugins.DilateSphereSliceBySlice;
import net.haesleinhuepf.clijx.plugins.DilateBoxSliceBySlice;
import net.haesleinhuepf.clijx.plugins.Copy;
import net.haesleinhuepf.clijx.plugins.CopySlice;
import net.haesleinhuepf.clijx.plugins.Crop2D;
import net.haesleinhuepf.clijx.plugins.Crop3D;
import net.haesleinhuepf.clijx.plugins.Set;
import net.haesleinhuepf.clijx.plugins.Flip2D;
import net.haesleinhuepf.clijx.plugins.Flip3D;
import net.haesleinhuepf.clijx.plugins.RotateLeft;
import net.haesleinhuepf.clijx.plugins.RotateRight;
import net.haesleinhuepf.clijx.plugins.Mask;
import net.haesleinhuepf.clijx.plugins.MaskStackWithPlane;
import net.haesleinhuepf.clijx.plugins.MaximumZProjection;
import net.haesleinhuepf.clijx.plugins.MeanZProjection;
import net.haesleinhuepf.clijx.plugins.MinimumZProjection;
import net.haesleinhuepf.clijx.plugins.Power;
import net.haesleinhuepf.clijx.plugins.tenengradfusion.AbstractTenengradFusion;
import net.haesleinhuepf.clijx.plugins.DivideImages;
import net.haesleinhuepf.clijx.plugins.MaximumImages;
import net.haesleinhuepf.clijx.plugins.MaximumImageAndScalar;
import net.haesleinhuepf.clijx.plugins.MinimumImages;
import net.haesleinhuepf.clijx.plugins.MinimumImageAndScalar;
import net.haesleinhuepf.clijx.plugins.MultiplyImageAndScalar;
import net.haesleinhuepf.clijx.plugins.MultiplyStackWithPlane;
import net.haesleinhuepf.clijx.plugins.CountNonZeroPixels2DSphere;
import net.haesleinhuepf.clijx.plugins.CountNonZeroPixelsSliceBySliceSphere;
import net.haesleinhuepf.clijx.plugins.CountNonZeroVoxels3DSphere;
import net.haesleinhuepf.clijx.plugins.SumZProjection;
import net.haesleinhuepf.clijx.plugins.SumOfAllPixels;
import net.haesleinhuepf.clijx.plugins.CenterOfMass;
import net.haesleinhuepf.clijx.plugins.Invert;
import net.haesleinhuepf.clijx.plugins.Downsample2D;
import net.haesleinhuepf.clijx.plugins.Downsample3D;
import net.haesleinhuepf.clijx.plugins.DownsampleSliceBySliceHalfMedian;
import net.haesleinhuepf.clijx.plugins.LocalThreshold;
import net.haesleinhuepf.clijx.plugins.GradientX;
import net.haesleinhuepf.clijx.plugins.GradientY;
import net.haesleinhuepf.clijx.plugins.GradientZ;
import net.haesleinhuepf.clijx.plugins.MultiplyImageAndCoordinate;
import net.haesleinhuepf.clijx.plugins.Mean2DBox;
import net.haesleinhuepf.clijx.plugins.Mean2DSphere;
import net.haesleinhuepf.clijx.plugins.Mean3DBox;
import net.haesleinhuepf.clijx.plugins.Mean3DSphere;
import net.haesleinhuepf.clijx.plugins.MeanSliceBySliceSphere;
import net.haesleinhuepf.clijx.plugins.MeanOfAllPixels;
import net.haesleinhuepf.clijx.plugins.Median2DBox;
import net.haesleinhuepf.clijx.plugins.Median2DSphere;
import net.haesleinhuepf.clijx.plugins.Median3DBox;
import net.haesleinhuepf.clijx.plugins.Median3DSphere;
import net.haesleinhuepf.clijx.plugins.MedianSliceBySliceBox;
import net.haesleinhuepf.clijx.plugins.MedianSliceBySliceSphere;
import net.haesleinhuepf.clijx.plugins.Maximum2DSphere;
import net.haesleinhuepf.clijx.plugins.Maximum3DSphere;
import net.haesleinhuepf.clijx.plugins.Maximum2DBox;
import net.haesleinhuepf.clijx.plugins.Maximum3DBox;
import net.haesleinhuepf.clijx.plugins.MaximumSliceBySliceSphere;
import net.haesleinhuepf.clijx.plugins.Minimum2DSphere;
import net.haesleinhuepf.clijx.plugins.Minimum3DSphere;
import net.haesleinhuepf.clijx.plugins.Minimum2DBox;
import net.haesleinhuepf.clijx.plugins.Minimum3DBox;
import net.haesleinhuepf.clijx.plugins.MinimumSliceBySliceSphere;
import net.haesleinhuepf.clijx.plugins.MultiplyImages;
import net.haesleinhuepf.clijx.plugins.Blur2D;
import net.haesleinhuepf.clijx.plugins.Blur3D;
import net.haesleinhuepf.clijx.plugins.Blur3DSliceBySlice;
import net.haesleinhuepf.clijx.plugins.ResliceBottom;
import net.haesleinhuepf.clijx.plugins.ResliceTop;
import net.haesleinhuepf.clijx.plugins.ResliceLeft;
import net.haesleinhuepf.clijx.plugins.ResliceRight;
import net.haesleinhuepf.clijx.plugins.Rotate2D;
import net.haesleinhuepf.clijx.plugins.Rotate3D;
import net.haesleinhuepf.clijx.plugins.Scale2D;
import net.haesleinhuepf.clijx.plugins.Scale3D;
import net.haesleinhuepf.clijx.plugins.Translate2D;
import net.haesleinhuepf.clijx.plugins.Translate3D;
import net.haesleinhuepf.clijx.base.Clear;
import net.haesleinhuepf.clijx.base.ClInfo;
import net.haesleinhuepf.clijx.base.ConvertFloat;
import net.haesleinhuepf.clijx.base.ConvertUInt8;
import net.haesleinhuepf.clijx.base.ConvertUInt16;
import net.haesleinhuepf.clijx.base.Create2D;
import net.haesleinhuepf.clijx.base.Create3D;
import net.haesleinhuepf.clijx.base.Pull;
import net.haesleinhuepf.clijx.base.PullBinary;
import net.haesleinhuepf.clijx.base.Push;
import net.haesleinhuepf.clijx.base.PushCurrentSlice;
import net.haesleinhuepf.clijx.base.PushCurrentZStack;
import net.haesleinhuepf.clijx.base.Release;
import net.haesleinhuepf.clijx.plugins.AddImageAndScalar;
import net.haesleinhuepf.clijx.plugins.DetectMinimaBox;
import net.haesleinhuepf.clijx.plugins.DetectMaximaBox;
import net.haesleinhuepf.clijx.plugins.DetectMaximaSliceBySliceBox;
import net.haesleinhuepf.clijx.plugins.DetectMinimaSliceBySliceBox;
import net.haesleinhuepf.clijx.plugins.MaximumOfAllPixels;
import net.haesleinhuepf.clijx.plugins.MinimumOfAllPixels;
import net.haesleinhuepf.clijx.plugins.ReportMemory;
import net.haesleinhuepf.clijx.plugins.splitstack.AbstractSplitStack;
import net.haesleinhuepf.clijx.plugins.TopHatOctagonSliceBySlice;
import net.haesleinhuepf.clijx.plugins.SetColumn;
import net.haesleinhuepf.clijx.plugins.SetRow;
import net.haesleinhuepf.clijx.plugins.SumYProjection;
import net.haesleinhuepf.clijx.matrix.AverageDistanceOfTouchingNeighbors;
import net.haesleinhuepf.clijx.matrix.LabelledSpotsToPointList;
import net.haesleinhuepf.clijx.matrix.LabelSpots;
import net.haesleinhuepf.clijx.matrix.MinimumDistanceOfTouchingNeighbors;
import net.haesleinhuepf.clijx.io.WriteVTKLineListToDisc;
import net.haesleinhuepf.clijx.io.WriteXYZPointListToDisc;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public abstract interface CLIJxOps {
   CLIJ getCLIJ();
   CLIJ2 getCLIJ2();
   CLIJx getCLIJx();
   

    // net.haesleinhuepf.clij.kernels.Kernels
    //----------------------------------------------------
    /**
     * 
     */
    default boolean detectOptima(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, boolean arg4) {
        return Kernels.detectOptima(getCLIJ(), arg1, arg2, new Double (arg3).intValue(), arg4);
    }

    /**
     * 
     */
    default boolean detectOptima(ClearCLImage arg1, ClearCLImage arg2, double arg3, boolean arg4) {
        return Kernels.detectOptima(getCLIJ(), arg1, arg2, new Double (arg3).intValue(), arg4);
    }

    /**
     * 
     */
    default boolean differenceOfGaussianSliceBySlice(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Kernels.differenceOfGaussianSliceBySlice(getCLIJ(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * 
     */
    default boolean convertToImageJBinary(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.convertToImageJBinary(getCLIJ(), arg1, arg2);
    }

    /**
     * 
     */
    default boolean convertToImageJBinary(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.convertToImageJBinary(getCLIJ(), arg1, arg2);
    }

    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    default boolean differenceOfGaussian(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Kernels.differenceOfGaussian(getCLIJ(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Determines the maximum projection of an image along a given dimension. Furthermore, the X and Y
     *  dimesions of the resulting image must be specified by the user according to its definition:
     * X = 0
     * Y = 1
     * Z = 2
     * 
     */
    default boolean maximumXYZProjection(ClearCLImage source, ClearCLImage destination_max, double dimensionX, double dimensionY, double projectedDimension) {
        return Kernels.maximumXYZProjection(getCLIJ(), source, destination_max, new Double (dimensionX).intValue(), new Double (dimensionY).intValue(), new Double (projectedDimension).intValue());
    }

    /**
     * Determines the maximum projection of an image along a given dimension. Furthermore, the X and Y
     *  dimesions of the resulting image must be specified by the user according to its definition:
     * X = 0
     * Y = 1
     * Z = 2
     * 
     */
    default boolean maximumXYZProjection(ClearCLBuffer source, ClearCLBuffer destination_max, double dimensionX, double dimensionY, double projectedDimension) {
        return Kernels.maximumXYZProjection(getCLIJ(), source, destination_max, new Double (dimensionX).intValue(), new Double (dimensionY).intValue(), new Double (projectedDimension).intValue());
    }

    /**
     * 
     */
    default boolean detectOptimaSliceBySlice(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, boolean arg4) {
        return Kernels.detectOptimaSliceBySlice(getCLIJ(), arg1, arg2, new Double (arg3).intValue(), arg4);
    }

    /**
     * 
     */
    default boolean detectOptimaSliceBySlice(ClearCLImage arg1, ClearCLImage arg2, double arg3, boolean arg4) {
        return Kernels.detectOptimaSliceBySlice(getCLIJ(), arg1, arg2, new Double (arg3).intValue(), arg4);
    }

    /**
     * 
     */
    default double[] sumPixelsSliceBySlice(ClearCLBuffer arg1) {
        return Kernels.sumPixelsSliceBySlice(getCLIJ(), arg1);
    }

    /**
     * 
     */
    default double[] sumPixelsSliceBySlice(ClearCLImage arg1) {
        return Kernels.sumPixelsSliceBySlice(getCLIJ(), arg1);
    }

    /**
     * 
     */
    default boolean multiplySliceBySliceWithScalars(ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3) {
        return Kernels.multiplySliceBySliceWithScalars(getCLIJ(), arg1, arg2, arg3);
    }

    /**
     * 
     */
    default boolean multiplySliceBySliceWithScalars(ClearCLImage arg1, ClearCLImage arg2, float[] arg3) {
        return Kernels.multiplySliceBySliceWithScalars(getCLIJ(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.BinaryUnion
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary union operator |.
     * All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>
     */
    default boolean binaryUnion(ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination) {
        return BinaryUnion.binaryUnion(getCLIJx(), operand1, operand2, destination);
    }


    // net.haesleinhuepf.clijx.plugins.BinaryIntersection
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary intersection operator &.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = x & y</pre>
     */
    default boolean binaryIntersection(ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination) {
        return BinaryIntersection.binaryIntersection(getCLIJx(), operand1, operand2, destination);
    }


    // net.haesleinhuepf.clijx.plugins.ConnectedComponentsLabeling
    //----------------------------------------------------
    /**
     * 
     */
    default boolean shiftIntensitiesToCloseGaps(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return ConnectedComponentsLabeling.shiftIntensitiesToCloseGaps(getCLIJx(), arg1, arg2);
    }

    /**
     * 
     */
    default boolean setNonZeroPixelsToPixelIndex(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ConnectedComponentsLabeling.setNonZeroPixelsToPixelIndex(getCLIJ(), arg1, arg2);
    }

    /**
     * Performs connected components analysis to a binary image and generates a label map.
     */
    default boolean connectedComponentsLabeling(ClearCLBuffer binary_input, ClearCLBuffer labeling_destination) {
        return ConnectedComponentsLabeling.connectedComponentsLabeling(getCLIJx(), binary_input, labeling_destination);
    }


    // net.haesleinhuepf.clijx.plugins.CountNonZeroPixels
    //----------------------------------------------------
    /**
     * Determines the number of all pixels in a given image which are not equal to 0. It will be stored in a new row of ImageJs
     * Results table in the column 'CountNonZero'.
     */
    default double countNonZeroPixels(ClearCLBuffer source) {
        return CountNonZeroPixels.countNonZeroPixels(getCLIJx(), source);
    }


    // net.haesleinhuepf.clijx.plugins.CrossCorrelation
    //----------------------------------------------------
    /**
     * Performs cross correlation analysis between two images. The second image is shifted by deltaPos in the given dimension. The cross correlation coefficient is calculated for each pixel in a range around the given pixel with given radius in the given dimension. Together with the original images it is recommended to hand over mean filtered images using the same radius.  
     */
    default boolean crossCorrelation(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4, ClearCLImage arg5, int arg6, int arg7, int arg8) {
        return CrossCorrelation.crossCorrelation(getCLIJ(), arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }

    /**
     * Performs cross correlation analysis between two images. The second image is shifted by deltaPos in the given dimension. The cross correlation coefficient is calculated for each pixel in a range around the given pixel with given radius in the given dimension. Together with the original images it is recommended to hand over mean filtered images using the same radius.  
     */
    default boolean crossCorrelation(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, ClearCLBuffer arg5, int arg6, int arg7, int arg8) {
        return CrossCorrelation.crossCorrelation(getCLIJ(), arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }


    // net.haesleinhuepf.clijx.plugins.DifferenceOfGaussian2D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    default boolean differenceOfGaussian2D(ClearCLBuffer input, ClearCLBuffer destination, double sigma1x, double sigma1y, double sigma2x, double sigma2y) {
        return DifferenceOfGaussian2D.differenceOfGaussian2D(getCLIJ(), input, destination, new Double (sigma1x).floatValue(), new Double (sigma1y).floatValue(), new Double (sigma2x).floatValue(), new Double (sigma2y).floatValue());
    }

    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    default boolean differenceOfGaussian(ClearCLBuffer input, ClearCLBuffer destination, double sigma1x, double sigma1y, double sigma2x, double sigma2y) {
        return DifferenceOfGaussian2D.differenceOfGaussian(getCLIJ(), input, destination, new Double (sigma1x).floatValue(), new Double (sigma1y).floatValue(), new Double (sigma2x).floatValue(), new Double (sigma2y).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.DifferenceOfGaussian3D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    default boolean differenceOfGaussian3D(ClearCLBuffer input, ClearCLBuffer destination, double sigma1x, double sigma1y, double sigma1z, double sigma2x, double sigma2y, double sigma2z) {
        return DifferenceOfGaussian3D.differenceOfGaussian3D(getCLIJ(), input, destination, new Double (sigma1x).floatValue(), new Double (sigma1y).floatValue(), new Double (sigma1z).floatValue(), new Double (sigma2x).floatValue(), new Double (sigma2y).floatValue(), new Double (sigma2z).floatValue());
    }

    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    default boolean differenceOfGaussian(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8) {
        return DifferenceOfGaussian3D.differenceOfGaussian(getCLIJ(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.Extrema
    //----------------------------------------------------
    /**
     * Returns an image with pixel values most distant from 0: f(x, y) = x if abs(x) > abs(y), y else.
     */
    default boolean extrema(ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination) {
        return Extrema.extrema(getCLIJ(), input1, input2, destination);
    }


    // net.haesleinhuepf.clijx.plugins.LocalExtremaBox
    //----------------------------------------------------
    /**
     * Applies a local minimum and maximum filter. Afterwards, the value is returned which is more far from zero.
     */
    default boolean localExtremaBox(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return LocalExtremaBox.localExtremaBox(getCLIJ(), input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.LocalID
    //----------------------------------------------------
    /**
     * local id
     */
    default boolean localID(ClearCLBuffer input, ClearCLBuffer destination) {
        return LocalID.localID(getCLIJ(), input, destination);
    }


    // net.haesleinhuepf.clijx.plugins.MaskLabel
    //----------------------------------------------------
    /**
     * Computes a masked image by applying a label mask to an image. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same position in the label_map image has the right index value i.
     * 
     * f(x,m,i) = (x if (m == i); (0 otherwise))
     */
    default boolean maskLabel(ClearCLBuffer source, ClearCLBuffer label_map, ClearCLBuffer destination, double label_index) {
        return MaskLabel.maskLabel(getCLIJ(), source, label_map, destination, new Double (label_index).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.MeanClosestSpotDistance
    //----------------------------------------------------
    /**
     * 
     */
    default double meanClosestSpotDistances(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return MeanClosestSpotDistance.meanClosestSpotDistances(getCLIJx(), arg1, arg2);
    }

    /**
     * 
     */
    default double[] meanClosestSpotDistances(ClearCLBuffer arg1, ClearCLBuffer arg2, boolean arg3) {
        return MeanClosestSpotDistance.meanClosestSpotDistances(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.MeanSquaredError
    //----------------------------------------------------
    /**
     * Determines the mean squared error (MSE) between two images. The MSE will be stored in a new row of ImageJs
     * Results table in the column 'MSE'.
     */
    default double meanSquaredError(ClearCLBuffer source1, ClearCLBuffer source2) {
        return MeanSquaredError.meanSquaredError(getCLIJ(), source1, source2);
    }


    // net.haesleinhuepf.clijx.plugins.MedianZProjection
    //----------------------------------------------------
    /**
     * Determines the median projection of an image stack along Z.
     */
    default boolean medianZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return MedianZProjection.medianZProjection(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.NonzeroMinimumDiamond
    //----------------------------------------------------
    /**
     * Apply a minimum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default boolean nonzeroMinimumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return NonzeroMinimumDiamond.nonzeroMinimumDiamond(getCLIJ(), arg1, arg2, arg3);
    }

    /**
     * Apply a minimum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default boolean nonzeroMinimumDiamond(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3) {
        return NonzeroMinimumDiamond.nonzeroMinimumDiamond(getCLIJx(), arg1, arg2, arg3);
    }

    /**
     * Apply a minimum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default ClearCLKernel nonzeroMinimumDiamond(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMinimumDiamond.nonzeroMinimumDiamond(getCLIJx(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clijx.plugins.Paste2D
    //----------------------------------------------------
    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste2D(ClearCLBuffer source, ClearCLBuffer destination, double destinationX, double destinationY) {
        return Paste2D.paste2D(getCLIJ(), source, destination, new Double (destinationX).intValue(), new Double (destinationY).intValue());
    }

    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste(ClearCLImage source, ClearCLImage destination, double destinationX, double destinationY) {
        return Paste2D.paste(getCLIJ(), source, destination, new Double (destinationX).intValue(), new Double (destinationY).intValue());
    }

    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste(ClearCLBuffer source, ClearCLBuffer destination, double destinationX, double destinationY) {
        return Paste2D.paste(getCLIJ(), source, destination, new Double (destinationX).intValue(), new Double (destinationY).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Paste3D
    //----------------------------------------------------
    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste3D(ClearCLBuffer source, ClearCLBuffer destination, double destinationX, double destinationY, double destinationZ) {
        return Paste3D.paste3D(getCLIJ(), source, destination, new Double (destinationX).intValue(), new Double (destinationY).intValue(), new Double (destinationZ).intValue());
    }

    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Paste3D.paste(getCLIJ(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Paste3D.paste(getCLIJ(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Presign
    //----------------------------------------------------
    /**
     * Determines the extrema of pixel values: f(x) = x / abs(x).
     */
    default boolean presign(ClearCLBuffer input, ClearCLBuffer destination) {
        return Presign.presign(getCLIJ(), input, destination);
    }


    // net.haesleinhuepf.clijx.plugins.JaccardIndex
    //----------------------------------------------------
    /**
     * Determines the overlap of two binary images using the Jaccard index.
     * A value of 0 suggests no overlap, 1 means perfect overlap.
     * The resulting Jaccard index is saved to the results table in the 'Jaccard_Index' column.
     * Note that the Sorensen-Dice coefficient can be calculated from the Jaccard index j using this formula:
     * <pre>s = f(j) = 2 j / (j + 1)</pre>
     */
    default double jaccardIndex(ClearCLBuffer source1, ClearCLBuffer source2) {
        return JaccardIndex.jaccardIndex(getCLIJx(), source1, source2);
    }


    // net.haesleinhuepf.clijx.plugins.SorensenDiceCoefficent
    //----------------------------------------------------
    /**
     * Determines the overlap of two binary images using the Sorensen-Dice coefficent.
     * A value of 0 suggests no overlap, 1 means perfect overlap.
     * The Sorensen-Dice coefficient is saved in the colum 'Sorensen_Dice_coefficient'.
     * Note that the Sorensen-Dice coefficient s can be calculated from the Jaccard index j using this formula:
     * <pre>s = f(j) = 2 j / (j + 1)</pre>
     */
    default double sorensenDiceCoefficient(ClearCLBuffer source1, ClearCLBuffer source2) {
        return SorensenDiceCoefficent.sorensenDiceCoefficient(getCLIJx(), source1, source2);
    }


    // net.haesleinhuepf.clijx.plugins.StandardDeviationZProjection
    //----------------------------------------------------
    /**
     * Determines the standard deviation projection of an image stack along Z.
     */
    default boolean standardDeviationZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return StandardDeviationZProjection.standardDeviationZProjection(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.StackToTiles
    //----------------------------------------------------
    /**
     * Stack to tiles.
     */
    default boolean stackToTiles(ClearCLImage source, ClearCLImage destination, double tiles_x, double tiles_y) {
        return StackToTiles.stackToTiles(getCLIJ(), source, destination, new Double (tiles_x).intValue(), new Double (tiles_y).intValue());
    }

    /**
     * Stack to tiles.
     */
    default boolean stackToTiles(ClearCLBuffer source, ClearCLBuffer destination, double tiles_x, double tiles_y) {
        return StackToTiles.stackToTiles(getCLIJ(), source, destination, new Double (tiles_x).intValue(), new Double (tiles_y).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.SubtractBackground2D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image and subtracts the result from the original image.
     */
    default boolean subtractBackground2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return SubtractBackground2D.subtractBackground2D(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

    /**
     * Applies Gaussian blur to the input image and subtracts the result from the original image.
     */
    default boolean subtractBackground(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return SubtractBackground2D.subtractBackground(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.SubtractBackground3D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image and subtracts the result from the original image.
     */
    default boolean subtractBackground(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return SubtractBackground3D.subtractBackground(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Applies Gaussian blur to the input image and subtracts the result from the original image.
     */
    default boolean subtractBackground3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return SubtractBackground3D.subtractBackground3D(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.TopHatBox
    //----------------------------------------------------
    /**
     * Applies a top-hat filter for background subtraction to the input image.
     */
    default boolean topHatBox(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return TopHatBox.topHatBox(getCLIJ(), input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.TopHatSphere
    //----------------------------------------------------
    /**
     * Applies a top-hat filter for background subtraction to the input image.
     */
    default boolean topHatSphere(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return TopHatSphere.topHatSphere(getCLIJ(), input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Exponential
    //----------------------------------------------------
    /**
     * Computes base exponential of all pixels values.
     * 
     * f(x) = exp(x)
     */
    default boolean exponential(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return Exponential.exponential(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.Logarithm
    //----------------------------------------------------
    /**
     * Computes base e logarithm of all pixels values.
     * 
     * f(x) = log(x)
     */
    default boolean logarithm(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return Logarithm.logarithm(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.matrix.GenerateDistanceMatrix
    //----------------------------------------------------
    /**
     * Takes two images containing coordinates and builds up a matrix containing distance between the points. Convention: image width represents number of points, height represents dimensionality (2D, 3D, ... 10D). The result image has width the first input image and height equals to the width of the second input image.
     */
    default boolean generateDistanceMatrix(ClearCLBuffer coordinate_list1, ClearCLBuffer coordinate_list2, ClearCLBuffer distance_matrix_destination) {
        return GenerateDistanceMatrix.generateDistanceMatrix(getCLIJx(), coordinate_list1, coordinate_list2, distance_matrix_destination);
    }


    // net.haesleinhuepf.clijx.matrix.ShortestDistances
    //----------------------------------------------------
    /**
     * Determine the shortest distance from a distance matrix. This corresponds to the minimum in a matrix for each individial column.
     */
    default boolean shortestDistances(ClearCLBuffer distance_matrix, ClearCLBuffer destination_minimum_distances) {
        return ShortestDistances.shortestDistances(getCLIJx(), distance_matrix, destination_minimum_distances);
    }


    // net.haesleinhuepf.clijx.matrix.SpotsToPointList
    //----------------------------------------------------
    /**
     * Transforms a spots image as resulting from maximum/minimum detection in an image where every column contains d 
     * pixels (with d = dimensionality of the original image) with the coordinates of the maxima/minima.
     */
    default boolean spotsToPointList(ClearCLBuffer input_spots, ClearCLBuffer destination_pointlist) {
        return SpotsToPointList.spotsToPointList(getCLIJx(), input_spots, destination_pointlist);
    }


    // net.haesleinhuepf.clijx.matrix.TransposeXY
    //----------------------------------------------------
    /**
     * Transpose X and Y axes of an image.
     */
    default boolean transposeXY(ClearCLBuffer input, ClearCLBuffer destination) {
        return TransposeXY.transposeXY(getCLIJx(), input, destination);
    }


    // net.haesleinhuepf.clijx.matrix.TransposeXZ
    //----------------------------------------------------
    /**
     * Transpose X and Z axes of an image.
     */
    default boolean transposeXZ(ClearCLBuffer input, ClearCLBuffer destination) {
        return TransposeXZ.transposeXZ(getCLIJx(), input, destination);
    }


    // net.haesleinhuepf.clijx.matrix.TransposeYZ
    //----------------------------------------------------
    /**
     * Transpose Y and Z axes of an image.
     */
    default boolean transposeYZ(ClearCLBuffer input, ClearCLBuffer destination) {
        return TransposeYZ.transposeYZ(getCLIJx(), input, destination);
    }


    // net.haesleinhuepf.clijx.piv.FastParticleImageVelocimetry
    //----------------------------------------------------
    /**
     * 
     */
    default boolean particleImageVelocimetry2D(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4, double arg5) {
        return FastParticleImageVelocimetry.particleImageVelocimetry2D(getCLIJ(), arg1, arg2, arg3, arg4, new Double (arg5).intValue());
    }

    /**
     * 
     */
    default boolean particleImageVelocimetry2D(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, double arg5) {
        return FastParticleImageVelocimetry.particleImageVelocimetry2D(getCLIJ(), arg1, arg2, arg3, arg4, new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.piv.ParticleImageVelocimetry
    //----------------------------------------------------
    /**
     * For every pixel in source image 1, determine the pixel with the most similar intensity in 
     *  the local neighborhood with a given radius in source image 2. Write the distance in 
     * X and Y in the two corresponding destination images.
     */
    default boolean particleImageVelocimetry(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, ClearCLBuffer arg5, double arg6, double arg7, double arg8, boolean arg9) {
        return ParticleImageVelocimetry.particleImageVelocimetry(getCLIJ(), arg1, arg2, arg3, arg4, arg5, new Double (arg6).intValue(), new Double (arg7).intValue(), new Double (arg8).intValue(), arg9);
    }


    // net.haesleinhuepf.clijx.piv.ParticleImageVelocimetryTimelapse
    //----------------------------------------------------
    /**
     * Run particle image velocimetry on a 2D+t timelapse.
     */
    default boolean particleImageVelocimetryTimelapse(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, int arg5, int arg6, int arg7, boolean arg8) {
        return ParticleImageVelocimetryTimelapse.particleImageVelocimetryTimelapse(getCLIJ(), arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }


    // net.haesleinhuepf.clijx.registration.DeformableRegistration2D
    //----------------------------------------------------
    /**
     * Applies particle image velocimetry to two images and registers them afterwards by warping input image 2 with a smoothed vector field.
     */
    default boolean deformableRegistration2D(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, int arg4, int arg5) {
        return DeformableRegistration2D.deformableRegistration2D(getCLIJ(), arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clijx.registration.TranslationRegistration
    //----------------------------------------------------
    /**
     * Measures center of mass of thresholded objects in the two input images and translates the second image so that it better fits to the first image.
     */
    default boolean translationRegistration(ClearCLBuffer arg1, ClearCLBuffer arg2, double[] arg3) {
        return TranslationRegistration.translationRegistration(getCLIJ(), arg1, arg2, arg3);
    }

    /**
     * Measures center of mass of thresholded objects in the two input images and translates the second image so that it better fits to the first image.
     */
    default boolean translationRegistration(ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination) {
        return TranslationRegistration.translationRegistration(getCLIJ(), input1, input2, destination);
    }


    // net.haesleinhuepf.clijx.registration.TranslationTimelapseRegistration
    //----------------------------------------------------
    /**
     * Applies 2D translation registration to every pair of t, t+1 slices of a 2D+t image stack.
     */
    default boolean translationTimelapseRegistration(ClearCLBuffer input, ClearCLBuffer output) {
        return TranslationTimelapseRegistration.translationTimelapseRegistration(getCLIJ(), input, output);
    }


    // net.haesleinhuepf.clijx.plugins.SetWhereXequalsY
    //----------------------------------------------------
    /**
     * Sets all pixel values a of a given image A to a constant value v in case its coordinates x == y. Otherwise the pixel is not overwritten.
     * If you want to initialize an identity transfrom matrix, set all pixels to 0 first.
     * 
     * <pre>f(a) = v</pre>
     */
    default boolean setWhereXequalsY(ClearCLImage source, double value) {
        return SetWhereXequalsY.setWhereXequalsY(getCLIJ(), source, new Double (value).floatValue());
    }

    /**
     * Sets all pixel values a of a given image A to a constant value v in case its coordinates x == y. Otherwise the pixel is not overwritten.
     * If you want to initialize an identity transfrom matrix, set all pixels to 0 first.
     * 
     * <pre>f(a) = v</pre>
     */
    default boolean setWhereXequalsY(ClearCLBuffer source, double value) {
        return SetWhereXequalsY.setWhereXequalsY(getCLIJ(), source, new Double (value).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.LaplaceSphere
    //----------------------------------------------------
    /**
     * Applies the Laplace operator (Diamond neighborhood) to an image.
     */
    default boolean laplaceSphere(ClearCLBuffer input, ClearCLBuffer destination) {
        return LaplaceSphere.laplaceSphere(getCLIJ(), input, destination);
    }


    // net.haesleinhuepf.clijx.plugins.Image2DToResultsTable
    //----------------------------------------------------
    /**
     * Converts an image into a table.
     */
    default ResultsTable image2DToResultsTable(ClearCLImage arg1, ResultsTable arg2) {
        return Image2DToResultsTable.image2DToResultsTable(getCLIJ(), arg1, arg2);
    }

    /**
     * Converts an image into a table.
     */
    default ResultsTable image2DToResultsTable(ClearCLBuffer arg1, ResultsTable arg2) {
        return Image2DToResultsTable.image2DToResultsTable(getCLIJ(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.WriteValuesToPositions
    //----------------------------------------------------
    /**
     * Takes an image with three/four rows (2D: height = 3; 3D: height = 4): x, y [, z] and v and target image. The value v will be written at position x/y[/z] in the target image.
     */
    default boolean writeValuesToPositions(ClearCLBuffer positionsAndValues, ClearCLBuffer destination) {
        return WriteValuesToPositions.writeValuesToPositions(getCLIJ(), positionsAndValues, destination);
    }


    // net.haesleinhuepf.clijx.plugins.GetSize
    //----------------------------------------------------
    /**
     * Reads out the size of an image [stack] and writes it to the results table in the columns 'Width', 'Height' and 'Depth'.
     */
    default long[] getSize(ClearCLBuffer source) {
        return GetSize.getSize(getCLIJ(), source);
    }


    // net.haesleinhuepf.clijx.matrix.MultiplyMatrix
    //----------------------------------------------------
    /**
     * Multiplies two matrices with each other.
     */
    default boolean multiplyMatrix(ClearCLBuffer matrix1, ClearCLBuffer matrix2, ClearCLBuffer matrix_destination) {
        return MultiplyMatrix.multiplyMatrix(getCLIJx(), matrix1, matrix2, matrix_destination);
    }


    // net.haesleinhuepf.clijx.matrix.MatrixEqual
    //----------------------------------------------------
    /**
     * Checks if all elements of a matrix are different by less than or equal to a given tolerance.
     * The result will be put in the results table as 1 if yes and 0 otherwise.
     */
    default boolean matrixEqual(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return MatrixEqual.matrixEqual(getCLIJ(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.PowerImages
    //----------------------------------------------------
    /**
     * Calculates x to the power of y pixel wise of two images X and Y.
     */
    default boolean powerImages(ClearCLBuffer input, ClearCLBuffer exponent, ClearCLBuffer destination) {
        return PowerImages.powerImages(getCLIJ(), input, exponent, destination);
    }


    // net.haesleinhuepf.clijx.plugins.Equal
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a == b; 0 otherwise. 
     */
    default boolean equal(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return Equal.equal(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.GreaterOrEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater or equal pixel wise.
     * 
     * f(a, b) = 1 if a >= b; 0 otherwise. 
     */
    default boolean greaterOrEqual(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return GreaterOrEqual.greaterOrEqual(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.Greater
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater pixel wise.
     * 
     * f(a, b) = 1 if a > b; 0 otherwise. 
     */
    default boolean greater(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return Greater.greater(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.Smaller
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller pixel wise.
     * 
     * f(a, b) = 1 if a < b; 0 otherwise. 
     */
    default boolean smaller(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return Smaller.smaller(getCLIJx(), source1, source2, destination);
    }


    // net.haesleinhuepf.clijx.plugins.SmallerOrEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller or equal pixel wise.
     * 
     * f(a, b) = 1 if a <= b; 0 otherwise. 
     */
    default boolean smallerOrEqual(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return SmallerOrEqual.smallerOrEqual(getCLIJx(), source1, source2, destination);
    }


    // net.haesleinhuepf.clijx.plugins.NotEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a != b; 0 otherwise. 
     */
    default boolean notEqual(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLBuffer arg3) {
        return NotEqual.notEqual(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.io.ReadImageFromDisc
    //----------------------------------------------------
    /**
     * Read an image from disc.
     */
    default ClearCLBuffer readImageFromDisc(String arg1) {
        return ReadImageFromDisc.readImageFromDisc(getCLIJ(), arg1);
    }


    // net.haesleinhuepf.clijx.io.ReadRawImageFromDisc
    //----------------------------------------------------
    /**
     * Reads a raw file from disc and pushes it immediately to the GPU.
     */
    default ClearCLBuffer readRawImageFromDisc(String arg1, double arg2, double arg3, double arg4, double arg5) {
        return ReadRawImageFromDisc.readRawImageFromDisc(getCLIJ(), arg1, new Double (arg2).intValue(), new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Reads a raw file from disc and pushes it immediately to the GPU.
     */
    default boolean readRawImageFromDisc(ClearCLBuffer arg1, String arg2) {
        return ReadRawImageFromDisc.readRawImageFromDisc(getCLIJ(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.io.PreloadFromDisc
    //----------------------------------------------------
    /**
     * This plugin takes two image filenames and loads them into RAM. The first image is returned immediately, the second image is loaded in the background and  will be returned when the plugin is called again.
     * 
     *  It is assumed that all images have the same size. If this is not the case, call release(image) before  getting the second image.
     */
    default ClearCLBuffer preloadFromDisc(ClearCLBuffer arg1, String arg2, String arg3, String arg4) {
        return PreloadFromDisc.preloadFromDisc(getCLIJ(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clijx.plugins.EqualConstant
    //----------------------------------------------------
    /**
     * Determines if an image A and a constant b are equal.
     * 
     * f(a, b) = 1 if a == b; 0 otherwise. 
     */
    default boolean equalConstant(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return EqualConstant.equalConstant(getCLIJx(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.GreaterOrEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater or equal pixel wise.
     * 
     * f(a, b) = 1 if a >= b; 0 otherwise. 
     */
    default boolean greaterOrEqualConstant(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return GreaterOrEqualConstant.greaterOrEqualConstant(getCLIJx(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.GreaterConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater pixel wise.
     * 
     * f(a, b) = 1 if a > b; 0 otherwise. 
     */
    default boolean greaterConstant(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return GreaterConstant.greaterConstant(getCLIJx(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.SmallerConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller pixel wise.
     * 
     * f(a, b) = 1 if a < b; 0 otherwise. 
     */
    default boolean smallerConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return SmallerConstant.smallerConstant(getCLIJx(), source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.SmallerOrEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller or equal pixel wise.
     * 
     * f(a, b) = 1 if a <= b; 0 otherwise. 
     */
    default boolean smallerOrEqualConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return SmallerOrEqualConstant.smallerOrEqualConstant(getCLIJx(), source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.NotEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a != b; 0 otherwise. 
     */
    default boolean notEqualConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return NotEqualConstant.notEqualConstant(getCLIJx(), source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clijx.painting.DrawBox
    //----------------------------------------------------
    /**
     * Draws a box at a given start point with given size. All pixels other than in the box are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    default boolean drawBox(ClearCLBuffer arg1, double arg2, double arg3, double arg4, double arg5) {
        return DrawBox.drawBox(getCLIJ(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Draws a box at a given start point with given size. All pixels other than in the box are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    default boolean drawBox(ClearCLBuffer destination, double x, double y, double z, double width, double height, double depth) {
        return DrawBox.drawBox(getCLIJ(), destination, new Double (x).floatValue(), new Double (y).floatValue(), new Double (z).floatValue(), new Double (width).floatValue(), new Double (height).floatValue(), new Double (depth).floatValue());
    }


    // net.haesleinhuepf.clijx.painting.DrawLine
    //----------------------------------------------------
    /**
     * Draws a line between two points with a given thickness. All pixels other than on the line are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    default boolean drawLine(ClearCLBuffer destination, double x1, double y1, double z1, double x2, double y2, double z2, double thickness) {
        return DrawLine.drawLine(getCLIJx(), destination, new Double (x1).floatValue(), new Double (y1).floatValue(), new Double (z1).floatValue(), new Double (x2).floatValue(), new Double (y2).floatValue(), new Double (z2).floatValue(), new Double (thickness).floatValue());
    }


    // net.haesleinhuepf.clijx.painting.DrawSphere
    //----------------------------------------------------
    /**
     * Draws a sphere around a given point with given radii in x, y and z (if 3D). All pixels other than in the sphere are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    default boolean drawSphere(ClearCLBuffer arg1, double arg2, double arg3, double arg4, double arg5) {
        return DrawSphere.drawSphere(getCLIJ(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Draws a sphere around a given point with given radii in x, y and z (if 3D). All pixels other than in the sphere are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    default boolean drawSphere(ClearCLBuffer destination, double x, double y, double z, double radius_x, double radius_y, double radius_z) {
        return DrawSphere.drawSphere(getCLIJ(), destination, new Double (x).floatValue(), new Double (y).floatValue(), new Double (z).floatValue(), new Double (radius_x).floatValue(), new Double (radius_y).floatValue(), new Double (radius_z).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.ReplaceIntensity
    //----------------------------------------------------
    /**
     * Replaces a specific intensity in an image with a given new value.
     */
    default boolean replaceIntensity(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return ReplaceIntensity.replaceIntensity(getCLIJ(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.BoundingBox
    //----------------------------------------------------
    /**
     * Determines the bounding box of all non-zero pixels in a binary image. The positions will be stored in a new row of ImageJs
     * Results table in the column 'BoundingBoxX', 'BoundingBoxY', 'BoundingBoxZ', 'BoundingBoxWidth', 'BoundingBoxHeight' 'BoundingBoxDepth'.In case of 2D images Z and depth will be zero.
     */
    default double[] boundingBox(ClearCLBuffer source) {
        return BoundingBox.boundingBox(getCLIJ(), source);
    }


    // net.haesleinhuepf.clijx.plugins.MinimumOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the minimum intensity in an image, but only in pixels which have non-zero values in another mask image.
     */
    default double minimumOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return MinimumOfMaskedPixels.minimumOfMaskedPixels(getCLIJ(), source, mask);
    }


    // net.haesleinhuepf.clijx.plugins.MaximumOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the maximum intensity in an image, but only in pixels which have non-zero values in another mask image.
     */
    default double maximumOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return MaximumOfMaskedPixels.maximumOfMaskedPixels(getCLIJ(), source, mask);
    }


    // net.haesleinhuepf.clijx.plugins.MeanOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the mean intensity in an image, but only in pixels which have non-zero values in another binary mask image.
     */
    default double meanOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return MeanOfMaskedPixels.meanOfMaskedPixels(getCLIJx(), source, mask);
    }


    // net.haesleinhuepf.clijx.plugins.LabelToMask
    //----------------------------------------------------
    /**
     * Masks a single label in a label map: Sets all pixels in the target image to 1, where the given label index was present in the label map. Other pixels are set to 0.
     */
    default boolean labelToMask(ClearCLBuffer label_map_source, ClearCLBuffer mask_destination, double label_index) {
        return LabelToMask.labelToMask(getCLIJx(), label_map_source, mask_destination, new Double (label_index).floatValue());
    }


    // net.haesleinhuepf.clijx.matrix.NClosestPoints
    //----------------------------------------------------
    /**
     * Determine the n point indices with shortest distance for all points in a distance matrix.
     * This corresponds to the n row indices with minimum values for each column of the distance matrix.
     */
    default boolean nClosestPoints(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return NClosestPoints.nClosestPoints(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.matrix.GaussJordan
    //----------------------------------------------------
    /**
     * Gauss Jordan elimination algorithm for solving linear equation systems. Ent the equation coefficients as an n*n sized image A and an n*1 sized image B:
     * <pre>a(1,1)*x + a(2,1)*y + a(3,1)+z = b(1)
     * a(2,1)*x + a(2,2)*y + a(3,2)+z = b(2)
     * a(3,1)*x + a(3,2)*y + a(3,3)+z = b(3)
     * </pre>
     * The results will then be given in an n*1 image with values [x, y, z].
     * 
     * Adapted from: 
     * https://github.com/qbunia/rodinia/blob/master/opencl/gaussian/gaussianElim_kernels.cl
     * L.G. Szafaryn, K. Skadron and J. Saucerman. "Experiences Accelerating MATLAB Systems
     * //Biology Applications." in Workshop on Biomedicine in Computing (BiC) at the International
     * //Symposium on Computer Architecture (ISCA), June 2009.
     */
    default boolean gaussJordan(ClearCLBuffer A_matrix, ClearCLBuffer B_result_vector, ClearCLBuffer solution_destination) {
        return GaussJordan.gaussJordan(getCLIJ(), A_matrix, B_result_vector, solution_destination);
    }


    // net.haesleinhuepf.clijx.plugins.StatisticsOfLabelledPixels
    //----------------------------------------------------
    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     */
    default double[] statisticsOfLabelledPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3) {
        return StatisticsOfLabelledPixels.statisticsOfLabelledPixels(getCLIJ(), arg1, arg2, arg3);
    }

    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     */
    default ResultsTable statisticsOfLabelledPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, ResultsTable arg3) {
        return StatisticsOfLabelledPixels.statisticsOfLabelledPixels(getCLIJ(), arg1, arg2, arg3);
    }

    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     */
    default double[][] statisticsOfLabelledPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4) {
        return StatisticsOfLabelledPixels.statisticsOfLabelledPixels(getCLIJ(), arg1, arg2, arg3, arg4);
    }

    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     */
    default double[][] statisticsOfLabelledPixels(ClearCLBuffer input, ClearCLBuffer labelmap) {
        return StatisticsOfLabelledPixels.statisticsOfLabelledPixels(getCLIJ(), input, labelmap);
    }


    // net.haesleinhuepf.clijx.plugins.VarianceOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the variance of all pixels in an image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Variance'.
     */
    default double varianceOfAllPixels(ClearCLBuffer source) {
        return VarianceOfAllPixels.varianceOfAllPixels(getCLIJx(), source);
    }

    /**
     * Determines the variance of all pixels in an image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Variance'.
     */
    default double varianceOfAllPixels(ClearCLImageInterface arg1, double arg2) {
        return VarianceOfAllPixels.varianceOfAllPixels(getCLIJx(), arg1, new Double (arg2).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.StandardDeviationOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the standard deviation of all pixels in an image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Standard_deviation'.
     */
    default double standardDeviationOfAllPixels(ClearCLImageInterface arg1) {
        return StandardDeviationOfAllPixels.standardDeviationOfAllPixels(getCLIJx(), arg1);
    }

    /**
     * Determines the standard deviation of all pixels in an image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Standard_deviation'.
     */
    default double standardDeviationOfAllPixels(ClearCLImageInterface arg1, double arg2) {
        return StandardDeviationOfAllPixels.standardDeviationOfAllPixels(getCLIJx(), arg1, new Double (arg2).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.VarianceOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the variance in an image, but only in pixels which have non-zero values in another binary mask image. The result is put in the results table as new column named 'Masked_variance'.
     */
    default double varianceOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return VarianceOfMaskedPixels.varianceOfMaskedPixels(getCLIJx(), source, mask);
    }

    /**
     * Determines the variance in an image, but only in pixels which have non-zero values in another binary mask image. The result is put in the results table as new column named 'Masked_variance'.
     */
    default double varianceOfMaskedPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return VarianceOfMaskedPixels.varianceOfMaskedPixels(getCLIJx(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.StandardDeviationOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the standard deviation of all pixels in an image which have non-zero value in a corresponding mask image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Masked_standard_deviation'.
     */
    default double standardDeviationOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return StandardDeviationOfMaskedPixels.standardDeviationOfMaskedPixels(getCLIJx(), source, mask);
    }

    /**
     * Determines the standard deviation of all pixels in an image which have non-zero value in a corresponding mask image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Masked_standard_deviation'.
     */
    default double standardDeviationOfMaskedPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return StandardDeviationOfMaskedPixels.standardDeviationOfMaskedPixels(getCLIJx(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.ExcludeLabelsOnEdges
    //----------------------------------------------------
    /**
     * Removes all labels from a label map which touch the edges of the image (in X, Y and Z if the image is 3D). Remaining label elements are renumbered afterwards.
     */
    default boolean excludeLabelsOnEdges(ClearCLBuffer label_map_input, ClearCLBuffer label_map_destination) {
        return ExcludeLabelsOnEdges.excludeLabelsOnEdges(getCLIJ(), label_map_input, label_map_destination);
    }


    // net.haesleinhuepf.clijx.plugins.BinarySubtract
    //----------------------------------------------------
    /**
     * Subtracts one binary image from another.
     */
    default boolean binarySubtract(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return BinarySubtract.binarySubtract(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.BinaryEdgeDetection
    //----------------------------------------------------
    /**
     * Determines pixels/voxels which are on the surface of a binary objects and sets only them to 1 in the destination image. All other pixels are set to 0.
     */
    default boolean binaryEdgeDetection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return BinaryEdgeDetection.binaryEdgeDetection(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.DistanceMap
    //----------------------------------------------------
    /**
     * Generates a distance map from a binary image. Pixels with non-zero value in the binary image are set to a number representing the distance to the closest zero-value pixel.
     */
    default boolean distanceMap(ClearCLBuffer source, ClearCLBuffer destination) {
        return DistanceMap.distanceMap(getCLIJx(), source, destination);
    }

    /**
     * 
     */
    default boolean localPositiveMinimum(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return DistanceMap.localPositiveMinimum(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.PullAsROI
    //----------------------------------------------------
    /**
     * Pulls a binary image from the GPU memory and puts it on the currently active ImageJ window.
     */
    default Roi pullAsROI(ClearCLBuffer binary_input) {
        return PullAsROI.pullAsROI(getCLIJx(), binary_input);
    }


    // net.haesleinhuepf.clijx.plugins.PullLabelsToROIManager
    //----------------------------------------------------
    /**
     * Pulls all labels in a label map as ROIs to the ROI manager.
     */
    default boolean pullLabelsToROIManager(ClearCLBuffer arg1, RoiManager arg2) {
        return PullLabelsToROIManager.pullLabelsToROIManager(getCLIJx(), arg1, arg2);
    }

    /**
     * Pulls all labels in a label map as ROIs to the ROI manager.
     */
    default boolean pullLabelsToROIManager(ClearCLBuffer binary_input) {
        return PullLabelsToROIManager.pullLabelsToROIManager(getCLIJx(), binary_input);
    }


    // net.haesleinhuepf.clijx.plugins.NonzeroMaximumDiamond
    //----------------------------------------------------
    /**
     * Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default boolean nonzeroMaximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return NonzeroMaximumDiamond.nonzeroMaximumDiamond(getCLIJ(), arg1, arg2, arg3);
    }

    /**
     * Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default boolean nonzeroMaximumDiamond(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3) {
        return NonzeroMaximumDiamond.nonzeroMaximumDiamond(getCLIJx(), arg1, arg2, arg3);
    }

    /**
     * Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default ClearCLKernel nonzeroMaximumDiamond(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMaximumDiamond.nonzeroMaximumDiamond(getCLIJx(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clijx.plugins.OnlyzeroOverwriteMaximumDiamond
    //----------------------------------------------------
    /**
     * TODO
     */
    default boolean onlyzeroOverwriteMaximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return OnlyzeroOverwriteMaximumDiamond.onlyzeroOverwriteMaximumDiamond(getCLIJ(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.OnlyzeroOverwriteMaximumBox
    //----------------------------------------------------
    /**
     * 
     */
    default boolean onlyzeroOverwriteMaximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return OnlyzeroOverwriteMaximumBox.onlyzeroOverwriteMaximumBox(getCLIJ(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.matrix.GenerateTouchMatrix
    //----------------------------------------------------
    /**
     * Takes a labelmap with n labels and generates a (n+1)*(n+1) matrix where all pixels are set to 0 exept those where labels are touching.Only half of the matrix is filled (with x < y). For example, if labels 3 and 4 are touching then the pixel (3,4) in the matrix will be set to 1.
     */
    default boolean generateTouchMatrix(ClearCLBuffer label_map, ClearCLBuffer touch_matrix_destination) {
        return GenerateTouchMatrix.generateTouchMatrix(getCLIJx(), label_map, touch_matrix_destination);
    }


    // net.haesleinhuepf.clijx.plugins.DetectLabelEdges
    //----------------------------------------------------
    /**
     * Takes a labelmap and returns an image where all pixels on label edges are set to 1 and all other pixels to 0.
     */
    default boolean detectLabelEdges(ClearCLBuffer label_map, ClearCLBuffer edge_image_destination) {
        return DetectLabelEdges.detectLabelEdges(getCLIJx(), label_map, edge_image_destination);
    }


    // net.haesleinhuepf.clijx.plugins.StopWatch
    //----------------------------------------------------
    /**
     * Measures time and outputs delay to last call.
     */
    default boolean stopWatch(String arg1) {
        return StopWatch.stopWatch(getCLIJ(), arg1);
    }


    // net.haesleinhuepf.clijx.matrix.CountTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touching-neighbors-matrix as input and delivers a vector with number of touching neighbors per label as a vector.
     */
    default boolean countTouchingNeighbors(ClearCLBuffer touch_matrix, ClearCLBuffer touching_neighbors_count_destination) {
        return CountTouchingNeighbors.countTouchingNeighbors(getCLIJx(), touch_matrix, touching_neighbors_count_destination);
    }


    // net.haesleinhuepf.clijx.plugins.ReplaceIntensities
    //----------------------------------------------------
    /**
     * Replaces integer intensities specified in a vector image. The vector image must be 3D with size (m, 1, 1) where m corresponds to the maximum intensity in the original image. Assuming the vector image contains values (0, 1, 0, 2) means: 
     *  * All pixels with value 0 (first entry in the vector image) get value 0
     *  * All pixels with value 1 get value 1
     *  * All pixels with value 2 get value 0
     *  * All pixels with value 3 get value 2
     * 
     */
    default boolean replaceIntensities(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return ReplaceIntensities.replaceIntensities(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.painting.DrawTwoValueLine
    //----------------------------------------------------
    /**
     * Draws a line between two points with a given thickness. Pixels close to point 1 are set to value1. Pixels closer to point 2 are set to value2 All pixels other than on the line are untouched. Consider using clij.set(buffer, 0); in advance.
     */
    default boolean drawTwoValueLine(ClearCLBuffer destination, double x1, double y1, double z1, double x2, double y2, double z2, double thickness, double value1, double destination0) {
        return DrawTwoValueLine.drawTwoValueLine(getCLIJx(), destination, new Double (x1).floatValue(), new Double (y1).floatValue(), new Double (z1).floatValue(), new Double (x2).floatValue(), new Double (y2).floatValue(), new Double (z2).floatValue(), new Double (thickness).floatValue(), new Double (value1).floatValue(), new Double (destination0).floatValue());
    }


    // net.haesleinhuepf.clijx.matrix.AverageDistanceOfNClosestPoints
    //----------------------------------------------------
    /**
     * Determine the n point indices with shortest distance for all points in a distance matrix.
     * This corresponds to the n row indices with minimum values for each column of the distance matrix.
     */
    default boolean averageDistanceOfClosestPoints(ClearCLBuffer distance_matrix, ClearCLBuffer indexlist_destination, double nClosestPointsTofind) {
        return AverageDistanceOfNClosestPoints.averageDistanceOfClosestPoints(getCLIJx(), distance_matrix, indexlist_destination, new Double (nClosestPointsTofind).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.SaveAsTIF
    //----------------------------------------------------
    /**
     * Pulls an image from the GPU memory and saves it as TIF to disc.
     */
    default boolean saveAsTIF(ClearCLBuffer arg1, String arg2) {
        return SaveAsTIF.saveAsTIF(getCLIJ(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.ConnectedComponentsLabelingInplace
    //----------------------------------------------------
    /**
     * Performs connected components analysis to a binary image and generates a label map.
     */
    default boolean connectedComponentsLabelingInplace(ClearCLBuffer binary_source_labeling_destination) {
        return ConnectedComponentsLabelingInplace.connectedComponentsLabelingInplace(getCLIJx(), binary_source_labeling_destination);
    }


    // net.haesleinhuepf.clijx.matrix.TouchMatrixToMesh
    //----------------------------------------------------
    /**
     * Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a touch matrix of size n*n to draw lines from all points to points if the corresponding pixel in the touch matrix is 1.
     */
    default boolean touchMatrixToMesh(ClearCLBuffer pointlist, ClearCLBuffer touch_matrix, ClearCLBuffer mesh_destination) {
        return TouchMatrixToMesh.touchMatrixToMesh(getCLIJx(), pointlist, touch_matrix, mesh_destination);
    }


    // net.haesleinhuepf.clijx.plugins.AutomaticThresholdInplace
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one 
     * of these methods in the method text field:
     * [Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]
     */
    default boolean automaticThresholdInplace(ClearCLBuffer arg1, String arg2) {
        return AutomaticThresholdInplace.automaticThresholdInplace(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.DifferenceOfGaussianInplace3D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    default boolean differenceOfGaussianInplace3D(ClearCLBuffer input_and_destination, double sigma1x, double sigma1y, double sigma1z, double sigma2x, double sigma2y, double sigma2z) {
        return DifferenceOfGaussianInplace3D.differenceOfGaussianInplace3D(getCLIJ(), input_and_destination, new Double (sigma1x).floatValue(), new Double (sigma1y).floatValue(), new Double (sigma1z).floatValue(), new Double (sigma2x).floatValue(), new Double (sigma2y).floatValue(), new Double (sigma2z).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.AbsoluteInplace
    //----------------------------------------------------
    /**
     * Computes the absolute value of every individual pixel x in a given image.
     * 
     * <pre>f(x) = |x| </pre>
     */
    default boolean absoluteInplace(ClearCLBuffer arg1) {
        return AbsoluteInplace.absoluteInplace(getCLIJx(), arg1);
    }


    // net.haesleinhuepf.clijx.plugins.Resample
    //----------------------------------------------------
    /**
     * Resamples an image with given size factors using an affine transform.
     */
    default boolean resample(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5, boolean arg6) {
        return Resample.resample(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6);
    }


    // net.haesleinhuepf.clijx.plugins.EqualizeMeanIntensitiesOfSlices
    //----------------------------------------------------
    /**
     * Determines correction factors for each z-slice so that the average intensity in all slices can be made the same and multiplies these factors with the slices.
     * This functionality is similar to the 'Simple Ratio Bleaching Correction' in Fiji.
     */
    default boolean equalizeMeanIntensitiesOfSlices(ClearCLBuffer input, ClearCLBuffer destination, double referenceSlice) {
        return EqualizeMeanIntensitiesOfSlices.equalizeMeanIntensitiesOfSlices(getCLIJ(), input, destination, new Double (referenceSlice).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Watershed
    //----------------------------------------------------
    /**
     * Apply a binary watershed to a binary image and introduces black pixels between objects.
     */
    default boolean watershed(ClearCLBuffer binary_source, ClearCLBuffer destination) {
        return Watershed.watershed(getCLIJx(), binary_source, destination);
    }


    // net.haesleinhuepf.clijx.plugins.ResliceRadial
    //----------------------------------------------------
    /**
     * Computes a radial projection of an image stack. Starting point for the line is the center in any 
     * X/Y-plane of a given input image stack. This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.
     */
    default boolean resliceRadial(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8) {
        return ResliceRadial.resliceRadial(getCLIJ(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue());
    }

    /**
     * Computes a radial projection of an image stack. Starting point for the line is the center in any 
     * X/Y-plane of a given input image stack. This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.
     */
    default boolean resliceRadial(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return ResliceRadial.resliceRadial(getCLIJ(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes a radial projection of an image stack. Starting point for the line is the center in any 
     * X/Y-plane of a given input image stack. This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.
     */
    default boolean resliceRadial(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return ResliceRadial.resliceRadial(getCLIJ(), arg1, arg2, new Double (arg3).floatValue());
    }

    /**
     * 
     */
    @Deprecated
    default boolean radialProjection(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return ResliceRadial.radialProjection(getCLIJ(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.ShowRGB
    //----------------------------------------------------
    /**
     * Visualises three 2D images as one RGB image
     */
    default boolean showRGB(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, String arg4) {
        return ShowRGB.showRGB(getCLIJ(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clijx.plugins.ShowGrey
    //----------------------------------------------------
    /**
     * Visualises a single 2D image.
     */
    default boolean showGrey(ClearCLBuffer arg1, String arg2) {
        return ShowGrey.showGrey(getCLIJ(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.Sobel
    //----------------------------------------------------
    /**
     * Convolve the image with the Sobel kernel.
     */
    default boolean sobel(ClearCLBuffer source, ClearCLBuffer destination) {
        return Sobel.sobel(getCLIJ(), source, destination);
    }


    // net.haesleinhuepf.clijx.plugins.Absolute
    //----------------------------------------------------
    /**
     * Computes the absolute value of every individual pixel x in a given image.
     * 
     * <pre>f(x) = |x| </pre>
     */
    default boolean absolute(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return Absolute.absolute(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.LaplaceBox
    //----------------------------------------------------
    /**
     * Applies the Laplace operator (Box neighborhood) to an image.
     */
    default boolean laplaceBox(ClearCLBuffer input, ClearCLBuffer destination) {
        return LaplaceBox.laplaceBox(getCLIJ(), input, destination);
    }


    // net.haesleinhuepf.clijx.plugins.BottomHatBox
    //----------------------------------------------------
    /**
     * Apply a bottom-hat filter for background subtraction to the input image.
     */
    default boolean bottomHatBox(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return BottomHatBox.bottomHatBox(getCLIJ(), input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.BottomHatSphere
    //----------------------------------------------------
    /**
     * Applies a bottom-hat filter for background subtraction to the input image.
     */
    default boolean bottomHatSphere(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return BottomHatSphere.bottomHatSphere(getCLIJ(), input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.ClosingBox
    //----------------------------------------------------
    /**
     * Apply a binary closing to the input image by calling n dilations and n erosions subsequenntly.
     */
    default boolean closingBox(ClearCLBuffer input, ClearCLBuffer destination, double number_of_dilations_and_erosions) {
        return ClosingBox.closingBox(getCLIJ(), input, destination, new Double (number_of_dilations_and_erosions).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.ClosingDiamond
    //----------------------------------------------------
    /**
     * Apply a binary closing to the input image by calling n dilations and n erosions subsequenntly.
     */
    default boolean closingDiamond(ClearCLBuffer input, ClearCLBuffer destination, double number_of_dilations_and_erotions) {
        return ClosingDiamond.closingDiamond(getCLIJ(), input, destination, new Double (number_of_dilations_and_erotions).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.OpeningBox
    //----------------------------------------------------
    /**
     * Apply a binary opening to the input image by calling n erosions and n dilations subsequenntly.
     */
    default boolean openingBox(ClearCLBuffer input, ClearCLBuffer destination, double number_of_erotions_and_dilations) {
        return OpeningBox.openingBox(getCLIJ(), input, destination, new Double (number_of_erotions_and_dilations).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.OpeningDiamond
    //----------------------------------------------------
    /**
     * Apply a binary opening to the input image by calling n erosions and n dilations subsequenntly.
     */
    default boolean openingDiamond(ClearCLBuffer input, ClearCLBuffer destination, double number_of_erotions_and_dilations) {
        return OpeningDiamond.openingDiamond(getCLIJ(), input, destination, new Double (number_of_erotions_and_dilations).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.MaximumXProjection
    //----------------------------------------------------
    /**
     * Determines the maximum projection of an image along X.
     */
    default boolean maximumXProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return MaximumXProjection.maximumXProjection(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.MaximumYProjection
    //----------------------------------------------------
    /**
     * Determines the maximum projection of an image along X.
     */
    default boolean maximumYProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return MaximumYProjection.maximumYProjection(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.ProjectMaximumZBounded
    //----------------------------------------------------
    /**
     * Determines the maximum projection of an image along Z within a given z range.
     */
    default boolean projectMaximumZBounded(ClearCLBuffer source, ClearCLBuffer destination_max, double min_z, double max_z) {
        return ProjectMaximumZBounded.projectMaximumZBounded(getCLIJ(), source, destination_max, new Double (min_z).intValue(), new Double (max_z).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.ProjectMinimumZBounded
    //----------------------------------------------------
    /**
     * Determines the minimum projection of an image along Z within a given z range.
     */
    default boolean projectMinimumZBounded(ClearCLBuffer source, ClearCLBuffer destination_min, double min_z, double max_z) {
        return ProjectMinimumZBounded.projectMinimumZBounded(getCLIJ(), source, destination_min, new Double (min_z).intValue(), new Double (max_z).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.ProjectMeanZBounded
    //----------------------------------------------------
    /**
     * Determines the mean projection of an image along Z within a given z range.
     */
    default boolean projectMeanZBounded(ClearCLBuffer source, ClearCLBuffer destination_mean, double min_z, double max_z) {
        return ProjectMeanZBounded.projectMeanZBounded(getCLIJ(), source, destination_mean, new Double (min_z).intValue(), new Double (max_z).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.NonzeroMaximumBox
    //----------------------------------------------------
    /**
     * Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default ClearCLKernel nonzeroMaximumBox(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMaximumBox.nonzeroMaximumBox(getCLIJx(), arg1, arg2, arg3, arg4);
    }

    /**
     * Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default boolean nonzeroMaximumBox(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3) {
        return NonzeroMaximumBox.nonzeroMaximumBox(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.NonzeroMinimumBox
    //----------------------------------------------------
    /**
     * 
     */
    default ClearCLKernel nonzeroMinimumBox(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMinimumBox.nonzeroMinimumBox(getCLIJx(), arg1, arg2, arg3, arg4);
    }

    /**
     * 
     */
    default boolean nonzeroMinimumBox(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3) {
        return NonzeroMinimumBox.nonzeroMinimumBox(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.ProjectMinimumThresholdedZBounded
    //----------------------------------------------------
    /**
     * Determines the minimum projection of all pixels in an image above a given threshold along Z within a given z range.
     */
    default boolean projectMinimumThresholdedZBounded(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return ProjectMinimumThresholdedZBounded.projectMinimumThresholdedZBounded(getCLIJ(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.MeanOfPixelsAboveThreshold
    //----------------------------------------------------
    /**
     * Determines the mean intensity in an image, but only in pixels which are above a given threshold.
     */
    default double meanOfPixelsAboveThreshold(ClearCLBuffer source, double threshold) {
        return MeanOfPixelsAboveThreshold.meanOfPixelsAboveThreshold(getCLIJx(), source, new Double (threshold).floatValue());
    }


    // net.haesleinhuepf.clijx.gui.OrganiseWindows
    //----------------------------------------------------
    /**
     * Organises windows on screen.
     */
    default boolean organiseWindows(int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
        return OrganiseWindows.organiseWindows(getCLIJ(), arg1, arg2, arg3, arg4, arg5, arg6);
    }


    // net.haesleinhuepf.clijx.matrix.DistanceMatrixToMesh
    //----------------------------------------------------
    /**
     * Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a distance matrix of size n*n to draw lines from all points to points if the corresponding pixel in the distance matrix is smaller than a given distance threshold.
     */
    default boolean distanceMatrixToMesh(ClearCLBuffer pointlist, ClearCLBuffer distance_matrix, ClearCLBuffer mesh_destination, double maximumDistance) {
        return DistanceMatrixToMesh.distanceMatrixToMesh(getCLIJx(), pointlist, distance_matrix, mesh_destination, new Double (maximumDistance).floatValue());
    }


    // net.haesleinhuepf.clijx.matrix.PointIndexListToMesh
    //----------------------------------------------------
    /**
     * Meshes all points in a given point list which are indiced in a corresponding index list. TODO: Explain better
     */
    default boolean pointIndexListToMesh(ClearCLBuffer pointlist, ClearCLBuffer indexList, ClearCLBuffer Mesh) {
        return PointIndexListToMesh.pointIndexListToMesh(getCLIJx(), pointlist, indexList, Mesh);
    }


    // net.haesleinhuepf.clijx.plugins.MinimumOctagon
    //----------------------------------------------------
    /**
     * Applies a minimum filter with kernel size 3x3 n times to an image iteratively. Odd iterations are done with box neighborhood, even iterations with a diamond. Thus, with n > 2, the filter shape is an octagon. The given number of iterations makes the filter result very similar to minimum sphere. Approximately:radius = iterations - 2
     */
    default boolean minimumOctagon(ClearCLBuffer input, ClearCLBuffer destination, double iterations) {
        return MinimumOctagon.minimumOctagon(getCLIJx(), input, destination, new Double (iterations).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.MaximumOctagon
    //----------------------------------------------------
    /**
     * Applies a maximum filter with kernel size 3x3 n times to an image iteratively. Odd iterations are done with box neighborhood, even iterations with a diamond. Thus, with n > 2, the filter shape is an octagon. The given number of iterations makes the filter result very similar to minimum sphere. Approximately:radius = iterations - 2
     */
    default boolean maximumOctagon(ClearCLBuffer input, ClearCLBuffer destination, double iterations) {
        return MaximumOctagon.maximumOctagon(getCLIJx(), input, destination, new Double (iterations).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.TopHatOctagon
    //----------------------------------------------------
    /**
     * Applies a minimum filter with kernel size 3x3 n times to an image iteratively. Odd iterations are done with box neighborhood, even iterations with a diamond. Thus, with n > 2, the filter shape is an octagon. The given number of iterations - 2 makes the filter result very similar to minimum sphere.
     */
    default boolean topHatOctagon(ClearCLBuffer input, ClearCLBuffer destination, double iterations) {
        return TopHatOctagon.topHatOctagon(getCLIJx(), input, destination, new Double (iterations).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.AddImages
    //----------------------------------------------------
    /**
     * Calculates the sum of pairs of pixels x and y of two images X and Y.
     * 
     * <pre>f(x, y) = x + y</pre>
     */
    default boolean addImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return AddImages.addImages(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.AddImagesWeighted
    //----------------------------------------------------
    /**
     * Calculates the sum of pairs of pixels x and y from images X and Y weighted with factors a and b.
     * 
     * <pre>f(x, y, a, b) = x * a + y * b</pre>
     */
    default boolean addImagesWeighted(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, double arg4, double arg5) {
        return AddImagesWeighted.addImagesWeighted(getCLIJ2(), arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.SubtractImages
    //----------------------------------------------------
    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    @Deprecated
    default boolean subtract(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return SubtractImages.subtract(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    default boolean subtractImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return SubtractImages.subtractImages(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.ShowGlasbeyOnGrey
    //----------------------------------------------------
    /**
     * Visualises two 2D images as one RGB image. The first channel is shown in grey, the second with glasbey LUT.
     */
    default boolean showGlasbeyOnGrey(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return ShowGlasbeyOnGrey.showGlasbeyOnGrey(getCLIJ(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.weka.ApplyWekaModel
    //----------------------------------------------------
    /**
     * Applies a Weka model using functionality of Fijis Trainable Weka Segmentation plugin.
     * It takes a 3D feature stack (e.g. first plane original image, second plane blurred, third plane edge image)and applies a pre-trained a Weka model. Take care that the feature stack has been generated in the sameway as for training the model!
     */
    default boolean applyWekaModel(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return ApplyWekaModel.applyWekaModel(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.weka.TrainWekaModel
    //----------------------------------------------------
    /**
     * Trains a Weka model using functionality of Fijis Trainable Weka Segmentation plugin.
     * It takes a 3D feature stack (e.g. first plane original image, second plane blurred, third plane edge image)and trains a Weka model. This model will be saved to disc.
     * The given groundTruth image is supposed to be a label map where pixels with value 1 represent class 1, pixels with value 2 represent class 2 and so on. Pixels with value 0 will be ignored for training.
     */
    default boolean trainWekaModel(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return TrainWekaModel.trainWekaModel(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.AffineTransform2D
    //----------------------------------------------------
    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform2D(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return AffineTransform2D.affineTransform2D(getCLIJx(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform2D(ClearCLImage arg1, ClearCLImageInterface arg2, net.imglib2.realtransform.AffineTransform2D arg3) {
        return AffineTransform2D.affineTransform2D(getCLIJx(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform2D(ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3) {
        return AffineTransform2D.affineTransform2D(getCLIJx(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform2D(ClearCLBuffer arg1, ClearCLBuffer arg2, net.imglib2.realtransform.AffineTransform2D arg3) {
        return AffineTransform2D.affineTransform2D(getCLIJx(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform2D(ClearCLImage arg1, ClearCLImageInterface arg2, float[] arg3) {
        return AffineTransform2D.affineTransform2D(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.AffineTransform3D
    //----------------------------------------------------
    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform3D(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return AffineTransform3D.affineTransform3D(getCLIJx(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform3D(ClearCLImage arg1, ClearCLImageInterface arg2, net.imglib2.realtransform.AffineTransform3D arg3) {
        return AffineTransform3D.affineTransform3D(getCLIJx(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform3D(ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3) {
        return AffineTransform3D.affineTransform3D(getCLIJx(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform3D(ClearCLBuffer arg1, ClearCLBuffer arg2, net.imglib2.realtransform.AffineTransform3D arg3) {
        return AffineTransform3D.affineTransform3D(getCLIJx(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform3D(ClearCLImage arg1, ClearCLImageInterface arg2, float[] arg3) {
        return AffineTransform3D.affineTransform3D(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.ApplyVectorField2D
    //----------------------------------------------------
    /**
     * Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 
     */
    default boolean applyVectorfield(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLImageInterface arg4) {
        return ApplyVectorField2D.applyVectorfield(getCLIJx(), arg1, arg2, arg3, arg4);
    }

    /**
     * Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 
     */
    default boolean applyVectorfield2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLImageInterface arg4) {
        return ApplyVectorField2D.applyVectorfield2D(getCLIJx(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clijx.plugins.ApplyVectorField3D
    //----------------------------------------------------
    /**
     * Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 
     */
    default boolean applyVectorfield(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLImageInterface arg4, ClearCLImageInterface arg5) {
        return ApplyVectorField3D.applyVectorfield(getCLIJx(), arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Deforms an image stack according to distances provided in the given vector image stacks. It is recommended to use 32-bit image stacks for input, output and vector image stacks. 
     */
    default boolean applyVectorfield3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLImageInterface arg4, ClearCLImageInterface arg5) {
        return ApplyVectorField3D.applyVectorfield3D(getCLIJx(), arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clijx.plugins.ArgMaximumZProjection
    //----------------------------------------------------
    /**
     * Determines the maximum projection of an image stack along Z.
     * Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).
     */
    default boolean argMaximumZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return ArgMaximumZProjection.argMaximumZProjection(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.Histogram
    //----------------------------------------------------
    /**
     * Determines the histogram of a given image.
     */
    default boolean histogram(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, boolean arg6, boolean arg7) {
        return Histogram.histogram(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6, arg7);
    }

    /**
     * Determines the histogram of a given image.
     */
    default boolean histogram(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, boolean arg6) {
        return Histogram.histogram(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6);
    }

    /**
     * Determines the histogram of a given image.
     */
    default float[] histogram(ClearCLBuffer arg1, double arg2, double arg3, double arg4) {
        return Histogram.histogram(getCLIJx(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).intValue());
    }

    /**
     * Determines the histogram of a given image.
     */
    default ClearCLBuffer histogram(ClearCLBuffer arg1) {
        return Histogram.histogram(getCLIJx(), arg1);
    }

    /**
     * Determines the histogram of a given image.
     */
    default boolean histogram(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Histogram.histogram(getCLIJx(), arg1, arg2);
    }

    /**
     * 
     */
    @Deprecated
    default boolean fillHistogram(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return Histogram.fillHistogram(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.AutomaticThreshold
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one 
     * of these methods in the method text field:
     * [Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]
     */
    default boolean automaticThreshold(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return AutomaticThreshold.automaticThreshold(getCLIJx(), arg1, arg2, arg3);
    }

    /**
     * The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one 
     * of these methods in the method text field:
     * [Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]
     */
    default boolean automaticThreshold(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3, double arg4, double arg5, double arg6) {
        return AutomaticThreshold.automaticThreshold(getCLIJx(), arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Threshold
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1. All pixel values x of a given input image with 
     * value larger or equal to a given threshold t will be set to 1.
     * 
     * f(x,t) = (1 if (x >= t); (0 otherwise))
     * 
     * This plugin is comparable to setting a raw threshold in ImageJ and using the 'Convert to Mask' menu.
     */
    default boolean threshold(ClearCLBuffer source, ClearCLBuffer destination, double threshold) {
        return Threshold.threshold(getCLIJx(), source, destination, new Double (threshold).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.BinaryOr
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary OR operator |.
     * All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>
     */
    default boolean binaryOr(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return BinaryOr.binaryOr(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.BinaryAnd
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary AND operator &.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = x & y</pre>
     */
    default boolean binaryAnd(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return BinaryAnd.binaryAnd(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.BinaryXOr
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary operators AND &, OR | and NOT ! implementing the XOR operator.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = (x & !y) | (!x & y)</pre>
     */
    default boolean binaryXOr(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return BinaryXOr.binaryXOr(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.BinaryNot
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from an image X by negating its pixel values
     * x using the binary NOT operator !
     * All pixel values except 0 in the input image are interpreted as 1.
     * 
     * <pre>f(x) = !x</pre>
     */
    default boolean binaryNot(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return BinaryNot.binaryNot(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.ErodeSphere
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    default boolean erodeSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ErodeSphere.erodeSphere(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.ErodeBox
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    default boolean erodeBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ErodeBox.erodeBox(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.ErodeSphereSliceBySlice
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean erodeSphereSliceBySlice(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ErodeSphereSliceBySlice.erodeSphereSliceBySlice(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.ErodeBoxSliceBySlice
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean erodeBoxSliceBySlice(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ErodeBoxSliceBySlice.erodeBoxSliceBySlice(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.DilateSphere
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    default boolean dilateSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return DilateSphere.dilateSphere(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.DilateBox
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    default boolean dilateBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return DilateBox.dilateBox(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.DilateSphereSliceBySlice
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean dilateSphereSliceBySlice(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return DilateSphereSliceBySlice.dilateSphereSliceBySlice(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.DilateBoxSliceBySlice
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean dilateBoxSliceBySlice(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return DilateBoxSliceBySlice.dilateBoxSliceBySlice(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.Copy
    //----------------------------------------------------
    /**
     * Copies an image.
     * 
     * <pre>f(x) = x</pre>
     */
    default boolean copy(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return Copy.copy(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.CopySlice
    //----------------------------------------------------
    /**
     * This method has two purposes: 
     * It copies a 2D image to a given slice z position in a 3D image stack or 
     * It copies a given slice at position z in an image stack to a 2D image.
     * 
     * The first case is only available via ImageJ macro. If you are using it, it is recommended that the 
     * target 3D image already pre-exists in GPU memory before calling this method. Otherwise, CLIJ create 
     * the image stack with z planes.
     */
    default boolean copySlice(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return CopySlice.copySlice(getCLIJx(), arg1, arg2, new Double (arg3).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Crop2D
    //----------------------------------------------------
    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Crop2D.crop(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }

    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop2D(ClearCLBuffer source, ClearCLBuffer destination, double startX, double startY, double width, double height) {
        return Crop2D.crop2D(getCLIJ(), source, destination, new Double (startX).intValue(), new Double (startY).intValue(), new Double (width).intValue(), new Double (height).intValue());
    }

    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Crop2D.crop2D(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Crop3D
    //----------------------------------------------------
    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Crop3D.crop(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Crops a given sub-stack out of a given image stack.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop3D(ClearCLBuffer source, ClearCLBuffer destination, double startX, double startY, double startZ, double width, double height, double depth) {
        return Crop3D.crop3D(getCLIJ(), source, destination, new Double (startX).intValue(), new Double (startY).intValue(), new Double (startZ).intValue(), new Double (width).intValue(), new Double (height).intValue(), new Double (depth).intValue());
    }

    /**
     * Crops a given sub-stack out of a given image stack.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Crop3D.crop3D(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Set
    //----------------------------------------------------
    /**
     * Sets all pixel values x of a given image X to a constant value v.
     * 
     * <pre>f(x) = v</pre>
     */
    default boolean set(ClearCLImageInterface arg1, double arg2) {
        return Set.set(getCLIJx(), arg1, new Double (arg2).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.Flip2D
    //----------------------------------------------------
    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    default boolean flip(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3, boolean arg4) {
        return Flip2D.flip(getCLIJx(), arg1, arg2, arg3, arg4);
    }

    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    default boolean flip2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3, boolean arg4) {
        return Flip2D.flip2D(getCLIJx(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clijx.plugins.Flip3D
    //----------------------------------------------------
    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    default boolean flip(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3, boolean arg4, boolean arg5) {
        return Flip3D.flip(getCLIJx(), arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Flips an image in X, Y and/or Z direction depending on boolean flags.
     */
    default boolean flip3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3, boolean arg4, boolean arg5) {
        return Flip3D.flip3D(getCLIJx(), arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clijx.plugins.RotateLeft
    //----------------------------------------------------
    /**
     * Rotates a given input image by 90 degrees counter-clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    default boolean rotateLeft(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return RotateLeft.rotateLeft(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.RotateRight
    //----------------------------------------------------
    /**
     * Rotates a given input image by 90 degrees clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    default boolean rotateRight(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return RotateRight.rotateRight(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.Mask
    //----------------------------------------------------
    /**
     * Computes a masked image by applying a mask to an image. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    default boolean mask(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return Mask.mask(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.MaskStackWithPlane
    //----------------------------------------------------
    /**
     * Computes a masked image by applying a 2D mask to an image stack. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same spatial position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    default boolean maskStackWithPlane(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return MaskStackWithPlane.maskStackWithPlane(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.MaximumZProjection
    //----------------------------------------------------
    /**
     * Determines the maximum projection of an image along Z.
     */
    default boolean maximumZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return MaximumZProjection.maximumZProjection(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.MeanZProjection
    //----------------------------------------------------
    /**
     * Determines the mean average projection of an image along Z.
     */
    default boolean meanZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return MeanZProjection.meanZProjection(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.MinimumZProjection
    //----------------------------------------------------
    /**
     * Determines the minimum projection of an image along Z.
     */
    default boolean minimumZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return MinimumZProjection.minimumZProjection(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.Power
    //----------------------------------------------------
    /**
     * Computes all pixels value x to the power of a given exponent a.
     * 
     * <pre>f(x, a) = x ^ a</pre>
     */
    default boolean power(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return Power.power(getCLIJx(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.tenengradfusion.AbstractTenengradFusion
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.plugins.DivideImages
    //----------------------------------------------------
    /**
     * Divides two images X and Y by each other pixel wise.
     * 
     * <pre>f(x, y) = x / y</pre>
     */
    default boolean divideImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return DivideImages.divideImages(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.MaximumImages
    //----------------------------------------------------
    /**
     * Computes the maximum of a pair of pixel values x, y from two given images X and Y.
     * 
     * <pre>f(x, y) = max(x, y)</pre>
     */
    default boolean maximumImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return MaximumImages.maximumImages(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.MaximumImageAndScalar
    //----------------------------------------------------
    /**
     * Computes the maximum of a constant scalar s and each pixel value x in a given image X.
     * 
     * <pre>f(x, s) = max(x, s)</pre>
     */
    default boolean maximumImageAndScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return MaximumImageAndScalar.maximumImageAndScalar(getCLIJx(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.MinimumImages
    //----------------------------------------------------
    /**
     * Computes the minimum of a pair of pixel values x, y from two given images X and Y.
     * 
     * <pre>f(x, y) = min(x, y)</pre>
     */
    default boolean minimumImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return MinimumImages.minimumImages(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.MinimumImageAndScalar
    //----------------------------------------------------
    /**
     * Computes the maximum of a constant scalar s and each pixel value x in a given image X.
     * 
     * <pre>f(x, s) = min(x, s)</pre>
     */
    default boolean minimumImageAndScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return MinimumImageAndScalar.minimumImageAndScalar(getCLIJx(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.MultiplyImageAndScalar
    //----------------------------------------------------
    /**
     * Multiplies all pixels value x in a given image X with a constant scalar s.
     * 
     * <pre>f(x, s) = x * s</pre>
     */
    default boolean multiplyImageAndScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return MultiplyImageAndScalar.multiplyImageAndScalar(getCLIJx(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.MultiplyStackWithPlane
    //----------------------------------------------------
    /**
     * Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. x and y are at 
     * the same spatial position within a plane.
     * 
     * <pre>f(x, y) = x * y</pre>
     */
    default boolean multiplyStackWithPlane(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return MultiplyStackWithPlane.multiplyStackWithPlane(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.CountNonZeroPixels2DSphere
    //----------------------------------------------------
    /**
     * Counts non-zero pixels in a sphere around every pixel.Put the number in the result image.
     */
    default boolean countNonZeroPixels2DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CountNonZeroPixels2DSphere.countNonZeroPixels2DSphere(getCLIJx(), source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * 
     */
    default boolean countNonZeroPixelsLocally(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return CountNonZeroPixels2DSphere.countNonZeroPixelsLocally(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.CountNonZeroPixelsSliceBySliceSphere
    //----------------------------------------------------
    /**
     * Counts non-zero pixels in a sphere around every pixel slice by slice in a stack and puts the resulting number in the destination image stack.
     */
    default boolean countNonZeroPixelsSliceBySliceSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CountNonZeroPixelsSliceBySliceSphere.countNonZeroPixelsSliceBySliceSphere(getCLIJx(), source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * 
     */
    default boolean countNonZeroPixelsLocallySliceBySlice(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return CountNonZeroPixelsSliceBySliceSphere.countNonZeroPixelsLocallySliceBySlice(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.CountNonZeroVoxels3DSphere
    //----------------------------------------------------
    /**
     * Counts non-zero voxels in a sphere around every voxel.Put the number in the result image.
     */
    default boolean countNonZeroVoxels3DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return CountNonZeroVoxels3DSphere.countNonZeroVoxels3DSphere(getCLIJx(), source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }

    /**
     * 
     */
    default boolean countNonZeroVoxelsLocally(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return CountNonZeroVoxels3DSphere.countNonZeroVoxelsLocally(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.SumZProjection
    //----------------------------------------------------
    /**
     * Determines the sum projection of an image along Z.
     */
    default boolean sumZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return SumZProjection.sumZProjection(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.SumOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Sum'.
     */
    default double sumOfAllPixels(ClearCLImageInterface arg1) {
        return SumOfAllPixels.sumOfAllPixels(getCLIJx(), arg1);
    }

    /**
     * Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Sum'.
     */
    default double sumPixels(ClearCLImageInterface arg1) {
        return SumOfAllPixels.sumPixels(getCLIJx(), arg1);
    }


    // net.haesleinhuepf.clijx.plugins.CenterOfMass
    //----------------------------------------------------
    /**
     * Determines the center of mass of an image or image stack and writes the result in the results table
     * in the columns MassX, MassY and MassZ.
     */
    default double[] centerOfMass(ClearCLBuffer source) {
        return CenterOfMass.centerOfMass(getCLIJx(), source);
    }


    // net.haesleinhuepf.clijx.plugins.Invert
    //----------------------------------------------------
    /**
     * Computes the negative value of all pixels in a given image. It is recommended to convert images to 
     * 32-bit float before applying this operation.
     * 
     * <pre>f(x) = - x</pre>
     * 
     * For binary images, use binaryNot.
     */
    default boolean invert(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return Invert.invert(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.Downsample2D
    //----------------------------------------------------
    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    default boolean downsample2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Downsample2D.downsample2D(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    default boolean downsample(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Downsample2D.downsample(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.Downsample3D
    //----------------------------------------------------
    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    default boolean downsample3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Downsample3D.downsample3D(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    default boolean downsample(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Downsample3D.downsample(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.DownsampleSliceBySliceHalfMedian
    //----------------------------------------------------
    /**
     * Scales an image using scaling factors 0.5 for X and Y dimensions. The Z dimension stays untouched. Thus, each slice is processed separately.
     * The median method is applied. Thus, each pixel value in the destination image equals to the median of
     * four corresponding pixels in the source image.
     */
    default boolean downsampleSliceBySliceHalfMedian(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return DownsampleSliceBySliceHalfMedian.downsampleSliceBySliceHalfMedian(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.LocalThreshold
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 depending on if a pixel value x in image X 
     * was above of equal to the pixel value m in mask image M.
     * 
     * <pre>f(x) = (1 if (x >=  m)); (0 otherwise)</pre>
     */
    default boolean localThreshold(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return LocalThreshold.localThreshold(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.GradientX
    //----------------------------------------------------
    /**
     * Computes the gradient of gray values along X. Assuming a, b and c are three adjacent
     *  pixels in X direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    default boolean gradientX(ClearCLBuffer source, ClearCLBuffer destination) {
        return GradientX.gradientX(getCLIJx(), source, destination);
    }


    // net.haesleinhuepf.clijx.plugins.GradientY
    //----------------------------------------------------
    /**
     * Computes the gradient of gray values along Y. Assuming a, b and c are three adjacent
     *  pixels in Y direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    default boolean gradientY(ClearCLBuffer source, ClearCLBuffer destination) {
        return GradientY.gradientY(getCLIJx(), source, destination);
    }


    // net.haesleinhuepf.clijx.plugins.GradientZ
    //----------------------------------------------------
    /**
     * Computes the gradient of gray values along Z. Assuming a, b and c are three adjacent
     *  pixels in Z direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    default boolean gradientZ(ClearCLBuffer source, ClearCLBuffer destination) {
        return GradientZ.gradientZ(getCLIJx(), source, destination);
    }


    // net.haesleinhuepf.clijx.plugins.MultiplyImageAndCoordinate
    //----------------------------------------------------
    /**
     * Multiplies all pixel intensities with the x, y or z coordinate, depending on specified dimension.</pre>
     */
    default boolean multiplyImageAndCoordinate(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return MultiplyImageAndCoordinate.multiplyImageAndCoordinate(getCLIJx(), arg1, arg2, new Double (arg3).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Mean2DBox
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean mean2DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4) {
        return Mean2DBox.mean2DBox(getCLIJx(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clijx.plugins.Mean2DSphere
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean mean2DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Mean2DSphere.mean2DSphere(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Mean3DBox
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels cube neighborhood. The cubes size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean mean3DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4, int arg5) {
        return Mean3DBox.mean3DBox(getCLIJx(), arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean meanBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4, int arg5) {
        return Mean3DBox.meanBox(getCLIJx(), arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clijx.plugins.Mean3DSphere
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels spherical neighborhood. The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean mean3DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Mean3DSphere.mean3DSphere(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.MeanSliceBySliceSphere
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean meanSliceBySliceSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return MeanSliceBySliceSphere.meanSliceBySliceSphere(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.MeanOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the mean average of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Mean'.
     */
    default double meanOfAllPixels(ClearCLImageInterface arg1) {
        return MeanOfAllPixels.meanOfAllPixels(getCLIJx(), arg1);
    }


    // net.haesleinhuepf.clijx.plugins.Median2DBox
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    default boolean median2DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Median2DBox.median2DBox(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Median2DSphere
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the ellipse must have less than 1000 pixels.
     */
    default boolean median2DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Median2DSphere.median2DSphere(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Median3DBox
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels cuboid neighborhood. The cuboid size is specified by 
     * its half-width, half-height and half-depth (radius).
     * 
     * For technical reasons, the volume of the cuboid must contain less than 1000 voxels.
     */
    default boolean median3DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Median3DBox.median3DBox(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Median3DSphere
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels spherical neighborhood. The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     * 
     * For technical reasons, the volume of the sphere must contain less than 1000 voxels.
     */
    default boolean median3DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Median3DSphere.median3DSphere(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.MedianSliceBySliceBox
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels rectangular neighborhood. This is done slice-by-slice in a 3D 
     * image stack. The rectangle is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    default boolean median3DSliceBySliceBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return MedianSliceBySliceBox.median3DSliceBySliceBox(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.MedianSliceBySliceSphere
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels ellipsoidal neighborhood. This is done slice-by-slice in a 3D 
     * image stack. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the ellipse must have less than 1000 pixels.
     */
    default boolean median3DSliceBySliceSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return MedianSliceBySliceSphere.median3DSliceBySliceSphere(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Maximum2DSphere
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean maximum2DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Maximum2DSphere.maximum2DSphere(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Maximum3DSphere
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels spherical neighborhood. The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean maximum3DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Maximum3DSphere.maximum3DSphere(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Maximum2DBox
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean maximum2DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4) {
        return Maximum2DBox.maximum2DBox(getCLIJx(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clijx.plugins.Maximum3DBox
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels cube neighborhood. The cubes size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean maximum3DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4, int arg5) {
        return Maximum3DBox.maximum3DBox(getCLIJx(), arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean maximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4, int arg5) {
        return Maximum3DBox.maximumBox(getCLIJx(), arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clijx.plugins.MaximumSliceBySliceSphere
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean maximum3DSliceBySliceSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return MaximumSliceBySliceSphere.maximum3DSliceBySliceSphere(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Minimum2DSphere
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean minimum2DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Minimum2DSphere.minimum2DSphere(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Minimum3DSphere
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels spherical neighborhood. The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean minimum3DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Minimum3DSphere.minimum3DSphere(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.Minimum2DBox
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean minimum2DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4) {
        return Minimum2DBox.minimum2DBox(getCLIJx(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clijx.plugins.Minimum3DBox
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels cube neighborhood. The cubes size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean minimum3DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4, int arg5) {
        return Minimum3DBox.minimum3DBox(getCLIJx(), arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean minimumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4, int arg5) {
        return Minimum3DBox.minimumBox(getCLIJx(), arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clijx.plugins.MinimumSliceBySliceSphere
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean minimum3DSliceBySliceSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return MinimumSliceBySliceSphere.minimum3DSliceBySliceSphere(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.MultiplyImages
    //----------------------------------------------------
    /**
     * Multiplies all pairs of pixel values x and y from two image X and Y.
     * 
     * <pre>f(x, y) = x * y</pre>
     */
    default boolean multiplyImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return MultiplyImages.multiplyImages(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.plugins.Blur2D
    //----------------------------------------------------
    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    default boolean blur2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Blur2D.blur2D(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    default boolean blur(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Blur2D.blur(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.Blur3D
    //----------------------------------------------------
    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X, Y and Z. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    default boolean blur3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Blur3D.blur3D(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    default boolean blur(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Blur3D.blur(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.Blur3DSliceBySlice
    //----------------------------------------------------
    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The Gaussian blur is applied slice by slice in 2D.
     */
    @Deprecated
    default boolean blurSliceBySlice(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Blur3DSliceBySlice.blurSliceBySlice(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The Gaussian blur is applied slice by slice in 2D.
     */
    @Deprecated
    default boolean blurSliceBySlice(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5, double arg6) {
        return Blur3DSliceBySlice.blurSliceBySlice(getCLIJx(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.ResliceBottom
    //----------------------------------------------------
    /**
     * Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
     * offers less flexibility such as interpolation.
     */
    default boolean resliceBottom(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ResliceBottom.resliceBottom(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.ResliceTop
    //----------------------------------------------------
    /**
     * Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
     * offers less flexibility such as interpolation.
     */
    default boolean resliceTop(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ResliceTop.resliceTop(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.ResliceLeft
    //----------------------------------------------------
    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    default boolean resliceLeft(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ResliceLeft.resliceLeft(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.ResliceRight
    //----------------------------------------------------
    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    default boolean resliceRight(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ResliceRight.resliceRight(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.plugins.Rotate2D
    //----------------------------------------------------
    /**
     * Rotates an image in plane. All angles are entered in degrees. If the image is not rotated around 
     * the center, it is rotated around the coordinate origin.
     * 
     * It is recommended to apply the rotation to an isotropic image.
     */
    default boolean rotate2D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, boolean arg4) {
        return Rotate2D.rotate2D(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), arg4);
    }


    // net.haesleinhuepf.clijx.plugins.Rotate3D
    //----------------------------------------------------
    /**
     * Rotates an image stack in 3D. All angles are entered in degrees. If the image is not rotated around 
     * the center, it is rotated around the coordinate origin.
     * 
     * It is recommended to apply the rotation to an isotropic image stack.
     */
    default boolean rotate3D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, boolean arg6) {
        return Rotate3D.rotate3D(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6);
    }


    // net.haesleinhuepf.clijx.plugins.Scale2D
    //----------------------------------------------------
    /**
     * DEPRECATED: CLIJ scale() is deprecated. Use scale2D or scale3D instead!
     */
    default boolean scale(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return Scale2D.scale(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

    /**
     * Scales an image with a given factor.
     */
    default boolean scale2D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return Scale2D.scale2D(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.Scale3D
    //----------------------------------------------------
    /**
     * DEPRECATED: CLIJ scale() is deprecated. Use scale2D or scale3D instead!
     */
    default boolean scale(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Scale3D.scale(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Scales an image with a given factor.
     */
    default boolean scale3D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Scale3D.scale3D(getCLIJx(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.Translate2D
    //----------------------------------------------------
    /**
     * Translate an image stack in X and Y.
     */
    default boolean translate2D(ClearCLBuffer source, ClearCLBuffer destination, double translateX, double translateY) {
        return Translate2D.translate2D(getCLIJx(), source, destination, new Double (translateX).floatValue(), new Double (translateY).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.Translate3D
    //----------------------------------------------------
    /**
     * Translate an image stack in X, Y and Z.
     */
    default boolean translate3D(ClearCLBuffer source, ClearCLBuffer destination, double translateX, double translateY, double translateZ) {
        return Translate3D.translate3D(getCLIJx(), source, destination, new Double (translateX).floatValue(), new Double (translateY).floatValue(), new Double (translateZ).floatValue());
    }


    // net.haesleinhuepf.clijx.base.Clear
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.base.ClInfo
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.base.ConvertFloat
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.base.ConvertUInt8
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.base.ConvertUInt16
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.base.Create2D
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.base.Create3D
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.base.Pull
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.base.PullBinary
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.base.Push
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.base.PushCurrentSlice
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.base.PushCurrentZStack
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.base.Release
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.plugins.AddImageAndScalar
    //----------------------------------------------------
    /**
     * Adds a scalar value s to all pixels x of a given image X.
     * 
     * <pre>f(x, s) = x + s</pre>
     */
    default boolean addImageAndScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return AddImageAndScalar.addImageAndScalar(getCLIJx(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.DetectMinimaBox
    //----------------------------------------------------
    /**
     * Detects local minima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
     * there is no other pixel in a given radius which has a lower intensity, and to 0 otherwise.
     */
    default boolean detectMinimaBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return DetectMinimaBox.detectMinimaBox(getCLIJx(), arg1, arg2, new Double (arg3).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.DetectMaximaBox
    //----------------------------------------------------
    /**
     * Detects local maxima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
     * there is no other pixel in a given radius which has a higher intensity, and to 0 otherwise.
     */
    default boolean detectMaximaBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return DetectMaximaBox.detectMaximaBox(getCLIJx(), arg1, arg2, new Double (arg3).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.DetectMaximaSliceBySliceBox
    //----------------------------------------------------
    /**
     * Detects local maxima in a given square neighborhood of an input image stack. The input image stack is 
     * processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
     * given radius which has a higher intensity, and to 0 otherwise.
     */
    default boolean detectMaximaSliceBySliceBox(ClearCLBuffer source, ClearCLBuffer destination, double radius) {
        return DetectMaximaSliceBySliceBox.detectMaximaSliceBySliceBox(getCLIJx(), source, destination, new Double (radius).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.DetectMinimaSliceBySliceBox
    //----------------------------------------------------
    /**
     * Detects local minima in a given square neighborhood of an input image stack. The input image stack is 
     * processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
     * given radius which has a lower intensity, and to 0 otherwise.
     */
    default boolean detectMinimaSliceBySliceBox(ClearCLBuffer source, ClearCLBuffer destination, double radius) {
        return DetectMinimaSliceBySliceBox.detectMinimaSliceBySliceBox(getCLIJx(), source, destination, new Double (radius).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.MaximumOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the maximum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Max'.
     */
    default double maximumOfAllPixels(ClearCLImageInterface arg1) {
        return MaximumOfAllPixels.maximumOfAllPixels(getCLIJx(), arg1);
    }


    // net.haesleinhuepf.clijx.plugins.MinimumOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the minimum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Min'.
     */
    default double minimumOfAllPixels(ClearCLImageInterface arg1) {
        return MinimumOfAllPixels.minimumOfAllPixels(getCLIJx(), arg1);
    }


    // net.haesleinhuepf.clijx.plugins.ReportMemory
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.plugins.splitstack.AbstractSplitStack
    //----------------------------------------------------

    // net.haesleinhuepf.clijx.plugins.TopHatOctagonSliceBySlice
    //----------------------------------------------------
    /**
     * Applies a minimum filter with kernel size 3x3 n times to an image iteratively. Odd iterations are done with box neighborhood, even iterations with a diamond. Thus, with n > 2, the filter shape is an octagon. The given number of iterations - 2 makes the filter result very similar to minimum sphere.
     */
    default boolean topHatOctagonSliceBySlice(ClearCLBuffer input, ClearCLBuffer destination, double iterations) {
        return TopHatOctagonSliceBySlice.topHatOctagonSliceBySlice(getCLIJx(), input, destination, new Double (iterations).intValue());
    }


    // net.haesleinhuepf.clijx.plugins.SetColumn
    //----------------------------------------------------
    /**
     * Sets all pixel values x of a given column in X to a constant value v.
     * 
     * <pre>f(x) = v</pre>
     */
    default boolean setColumn(ClearCLImageInterface arg1, double arg2, double arg3) {
        return SetColumn.setColumn(getCLIJx(), arg1, new Double (arg2).intValue(), new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.SetRow
    //----------------------------------------------------
    /**
     * Sets all pixel values x of a given row in X to a constant value v.
     * 
     * <pre>f(x) = v</pre>
     */
    default boolean setRow(ClearCLImageInterface arg1, double arg2, double arg3) {
        return SetRow.setRow(getCLIJx(), arg1, new Double (arg2).intValue(), new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.plugins.SumYProjection
    //----------------------------------------------------
    /**
     * Determines the sum intensity projection of an image along Z.
     */
    default boolean sumYProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return SumYProjection.sumYProjection(getCLIJx(), arg1, arg2);
    }


    // net.haesleinhuepf.clijx.matrix.AverageDistanceOfTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touch matrix and a distance matrix to determine the average distance of touching neighbors for every object.
     */
    default boolean averageDistanceOfTouchingNeighbors(ClearCLBuffer distance_matrix, ClearCLBuffer touch_matrix, ClearCLBuffer average_distancelist_destination) {
        return AverageDistanceOfTouchingNeighbors.averageDistanceOfTouchingNeighbors(getCLIJx(), distance_matrix, touch_matrix, average_distancelist_destination);
    }


    // net.haesleinhuepf.clijx.matrix.LabelledSpotsToPointList
    //----------------------------------------------------
    /**
     * Transforms a labelmap of spots (single pixels with values 1, 2, ..., n for n spots) as resulting from connected components analysis in an image where every column contains d 
     * pixels (with d = dimensionality of the original image) with the coordinates of the maxima/minima.
     */
    default boolean labelledSpotsToPointList(ClearCLBuffer input_labelled_spots, ClearCLBuffer destination_pointlist) {
        return LabelledSpotsToPointList.labelledSpotsToPointList(getCLIJx(), input_labelled_spots, destination_pointlist);
    }


    // net.haesleinhuepf.clijx.matrix.LabelSpots
    //----------------------------------------------------
    /**
     * Transforms a spots image as resulting from maximum/minimum detection in an image of the same size where every spot has a number 1, 2, ... n.
     */
    default boolean labelSpots(ClearCLBuffer input_spots, ClearCLBuffer labelled_spots_destination) {
        return LabelSpots.labelSpots(getCLIJx(), input_spots, labelled_spots_destination);
    }


    // net.haesleinhuepf.clijx.matrix.MinimumDistanceOfTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touch matrix and a distance matrix to determine the shortest distance of touching neighbors for every object.
     */
    default boolean minimumDistanceOfTouchingNeighbors(ClearCLBuffer distance_matrix, ClearCLBuffer touch_matrix, ClearCLBuffer minimum_distancelist_destination) {
        return MinimumDistanceOfTouchingNeighbors.minimumDistanceOfTouchingNeighbors(getCLIJx(), distance_matrix, touch_matrix, minimum_distancelist_destination);
    }


    // net.haesleinhuepf.clijx.io.WriteVTKLineListToDisc
    //----------------------------------------------------
    /**
     * Takes a point list image representing n points (n*2 for 2D points, n*3 for 3D points) and a corresponding touch matrix , sized (n+1)*(n+1), and exports them in VTK format.
     */
    default boolean writeVTKLineListToDisc(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return WriteVTKLineListToDisc.writeVTKLineListToDisc(getCLIJx(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.io.WriteXYZPointListToDisc
    //----------------------------------------------------
    /**
     * Takes a point list image representing n points (n*2 for 2D points, n*3 for 3D points) and exports them in XYZ format.
     */
    default boolean writeXYZPointListToDisc(ClearCLBuffer arg1, String arg2) {
        return WriteXYZPointListToDisc.writeXYZPointListToDisc(getCLIJx(), arg1, arg2);
    }

}
// 336 methods generated.
