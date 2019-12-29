// CLIJ example macro: spot_distance_measurement.ijm
//
// This macro shows how find spots in an image, shifts
// the spots to simulate motion and measures the shift 
// afterwards by determining closest neighbors between
// the images.
//
// Author: Robert Haase
// July 2019 in Woods Hole
// ---------------------------------------------


// Get test data
// run("Blobs (25K)");
open("C:/structure/data/blobs.tif");

getDimensions(width, height, channels, slices, frames);
input = getTitle();

mask = "mask";
labelmap = "labelmap";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push data to GPU
Ext.CLIJx_push(input);

// cleanup ImageJ
run("Close All");

blurred = "blurred";

Ext.CLIJx_blur2D(input, blurred, 5, 5);

detected = "detected";
Ext.CLIJx_detectMaximaBox(blurred, detected, 3);

shiftDetected = "shiftDetected";
Ext.CLIJx_translate2D(detected, shiftDetected, 1, 0);
Ext.CLIJx_pull(shiftDetected);

pointlist1 = "pointlist1";
Ext.CLIJx_spotsToPointList(detected, pointlist1);
Ext.CLIJx_pull(pointlist1);

pointlist2 = "pointlist2";
Ext.CLIJx_spotsToPointList(shiftDetected, pointlist2);
Ext.CLIJx_pull(pointlist2);

distance_matrix = "distance_matrix";
Ext.CLIJx_generateDistanceMatrix(pointlist1, pointlist2, distance_matrix);

Ext.CLIJx_pull(distance_matrix);

minimum_distances = "minimum_distances";
Ext.CLIJx_shortestDistances(distance_matrix, minimum_distances);

Ext.CLIJx_meanOfAllPixels(minimum_distances);
meanDistance = getResult("Mean", nResults() - 1);
IJ.log("mean distance: " + meanDistance);


Ext.CLIJx_pull(minimum_distances);



